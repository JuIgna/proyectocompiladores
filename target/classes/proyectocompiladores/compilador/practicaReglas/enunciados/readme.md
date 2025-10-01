
# Análisis Sintáctico de Expresiones

## Enunciado

Para **cada uno de los ejercicios** que se presentan a continuación, realiza lo siguiente:

1. El **análisis sintáctico descendente**.
2. El **análisis sintáctico ascendente** .
3. Construye las **tablas de análisis sintáctico** correspondientes (descendente y ascendente).
4. Dibuja el **árbol de derivación** o **grafo de análisis sintáctico** para cada expresión.
5. Pasar las reglas a .g4 y verificar que funcione

---

## Ejercicio 1: 
**Expresión:** `function(a, , c)`  
**Reglas gramaticales:**
```
S → id ( L )  
L → L , E | E | ε  
E → id
```

---

## Ejercicio 2: 
**Expresión:** `a + (b * c`  
**Reglas gramaticales:**
```
E → E + T | T  
T → T * F | F  
F → ( E ) | id
```

---


---

## Ejercicio 3: 
**Expresión:** `a + b * c`  
**Reglas gramaticales:**
```
E → E + T | T  
T → T * F | F  
F → id
```

---

## Ejercicio 4: 
**Expresión:** `(a + b) * c - d`  
**Reglas gramaticales:**
```
E → E + T | E - T | T  
T → T * F | F  
F → ( E ) | id
```

---

## Ejercicio 5: 
**Expresión:** `x = y + z`  
**Reglas gramaticales:**
```
S → id = E  
E → E + T | T  
T → id
```

---

## Ejercicio 6:
**Expresión:** `if (a > b) then x = y`  
**Reglas gramaticales:**
```
S → if ( C ) then S  
C → E > E  
E → id  
S → id = E
```

---

## Ejercicio 7: 
**Expresión:** `while (x > 0) x = x - 1`  
**Reglas gramaticales:**
```
S → while ( C ) S  
C → E > E  
E → id | num  
S → id = E  
E → E - T | T  
T → id | num
```

---

## Ejercicio 8
**Expresión:** `a + b * c / d - e`  
**Reglas gramaticales:**
```
E → E + T | E - T | T  
T → T * F | T / F | F  
F → id
```

---

## Ejercicio 9
**Expresión:** `int a[10]`  
**Reglas gramaticales:**
```
D → tipo id [ num ]  
tipo → int
```

---

## Ejercicio 10
**Expresión:** `calculate(x, y + z)`  
**Reglas gramaticales:**
```
S → id ( L )  
L → L , E | E  
E → E + T | T  
T → id
```

---

## Ejercicio 11
**Expresión:** `{ x = 1; y = 2; }`  
**Reglas gramaticales:**
```
B → { SL }  
SL → SL ; S | S  
S → id = E  
E → id | num
```

---

## Ejercicio 12
**Expresión:** `int x = a * (b + c)`  
**Reglas gramaticales:**
```
D → tipo id = E  
tipo → int  
E → T | E + T  
T → F | T * F  
F → id | ( E )
```

---


---

## Ejercicio 13
**Expresión:** `x + "string"`  
**Reglas gramaticales:**
```
E → E + T | T  
T → id | num
```

---


---

## Ejercicio 14
**Expresión:** `if (x > y)`  
**Reglas gramaticales:**
```
S → if ( C ) S  
C → E > E  
E → id
```

---


---

## Ejercicio 15
**Expresión:** `x = y +`  
**Reglas gramaticales:**
```
S → id = E  
E → E + T | T  
T → id
```

---

# Ejercicios de Expresiones Regulares para Técnicas de Compilación

## Ejercicio 1: Identificación de números hexadecimales
**Descripción**: Reconocer números hexadecimales con prefijo 0x o 0X  
**Ejemplos**:
- Válidos: `0xA3F`, `0X12CD`, `0xabcdef`, `0X123`
- Inválidos: `A3F`, `123`, `0x`, `0xG4`

