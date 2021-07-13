// Derived from reading https://github.com/ron-rs/ron
// TODO: sync this up fully with the RON grammar document: https://github.com/ron-rs/ron/blob/master/docs/grammar.md
grammar RON;

root
   : value EOF
   ;

tuple
   : START_TUPLE value (',' value)* (',')? END_TUPLE
   | START_TUPLE END_TUPLE
   ;

// RON enums with child fields are not currently supported
enumeration
   : IDENTIFIER (START_TUPLE END_TUPLE)?
   ;

struct
    : IDENTIFIER? START_TUPLE structEntry (',' structEntry)* ','? END_TUPLE;

structEntry
    : IDENTIFIER ':' value
    ;

map
   : START_MAP mapEntry (',' mapEntry)* ','? END_MAP
   | START_MAP END_MAP
   ;

mapEntry
   : STRING ':' value
   ;

array
   : START_ARRAY value (',' value)* ','? END_ARRAY
   | START_ARRAY END_ARRAY
   ;

value
   : STRING
   | NUMBER
   | map
   | array
   | tuple
   | enumeration
   | struct
   | TRUE
   | FALSE
   | INF
   | MINUS_INF
   | NAN
   ;

TRUE: 'true';

FALSE: 'false';

INF: 'inf';
MINUS_INF: '-inf';

NAN: 'NaN';

STRING: '"' (ESC | SAFECODEPOINT)* '"';

NUMBER: '-'? INT ('.' [0-9] +)? EXP?;

// struct keys, enum names, struct names
IDENTIFIER: [a-zA-Z] [0-9a-zA-Z]*;

START_MAP: '{';
END_MAP: '}';

START_ARRAY: '[';
END_ARRAY: ']';

// the idea of 'tuple' is reused across RON structs and enums ('tuple struct' etc)
START_TUPLE: '(';
END_TUPLE: ')';

fragment ESC:
    '\\' (["\\/bfnrt] | UNICODE);

fragment UNICODE:
    'u' HEX HEX HEX HEX;

fragment HEX:
    [0-9a-fA-F];

fragment SAFECODEPOINT:
    ~ ["\\\u0000-\u001F];

fragment INT
   : '0' | [1-9] [0-9]*;

// no leading zeros
fragment EXP
   : [Ee] [+\-]? INT;

// \- since - means "range" inside [...]
WS: [ \t\n\r] + -> skip;

COMMENT
   : '/*' .*? '*/' -> skip;

LINE_COMMENT
   : '//' ~[\r\n]* -> skip;