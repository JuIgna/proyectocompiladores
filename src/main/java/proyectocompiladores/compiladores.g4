grammar compiladores;

 @header { package proyectocompiladores; }


fragment DIGITO: [0-9];
fragment LETRA: [a-zA-Z];

NUM: DIGITO+;
DOUBLE_NUM: DIGITO+ '.' DIGITO* | DIGITO* '.' DIGITO+;

TIPO_INT: 'int';
TIPO_DOUBLE: 'double';
TIPO_VOID: 'void';
COND_IF: 'if';
COND_ELSE: 'else';
CICLO_FOR: 'for';
CICLO_WHILE: 'while';
RETORNO: 'return';
TIPO_BOOL: 'bool';
VALOR_TRUE: 'true';
VALOR_FALSE: 'false';
FUNC_PRINT: 'printf';

PUNTO_COMA: ';';
COMA: ',';
ASIG: '=';
SUM: '+';
RES: '-';
MUL: '*';
DIV: '/';
MODULO: '%';
PARENTESIS_A: '(';
PARENTESIS_C: ')';
LLAVE_A: '{';
LLAVE_C: '}';
COMP_OP: '==' | '!=' | '<' | '>' | '<=' | '>=';
ID: (LETRA | '_') (LETRA | DIGITO | '_')*;
PUNTO: '.';
CADENA: '"' (~["\\] | '\\' .)* '"';
AND_OP: '&&';
OR_OP: '||';
INCR: SUM SUM;
DECR: RES RES;

WS: [ \t\r\n]+ -> skip;

programa: (bloqueFuncion | instruccion)* EOF;

bloqueInstrucciones: (instruccion)*;

instruccion:
    defFuncion
    | bloque
    | definicion
    | asignar
    | controlFlujo
    | llamadaFuncion PUNTO_COMA
    | expresion PUNTO_COMA
    | retorno
    ;

bloque: LLAVE_A bloqueInstrucciones? LLAVE_C;

error: ~LLAVE_C+;

defFuncion: tipo ID PARENTESIS_A parametros? PARENTESIS_C PUNTO_COMA;

llamadaFuncion: ID PARENTESIS_A (expresion (COMA expresion)*)? PARENTESIS_C;

bloqueFuncion: tipo ID PARENTESIS_A parametros? PARENTESIS_C bloque;

parametros: param (COMA param)*;

param: tipo ID;

tipo: TIPO_INT | TIPO_DOUBLE | TIPO_BOOL | TIPO_VOID;

definicion: tipo ID (ASIG expresion)? PUNTO_COMA;

asignar: ID ASIG expresion PUNTO_COMA;

controlFlujo: ifElse | whileLoop | forLoop;

ifElse: COND_IF PARENTESIS_A expresion PARENTESIS_C bloque (COND_ELSE bloque)?;

whileLoop: CICLO_WHILE PARENTESIS_A expresion PARENTESIS_C bloque;

forLoop:
    CICLO_FOR PARENTESIS_A inicializacion PUNTO_COMA? condicion? PUNTO_COMA actualizacion? PARENTESIS_C bloque;

inicializacion:
    definicion
    | asignar
    |
    ;

condicion:
    expresion
    |
    ;

actualizacion:
    (asignar | incrementoDecremento) (COMA (asignar | incrementoDecremento))*;

expresion:
    expresionLogica
    ;

expresionLogica:
    expresionComparacion (op_logicas expresionComparacion)*;

expresionComparacion:
    expresionAritmetica (COMP_OP expresionAritmetica)?;

expresionAritmetica:
    termino ((SUM | RES) termino)*;

termino:
    factor ((MUL | DIV | MODULO) factor)*;

factor:
    PARENTESIS_A expresion PARENTESIS_C
    | booleano
    | ID
    | CADENA
    | llamadaPrints
    | llamadaFuncion
    | incrementoDecremento
    | RES? NUM
    | DOUBLE_NUM
    ;

op_logicas: AND_OP | OR_OP;

llamadaPrints:
    FUNC_PRINT PARENTESIS_A ((CADENA (COMA expresion)* | expresion))? PARENTESIS_C;

argumentos: expresion (COMA expresion)*;

booleano: VALOR_TRUE | VALOR_FALSE;

incrementoDecremento: ID (INCR | DECR);

retorno: RETORNO expresion? PUNTO_COMA;
