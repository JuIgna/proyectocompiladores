Debo presentar un examen final sobre un proyecto que usa java y antlr 4, la idea es que recibe un cpp como entrada con un codigo que ya probo
mi profesor y debe producir la salida del archivo salida_correcta. Esto es lo que debe dar como salida:


* Formato de Salida Esperada

```
🚀 Iniciando compilación de: ejemplo_correcto.cpp
============================================================

=== 1. ANÁLISIS LÉXICO ===
✅ Análisis léxico completado sin errores.
   📊 Tokens procesados: 152

=== 2. ANÁLISIS SINTÁCTICO ===
✅ Análisis sintáctico completado sin errores.
   📊 Árbol sintáctico generado correctamente

=== 3. VISUALIZACIÓN DEL AST ===
   📊 Ventana del árbol sintáctico abierta

=== 4. ANÁLISIS SEMÁNTICO ===
   📋 Tabla de símbolos construida:

=== TABLA DE SÍMBOLOS ===
NOMBRE          TIPO       CATEGORÍA       LÍNEA      COLUMNA    ÁMBITO          DETALLES
--------------------------------------------------------------------------------------------
contadorGlobal  int        variable        5          4          global          [private]
valorPi         double     variable        6          7          global          [private]
inicial         char       variable        7          5          global          [private]
activo          bool       variable        8          5          global          [private]
sumar           int        funcion         11         4          global          [private] [int, int]
a               int        parametro       11         14         sumar
b               int        parametro       11         21         sumar
resultado       int        variable        12         8          sumar           [private]
main            int        funcion         19         4          global          [private]
estado          int        variable        20         8          main            [private]
temp            int        variable        21         8          main            [private]
numeros         int        variable        22         8          main            [arr:3] [private]
auxiliar        int        variable        50         12         main            [private]

✅ Análisis semántico completado sin errores.

=== 5. GENERACIÓN DE CÓDIGO INTERMEDIO ===
   🎯 Iniciando recorrido del AST con CodigoVisitor...
   📝 Código de tres direcciones generado:

  0: // Código de tres direcciones generado
  1: PROGRAMA_INICIO:
  2: // Declaración de variables globales
  3: DECLARE contadorGlobal int
  4: DECLARE valorPi double
  5: DECLARE inicial char
  6: DECLARE activo bool
  7: func_sumar:
  8: PARAM a int
  9: PARAM b int
 10: t1 = a + b
 11: t2 = contadorGlobal + 1
 12: contadorGlobal = t2
 13: return t1
 14: func_main:
 15: DECLARE estado int
 16: DECLARE temp int
 17: DECLARE numeros[3] int
 18: contadorGlobal = 0
 19: valorPi = 3.14
 20: inicial = 'M'
 21: activo = false
 22: numeros[0] = 10
 23: numeros[1] = 20
 24: numeros[2] = 30
 25: t3 = numeros[0] + numeros[1]
 26: t4 = t3 * 2
 27: t5 = t4 / 3
 28: temp = t5 % 5
 29: t6 = 5 + 3
 30: t7 = 10 * 2
 31: t8 = 15 - 7
 32: temp = temp
 33: t9 = temp
 34: t9 = t9
 35: CALL func_sumar, temp, 5
 36: estado = RETURN_VALUE
 37: contadorGlobal = estado
 38: valorPi = temp
 39: inicial = 'X'
 40: activo = true
 41: t10 = estado > 0
 42: if t10 goto THEN_1
 43: goto END_IF_1
 44: THEN_1:
 45: t11 = estado + 10
 46: estado = t11
 47: END_IF_1:
 48: goto FINAL
 49: CODIGO_MUERTO:
 50: t12 = 999
 51: temp = t12
 52: FINAL:
 53: return estado
 54: PROGRAMA_FIN:

✅ Código intermedio guardado en: ejemplo_correcto_codigo_intermedio.txt

=== 6. OPTIMIZACIÓN DE CÓDIGO ===
   🔧 Aplicando optimizaciones al código intermedio...
✅ Optimización completada:
   📊 Instrucciones originales: 55
   📊 Instrucciones optimizadas: 49
   📊 Instrucciones eliminadas: 6
   📊 Reducción de código: 10,91%

   📝 Código optimizado:

  0: // Código de tres direcciones generado
  1: PROGRAMA_INICIO:
  2: // Declaración de variables globales
  3: DECLARE contadorGlobal int
  4: DECLARE valorPi double
  5: DECLARE inicial char
  6: DECLARE activo bool
  7: func_sumar:
  8: PARAM a int
  9: PARAM b int
 10: t1 = a + b
 11: t2 = contadorGlobal + 1
 12: contadorGlobal = t2
 13: return t1
 14: func_main:
 15: DECLARE estado int
 16: DECLARE temp int
 17: DECLARE numeros[3] int
 18: contadorGlobal = 0
 19: valorPi = 3.14
 20: inicial = 'M'
 21: activo = false
 22: numeros[0] = 10
 23: numeros[1] = 20
 24: numeros[2] = 30
 25: t3 = numeros[0] + numeros[1]
 26: t4 = t3 * 2
 27: t5 = t4 / 3
 28: temp = t5 % 5
 29: t6 = 8
 30: t7 = 20
 31: t8 = 8
 32: t9 = temp
 33: CALL func_sumar, temp, 5
 34: estado = RETURN_VALUE
 35: contadorGlobal = estado
 36: valorPi = temp
 37: inicial = 'X'
 38: activo = true
 39: t10 = estado > 0
 40: if t10 goto THEN_1
 41: goto END_IF_1
 42: THEN_1:
 43: t11 = estado + 10
 44: estado = t11
 45: END_IF_1:
 46: goto FINAL
 47: FINAL:
 48: return estado
 49: PROGRAMA_FIN:

✅ Código optimizado guardado en: ejemplo_correcto_codigo_optimizado.txt

=== 7. RESUMEN DE COMPILACIÓN ===
   📁 Archivo procesado: ejemplo_correcto.cpp
   🔤 Tokens analizados: 152
   📊 Símbolos en tabla: 13
   📝 Instrucciones generadas: 55
   🔧 Instrucciones optimizadas: 49
   📄 Archivo código intermedio: ejemplo_correcto_codigo_intermedio.txt
   📄 Archivo código optimizado: ejemplo_correcto_codigo_optimizado.txt

🎉 ¡COMPILACIÓN Y OPTIMIZACIÓN EXITOSA! 🎉
```

