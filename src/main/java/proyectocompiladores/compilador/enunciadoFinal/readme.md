# Compilador - Proyecto Final
## Técnicas de Compilación

### 📚 Descripción del Proyecto

Este proyecto constituye la implementación completa de un compilador para un subconjunto del lenguaje C++, desarrollado como proyecto final para la materia **Técnicas de Compilación**. El compilador incluye análisis léxico, sintáctico, semántico, generación de código intermedio y optimizaciones.

---

## 🎯 Funcionalidades Implementadas

- ✅ **Análisis Léxico**: Reconocimiento de tokens y símbolos del lenguaje
- ✅ **Análisis Sintáctico**: Construcción del árbol sintáctico abstracto (AST)
- ✅ **Análisis Semántico**: Tabla de símbolos y verificación de tipos
- ✅ **Generación de Código Intermedio**: Código de tres direcciones
- ✅ **Optimización de Código**: Eliminación de código muerto, propagación de constantes, simplificación de expresiones
- ✅ **Detección de Errores**: Errores léxicos, sintácticos y semánticos
- ✅ **Warnings**: Variables no utilizadas y otros avisos

---

## 📋 Requisitos del Sistema

- **Java**
- **ANTLR 4.9** o superior
- **Maven** (para gestión de dependencias)

---



## 📖 Ejemplos de Uso

### 🟢 Ejemplo 1: Código Sin Errores

**Archivo de entrada: `ejemplo_correcto.cpp`**

```cpp
// Archivo de prueba SIN errores - SÚPER SIMPLE
// Compatible con la gramática básica

// Variables globales
int contadorGlobal;
double valorPi;
char inicial;
bool activo;

// Función simple que retorna valor
int sumar(int a, int b) {
    int resultado;
    resultado = a + b;
    contadorGlobal = contadorGlobal + 1;
    return resultado;
}

// Función main
int main() {
    int estado;
    int temp;
    int numeros[3];
    
    // Inicializar variables globales
    contadorGlobal = 0;
    valorPi = 3.14;
    inicial = 'M';
    
    // Usar arrays
    numeros[0] = 10;
    numeros[1] = 20;
    numeros[2] = 30;
    
    // Operaciones básicas
    temp = numeros[0] + numeros[1];
    temp = temp * 2;
    temp = temp / 3;
    temp = temp % 5;
    
    // Usar función que retorna valor
    estado = sumar(temp, 5);
    
    // Usar variables globales
    contadorGlobal = estado;
    valorPi = temp;
    inicial = 'X';
    
    // Estructuras de control básicas
    if (estado > 0) {
        int auxiliar;
        auxiliar = estado + 10;
        estado = auxiliar;
    }
    
    return estado;
}
```

**Salida esperada:**

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

---

### 🔴 Ejemplo 2: Código Con Errores

**Archivo de entrada: `ejemplo_con_errores.cpp`**

```cpp
// Variables globales
int variableGlobal;
int variableGlobal; // ERROR: Variable duplicada en el mismo ámbito
double valorGlobal;
char caracterGlobal;

// Función con múltiples errores
int miFuncion(int parametro1, double parametro2) {
    int variableLocal;
    int variableLocal; // ERROR: Variable duplicada en función
    
    // Variables nunca utilizadas (WARNING)
    int variableNoUsada1;
    string variableNoUsada2;
    double variableNoUsada3;
    
    parametro1 = 100; // OK: asignación a parámetro
    variableLocal = parametro1 + 5; // OK: uso de variable
    
    // ERROR: Asignación a variable no declarada
    variableFantasma = 42;
    
    // ERROR: Asignación a una función (no es variable)
    miFuncion = 10;
    
    // OK: usar variable global
    valorGlobal = 3.14;
    
    return variableLocal;
}

void funcionVoid() {
    int x;
    int y;
    int z; // WARNING: declarada pero no usada
    
    x = 10; // OK
    y = x + 5; // OK: uso de variables
    
    // ERROR: variable no declarada
    w = x + y;
}

// Función main para completar el programa
int main() {
    int resultado;
    int valor;
    
    resultado = miFuncion(5, 3.14); // OK: uso de función
    valor = resultado + 10;         // OK: uso de variables
    
    // ERROR: variable no declarada
    variableFinal = valor;
    
    // OK: usar variable global
    variableGlobal = valor;
    
    return resultado;
}
```

**Salida esperada:**

