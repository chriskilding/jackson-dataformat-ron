package com.fasterxml.jackson.dataformat.ron.databind.deser.transformers;

public class DeserializerChain {

    public static Deserializer[] standard() {
        return new Deserializer[] {
                new BooleanDeserializer(),
                new DoubleDeserializer(),
                new FloatDeserializer(),
                new IntegerDeserializer(),
                new LongDeserializer(),
                new BigIntegerDeserializer(),
                new BigDecimalDeserializer(),
                new StringDeserializer()
        };
    }
}
