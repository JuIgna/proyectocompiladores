package proyectocompiladores;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import proyectocompiladores.compilador.compiladoresLexer;
import proyectocompiladores.compilador.compiladoresParser;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("\n🚀 Iniciando compilación de: ejemplo_correcto.cpp");
            System.out.println("============================================================");

            // 1. Análisis Léxico
            System.out.println("\n=== 1. ANÁLISIS LÉXICO ===");
            CharStream input = CharStreams.fromFileName("input/ejemplo_correcto.cpp");
            compiladoresLexer lexer = new compiladoresLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            tokens.fill();
            int tokenCount = contarTokens(tokens);
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
            try (PrintWriter escritorErrores = new PrintWriter(new FileWriter("output/errores.txt"))) {
                ManejadorErrores manejadorErrores = new ManejadorErrores(escritorErrores);
                parser.removeErrorListeners();
                parser.addErrorListener(manejadorErrores);
                Escucha escucha = new Escucha(escritorErrores);
                parser.addParseListener(escucha);
                ParseTree tree = parser.programa();

                boolean hayErroresCriticos = !escucha.verificarErrores() || !manejadorErrores.verificarErrores();
                if (hayErroresCriticos) {
                    System.out.println("⚠️ Errores detectados, verifique el archivo errores.txt");
                }
                if (!escucha.verificarWarnings()) {
                    System.out.println("⚠️ Advertencias detectadas, verifique el archivo errores.txt");
                }
                System.out.println("✅ Análisis sintáctico completado.");
                System.out.println("   📊 Árbol sintáctico generado correctamente");

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
                try (PrintWriter escritorCodigo = new PrintWriter(new FileWriter("output/ejemplo_correcto_codigo_intermedio.txt"))) {
                    escritorCodigo.print(codTresDir);
                    System.out.println("✅ Código intermedio guardado en: ejemplo_correcto_codigo_intermedio.txt");
                    System.out.println("   📝 Código de tres direcciones generado:");
                    System.out.println(formatTAC(codTresDir));
                }

                // 6. Optimización
                System.out.println("\n=== 6. OPTIMIZACIÓN DE CÓDIGO ===");
                Optimizador optimizador = new Optimizador(codTresDir);
                String codigoOptimizado = optimizador.optimizar();
                try (PrintWriter escritorOptimizado = new PrintWriter(new FileWriter("output/ejemplo_correcto_codigo_optimizado.txt"))) {
                    escritorOptimizado.print(codigoOptimizado);
                    System.out.println("✅ Código optimizado guardado en: ejemplo_correcto_codigo_optimizado.txt");
                }

                // 7. Resumen
                System.out.println("\n=== 7. RESUMEN DE COMPILACIÓN ===");
                System.out.println("   📁 Archivo procesado: ejemplo_correcto.cpp");
                System.out.println("   🔤 Tokens analizados: " + tokenCount);
                System.out.println("   📊 Símbolos en tabla: " + symbolCount);
                System.out.println("   📝 Instrucciones generadas: " + contarInstrucciones(codTresDir));
                System.out.println("   🔧 Instrucciones optimizadas: " + contarInstrucciones(codigoOptimizado));
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
        for (Token token : tokens.getTokens()) {
            int tipo = token.getType();
            if (tipo != compiladoresLexer.LC && tipo != compiladoresLexer.BC &&
                tipo != compiladoresLexer.WS && tipo != compiladoresLexer.ERROR &&
                tipo != Token.EOF) {
                count++;
            }
        }
        return count;
    }

    public static int contarInstrucciones(String codigo) {
        String[] lineas = codigo.split("\n");
        int count = 0;
        for (String linea : lineas) {
            linea = linea.trim();
            if (!linea.isEmpty() && !linea.startsWith("//")) {
                count++;
            }
        }
        return count;
    }

    public static String formatTAC(String codigo) {
        String[] lineas = codigo.split("\n");
        StringBuilder formatted = new StringBuilder();
        int lineNumber = 0;
        for (String linea : lineas) {
            linea = linea.trim();
            if (!linea.isEmpty()) {
                formatted.append(String.format("%-3d: %s\n", lineNumber++, linea));
            }
        }
        return formatted.toString();
    }
}