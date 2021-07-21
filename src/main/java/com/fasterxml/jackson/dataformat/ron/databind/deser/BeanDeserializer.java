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
import com.fasterxml.jackson.dataformat.ron.antlr4.RONBaseVisitor;
import com.fasterxml.jackson.dataformat.ron.antlr4.RONParser;
import com.fasterxml.jackson.dataformat.ron.databind.RONEnum;
import com.fasterxml.jackson.dataformat.ron.databind.deser.transformers.Deserializer;
import com.fasterxml.jackson.dataformat.ron.databind.deser.transformers.DeserializerChain;
import com.fasterxml.jackson.dataformat.ron.util.Constructors;
import com.fasterxml.jackson.dataformat.ron.util.JavaTypes;
import com.fasterxml.jackson.dataformat.ron.util.Lists;
import com.fasterxml.jackson.dataformat.ron.util.Strings;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This ANTLR-based parser deserializes RON for the RONMapper.
 *
 * The user says what the types should be (specifying the base javaType). If the data doesn't conform to that, it's an error.
 */
public class BeanDeserializer extends RONBaseVisitor<Object> {

    private final Deserializer[] deserializers = DeserializerChain.standard();

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

        if (javaType.isEnumType()) {
            final String name = ctx.IDENTIFIER().getText();
            final EnumResolver resolver = EnumResolver.constructFor(deserializationConfig, javaType.getRawClass());
            final CompactStringObjectMap lookupByName = resolver.constructLookup();
            return lookupByName.find(name);
        } else {
            // we ended up here from a RONEnum annotation
            Class<?> klass = javaType.getRawClass();
            try {
                final Constructor<?> ctor = Constructors.getDefaultConstructor(klass);
                final Object newInstance = ctor.newInstance();

                if (ctx != null) {
                    checkHasName(klass, ctx.IDENTIFIER().getText());

                    final Field[] klassFields = klass.getFields();

                    final int numKlassFields = klassFields.length;
                    final int numRonEnumFields = ctx.value().size();

                    if (numKlassFields != numRonEnumFields) {
                        final String klassFieldNames = getFieldNames(klassFields);
                        throw new VisitorException("RON enum had " + numRonEnumFields + " fields, but the corresponding class had " + numKlassFields + " fields " + klassFieldNames);
                    }

                    for (int i = 0; i < numKlassFields; ++i) {
                        final Field field = klassFields[i];
                        final RONParser.ValueContext value = ctx.value(i);

                        final JavaType fieldType = JavaTypes.fromField(field);
                        javaTypes.push(fieldType);
                        final Object v = this.visitValue(value);
                        javaTypes.pop();

                        field.setAccessible(true);
                        field.set(newInstance, v);
                        field.setAccessible(false);
                    }
                }

                return newInstance;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new VisitorException("Can't instantiate the class from the enum", e);
            }
        }
    }

    private static String getFieldNames(Field[] fields) {
        final StringBuilder sb = new StringBuilder();

        sb.append("(");
        for (Field field: fields) {
            sb.append(field.getName());
            sb.append(",");
        }
        sb.append(")");

        return sb.toString();
    }

    private static void checkHasName(Class<?> klass, String name) {
        final String simpleKlassName = klass.getSimpleName();

        if (!name.equals(simpleKlassName)) {
            throw new VisitorException("RON construct " + name + " did not match the specified class name " + simpleKlassName);
        }
    }

    @Override
    public Object visitStruct(RONParser.StructContext ctx) {
        final JavaType javaType = javaTypes.peek();

        final Class<?> klass;

        if (javaType.isConcrete()) {
            klass = javaType.getRawClass();
        } else {
            final String structName = ctx.IDENTIFIER().getText();

            final Class<?> matchingSubclass = findMatchingSubclass(javaType, structName);

            if (matchingSubclass == null) {
                throw new VisitorException("Could not find a subclass that matches the struct name " + structName);
            }

            klass = matchingSubclass;
        }

        try {
            final Constructor<?> ctor = Constructors.getDefaultConstructor(klass);
            final Object newInstance = ctor.newInstance();

            if (ctx != null) {
                // set fields recursively - if the struct has fields
                for (RONParser.StructEntryContext entry : ctx.structEntry()) {
                    final String fieldName = entry.IDENTIFIER().getText();

                    final Field childField = newInstance.getClass().getField(fieldName);
                    final JavaType childJavaType = JavaTypes.fromField(childField);
                    javaTypes.push(childJavaType);
                    final Object value = visitValue(entry.value());
                    javaTypes.pop();
                    childField.setAccessible(true);
                    childField.set(newInstance, value);
                    childField.setAccessible(false);
                }
            }

            return newInstance;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            throw new VisitorException("Can't instantiate the class from the struct", e);
        }
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
                    throw new VisitorException("Could not construct collection type, or no default constructor available");
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
                throw new VisitorException("Could not construct array");
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
                throw new VisitorException("Could not construct collection");
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

    @Override
    public Object visitRoot(RONParser.RootContext ctx) {
        return this.visit(ctx.value());
    }

    @Override
    public Object visitValue(RONParser.ValueContext ctx) {
        final JavaType javaType = javaTypes.peek();

        for (Deserializer deserializer: deserializers) {
            if (deserializer.canApply(javaType)) {
                return deserializer.apply(ctx);
            }
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

        if (javaType.getRawClass().isAnnotationPresent(RONEnum.class)) {
            return this.visitEnumeration(ctx.enumeration());
        }

        if (ctx.enumeration() != null && !ctx.enumeration().value().isEmpty()) {
            throw new VisitorException("Could not map the RON enum to the target class. Please ensure that the target class has the @RONEnum annotation.");
        }

        return this.visitStruct(ctx.struct());
    }


}