Estas son las clases principales:
Escucha.java
Caminane.java
ManejadorErrores.java
Optimizador.java
Compiladores.g4Clases en carpeta "contexto"TablaSimbolos.java
Contexto.java
Funcion.java
Identificador.java
TipoDato.java
Variable.java


El analisis se hace con listener y la sintesis con visitor.

Esta es mi salida actual: (tiene diferencias con la salida esperada)
? Iniciando compilación de: ejemplo_correcto.cpp
============================================================

=== 1. ANÁLISIS LÉXICO ===
? Debug: Detalle de tokens:
   Token 1: 'int' (Tipo: 4)
   Token 2: 'contadorGlobal' (Tipo: 32)
   Token 3: ';' (Tipo: 17)
   Token 4: 'double' (Tipo: 5)
   Token 5: 'valorPi' (Tipo: 32)
   Token 6: ';' (Tipo: 17)
   Token 7: 'char' (Tipo: 7)
   Token 8: 'inicial' (Tipo: 32)
   Token 9: ';' (Tipo: 17)
   Token 10: 'bool' (Tipo: 13)
   Token 11: 'activo' (Tipo: 32)
   Token 12: ';' (Tipo: 17)
   Token 13: 'int' (Tipo: 4)
   Token 14: 'sumar' (Tipo: 32)
   Token 15: '(' (Tipo: 25)
   Token 16: 'int' (Tipo: 4)
   Token 17: 'a' (Tipo: 32)
   Token 18: ',' (Tipo: 18)
   Token 19: 'int' (Tipo: 4)
   Token 20: 'b' (Tipo: 32)
   Token 21: ')' (Tipo: 26)
   Token 22: '{' (Tipo: 27)
   Token 23: 'int' (Tipo: 4)
   Token 24: 'resultado' (Tipo: 32)
   Token 25: ';' (Tipo: 17)
   Token 26: 'resultado' (Tipo: 32)
   Token 27: '=' (Tipo: 19)
   Token 28: 'a' (Tipo: 32)
   Token 29: '+' (Tipo: 20)
   Token 30: 'b' (Tipo: 32)
   Token 31: ';' (Tipo: 17)
   Token 32: 'contadorGlobal' (Tipo: 32)
   Token 33: '=' (Tipo: 19)
   Token 34: 'contadorGlobal' (Tipo: 32)
   Token 35: '+' (Tipo: 20)
   Token 36: '1' (Tipo: 1)
   Token 37: ';' (Tipo: 17)
   Token 38: 'return' (Tipo: 12)
   Token 39: 'resultado' (Tipo: 32)
   Token 40: ';' (Tipo: 17)
   Token 41: '}' (Tipo: 28)
   Token 42: 'int' (Tipo: 4)
   Token 43: 'main' (Tipo: 32)
   Token 44: '(' (Tipo: 25)
   Token 45: ')' (Tipo: 26)
   Token 46: '{' (Tipo: 27)
   Token 47: 'int' (Tipo: 4)
   Token 48: 'estado' (Tipo: 32)
   Token 49: ';' (Tipo: 17)
   Token 50: 'int' (Tipo: 4)
   Token 51: 'temp' (Tipo: 32)
   Token 52: ';' (Tipo: 17)
   Token 53: 'int' (Tipo: 4)
   Token 54: 'numeros' (Tipo: 32)
   Token 55: '[' (Tipo: 29)
   Token 56: '3' (Tipo: 1)
   Token 57: ']' (Tipo: 30)
   Token 58: ';' (Tipo: 17)
   Token 59: 'contadorGlobal' (Tipo: 32)
   Token 60: '=' (Tipo: 19)
   Token 61: '0' (Tipo: 1)
   Token 62: ';' (Tipo: 17)
   Token 63: 'valorPi' (Tipo: 32)
   Token 64: '=' (Tipo: 19)
   Token 65: '3.14' (Tipo: 2)
   Token 66: ';' (Tipo: 17)
   Token 67: 'inicial' (Tipo: 32)
   Token 68: '=' (Tipo: 19)
   Token 69: ''M'' (Tipo: 3)
   Token 70: ';' (Tipo: 17)
   Token 71: 'numeros' (Tipo: 32)
   Token 72: '[' (Tipo: 29)
   Token 73: '0' (Tipo: 1)
   Token 74: ']' (Tipo: 30)
   Token 75: '=' (Tipo: 19)
   Token 76: '10' (Tipo: 1)
   Token 77: ';' (Tipo: 17)
   Token 78: 'numeros' (Tipo: 32)
   Token 79: '[' (Tipo: 29)
   Token 80: '1' (Tipo: 1)
   Token 81: ']' (Tipo: 30)
   Token 82: '=' (Tipo: 19)
   Token 83: '20' (Tipo: 1)
   Token 84: ';' (Tipo: 17)
   Token 85: 'numeros' (Tipo: 32)
   Token 86: '[' (Tipo: 29)
   Token 87: '2' (Tipo: 1)
   Token 88: ']' (Tipo: 30)
   Token 89: '=' (Tipo: 19)
   Token 90: '30' (Tipo: 1)
   Token 91: ';' (Tipo: 17)
   Token 92: 'temp' (Tipo: 32)
   Token 93: '=' (Tipo: 19)
   Token 94: 'numeros' (Tipo: 32)
   Token 95: '[' (Tipo: 29)
   Token 96: '0' (Tipo: 1)
   Token 97: ']' (Tipo: 30)
   Token 98: '+' (Tipo: 20)
   Token 99: 'numeros' (Tipo: 32)
   Token 100: '[' (Tipo: 29)
   Token 101: '1' (Tipo: 1)
   Token 102: ']' (Tipo: 30)
   Token 103: ';' (Tipo: 17)
   Token 104: 'temp' (Tipo: 32)
   Token 105: '=' (Tipo: 19)
   Token 106: 'temp' (Tipo: 32)
   Token 107: '*' (Tipo: 22)
   Token 108: '2' (Tipo: 1)
   Token 109: ';' (Tipo: 17)
   Token 110: 'temp' (Tipo: 32)
   Token 111: '=' (Tipo: 19)
   Token 112: 'temp' (Tipo: 32)
   Token 113: '/' (Tipo: 23)
   Token 114: '3' (Tipo: 1)
   Token 115: ';' (Tipo: 17)
   Token 116: 'temp' (Tipo: 32)
   Token 117: '=' (Tipo: 19)
   Token 118: 'temp' (Tipo: 32)
   Token 119: '%' (Tipo: 24)
   Token 120: '5' (Tipo: 1)
   Token 121: ';' (Tipo: 17)
   Token 122: 'estado' (Tipo: 32)
   Token 123: '=' (Tipo: 19)
   Token 124: 'sumar' (Tipo: 32)
   Token 125: '(' (Tipo: 25)
   Token 126: 'temp' (Tipo: 32)
   Token 127: ',' (Tipo: 18)
   Token 128: '5' (Tipo: 1)
   Token 129: ')' (Tipo: 26)
   Token 130: ';' (Tipo: 17)
   Token 131: 'contadorGlobal' (Tipo: 32)
   Token 132: '=' (Tipo: 19)
   Token 133: 'estado' (Tipo: 32)
   Token 134: ';' (Tipo: 17)
   Token 135: 'valorPi' (Tipo: 32)
   Token 136: '=' (Tipo: 19)
   Token 137: 'temp' (Tipo: 32)
   Token 138: ';' (Tipo: 17)
   Token 139: 'inicial' (Tipo: 32)
   Token 140: '=' (Tipo: 19)
   Token 141: ''X'' (Tipo: 3)
   Token 142: ';' (Tipo: 17)
   Token 143: 'if' (Tipo: 8)
   Token 144: '(' (Tipo: 25)
   Token 145: 'estado' (Tipo: 32)
   Token 146: '>' (Tipo: 31)
   Token 147: '0' (Tipo: 1)
   Token 148: ')' (Tipo: 26)
   Token 149: '{' (Tipo: 27)
   Token 150: 'int' (Tipo: 4)
   Token 151: 'auxiliar' (Tipo: 32)
   Token 152: ';' (Tipo: 17)
   Token 153: 'auxiliar' (Tipo: 32)
   Token 154: '=' (Tipo: 19)
   Token 155: 'estado' (Tipo: 32)
   Token 156: '+' (Tipo: 20)
   Token 157: '10' (Tipo: 1)
   Token 158: ';' (Tipo: 17)
   Token 159: 'estado' (Tipo: 32)
   Token 160: '=' (Tipo: 19)
   Token 161: 'auxiliar' (Tipo: 32)
   Token 162: ';' (Tipo: 17)
   Token 163: '}' (Tipo: 28)
   Token 164: 'return' (Tipo: 12)
   Token 165: 'estado' (Tipo: 32)
   Token 166: ';' (Tipo: 17)
   Token 167: '}' (Tipo: 28)
