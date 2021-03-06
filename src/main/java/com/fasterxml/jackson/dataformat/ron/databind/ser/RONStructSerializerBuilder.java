package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
import com.fasterxml.jackson.dataformat.ron.databind.RONEnum;

public class RONStructSerializerBuilder extends BeanSerializerBuilder {
    private final static BeanPropertyWriter[] NO_PROPERTIES = new BeanPropertyWriter[0];

    public RONStructSerializerBuilder(BeanDescription beanDesc) {
        super(beanDesc);
    }

    @Override
    public JsonSerializer<?> build() {

        BeanPropertyWriter[] properties;
        // No properties, any getter or object id writer?
        // No real serializer; caller gets to handle
        if (_properties == null || _properties.isEmpty()) {
            properties = NO_PROPERTIES;
        } else {
            properties = _properties.toArray(new BeanPropertyWriter[_properties.size()]);
            if (_config.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
                for (BeanPropertyWriter property : properties) {
                    property.fixAccess(_config);
                }
            }
        }

        if (_beanDesc.getClassAnnotations().has(RONEnum.class)) {
            return new RONComplexEnumSerializer(properties);
        }

        return new RONStructSerializer(properties);
    }

}
