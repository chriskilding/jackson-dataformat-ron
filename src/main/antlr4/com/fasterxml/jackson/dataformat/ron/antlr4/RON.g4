// Derived from reading https://github.com/ron-rs/ron
// TODO: sync this up fully with the RON grammar document: https://github.com/ron-rs/ron/blob/master/docs/grammar.md
grammar RON;

root
   : value EOF
   ;

tuple
   : '(' value (',' value)* (',')? ')'
   | UNIT
   ;

// TODO support enums with child fields
enumeration
   : BAREWORD UNIT?
   ;

struct
    : BAREWORD? '(' structEntry (',' structEntry)* ','? ')';

structEntry
    : BAREWORD ':' value
    ;

map
   : '{' mapEntry (',' mapEntry)* ','? '}'
   | '{' '}'
   ;

mapEntry
   : STRING ':' value
   ;

array
   : '[' value (',' value)* ','? ']'
   | '[' ']'
   ;

value
   : STRING
   | NUMBER
   | map
   | array
   | tuple
   | enumeration
   | struct
   | UNIT
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

UNIT: '(' ')';

STRING: '"' (ESC | SAFECODEPOINT)* '"';

NUMBER: '-'? INT ('.' [0-9] +)? EXP?;

// struct keys, enum names, struct names
BAREWORD: [a-zA-Z] [0-9a-zA-Z]*;

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