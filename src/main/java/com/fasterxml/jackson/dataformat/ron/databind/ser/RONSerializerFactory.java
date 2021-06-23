package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
import com.fasterxml.jackson.databind.ser.BasicSerializerFactory;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
import com.fasterxml.jackson.databind.ser.SerializerFactory;
import com.fasterxml.jackson.databind.ser.Serializers;

public class RONSerializerFactory extends BeanSerializerFactory {

    /**
     * This factory is stateless, so a single shared global (== singleton) instance can be used
     * without thread-safety issues.
     */
    public static final RONSerializerFactory instance = new RONSerializerFactory(null);

    public RONSerializerFactory(SerializerFactoryConfig config) {
        super(config);
    }

//    @Override
//    public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType type) throws JsonMappingException {
//        return super.createSerializer(prov, type);
//    }

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

}
