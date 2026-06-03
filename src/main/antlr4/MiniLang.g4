grammar MiniLang;

programa : instruccion* EOF ;

instruccion
    : imprimir
    ;

imprimir
    : 'print' '(' CADENA ')' ';'
    ;

CADENA : '"' .*? '"' ;

WS : [ \t\r\n]+ -> skip ;