? Debug: Total de tokens válidos: 167
? Análisis léxico completado sin errores.
   ? Tokens procesados: 167

=== 2. ANÁLISIS SINTÁCTICO ===
? Debug: Verificando tokens...
Token: int (Tipo: 4)
Token: contadorGlobal (Tipo: 32)
Token: ; (Tipo: 17)
Token: double (Tipo: 5)
Token: valorPi (Tipo: 32)
Token: ; (Tipo: 17)
Token: char (Tipo: 7)
Token: inicial (Tipo: 32)
Token: ; (Tipo: 17)
Token: bool (Tipo: 13)
Token: activo (Tipo: 32)
Token: ; (Tipo: 17)
Token: int (Tipo: 4)
Token: sumar (Tipo: 32)
Token: ( (Tipo: 25)
Token: int (Tipo: 4)
Token: a (Tipo: 32)
Token: , (Tipo: 18)
Token: int (Tipo: 4)
Token: b (Tipo: 32)
Token: ) (Tipo: 26)
Token: { (Tipo: 27)
Token: int (Tipo: 4)
Token: resultado (Tipo: 32)
Token: ; (Tipo: 17)
Token: resultado (Tipo: 32)
Token: = (Tipo: 19)
Token: a (Tipo: 32)
Token: + (Tipo: 20)
Token: b (Tipo: 32)
Token: ; (Tipo: 17)
Token: contadorGlobal (Tipo: 32)
Token: = (Tipo: 19)
Token: contadorGlobal (Tipo: 32)
Token: + (Tipo: 20)
Token: 1 (Tipo: 1)
Token: ; (Tipo: 17)
Token: return (Tipo: 12)
Token: resultado (Tipo: 32)
Token: ; (Tipo: 17)
Token: } (Tipo: 28)
Token: int (Tipo: 4)
Token: main (Tipo: 32)
Token: ( (Tipo: 25)
Token: ) (Tipo: 26)
Token: { (Tipo: 27)
Token: int (Tipo: 4)
Token: estado (Tipo: 32)
Token: ; (Tipo: 17)
Token: int (Tipo: 4)
Token: temp (Tipo: 32)
Token: ; (Tipo: 17)
Token: int (Tipo: 4)
Token: numeros (Tipo: 32)
Token: [ (Tipo: 29)
Token: 3 (Tipo: 1)
Token: ] (Tipo: 30)
Token: ; (Tipo: 17)
Token: contadorGlobal (Tipo: 32)
Token: = (Tipo: 19)
Token: 0 (Tipo: 1)
Token: ; (Tipo: 17)
Token: valorPi (Tipo: 32)
Token: = (Tipo: 19)
Token: 3.14 (Tipo: 2)
Token: ; (Tipo: 17)
Token: inicial (Tipo: 32)
Token: = (Tipo: 19)
Token: 'M' (Tipo: 3)
Token: ; (Tipo: 17)
Token: numeros (Tipo: 32)
Token: [ (Tipo: 29)
Token: 0 (Tipo: 1)
Token: ] (Tipo: 30)
Token: = (Tipo: 19)
Token: 10 (Tipo: 1)
Token: ; (Tipo: 17)
Token: numeros (Tipo: 32)
Token: [ (Tipo: 29)
Token: 1 (Tipo: 1)
Token: ] (Tipo: 30)
Token: = (Tipo: 19)
Token: 20 (Tipo: 1)
Token: ; (Tipo: 17)
Token: numeros (Tipo: 32)
Token: [ (Tipo: 29)
Token: 2 (Tipo: 1)
Token: ] (Tipo: 30)
Token: = (Tipo: 19)
Token: 30 (Tipo: 1)
Token: ; (Tipo: 17)
Token: temp (Tipo: 32)
Token: = (Tipo: 19)
Token: numeros (Tipo: 32)
Token: [ (Tipo: 29)
Token: 0 (Tipo: 1)
Token: ] (Tipo: 30)
Token: + (Tipo: 20)
Token: numeros (Tipo: 32)
Token: [ (Tipo: 29)
Token: 1 (Tipo: 1)
Token: ] (Tipo: 30)
Token: ; (Tipo: 17)
Token: temp (Tipo: 32)
Token: = (Tipo: 19)
Token: temp (Tipo: 32)
Token: * (Tipo: 22)
Token: 2 (Tipo: 1)
Token: ; (Tipo: 17)
Token: temp (Tipo: 32)
Token: = (Tipo: 19)
Token: temp (Tipo: 32)
Token: / (Tipo: 23)
Token: 3 (Tipo: 1)
Token: ; (Tipo: 17)
Token: temp (Tipo: 32)
Token: = (Tipo: 19)
Token: temp (Tipo: 32)
Token: % (Tipo: 24)
Token: 5 (Tipo: 1)
Token: ; (Tipo: 17)
Token: estado (Tipo: 32)
Token: = (Tipo: 19)
Token: sumar (Tipo: 32)
Token: ( (Tipo: 25)
Token: temp (Tipo: 32)
Token: , (Tipo: 18)
Token: 5 (Tipo: 1)
Token: ) (Tipo: 26)
Token: ; (Tipo: 17)
Token: contadorGlobal (Tipo: 32)
Token: = (Tipo: 19)
Token: estado (Tipo: 32)
Token: ; (Tipo: 17)
Token: valorPi (Tipo: 32)
Token: = (Tipo: 19)
Token: temp (Tipo: 32)
Token: ; (Tipo: 17)
Token: inicial (Tipo: 32)
Token: = (Tipo: 19)
Token: 'X' (Tipo: 3)
Token: ; (Tipo: 17)
Token: if (Tipo: 8)
Token: ( (Tipo: 25)
Token: estado (Tipo: 32)
Token: > (Tipo: 31)
Token: 0 (Tipo: 1)
Token: ) (Tipo: 26)
Token: { (Tipo: 27)
Token: int (Tipo: 4)
Token: auxiliar (Tipo: 32)
Token: ; (Tipo: 17)
Token: auxiliar (Tipo: 32)
Token: = (Tipo: 19)
Token: estado (Tipo: 32)
Token: + (Tipo: 20)
Token: 10 (Tipo: 1)
Token: ; (Tipo: 17)
Token: estado (Tipo: 32)
Token: = (Tipo: 19)
Token: auxiliar (Tipo: 32)
Token: ; (Tipo: 17)
Token: } (Tipo: 28)
Token: return (Tipo: 12)
Token: estado (Tipo: 32)
Token: ; (Tipo: 17)
Token: } (Tipo: 28)
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'contadorGlobal' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'valorPi' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'inicial' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'activo' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'resultado' en ámbito 'global'
? Debug: Entrando en exitCuerpoFuncion
? Debug: Tipo encontrado en posición 0: int
? Debug: Función 'sumar' agregada al contexto global
? Debug: Contexto de función 'sumar' tiene 7 identificadores
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'estado' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'temp' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'numeros' en ámbito 'global'
? Debug: Procesando declaración en ámbito: global
? Debug: Agregada variable 'auxiliar' en ámbito 'global'
? Debug: Entrando en exitCuerpoFuncion
? Debug: Tipo encontrado en posición 0: int
? Debug: Función 'main' agregada al contexto global
? Debug: Contexto de función 'main' tiene 8 identificadores
? Debug: Entrando en exitPrograma
? Debug: Contextos disponibles: 1
? Debug: Contexto global tiene 8 identificadores
? Debug: Estructura del AST:
(programa (instruccion (declaracion (tipo int) (declarador contadorGlobal) ;)) (instruccion (declaracion (tipo double) (declarador valorPi) ;)) (instruccion (declaracion (tipo char) (declarador inicial) ;)) (instruccion (declaracion (tipo bool) (declarador activo) ;)) (cuerpoFuncion (tipo int) sumar ( (parametros (parametro (tipo int) a) , (parametro (tipo int) b)) ) (bloque { (instrucciones (instruccion (declaracion (tipo int) (declarador resultado) ;)) (instruccion (asignacion resultado = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor a)) + (termino (factor b)))))) ;)) (instruccion (asignacion contadorGlobal = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor contadorGlobal)) + (termino (factor 1)))))) ;)) (instruccion (return return (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor resultado)))))) ;))) })) (cuerpoFuncion (tipo int) main ( ) (bloque { (instrucciones (instruccion (declaracion (tipo int) (declarador estado) ;)) (instruccion (declaracion (tipo int) (declarador temp) ;)) (instruccion (declaracion (tipo int) (declarador numeros [ 3 ]) ;)) (instruccion (asignacion contadorGlobal = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 0)))))) ;)) (instruccion (asignacion valorPi = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 3.14)))))) ;)) (instruccion (asignacion inicial = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 'M')))))) ;)) (instruccion (asignacion numeros [ (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 0)))))) ] = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 10)))))) ;)) (instruccion (asignacion numeros [ (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 1)))))) ] = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 20)))))) ;)) (instruccion (asignacion numeros [ (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 2)))))) ] = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 30)))))) ;)) (instruccion (asignacion temp = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor numeros [ (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 0)))))) ])) + (termino (factor numeros [ (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 1)))))) ])))))) ;)) (instruccion (asignacion temp = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor temp) * (factor 2)))))) ;)) (instruccion (asignacion temp = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor temp) / (factor 3)))))) ;)) (instruccion (asignacion temp = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor temp) % (factor 5)))))) ;)) (instruccion (asignacion estado = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor (llamadaFuncion sumar ( (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor temp)))))) , (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 5)))))) )))))))) ;)) (instruccion (asignacion contadorGlobal = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor estado)))))) ;)) (instruccion (asignacion valorPi = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor temp)))))) ;)) (instruccion (asignacion inicial = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor 'X')))))) ;)) (instruccion (estructuraControl (ifElse if ( (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor estado))) > (expresionAritmetica (termino (factor 0)))))) ) (bloque { (instrucciones (instruccion (declaracion (tipo int) (declarador auxiliar) ;)) (instruccion (asignacion auxiliar = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor estado)) + (termino (factor 10)))))) ;)) (instruccion (asignacion estado = (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor auxiliar)))))) ;))) })))) (instruccion (return return (expresion (expresionLogica (expresionComparacion (expresionAritmetica (termino (factor estado)))))) ;))) })) <EOF>)
?? Debug: Verificando errores...
? Debug: verificarErrores() - errores = 2
 Debug: escucha.verificarErrores() = true
 Debug: manejadorErrores.verificarErrores() = true
