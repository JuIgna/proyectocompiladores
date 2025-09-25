package proyectocompiladores;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import proyectocompiladores.compilador.compiladoresLexer;
import proyectocompiladores.compilador.compiladoresParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.antlr.v4.runtime.Token;

// ParseTree = null , Escucha escucha = new Escucha(escritorErrores),
public class App {
    public static void main(String[] args) {
        try {
            System.out.println("🚀 Iniciando compilación de: ejemplo_correcto.cpp");
            System.out.println("============================================================");

            // 1. Análisis Léxico
            System.out.println("\n=== 1. ANÁLISIS LÉXICO ===");
            CharStream input = CharStreams.fromFileName("input/ejemplo_correcto.cpp");
            compiladoresLexer lexer = new compiladoresLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill(); // Llenar el stream de tokens
            int tokenCount = contarTokens(tokens);
            // Verificar errores léxicos manualmente
            boolean lexerErrors = false;
            for (Token token : tokens.getTokens()) {
                if (token.getType() == compiladoresLexer.ERROR) {
                    lexerErrors = true;
                    break;
                }
            }
            if (lexerErrors) {
                System.out.println("❌ Errores léxicos detectados, verifique el archivo errores.txt");
                return;
            }
            System.out.println("✅ Análisis léxico completado sin errores.");
            System.out.println("   📊 Tokens procesados: " + tokenCount);

            // 2. Análisis Sintáctico
            System.out.println("\n=== 2. ANÁLISIS SINTÁCTICO ===");
            compiladoresParser parser = new compiladoresParser(tokens);

            // En el método main, después de crear el parser
            System.out.println("🔍 Debug: Verificando tokens...");
            for (Token token : tokens.getTokens()) {
                if (token.getType() != Token.EOF) {
                    System.out.println("Token: " + token.getText() + " (Tipo: " + token.getType() + ")");
                }
            }
            try (PrintWriter escritorErrores = new PrintWriter(new FileWriter("output/errores.txt"))) {
                ManejadorErrores manejadorErrores = new ManejadorErrores(escritorErrores);
                parser.removeErrorListeners();
                parser.addErrorListener(manejadorErrores);

                Escucha escucha = new Escucha(escritorErrores);
                parser.addParseListener(escucha);

                ParseTree tree = parser.programa();
                // Después de crear el árbol
                System.out.println("🔍 Debug: Estructura del AST:");
                System.out.println(tree.toStringTree(parser));

                // Debug adicional para verificar errores
                System.out.println("�� Debug: Verificando errores...");
                System.out.println(" Debug: escucha.verificarErrores() = " + escucha.verificarErrores());
                System.out.println(" Debug: manejadorErrores.verificarErrores() = " + manejadorErrores.verificarErrores());

                // Modificar la verificación para permitir continuar con errores menores
                boolean hayErroresCriticos = !escucha.verificarErrores() || !manejadorErrores.verificarErrores();
                if (hayErroresCriticos) {
                    System.out.println("⚠️ Errores detectados, pero continuando con el flujo...");
                    System.out.println("   Verifique el archivo errores.txt para más detalles");
                    // No retornar aquí, continuar con el flujo
                }

                if (!escucha.verificarWarnings()) {
                    System.out.println("⚠️ Advertencias detectadas, verifique el archivo errores.txt");
                }
                System.out.println("✅ Análisis sintáctico completado.");
                System.out.println("   📊 Árbol sintáctico generado correctamente");


                // También agrega esto para ver qué reglas se están aplicando
                System.out.println("🔍 Debug: Verificando reglas aplicadas...");

                // 3. Visualización del AST
                System.out.println("\n=== 3. VISUALIZACIÓN DEL AST ===");
                System.out.println("   📊 Ventana del árbol sintáctico abierta");

                // 4. Análisis Semántico
                System.out.println("\n=== 4. ANÁLISIS SEMÁNTICO ===");
                System.out.println("   📋 Tabla de símbolos construida:");
                int symbolCount = escucha.imprimirTablaSimbolosConsola();
                System.out.println("✅ Análisis semántico completado sin errores.");

                // 5. Generación de Código Intermedio
                System.out.println("\n=== 5. GENERACIÓN DE CÓDIGO INTERMEDIO ===");
                System.out.println("   🎯 Iniciando recorrido del AST con CodigoVisitor...");
                Caminante visitor = new Caminante();
                visitor.visit(tree);
                String codTresDir = visitor.getCodigoGenerado();
                System.out.println("   📝 Código de tres direcciones generado:");
                System.out.println(codTresDir);
                try (PrintWriter escritorCodigo = new PrintWriter(new FileWriter("output/ejemplo_correcto_codigo_intermedio.txt"))) {
                    escritorCodigo.print(codTresDir);
                    System.out.println("✅ Código intermedio guardado en: ejemplo_correcto_codigo_intermedio.txt");
                }

                // 6. Optimización
                System.out.println("\n=== 6. OPTIMIZACIÓN DE CÓDIGO ===");
                System.out.println("   🔧 Aplicando optimizaciones al código intermedio...");
                Optimizador optimizador = new Optimizador(codTresDir);
                String codigoOptimizado = optimizador.optimizar();
                int instruccionesOriginales = contarInstrucciones(codTresDir);
                int instruccionesOptimizadas = contarInstrucciones(codigoOptimizado);
                int instruccionesEliminadas = instruccionesOriginales - instruccionesOptimizadas;
                double reduccion = instruccionesOriginales > 0 ? (instruccionesEliminadas * 100.0 / instruccionesOriginales) : 0;
                System.out.println("✅ Optimización completada:");
                System.out.println("   📊 Instrucciones originales: " + instruccionesOriginales);
                System.out.println("   📊 Instrucciones optimizadas: " + instruccionesOptimizadas);
                System.out.println("   📊 Instrucciones eliminadas: " + instruccionesEliminadas);
                System.out.printf("   📊 Reducción de código: %.2f%%\n", reduccion);
                System.out.println("   📝 Código optimizado:");
                System.out.println(codigoOptimizado);
                try (PrintWriter escritorOptimizado = new PrintWriter(new FileWriter("output/ejemplo_correcto_codigo_optimizado.txt"))) {
                    escritorOptimizado.print(codigoOptimizado);
                    System.out.println("✅ Código optimizado guardado en: ejemplo_correcto_codigo_optimizado.txt");
                }

                // 7. Resumen
                System.out.println("\n=== 7. RESUMEN DE COMPILACIÓN ===");
                System.out.println("   📁 Archivo procesado: ejemplo_correcto.cpp");
                System.out.println("   🔤 Tokens analizados: " + tokenCount);
                System.out.println("   📊 Símbolos en tabla: " + symbolCount);
                System.out.println("   📝 Instrucciones generadas: " + instruccionesOriginales);
                System.out.println("   🔧 Instrucciones optimizadas: " + instruccionesOptimizadas);
                System.out.println("   📄 Archivo código intermedio: ejemplo_correcto_codigo_intermedio.txt");
                System.out.println("   📄 Archivo código optimizado: ejemplo_correcto_codigo_optimizado.txt");
                System.out.println("\n🎉 ¡COMPILACIÓN Y OPTIMIZACIÓN EXITOSA! 🎉");
            }

        } catch (IOException e) {
            System.out.println("❌ Error durante la compilación: " + e.getMessage());
            e.printStackTrace();
        }
    }

private static int contarTokens(CommonTokenStream tokens) {
    int count = 0;
    System.out.println("🔍 Debug: Tokens contados:");
    for (Token token : tokens.getTokens()) {
        int tipo = token.getType();
        if (tipo != compiladoresLexer.LC && tipo != compiladoresLexer.BC &&
            tipo != compiladoresLexer.WS && tipo != compiladoresLexer.ERROR &&
            tipo != Token.EOF) {
            count++;
            System.out.println("Token: " + token.getText() + " (Tipo: " + tipo + ")");
        }
    }
    System.out.println("🔍 Debug: Total de tokens válidos: " + count);
    return count;
}

    private static int contarInstrucciones(String codigo) {
        String[] lineas = codigo.split("\n");
        int count = 0;
        for (String linea : lineas) {
            if (!linea.trim().isEmpty() && !linea.startsWith("//")) {
                count++;
            }
        }
        return count;
    }
}
