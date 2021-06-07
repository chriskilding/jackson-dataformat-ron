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
- [ ] Enums
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
- Enums: Can we lean on `@JsonFormat` or custom `StdSerializer` implementations to read/write enums?
- Tuples: How to deal with them?
- What about RON field ordering?