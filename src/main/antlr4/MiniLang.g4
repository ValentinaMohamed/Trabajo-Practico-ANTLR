grammar MiniLang;

programa : instruccion* EOF ;

instruccion
    : imprimir
    | declaracion
    | asignacion
    ;

imprimir
    : 'print' '(' expresion ')' ';'
    ;

declaracion
    : 'var' ID ':' tipo ('=' expresion)? ';'
    ;
    
asignacion
    : ID '=' expresion ';'
    ;

tipo
    : 'int'
    | 'real'
    | 'string'
    | 'bool'
    ;

expresion
    : CADENA
    | ENTERO
    | REAL
    | BOOLEANO
    | ID
    ;

ID : [a-zA-Z_][a-zA-Z0-9_]* ;
CADENA   : '"' .*? '"' ;
REAL     : [0-9]+ '.' [0-9]+ ;
ENTERO   : [0-9]+ ;
BOOLEANO : 'true' | 'false' ;

COMENTARIO_LINEA
    : '//' ~[\r\n]* -> skip
    ;

COMENTARIO_BLOQUE
    : '/*' .*? '*/' -> skip
    ;

WS : [ \t\r\n]+ -> skip ;