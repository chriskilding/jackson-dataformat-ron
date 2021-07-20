package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;

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
     * Use the RON enum serializer instead of the default (which would turn the enum into a string).
     */
    @Override
    protected JsonSerializer<?> buildEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc) throws JsonMappingException {
        Class<Enum<?>> enumClass = (Class<Enum<?>>) type.getRawClass();
        return RONSimpleEnumSerializer.construct(enumClass, config);
    }

    /**
     * Use the RON struct serializer instead of the default (which would turn the bean into a JSON object).
     */
    @Override
    protected BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDesc) {
        return new RONStructSerializerBuilder(beanDesc);
    }
}
