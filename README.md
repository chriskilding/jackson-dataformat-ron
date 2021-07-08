# jackson-dataformat-ron

Experimental [Rust Object Notation](https://github.com/ron-rs/ron) (RON) support for Jackson.

This project is a prototype. **It is not suitable for production use**.

## Overview

There are times when JSON is too limited to represent the concepts you need to serialize. JSON notably lacks support for
types, enums, and tuples. In these situations, RON provides a more expressive syntax that can handle these concepts. (For a longer explanation of when and why you would want to use RON, see the upstream
project: https://github.com/ron-rs/ron.)

This project is a prototype to find out whether it is feasible for RON to be a backend for Jackson, and thus allow
developers in the JVM ecosystem to use RON.

The Jackson RON backend supports the following RON types:

| Type | Generator | Parser | ObjectMapper (read) | ObjectMapper (write) |
--- | --- | --- | --- | ---
|Scalars|Y|Y|Y|Y|
|Maps|Y|Y|Y|Y|
|Arrays|Y|Y|Y|Y|
|Structs|Y|Y|Y|Y|
|Enums|Y|Y|Y<sup>1</sup>|Y<sup>1</sup>|
|Tuples|Y|Y|N<sup>2</sup>|N<sup>2</sup>|

<small><sup>1</sup> Java enums are simple, and cannot have user-defined child fields. Therefore the ObjectMapper only supports simple RON enums without child fields.</small>

<small><sup>2</sup> Java does not have a native concept of tuples. Therefore the ObjectMapper does not support them.</small>

The Jackson RON backend also supports the following RON features:

- Trailing commas (parser ignores them)
- Comments (parser ignores them)

### Limits of RON support

The low-level `RONParser` and `RONGenerator` support all of RON.

The high-level `RONMapper` only supports the subset of RON types that have equivalents in Java's type system. This is
to preserve the property of **round-trip compatibility**, i.e. `serialize(deserialize(x)) = x` using default
ObjectMapper parameters.

Long explanation: It would be possible, with an extension like a `@RONFormat(shape = Shape.ENUM)` annotation, to
manually instruct the ObjectMapper to de/serialize a RON enum `Foo(1, true)` into a POJO with a
matching `Foo(int, boolean)` constructor. The trouble is that if this manual override is missing, the default
serialization of the POJO is ambiguous; the ObjectMapper cannot know that the POJO should turn back into a RON enum.
It might serialize it as a struct instead, creating an unexpected surprise for the user. Therefore it is safer to not support RON types beyond those with Java equivalents.

## Setup

First build this project from source:

```shell
mvn clean install
```

Then add the dependency to your Maven POM:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-ron</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Usage

Use the `RONGenerator`, `RONParser`, or `RONMapper` from your code as you would any other Jackson backend.

### RONGenerator

To write RON constructs, call the RON-specific `writeXXX` methods on the `RONGenerator`:

- Enums: `writeStartEnum(String)` / `writeEndEnum()`
- Simple enums: `writeEnum(String)`
- Structs: `writeStartStruct()` / `writeEndStruct()`
- Named structs: `writeStartStruct(String)` / `writeEndStruct()`
- Tuples: `writeStartTuple()` / `writeEndTuple()`

```java
public class GeneratorExample {
    public void run() {
        StringWriter w = new StringWriter();
        try (RONGenerator generator = new RONFactory().createGenerator(w)) {
            generator.writeStartStruct("Person");
            generator.writeFieldName("givenName");
            generator.writeString("Joe");
            generator.writeFieldName("familyName");
            generator.writeNumber("Bloggs");
            generator.writeEndStruct();
        }
        String s = w.toString();
        // => Person(givenName:"Joe",familyName:"Bloggs")
    }
}
```

### RONParser

To read RON constructs, call the RON-specific reader methods on the RONParser:

- `nextIdentifier()` (struct names, struct keys, enum names)
- `nextRONToken()` (any token)

Note: `nextToken()` is provided for compatibility with Jackson's `JsonParser` supertype, but it is fragile. It only works if the input is constrained to the JSON subset of RON. You should generally use `nextRONToken()` instead.

```java
public class ParserExample {

    void parseStruct() {
        Reader ron = new StringReader("Person(givenName:\"Joe\",familyName:\"Bloggs\")");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            String ident1 = parser.nextIdentifier();     // => "Person"
            parser.nextRONToken();                       // enter struct
            String field1 = parser.nextIdentifier();     // => "givenName"
            String givenName = parser.nextTextValue();   // => "Joe"
            String field2 = parser.nextIdentifier();     // => "familyName"
            String familyName = parser.nextTextValue();  // => "Bloggs"
            parser.nextRONToken();                       // exit struct
        }
    }

    void parseTuple() {
        Reader ron = new StringReader("(1, true)");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            parser.nextRONToken();                       // enter tuple
            int field1 = parser.nextIntValue();          // => 1
            boolean field2 = parser.nextBooleanValue();  // => true
            parser.nextRONToken();                       // exit tuple
        }
    }
    
    void parseEnum() {
        Reader ron = new StringReader("Foo(1, true)");
  
        try (RONParser parser = new RONFactory().createParser(ron)) {
            String enumName = parser.nextIdentifier();   // => "Foo"
            parser.nextRONToken();                       // enter enum
            int field1 = parser.nextIntValue();          // => 1
            boolean field2 = parser.nextBooleanValue();  // => true
            parser.nextRONToken();                       // exit enum
        }
    }
}
```

### RONMapper

In Rust, serialization is driven strongly by convention: objects are mapped to their closest RON type. We follow this
convention as closely as possible.

| Java Type | RON Type |
--- | ---
|java.util.Map|Map|
|Array|Array|
|java.util.Collection|Array|
|Enum|Enum<sup>1</sup>|
|POJO|Struct|

<small><sup>1</sup> Only simple enums (without child fields) are supported.</small>

To read or write an object, just use the `RONMapper` like you would use the `ObjectMapper`:

```java
class MapperExample {

    // A typical POJO
    static class Book {
        public boolean abridged;
        public int numberOfPages;

        Book(boolean abridged, int numberOfPages) {
            this.abridged = abridged;
            this.numberOfPages = numberOfPages;
        }
    }

    void write() {
        Book book = new Book(true, 1);
        String str = new RONMapper().writeValueAsString(book);
        // => Book(abridged:true,numberOfPages:1)
    }

    void read() {
        String ron = "Book(abridged:true,numberOfPages:1)";
        Book book = new RONMapper().readValue(ron, Book.class);
        // => new Book(true, 1)
    }
}
```

## Examples

Examples of things you can do with Jackson + RON that would be cumbersome or impossible with Jackson + JSON.

### Polymorphism with named structs

RON's named structs make polymorphism and heterogenous data arrays easier.

Imagine you have a class hierarchy like this:

```java
public interface Animal {
    String sound();
}

public class Cat implements Animal {
    public boolean meow;

    public Cat() {
    }

    public String sound() {
        return "meow";
    }
}

public class Dog implements Animal {
    public int barks;

    public Dog() {
    }

    public String sound() {
        return "bark";
    }
}
```

With JSON you have to use one of **several** **informal** ways to encode the type information in a JSON object (options include a synthetic property e.g. `@type`, or a fake union type e.g. wrapper object, wrapper array):

```json
{
  "@type": "Dog",
  "barks": 2
}
```

```json
{
  "@type": "Cat",
  "meow": true
}
```

You must also annotate the supertype to tell Jackson which encoding strategy you're using:

```java
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
        @JsonSubTypes.Type(Cat.class),
        @JsonSubTypes.Type(Dog.class),
})
public interface Animal {
    String sound();
}
```

By contrast, RON struct names mean there is **one** **standardised** way to encode the type information.

The RON looks like this:

```ron
Dog(barks: 2)
```

```ron
Cat(meow: true)
```

The annotations are simply the list of possible subtypes:

```java
@JsonSubTypes({
        @JsonSubTypes.Type(Cat.class),
        @JsonSubTypes.Type(Dog.class),
})
public interface Animal {
    String sound();
}
```

## Limitations

The following intentional design limitations are in place due to the prototype nature of this code:

- The `RONGenerator` only supports `Reader` and `Writer` based de/serializers. It does not support char array
  de/serializers.
- There is no pretty printer for RON. The `RONGenerator` produces the compact form, without whitespace.
- The `RONMapper` does not support custom de/serialization `Features`.
