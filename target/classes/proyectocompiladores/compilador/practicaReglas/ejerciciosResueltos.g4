grammar ejerciciosResueltos;
@header { package proyectocompiladores.compilador.practicaReglas; } // en el header se determina el paquete de las clases generadas

// reglas generales a todos los ejercicios
// ignorar comentarios
LC: '//' ~[\r\n]* -> skip;
BC: '/*' .*? '*/' -> skip;

// ejercicio 5
/* 
s: ID IGUAL expresion;
expresion: expresion SUMA termino | termino;
termino: ID;

ID: [a-zA-Z_][a-zA-Z0-9_]*;
SUMA: '+';
IGUAL: '=';

*/

// ejercicio 14
IF: 'if';
MAYOR: '>';
ID: [a-zA-Z_][a-zA-Z0-9_]*;
PA: '(';
PC: ')';

s: IF PA condicion PC s;
condicion: e MAYOR e;
e: ID;



