grammar compiladores;

@header {
package proyectocompiladores;
}

// Fragmentos básicos
fragment DIGITO: [0-9];
fragment CARACTER: [a-zA-Z];

// Tokens básicos
NUMERO: DIGITO+;
ID: (CARACTER | '_') (CARACTER | DIGITO | '_')*;
PYC: ';';
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
COMP: '==' | '!=' | '<' | '>' | '<=' | '>=';
AND: '&&';
OR: '||';
WS: [ \t\r\n]+ -> skip;

// Reglas iniciales
programa: (bloque | instruccion)* EOF;

bloque: LLA instrucciones? LLC;

instrucciones: (instruccion)*;

instruccion:
    declaracion
    | asignacion
    | expresion PYC
    ;

declaracion: tipo ID (IGUAL expresion)? PYC;

asignacion: ID IGUAL expresion PYC;

expresion: termino ((SUMA | RESTA | MULT | DIV | MOD | COMP | AND | OR) termino)*;

termino: NUMERO | ID;

tipo: 'int' | 'double' | 'bool';