## Ejercicio 2: Validación de direcciones IPv4
**Descripción**: Validar direcciones IPv4  
**Ejemplos**:
- Válidos: `192.168.0.1`, `10.0.0.1`, `255.255.255.0`, `127.0.0.1`
- Inválidos: `256.0.0.1`, `192.168.0`, `192.168.0.1.5`, `192.168.0.a`

## Ejercicio 3: Reconocimiento de nombres de archivos con extensión
**Descripción**: Identificar nombres de archivo válidos con su extensión  
**Ejemplos**:
- Válidos: `documento.pdf`, `archivo-2024.txt`, `main.cpp`, `index.html`
- Inválidos: `.hidden`, `archivo sin espacios.doc`, `archivo?.jpg`, `archivo`

## Ejercicio 4: Validación de fechas en formato ISO 8601
**Descripción**: Reconocer fechas en el formato estándar ISO 8601 (YYYY-MM-DD)  
**Ejemplos**:
- Válidos: `2024-05-12`, `1999-12-31`, `2023-01-01`
- Inválidos: `24-05-12`, `2023/01/01`, `2023-13-01`, `2023-02-30`

## Ejercicio 5: Validación de rangos de fecha y hora en formato DD-MM-YYYY HH:MM
**Descripción**: Reconocer rangos de fechas y horas en formato "DD-MM-YYYY HH:MM a DD-MM-YYYY HH:MM", donde se utilizan guiones para separar los componentes de la fecha, se usa formato de 24 horas sin segundos, y los rangos se separan con la palabra "a".  

**Ejemplos**:
- Válidos:
  - `14-05-2024 18:45 a 15-05-2024 22:00`
  - `01-01-2023 00:00 a 31-12-2023 23:59`
- Inválidos:
  - `14/05/2024 18:45 a 15/05/2024 22:00` (usa barras en lugar de guiones)
  - `14-05-2024 18:45:30 a 15-05-2024 22:00` (incluye segundos)
  - `14-05-2024 18:45 hasta 15-05-2024 22:00` (usa "hasta" en lugar de "a")
  - `32-05-2024 18:45 a 15-05-2024 22:00` (día inválido)
  - `14-13-2024 18:45 a 15-05-2024 22:00` (mes inválido)
  - `14-05-2024 24:45 a 15-05-2024 22:00` (hora inválida)
  - `14-05-2024 18:60 a 15-05-2024 22:00` (minutos inválidos)

## Ejercicio 6: Validación de CUIT/CUIL argentino
**Descripción**: Validar números de CUIT/CUIL argentinos  
**Ejemplos**:
- Válidos: `20-12345678-9`, `27123456789`, `30-44556677-8`
- Inválidos: `12-12345678-9`, `20-123456-9`, `20-123456789-9`, `20-1234567X-9`

## Ejercicio 7: Extracción de directivas de preprocesador C/C++
**Descripción**: Reconocer directivas de preprocesador en código C/C++  
**Ejemplos**:
- Válidos: `#include <stdio.h>`, `  #define MAX 100`, `#ifdef DEBUG`, `#pragma once`
- Inválidos: `int #define = 5;`, `// #include comentario`, `#invalid`

## Ejercicio 8: Validación de nombres de clases en notación CamelCase
**Descripción**: Validar nombres de clases que siguen la convención CamelCase  
**Ejemplos**:
- Válidos: `Person`, `StudentRecord`, `ArrayList`, `Model3D`
- Inválidos: `person`, `Student_Record`, `3DModel`, `_Class`

## Ejercicio 9: Reconocimiento de operaciones aritméticas
**Descripción**: Identificar operaciones aritméticas básicas  
**Ejemplos**:
- Válidos: `3+4`, `10-2`, `5*7`, `20/5`, `3+4*5`, `10-2/7+1`
- Inválidos: `3++4`, `*5`, `10-`, `3 + 4`, `(3+4)`

## Ejercicio 10: Reconocimiento de comentarios en código C/C++
**Descripción**: Reconocer comentarios de una línea (//) y multi-línea (/* */) en código C/C++  
**Ejemplos**:
- Válidos: 
  - `// Este es un comentario de una línea`
  - `/* Este es un comentario
     de múltiples líneas */`
- Inválidos: 
  - `/ Esto no es un comentario`
  - `** Esto tampoco es un comentario */`
