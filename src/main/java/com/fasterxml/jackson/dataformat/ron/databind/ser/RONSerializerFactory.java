package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.*;
import com.fasterxml.jackson.databind.ser.std.MapSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RONSerializerFactory extends BeanSerializerFactory {

    /**
     * This factory is stateless, so a single shared global (== singleton) instance can be used
     * without thread-safety issues.
     */
    public static final RONSerializerFactory instance = new RONSerializerFactory(null);

    public RONSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }

    /**
     * Use the RON enum serializer instead of the default.
     */
    @Override
    protected JsonSerializer<?> buildEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        /* As per [databind#24], may want to use alternate shape, serialize as JSON Object.
         * Challenge here is that EnumSerializer does not know how to produce
         * POJO style serialization, so we must handle that special case separately;
         * otherwise pass it to EnumSerializer.
         */
        JsonFormat.Value format = beanDesc.findExpectedFormat(null);
        if (format.getShape() == JsonFormat.Shape.OBJECT) {
            // one special case: suppress serialization of "getDeclaringClass()"...
            ((BasicBeanDescription) beanDesc).removeProperty("declaringClass");
            // returning null will mean that eventually BeanSerializer gets constructed
            return null;
        }
        Class<Enum<?>> enumClass = (Class<Enum<?>>) type.getRawClass();
        return RONEnumSerializer.construct(enumClass, config);
    }

    @Override
    protected JsonSerializer<Object> constructBeanOrAddOnSerializer(SerializerProvider prov,
                                                                    JavaType type, BeanDescription beanDesc, boolean staticTyping)
            throws JsonMappingException {
        // 13-Oct-2010, tatu: quick sanity check: never try to create bean serializer for plain Object
        // 05-Jul-2012, tatu: ... but we should be able to just return "unknown type" serializer, right?
        if (beanDesc.getBeanClass() == Object.class) {
            return prov.getUnknownTypeSerializer(Object.class);
//            throw new IllegalArgumentException("Cannot create bean serializer for Object.class");
        }

        JsonSerializer<?> ser = _findUnsupportedTypeSerializer(prov, type, beanDesc);
        if (ser != null) {
            return (JsonSerializer<Object>) ser;
        }

        final SerializationConfig config = prov.getConfig();
        BeanSerializerBuilder builder = constructSerializerBuilder(beanDesc, config);

        // First: any detectable (auto-detect, annotations) properties to serialize?
        List<BeanPropertyWriter> props = findBeanProperties(prov, beanDesc, builder);
        if (props == null) {
            props = new ArrayList<BeanPropertyWriter>();
        } else {
            props = removeOverlappingTypeIds(prov, beanDesc, builder, props);
        }

        // [databind#638]: Allow injection of "virtual" properties:
        prov.getAnnotationIntrospector().findAndAddVirtualProperties(config, beanDesc.getClassInfo(), props);

        // [JACKSON-440] Need to allow modification bean properties to serialize:
        if (_factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod : _factoryConfig.serializerModifiers()) {
                props = mod.changeProperties(config, beanDesc, props);
            }
        }

        // Any properties to suppress?
        props = filterBeanProperties(config, beanDesc, props);

        // Need to allow reordering of properties to serialize
        if (_factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod : _factoryConfig.serializerModifiers()) {
                props = mod.orderProperties(config, beanDesc, props);
            }
        }

        // And if Object Id is needed, some preparation for that as well: better
        // do before view handling, mostly for the custom id case which needs
        // access to a property
        builder.setObjectIdWriter(constructObjectIdHandler(prov, beanDesc, props));

        builder.setProperties(props);
        builder.setFilterId(findFilterId(config, beanDesc));

        AnnotatedMember anyGetter = beanDesc.findAnyGetter();
        if (anyGetter != null) {
            JavaType anyType = anyGetter.getType();
            // copied from BasicSerializerFactory.buildMapSerializer():
            JavaType valueType = anyType.getContentType();
            TypeSerializer typeSer = createTypeSerializer(config, valueType);
            // last 2 nulls; don't know key, value serializers (yet)
            // 23-Feb-2015, tatu: As per [databind#705], need to support custom serializers
            JsonSerializer<?> anySer = findSerializerFromAnnotation(prov, anyGetter);
            if (anySer == null) {
                // TODO: support '@JsonIgnoreProperties' with any setter?
                anySer = MapSerializer.construct(/* ignored props*/ (Set<String>) null,
                        anyType, config.isEnabled(MapperFeature.USE_STATIC_TYPING),
                        typeSer, null, null, /*filterId*/ null);
            }
            // TODO: can we find full PropertyName?
            PropertyName name = PropertyName.construct(anyGetter.getName());
            BeanProperty.Std anyProp = new BeanProperty.Std(name, valueType, null,
                    anyGetter, PropertyMetadata.STD_OPTIONAL);
            builder.setAnyGetter(new AnyGetterWriter(anyProp, anyGetter, anySer));
        }
        // Next: need to gather view information, if any:
        processViews(config, builder);

        // Finally: let interested parties mess with the result bit more...
        if (_factoryConfig.hasSerializerModifiers()) {
            for (BeanSerializerModifier mod : _factoryConfig.serializerModifiers()) {
                builder = mod.updateBuilder(config, beanDesc, builder);
            }
        }

        try {
            ser = builder.build();
        } catch (RuntimeException e) {
            return prov.reportBadTypeDefinition(beanDesc, "Failed to construct BeanSerializer for %s: (%s) %s",
                    beanDesc.getType(), e.getClass().getName(), e.getMessage());
        }
        if (ser == null) { // Means that no properties were found
            // 21-Aug-2020, tatu: Empty Records should be fine tho
            if (type.isRecordType()) {
                return builder.createDummy();
            }

            // 06-Aug-2019, tatu: As per [databind#2390], we need to check for add-ons here,
            //    before considering fallbacks
            ser = findSerializerByAddonType(config, type, beanDesc, staticTyping);
            if (ser == null) {
                // If we get this far, there were no properties found, so no regular BeanSerializer
                // would be constructed. But, couple of exceptions.
                // First: if there are known annotations, just create 'empty bean' serializer
                if (beanDesc.hasKnownClassAnnotations()) {
                    return builder.createDummy();
                }
            }
        }
        return (JsonSerializer<Object>) ser;
    }

    protected RONStructSerializerBuilder constructSerializerBuilder(BeanDescription beanDesc, SerializationConfig config) {
        return new RONStructSerializerBuilder(beanDesc, config);
    }
}
