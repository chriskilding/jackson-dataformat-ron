# jackson-dataformat-ron

Experimental [Rust Object Notation](https://github.com/ron-rs/ron) (RON) support for Jackson.

This is a project to find out whether it is feasible for RON to be a backend for Jackson, or whether Jackon is too JSON-specific for that to work.

## Design notes

### Generator

Which RON features can a Jackson generator support?

- [x] True primitives (not string-wrapped)
  - `writeBoolean(boolean)`
  - `writeNumber(int)`
  - `writeNumber(long)`
  - `writeNumber(double)`
  - `writeNumber(float)`
- [x] Strings
  - `writeString(char[])`
  - `writeUTF8Sring(byte[])`
- [x] Maps (`writeStartObject()` / `writeFieldName(String)` / `writeEndObject()`)
- [x] Arrays (`writeStartArray()` / `writeEndArray()`)
- [ ] Enums (test `foo.getClass().isEnum()` in custom ObjectMapper?)
- [ ] Structs (optional names, field names not quoted)
- [ ] Tuples

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

Notably, the low-level API **does not** have these constructs:

- Generator: `writeStartRoundBracket()` / `writeEndRoundBracket()`
- Parser: `isExpectedStartRoundBracketToken()` (`JsonToken.START_ROUND_BRACKET` and `JsonToken.END_ROUND_BRACKET`)
  
Which we might need for structs, tuples, and enums.

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