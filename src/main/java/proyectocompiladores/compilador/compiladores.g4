grammar compiladores;
@header { package proyectocompiladores.compilador; } // en el header se determina el paquete de las clases generadas

fragment DIGITO: [0-9];
fragment CARACTER: [a-zA-Z];

NUMERO: DIGITO+;
DOUBLE_LITERAL: DIGITO+ '.' DIGITO* | DIGITO* '.' DIGITO+;
CHAR_LITERAL: '\'' . '\'';

INT: 'int';
DOUBLE: 'double';
VOID: 'void';
CHAR: 'char';
IF: 'if';
ELSE: 'else';
FOR: 'for';
WHILE: 'while';
RETURN: 'return';
BOOL: 'bool';
TRUE: 'true';
FALSE: 'false';
PRINTF: 'printf';

PYC: ';';
COMA: ',';
IGUAL: '=';
SUMA: '+';
RESTA: '-';
MULT: '*';
DIV: '/';
MOD: '%';
PA: '(';
PC: ')';
LLA: '{';
LLC: '}';
CORCHETE: '[';
CORCHETE_C: ']';
COMP: '==' | '!=' | '<' | '>' | '<=' | '>=';
ID: (CARACTER | '_') (CARACTER | DIGITO | '_')*;
PUNTO: '.';
STRING: '"' ( ~["\\] | '\\' .)* '"';
AND: '&&';
OR: '||';
INCREMENTO: SUMA SUMA;
DECREMENTO: RESTA RESTA;

WS: [ \t\r\n]+ -> skip;

// ignorar comentarios
LC: '//' ~[\r\n]* -> skip;
BC: '/*' .*? '*/' -> skip;
ERROR: . ; //c

programa: (cuerpoFuncion | instruccion)* EOF;

instrucciones: (instruccion)*;

instruccion:
	declaracionFuncion
	| bloque
	| declaracion
	| asignacion
	| estructuraControl
	| llamadaFuncion PYC
	| expresion PYC
	| return
	;

bloque: LLA instrucciones? LLC;

error: ~LLC+;

declaracionFuncion: tipo ID PA parametros? PC PYC;

llamadaFuncion: ID PA (expresion (COMA expresion)*)? PC;

cuerpoFuncion: tipo ID PA parametros? PC bloque;

parametros: parametro (COMA parametro)*;

parametro: tipo ID;

tipo: INT | DOUBLE | BOOL | VOID | CHAR;

//  declaracion: tipo ID (IGUAL expresion)? PYC;
 declaracion: tipo declarador (COMA declarador)* PYC;

 declarador: ID (CORCHETE NUMERO CORCHETE_C)? (IGUAL expresion)?;

asignacion: (ID | ID CORCHETE expresion CORCHETE_C) IGUAL expresion PYC;

estructuraControl: ifElse | whileLoop | forLoop;

ifElse: IF PA expresion PC bloque (ELSE bloque)?;

whileLoop: WHILE PA expresion PC bloque;

forLoop:
    FOR PA inicializacion? PYC condicion? PYC actualizacion? PC bloque;

inicializacion:
    declaracion
    | asignacion
    ;

condicion:
    expresion
    ;

actualizacion:
    (asignacion | incrementoDecremento) (COMA (asignacion | incrementoDecremento))*;


expresion:
    expresionLogica
    ;

expresionLogica:
    expresionComparacion (op_logicas expresionComparacion)*
    ;

expresionComparacion:
    expresionAritmetica (COMP expresionAritmetica)?
    ;

expresionAritmetica:
    termino ((SUMA | RESTA) termino)*
    ;

termino:
    factor ((MULT | DIV | MOD) factor)*
    ;

factor:
    PA expresion PC
    | booleano
    | ID (CORCHETE expresion CORCHETE_C)
    | ID
    | STRING
    | CHAR_LITERAL
    | llamadaPrints
    | llamadaFuncion
    | incrementoDecremento
    | RESTA? NUMERO
    | DOUBLE_LITERAL
    ;

op_aritmeticos: SUMA | RESTA | MULT | DIV | MOD;

op_logicas: AND | OR;




llamadaPrints:
	PRINTF PA ((STRING (COMA expresion)* | expresion))? PC;

argumentos: expresion (COMA expresion)*;

booleano: TRUE | FALSE;

incrementoDecremento: ID (INCREMENTO | DECREMENTO);

return: RETURN expresion? PYC
        ;

