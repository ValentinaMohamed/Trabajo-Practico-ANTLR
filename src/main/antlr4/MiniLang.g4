grammar MiniLang;

programa : instruccion* EOF ;

instruccion
    : imprimir
    ;

imprimir
    : 'print' '(' expresion ')' ';'
    ;

expresion
    : CADENA
    | ENTERO
    | REAL
    | BOOLEANO
    ;

CADENA   : '"' .*? '"' ;
ENTERO   : [0-9]+ ;
REAL     : [0-9]+ '.' [0-9]+ ;
BOOLEANO : 'true' | 'false' ;

WS : [ \t\r\n]+ -> skip ;