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
|Scalars|Y|?|Y|Y|
|Maps|Y|?|Y|Y|
|Arrays|Y|?|Y|Y|
|Enums|Y|?|?|?|
|Structs|Y|?|Y|Y|
|Tuples|Y|?|N<sup>1</sup>|N<sup>1</sup>|

<small><sup>1</sup> Java does not have a native concept of tuples. They can be read or written at the token level, but
cannot (currently) be used with the ObjectMapper.</small>

The Jackson RON backend also supports the following RON features:

- Trailing commas (parser ignores them)
- Comments (parser ignores them)

### Outcomes

The prototype has yielded the following outcomes:

- Though RON was made for a language (Rust) with a more powerful type system than Java's, it is not alien to use from Java, and has tangible benefits for Java programs.
- A couple of Jackson's `protected final` methods need to be changed to allow a RON backend to override their behavior. See code comments for details.

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

To write RON constructs, call the RON-specific `writeXXX` methods on the `RONGenerator`.

- Enums:
    - Simple enums: `writeEnum(String)`
    - Compound enums: `writeStartEnum(String)` / `writeEndEnum()`
- Structs:
    - Simple structs: `writeStartStruct()` / `writeEndStruct()`
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

TODO document

```java
public class ParserExample {
    public void run() {
        Reader ron = new StringReader("Person(givenName:\"Joe\",familyName:\"Bloggs\")");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            // TODO decide how to handle struct name
            parser.nextToken(); // enter struct
            String field1 = parser.nextFieldName();     // => "givenName"
            String givenName = parser.nextTextValue();  // => "Joe"
            String field2 = parser.nextFieldName();     // => "familyName"
            String familyName = parser.nextTextValue(); // => "Bloggs"
            parser.nextToken(); // exit struct
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
|Enum|Enum|
|POJO|Struct<sup>1</sup>|

<small>
<sup>1</sup> Java does not have a native concept of tuples, so POJOs can only be mapped to structs at the moment.
</small>

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

    static void write() {
        Book book = new Book(true, 1);
        String str = new RONMapper().writeValueAsString(book);
        // => Book(abridged:true,numberOfPages:1)
    }

    static void read() {
        String ron = "Book(abridged:true,numberOfPages:1)";
        Book book = new RONMapper().readValue(ron, Book.class);
        // => new Book(true, 1)
    }
}
```

## Limitations

The following design limitations are in place due to the prototype nature of this code:

- The `RONGenerator` only supports `Reader` and `Writer` based de/serializers. It does not support char array
  de/serializers.
- There is no pretty printer for RON.
- The `RONMapper` has no custom de/serialization `Features`.

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

    public Cat() {}

    public String sound() {
        return "meow";
    }
}

public class Dog implements Animal {
    public int barks;

    public Dog() {}

    public String sound() {
        return "bark";
    }
}
```

With JSON you have to use one of **several** **informal** strategies to encode the type information in a JSON object (options
include a property e.g. `@type`, a wrapper object acting as a fake union type, or a wrapper array):

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

You also have to tell Jackson which encoding strategy you're using, by annotating the supertype:

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
