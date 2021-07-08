package com.fasterxml.jackson.dataformat.ron.databind.deser;

import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedClassResolver;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
import com.fasterxml.jackson.databind.util.EnumResolver;
import com.fasterxml.jackson.dataformat.ron.util.Constructors;
import com.fasterxml.jackson.dataformat.ron.util.Lists;
import com.fasterxml.jackson.dataformat.ron.util.Strings;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONBaseVisitor;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * This ANTLR-based parser deserializes RON for the RONMapper.
 *
 * The user says what the types should be (specifying the base javaType). If the data doesn't conform to that, it's an error.
 */
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

    @Override
    public Object visitArray(RONParser.ArrayContext ctx) {
        final JavaType javaType = javaTypes.peek();

        if (javaType.isCollectionLikeType()) {
            final JavaType elementType = javaType.getContentType();

            final List coll;

            if (javaType.isConcrete()) {
                // TODO might be null
                final Constructor<?> ctor = Constructors.getDefaultConstructor(javaType.getRawClass());

                try {
                    coll = (List) ctor.newInstance();
                } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    throw new IllegalStateException("Could not construct collection type, or no default constructor available");
                }
            } else {
                // use a default concrete collection type
                coll = new ArrayList();
            }

            if (ctx != null) {
                for (RONParser.ValueContext value: ctx.value()) {
                    this.javaTypes.push(elementType);
                    Object element = this.visitValue(value);
                    coll.add(element);
                    this.javaTypes.pop();
                }
            }

            return coll;
        }

        if (javaType.isArrayType()) {
            final JavaType elementType = javaType.getContentType();

            // can't append to array, must use ArrayList as builder and then convert to array
            // can't simply do `new ArrayList()`, must use this indirection to construct an ArrayList with element type parameters
            final CollectionType collectionType = TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, elementType);
            final Constructor<?> ctor = Constructors.getDefaultConstructor(collectionType.getRawClass());
            List coll;
            try {
                coll = (List) ctor.newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Could not construct array");
            }

            if (ctx != null) {
                for (RONParser.ValueContext value: ctx.value()) {
                    this.javaTypes.push(elementType);
                    Object element = this.visitValue(value);
                    coll.add(element);
                    this.javaTypes.pop();
                }
            }

            return Lists.toArray(coll, elementType.getRawClass());
        }

        return null;
    }

    @Override
    public Object visitMap(RONParser.MapContext ctx) {
        final JavaType javaType = javaTypes.peek();

        final JavaType valueType = javaType.getContentType();
        final Map map;

        if (javaType.isConcrete()) {
            final Constructor<?> ctor = Constructors.getDefaultConstructor(javaType.getRawClass());
            try {
                map = (Map) ctor.newInstance();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Could not construct collection");
            }
        } else {
            // probably Map<K, V> so choose a default subtype
            map = new LinkedHashMap();
        }

        // iterate entries if it's not empty
        if (ctx.mapEntry() != null) {
            for (RONParser.MapEntryContext entry : ctx.mapEntry()) {
                final String key = Strings.removeEnclosingQuotes(entry.STRING().getText());

                javaTypes.push(valueType);
                final Object value = this.visitValue(entry.value());
                javaTypes.pop();

                map.put(key, value);
            }
        }

        return map;
    }

    /**
     * Find the subclass that matches the RON struct's name. Or null if it was not found.
     */
    private Class<?> findMatchingSubclass(JavaType javaType, String structName) {
        final SubtypeResolver subtypeResolver = deserializationConfig.getSubtypeResolver();
        final AnnotatedClass baseType = AnnotatedClassResolver.resolveWithoutSuperTypes(deserializationConfig, javaType, deserializationConfig);
        final Collection<NamedType> namedTypes = subtypeResolver.collectAndResolveSubtypesByClass(deserializationConfig, baseType);
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
            final Constructor<?> ctor = Constructors.getDefaultConstructor(klass);
            final Object newInstance = ctor.newInstance();

            if (ctx != null) {
                // set fields recursively - if the struct has fields
                for (RONParser.StructEntryContext entry : ctx.structEntry()) {
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