? Debug: verificarErrores() - errores = 2
?? Advertencias detectadas, verifique el archivo errores.txt
? Análisis sintáctico completado.
   ? Árbol sintáctico generado correctamente
? Debug: Verificando reglas aplicadas...

=== 3. VISUALIZACIÓN DEL AST ===
   ? Ventana del árbol sintáctico abierta

=== 4. ANÁLISIS SEMÁNTICO ===
   ? Tabla de símbolos construida:
? Debug: Contextos en contextoAuxiliar:
Contexto 0 tiene 8 identificadores:
  - a (global)
  - b (global)
  - valorPi (global)
  - sumar (global)
  - main (global)
  - contadorGlobal (global)
  - inicial (global)
  - activo (global)
=== TABLA DE SÍMBOLOS ===
NOMBRE          TIPO       CATEGORÍA       LÍNEA      COLUMNA    ÁMBITO          DETALLES
--------------------------------------------------------------------------------------------
a               int        parametro       11         14         global          [private]
b               int        parametro       11         21         global          [private]
valorPi         double     variable        6          7          global          [private]
sumar           int        funcion         11         4          global          [private] [int, int]
main            int        funcion         19         4          global          [private]
contadorGlobal  int        variable        5          4          global          [private]
inicial         char       variable        7          5          global          [private]
activo          bool       variable        8          5          global          [private]
? Análisis semántico completado sin errores.

