package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
import com.fasterxml.jackson.databind.type.ArrayType;
import com.fasterxml.jackson.databind.util.EnumResolver;

public class RONDeserializerFactory extends BeanDeserializerFactory {

    public static final RONDeserializerFactory instance = new RONDeserializerFactory(new DeserializerFactoryConfig());

    public RONDeserializerFactory(DeserializerFactoryConfig config) {
        super(config);
    }

    @Override
    public JsonDeserializer<?> createEnumDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        Class<Enum<?>> enumClass = (Class<Enum<?>>) type.getRawClass();
        final DeserializationConfig config = ctxt.getConfig();
        EnumResolver resolver = constructEnumResolver(enumClass, config, beanDesc.findJsonValueAccessor());
        return new RONEnumDeserializer(resolver);
    }

    @Override
    public JsonDeserializer<?> createArrayDeserializer(DeserializationContext ctxt, ArrayType type, BeanDescription beanDesc) throws JsonMappingException {
        return super.createArrayDeserializer(ctxt, type, beanDesc);
    }

    @Override
    public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        return super.buildBeanDeserializer(ctxt, type, beanDesc);
    }
}