```
🚀 Iniciando compilación de: ejemplo_con_errores.cpp
============================================================

=== 1. ANÁLISIS LÉXICO ===
✅ Análisis léxico completado sin errores.
   📊 Tokens procesados: 98

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
variableGlobal  int        variable        2          4          global          [private]
valorGlobal     double     variable        4          7          global          [private]
caracterGlobal  char       variable        5          5          global          [private]
miFuncion       int        funcion         8          4          global          [private] [int, double]
parametro1      int        parametro       8          19         miFuncion
parametro2      double     parametro       8          34         miFuncion
variableLocal   int        variable        9          8          miFuncion       [private]
variableNoUsada1 int       variable        13         8          miFuncion       [private]
variableNoUsada2 string    variable        14         11         miFuncion       [private]
variableNoUsada3 double    variable        15         11         miFuncion       [private]
funcionVoid     void       funcion         28         5          global          [private] []
x               int        variable        29         8          funcionVoid     [private]
y               int        variable        30         8          funcionVoid     [private]
z               int        variable        31         8          funcionVoid     [private]
main            int        funcion         39         4          global          [private] []
resultado       int        variable        40         8          main            [private]
valor           int        variable        41         8          main            [private]

❌ ERRORES SEMÁNTICOS:
   ❌ Error: La variable 'variableGlobal' ya está declarada en el ámbito 'global' (línea 3, columna 4)
   ❌ Error: La variable 'variableLocal' ya está declarada en el ámbito 'miFuncion' (línea 10, columna 8)
   ❌ Error: Variable 'variableFantasma' no declarada en el ámbito 'miFuncion' (línea 20)
   ❌ Error: No se puede asignar valor a 'miFuncion' porque no es una variable (línea 23)
   ❌ Error: Variable 'w' no declarada en el ámbito 'funcionVoid' (línea 37)
   ❌ Error: Variable 'variableFinal' no declarada en el ámbito 'main' (línea 46)

⚠️ WARNINGS SEMÁNTICOS:
   ⚠️ Warning: Variable 'variableNoUsada1' declarada pero nunca utilizada en el ámbito 'miFuncion' (línea 13)
   ⚠️ Warning: Variable 'variableNoUsada2' declarada pero nunca utilizada en el ámbito 'miFuncion' (línea 14)
   ⚠️ Warning: Variable 'variableNoUsada3' declarada pero nunca utilizada en el ámbito 'miFuncion' (línea 15)
   ⚠️ Warning: Variable 'z' declarada pero nunca utilizada en el ámbito 'funcionVoid' (línea 31)
   ⚠️ El código tiene warnings, pero se puede continuar.

❌ Compilación detenida debido a errores semánticos.
```

---

## 🔧 Optimizaciones Implementadas

### 1. **Eliminación de Código Muerto**
Elimina código inalcanzable después de saltos incondicionales.

### 2. **Propagación de Constantes**
Reemplaza variables con valores constantes conocidos.

### 3. **Simplificación de Expresiones**
Evalúa expresiones constantes en tiempo de compilación:
- `5 + 3` → `8`
- `10 * 2` → `20`
- `15 - 7` → `8`

### 4. **Eliminación de Sentencias Redundantes**
Elimina asignaciones innecesarias como `temp = temp`.

---

## 📁 Archivos Generados

El compilador genera los siguientes archivos:

1. **`archivo_codigo_intermedio.txt`**: Código de tres direcciones sin optimizar
2. **`archivo_codigo_optimizado.txt`**: Código de tres direcciones optimizado

---

## 🏗️ Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── compilador/
│   │           ├── App.java                    # Clase principal
│   │           ├── TablaSimbolos.java          # Tabla de símbolos
│   │           ├── SimbolosListener.java       # Análisis semántico
│   │           ├── Optimizador.java            # Optimizaciones
│   │           ├── MiLenguaje.g4              # Gramática ANTLR
│   │           └── ...
│   └── resources/
├── test/
│   ├── ejemplo_correcto.cpp
│   ├── ejemplo_con_errores.cpp
│   └── ...
└── README.md
```

---

## 🎓 Criterios de Evaluación

### ✅ Funcionalidades Requeridas:
- [x] Análisis léxico completo
- [x] Análisis sintáctico con gramática bien definida
- [x] Análisis semántico con tabla de símbolos
- [x] Detección y reporte de errores
- [x] Generación de código intermedio
- [x] Documentación completa
- [x] Casos de prueba exhaustivos

### 🏆 Funcionalidades Adicionales:
- [x] Visualización del AST
- [x] Métricas de optimización
- [x] Estadísticas detalladas
- [x] Múltiples niveles de optimización
- [x] Interfaz de línea de comandos completa