=== 5. GENERACIÓN DE CÓDIGO INTERMEDIO ===
   ? Iniciando recorrido del AST con CodigoVisitor...
   ? Código de tres direcciones generado:
0: // Código de tres direcciones generado
1: PROGRAMA_INICIO:
2: // Declaración de variables globales
3: DECLARE contadorGlobal int
4: DECLARE valorPi double
5: DECLARE inicial char
6: DECLARE activo bool
7: func_sumar:
8: PARAM a int
9: PARAM b int
10: DECLARE resultado int
t1 = a + b;
11: resultado = t1
t2 = contadorGlobal + 1;
12: contadorGlobal = t2
13: return resultado
14: func_main:
15: DECLARE estado int
16: DECLARE temp int
17: DECLARE numeros[3] int
18: contadorGlobal = 0
19: valorPi = 3.14
20: inicial = 'M'
21: numeros[0] = 10
22: numeros[1] = 20
23: numeros[2] = 30
t3 = 0 + 1;
24: temp = t3
t4 = temp * 2;
25: temp = t4
t5 = temp / 3;
26: temp = t5
t6 = temp % 5;
27: temp = t6
28: CALL func_sumar, temp, 5
29: t7 = RETURN_VALUE
30: estado = t7
31: contadorGlobal = estado
32: valorPi = temp
33: inicial = 'X'
t8 = estado > 0;
34: if t8 goto L0
35: goto L1
36: L0:
37: DECLARE auxiliar int
t9 = estado + 10;
38: auxiliar = t9
39: estado = auxiliar
40: L1:
41: return estado
42: PROGRAMA_FIN:

? Código intermedio guardado en: ejemplo_correcto_codigo_intermedio.txt

=== 6. OPTIMIZACIÓN DE CÓDIGO ===
   ? Aplicando optimizaciones al código intermedio...
? Optimización completada:
   ? Instrucciones originales: 51
   ? Instrucciones optimizadas: 51
   ? Instrucciones eliminadas: 0
   ? Reducción de código: 0,00%
   ? Código optimizado:
0: // Código de tres direcciones generado
1: PROGRAMA_INICIO:
2: // Declaración de variables globales
3: DECLARE contadorGlobal int
4: DECLARE valorPi double
5: DECLARE inicial char
6: DECLARE activo bool
7: func_sumar:
8: PARAM a int
9: PARAM b int
10: DECLARE resultado int
t1 = a + b;
11: resultado = t1
t2 = contadorGlobal + 1;
12: contadorGlobal = t2
13: return resultado
14: func_main:
15: DECLARE estado int
16: DECLARE temp int
17: DECLARE numeros[3] int
18: contadorGlobal = 0
19: valorPi = 3.14
20: inicial = 'M'
21: numeros[0] = 10
22: numeros[1] = 20
23: numeros[2] = 30
t3 = 1
24: temp = t3
t4 = temp * 2;
25: temp = t4
t5 = temp / 3;
26: temp = t5
t6 = temp % 5;
27: temp = t6
28: CALL func_sumar, temp, 5
29: t7 = RETURN_VALUE
30: estado = t7
31: contadorGlobal = estado
32: valorPi = temp
33: inicial = 'X'
t8 = estado > 0;
34: if t8 goto L0
35: goto L1
36: L0:
37: DECLARE auxiliar int
t9 = estado + 10;
38: auxiliar = t9
39: estado = auxiliar
40: L1:
41: return estado
42: PROGRAMA_FIN:

? Código optimizado guardado en: ejemplo_correcto_codigo_optimizado.txt

=== 7. RESUMEN DE COMPILACIÓN ===
   ? Archivo procesado: ejemplo_correcto.cpp
   ? Tokens analizados: 167
   ? Símbolos en tabla: 8
   ? Instrucciones generadas: 51
   ? Instrucciones optimizadas: 51
   ? Archivo código intermedio: ejemplo_correcto_codigo_intermedio.txt
   ? Archivo código optimizado: ejemplo_correcto_codigo_optimizado.txt

necesito que vayamos paso a paso implementando los cambios correctos en las clases para obtener la salida mas precisa posible a la esperada, para eso cualquier contexto que necesites no dudes en pedirmelo como clases o funciones. Muchas gracias por ayudarme y avancemos.

Esta es la estructura del proyecto
* archivos del paquete compilador:
compiladores.g4
clases generadas por el antlr

* archivos base
App.java
Caminante.java
Escucha.java
ManejadorErrores.java
Optimizador.java

* archivos del paquete contexto:
Contexto.java
Funcion.java
Identificador.java
TablaSimbolos.java
TipoDato.java
Variable.java