// Derived from reading https://github.com/ron-rs/ron
// TODO: sync this up fully with the RON grammar document: https://github.com/ron-rs/ron/blob/master/docs/grammar.md
grammar RON;

root
   : value
   ;

tuple
   : '(' value (',' value)* (',')? ')'
   | UNIT
   ;

enumeration
   : BAREWORD tuple?
   ;

struct
    : BAREWORD '(' structEntry (',' structEntry)* ','? ')';

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

// struct keys, enum names, struct names
fragment BAREWORD: BARECHAR*;

fragment BARECHAR:
    ~ [\u0000-\u001F];

fragment ESC:
    '\\' (["\\/bfnrt] | UNICODE);

fragment UNICODE:
    'u' HEX HEX HEX HEX;

fragment HEX:
    [0-9a-fA-F];

fragment SAFECODEPOINT:
    ~ ["\\\u0000-\u001F];

NUMBER: '-'? INT ('.' [0-9] +)? EXP?;

fragment INT
   : '0' | [1-9] [0-9]*;

// no leading zeros
fragment EXP
   : [Ee] [+\-]? INT;

// \- since - means "range" inside [...]
WS: [ \t\n\r] + -> skip;