package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
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

        final String name = ctx.IDENTIFIER().getText();

        final EnumResolver resolver = EnumResolver.constructFor(deserializationConfig, javaType.getRawClass());
        final CompactStringObjectMap lookupByName = resolver.constructLookup();
        return lookupByName.find(name);
    }

    @Override
    public Object visitStruct(RONParser.StructContext ctx) {
        final JavaType javaType = javaTypes.peek();

        if (javaType.isConcrete()) {
            return buildFromStruct(javaType.getRawClass(), ctx);
        } else {
            final String structName = ctx.IDENTIFIER().getText();
            // if null - we can't proceed unless we use jackson class annotations for polymorphism

            final Class<?> matchingSubclass = findMatchingSubclass(javaType, structName);

            if (matchingSubclass != null) {
                return buildFromStruct(matchingSubclass, ctx);
            }
        }

        return null;
    }

    /**
     * Find the subclass that matches the RON struct's name. Or null if it was not found.
     */
    private Class<?> findMatchingSubclass(JavaType javaType, String structName) {
        final SubtypeResolver subtypeResolver = deserializationConfig.getSubtypeResolver();
        final MapperConfig<?> cfg = deserializationConfig;
        final AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(cfg, javaType, cfg);
        final Collection<NamedType> namedTypes = subtypeResolver.collectAndResolveSubtypesByClass(cfg, baseType);
        for (NamedType namedType: namedTypes) {
            final Class<?> foundClass = namedType.getType();
            final String foundClassName = foundClass.getSimpleName();
            if (foundClassName.equals(structName)) {
                return foundClass;
            }
        }
        return null;
    }

    private Object buildFromStruct(Class<?> klass, RONParser.StructContext ctx) {
        try {
            final Constructor<?> ctor = klass.getDeclaredConstructors()[0];
            final Object newInstance = ctor.newInstance();

            // set fields recursively
            for (RONParser.StructEntryContext entry: ctx.structEntry()) {
                final String fieldName = entry.IDENTIFIER().getText();

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
    public Object visitRoot(RONParser.RootContext ctx) {
        return this.visit(ctx.value());
    }

    @Override
    public Object visitValue(RONParser.ValueContext ctx) {
        final JavaType javaType = javaTypes.peek();

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

        return this.visitStruct(ctx.struct());
    }


}
