# jackson-dataformat-ron

Experimental [Rust Object Notation](https://github.com/ron-rs/ron) (RON) support for Jackson.

This is a project to find out whether it is feasible for RON to be a backend for Jackson, or whether Jackon is too JSON-specific for that to work.

## Design notes

### Generator

Which RON features can a Jackson generator support?

- [x] True primitives (not string-wrapped)
  - `writeBoolean(bool)`
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

Questions:

- How can we support the missing RON features in a custom Jackson generator? Can we do it if we make a custom ObjectMapper?
- What about field ordering?

### Parser

Which RON features can a Jackson parser support?

- [x] Comments (we must ignore them in the parser)
- [x] Trailing commas (we must ignore them in the parser)
- [ ] TODO add others