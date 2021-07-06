package com.fasterxml.jackson.dataformat.ron.databind.ser;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;

public class RONStructSerializerBuilder extends BeanSerializerBuilder {
    private final static BeanPropertyWriter[] NO_PROPERTIES = new BeanPropertyWriter[0];

    public RONStructSerializerBuilder(BeanDescription beanDesc, SerializationConfig config) {
        super(beanDesc);
        this.setConfig(config);
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
                for (int i = 0, end = properties.length; i < end; ++i) {
                    properties[i].fixAccess(_config);
                }
            }
        }

        final boolean namedStruct = true; // TODO parameterize this
        return new RONStructSerializer(properties, namedStruct);
    }

}