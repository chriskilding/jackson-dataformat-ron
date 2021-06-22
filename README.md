# jackson-dataformat-ron

Experimental [Rust Object Notation](https://github.com/ron-rs/ron) (RON) support for Jackson.

## Overview

There are times when JSON is too limited to represent the concepts you need to serialize. JSON notably lacks support for
types, enums, and tuples. In these situations, RON provides a more expressive syntax that can handle these concepts. (
For a longer explanation of when and why you would want to use RON, see the upstream
project: https://github.com/ron-rs/ron.)

This project is a prototype to find out whether it is feasible for RON to be a backend for Jackson, and thus allow
developers in the JVM ecosystem to use RON.

The Jackson RON backend supports the following RON types:

| Type | Generator | Parser | ObjectMapper |
--- | --- | --- | ---
|Scalars|Y|?|Y|
|Objects|Y|?|Y|
|Arrays|Y|?|Y|
|Enums|Y|?|?|
|Structs|Y|?|?|
|Tuples|Y|?|?<sup>1</sup>|

<sup>1</sup> Tuples do not exist natively in Java. They can be read or written at the token level, but you must do extra work to use them at the mapper level.

It also supports the following RON features:

- Trailing commas (parser ignores them)
- Comments (parser ignores them)

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

To use RON-specific features (structs, enums, tuples etc.), call the methods that are specific to the RON
implementations.

### RONGenerator

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
        String s = w.toString(); // => Person(givenName:"Joe",familyName:"Bloggs")
    }
}
```

### RONParser

```java
public class ParserExample {
    public void run() {
        Reader ron = new StringReader("Person(givenName:\"Joe\",familyName:\"Bloggs\")");

        try (RONParser parser = new RONFactory().createParser(ron)) {
            // FIXME decide how to handle struct name
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

#### Reading

TODO add section

#### Writing

In Rust, serialization is driven strongly by convention: objects are serialized to their closest RON type. We follow this convention wherever possible, except when deficiencies in the Java type system create ambiguity:

| Java Type | RON Type |
--- | ---
|java.util.Map|Map|
|Array|Array|
|java.util.Collection|Array|
|Enum|Enum|
|POJO|Struct (default) or Tuple|

Java does not have a native concept of tuples, so the serialization of POJOs is ambiguous. We must use **annotations** to tell the RONMapper what we want to happen.

By default, an object is serialized as a **struct**:

```java
class Book {

    private boolean abridged;
    private int numberOfPages;
    
    public boolean isAbridged() {
        return abridged;
    }
    
    public void setAbridged(boolean abridged) {
        this.abridged = abridged;
    }
    
    public int getNumberOfPages() {
        return numberOfPages;
    }
    
    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}

// => Book(abridged: true, numberOfPages: 1)
```

To serialize as a **tuple** instead:

```java
@JsonSerialize(using = RONTupleSerializer.class)
class Book {
    // ...
}

// => (true, 1)
```

## Limitations

The following limitations are in place due to the prototype nature of this code:

- The `RONGenerator` uses direct calls to its delegate `Writer`. It does not use copy buffers.
- There is no pretty printer for RON.
