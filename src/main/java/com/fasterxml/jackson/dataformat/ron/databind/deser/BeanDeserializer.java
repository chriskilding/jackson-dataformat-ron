package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONBaseVisitor;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class BeanDeserializer extends RONBaseVisitor<Object> {

    private final ValueVisitor valueVisitor = new ValueVisitor();

    private final Deque<JavaType> javaTypes;
    private final DeserializationConfig deserializationConfig;

    public BeanDeserializer(JavaType javaType, DeserializationConfig deserializationConfig) {
        this.javaTypes = new ArrayDeque<>();
        this.javaTypes.add(javaType);
        this.deserializationConfig = deserializationConfig;
    }

    @Override
    public Object visitEnumeration(RONParser.EnumerationContext ctx) {
        final JavaType javaType = javaTypes.peek();

        final String name = ctx.BAREWORD().getText();

        final EnumResolver resolver = EnumResolver.constructFor(deserializationConfig, javaType.getRawClass());
        final CompactStringObjectMap lookupByName = resolver.constructLookup();
        return lookupByName.find(name);
    }

    @Override
    public Object visitMap(RONParser.MapContext ctx) {
        final JavaType javaType = javaTypes.peek();
        // assume it is indeed a map class
        final Map<String, Object> map = new HashMap<>();

        for (RONParser.MapEntryContext entry: ctx.mapEntry()) {
            final String key = entry.STRING().getText();
        }

        return map;
    }

    @Override
    public Object visitStruct(RONParser.StructContext ctx) {
        final JavaType javaType = javaTypes.peek();
        if (!javaType.isConcrete()) {
            final String simpleName = ctx.BAREWORD().getText();
            // if null - we can't proceed unless we use jackson class annotations for polymorphism

            // something like...
            // 1. get the known subclasses of the abstract javaType
            // 2. filter to just subclasses with same name as the named struct
            // 2. choose the subclass in the nearest package/classloader to us
        }

        try {
            final Constructor<?> ctor = javaType.getRawClass().getDeclaredConstructors()[0];
            final Object newInstance = ctor.newInstance();

            // set fields recursively
            for (RONParser.StructEntryContext entry: ctx.structEntry()) {
                final String fieldName = entry.BAREWORD().getText();

                final Field childField = newInstance.getClass().getDeclaredField(fieldName);
                final Class<?> childKlass = childField.getType();
                JavaType childJavaType = TypeFactory.defaultInstance().constructFromCanonical(childKlass.getCanonicalName());
                javaTypes.push(childJavaType);
                final Object value = this.visitValue(entry.value());
                javaTypes.pop();
                childField.setAccessible(true);
                childField.set(newInstance, value);
                childField.setAccessible(false);
            }

            return newInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new IllegalStateException("Can't instantiate the class from the struct", e);
        }
    }

    @Override
    public Object visitMapEntry(RONParser.MapEntryContext ctx) {
        return super.visitMapEntry(ctx);
    }

    @Override
    public Object visitArray(RONParser.ArrayContext ctx) {
        final JavaType javaType = javaTypes.peek();

        final JavaType[] typeParameters = javaType.findTypeParameters(Object.class);

        try {
            final Constructor<?> ctor = javaType.getRawClass().getDeclaredConstructors()[0];
            final Object newInstance = ctor.newInstance();
            for (RONParser.ValueContext value : ctx.value()) {
                final Method addMethod = newInstance.getClass().getDeclaredMethod("add");
            }

            return newInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new IllegalStateException("Can't instantiate the class from the array", e);
        }
    }

    @Override
    public Object visitRoot(RONParser.RootContext ctx) {
        return this.visit(ctx.value());
    }

    @Override
    public Object visitValue(RONParser.ValueContext ctx) {
        final JavaType javaType = javaTypes.peek();

        // FIXME the javaType to parse as must change contextually as recursion happens (push/pop)
        if (javaType.isTypeOrSubTypeOf(String.class)) {
            return valueVisitor.visitString(ctx);
        }

        if (javaType.isTypeOrSubTypeOf(Boolean.class) || javaType.isTypeOrSubTypeOf(boolean.class)) {
            return valueVisitor.visitBoolean(ctx);
        }

        if (javaType.isTypeOrSubTypeOf(Double.class) || javaType.isTypeOrSubTypeOf(double.class)) {
            return valueVisitor.visitDouble(ctx);
        }

        if (javaType.isTypeOrSubTypeOf(Float.class) || javaType.isTypeOrSubTypeOf(float.class)) {
            return valueVisitor.visitFloat(ctx);
        }

        if (javaType.isTypeOrSubTypeOf(Integer.class) || javaType.isTypeOrSubTypeOf(int.class)) {
            return valueVisitor.visitInt(ctx);
        }

        if (javaType.isTypeOrSubTypeOf(BigInteger.class)) {
            return valueVisitor.visitBigInteger(ctx);
        }

        if (javaType.isTypeOrSubTypeOf(BigDecimal.class)) {
            return valueVisitor.visitBigDecimal(ctx);
        }

        if (javaType.isMapLikeType()) {
            return this.visitMap(ctx.map());
        }

        if (javaType.isCollectionLikeType() || javaType.isArrayType()) {
            return this.visitArray(ctx.array());
        }

        if (javaType.isEnumType()) {
            return this.visitEnumeration(ctx.enumeration());
        }

        if (ctx.struct() != null) {
            return this.visitStruct(ctx.struct());
        }

        return null;
    }


}
