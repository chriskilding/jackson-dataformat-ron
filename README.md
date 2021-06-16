# jackson-dataformat-ron

Experimental [Rust Object Notation](https://github.com/ron-rs/ron) (RON) support for Jackson.

## Overview

There are times when JSON is too limited to represent the concepts you need to serialize. JSON notably lacks support for the following concepts:

- Types
- Enums
- Comments

In these situations, RON provides a more expressive syntax that can handle these concepts.

For a longer explanation of when and why you would want to use RON, see the upstream project: https://github.com/ron-rs/ron.

This project is a prototype to find out whether it is feasible for RON to be a backend for Jackson, and thus allow developers in the JVM ecosystem to use RON.

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

To use RON-specific features (structs, enums, tuples etc.), call the methods that are specific to the RON implementations.

`RONGenerator`:

```java
public class Example {
  public void example() {
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

### Feature support

This table shows the support for RON types in the Jackson APIs:

| Type | Generator | Parser | ObjectMapper |
--- | --- | --- | ---
|Scalars|Y|?|Y|
|Objects|Y|?|Y|
|Arrays|Y|?|Y|
|Enums|Y|?|?|
|Structs|Y|?|?|
|Tuples|Y|?|N<sup>1</sup>|
|Comments|?|N<sup>2</sup>|N<sup>2</sup>|

<sup>1</sup> Tuples do not exist natively in Java. They can only be read or written at the token level.
<br>
<sup>2</sup> The parser will ignore any comments in the RON source.


Going in the other direction, the following Jackson features are **not** supported in RON (attempting to use these features will result in a runtime error):

- Null values
- Schemas

## Limitations

The following limitations are in place due to the prototype nature of this code:

- The `RONGenerator` uses direct calls to its delegate `Writer`. It does not use copy buffers.
- There is no pretty printer for RON.

----

## Design notes

### Parser

Which RON features can a Jackson parser support?

- [x] True primitives
  - `getFloatValue(): float`
  - `getIntValue(): int`
  - `getLongValue(): long`
  - `getDoubleValue(): double`
  - `getBooleanValue(): boolean`
- [x] Strings
  - `getText(): String`
  - `getTextCharacters(): char[]` 
- [x] Comments (we must ignore them in the parser)
- [x] Trailing commas (we must ignore them in the parser)
- [x] Maps
- [x] Arrays
- [ ] Enums
- [ ] Structs
- [ ] Tuples

### Questions

- Structs: Can we get support for them if we make a custom ObjectMapper?
- Enums: Can we...
  - lean on `@JsonFormat` 
  - lean on custom Databind `Serializer` implementations
  - implement a custom `EnumResolver`
  - use `getClass().isEnum()`
- Tuples: How to deal with them?
- What about RON field ordering?

Note: the low-level Parser API **does not** have `isExpectedStartRoundBracketToken()` (`JsonToken.START_ROUND_BRACKET` and `JsonToken.END_ROUND_BRACKET`),  which we might need for structs, tuples, and enums.

### Enums

#### Parser

The ObjectMapper would look like this for reading:

```java
import com.fasterxml.jackson.databind.ObjectMapper;

class EnumTester {
  void test() {
    ObjectMapper mapper = new ObjectMapper();
    Foo foo = mapper.readValue("...", Foo.class);
  }
}
```

So you would know the type `klazz` inside a custom ObjectMapper. So you should be able to call `klazz.isEnum()` on that.

Could also use `EnumResolver` (the Jackson utility class) here?

### Structs

How will we know whether to de/serialize a RON entity as a Struct (vs a Map)?

TLDR: Looks possible in custom `ObjectMapper`. Don't know if possible in the streaming API yet.

#### Parser

Ask whether the `klass` to `readValue()` is an `instanceof` `Map` - if not then it's just to be treated as a (struct-like) class:

```java
import com.fasterxml.jackson.databind.ObjectMapper;

class EnumTester {
  void test() {
    ObjectMapper mapper = new ObjectMapper();
    Foo foo = mapper.readValue("...", Foo.class);
  }
}
```