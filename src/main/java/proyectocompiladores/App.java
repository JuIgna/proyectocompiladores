package proyectocompiladores;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import proyectocompiladores.compilador.compiladoresLexer;
import proyectocompiladores.compilador.compiladoresParser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

public class App {
    public static void main(String[] args) throws Exception {
        // leer el input de txt con codigo C
        CharStream input = CharStreams.fromFileName("input/entrada.txt");

        compiladoresLexer lexer = new compiladoresLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        compiladoresParser parser = new compiladoresParser(tokens);

        try (PrintWriter escritorErrores = new PrintWriter(new FileWriter("output/errores.txt"))) {
            ManejadorErrores manejadorErrores = new ManejadorErrores(escritorErrores);
            parser.removeErrorListeners();
            parser.addErrorListener(manejadorErrores);

            // Crear el escucha y agregarlo al parser
            Escucha escucha = new Escucha(escritorErrores);
            parser.addParseListener(escucha); // Agregar el listener

            // parsea el codigo
            ParseTree tree = parser.programa();

            // revisa y continua si solo hay warnings
            if (!escucha.verificarWarnings()) {
                System.out.println("Advertencias Detectadas, se reanuda la ejecucion, verifique el archivo errores.txt");
            }

            if (!escucha.verificarErrores() || !manejadorErrores.verificarErrores()) {
                System.out.println("Errores Detectados, se reanuda la ejecucion, verifique el archivo errores.txt");
            }

            // Usar el visitor para generar el codigo de tres direcciones
            Caminante visitor = new Caminante();
            visitor.visit(tree);
            System.out.println(tree.toStringTree(parser));

            // imprimir la tabla de simbolos en un archivo
            // escucha.imprimirTablaSimbolos("output/tabla_simbolos.txt");

            // escribir el codigo de tres direcciones en un archivo
            String codigoGenerado = visitor.getCodigoGenerado();
            try (PrintWriter escritorCodigo = new PrintWriter(new FileWriter("output/codigo_3direcciones.txt"))) {
                escritorCodigo.print(codigoGenerado);
                System.out.println("Código de tres direcciones generado en 'codigo_3direcciones.txt'.");
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo de código de tres direcciones.");
                e.printStackTrace();
            }

            // optimizar el codigo generado
            Optimizador optimizador = new Optimizador(codigoGenerado);
            String codigoOptimizado = optimizador.optimizar();

            try (PrintWriter escritorOptimizado = new PrintWriter(new FileWriter("output/codigo_optimizado.txt"))) {
                escritorOptimizado.print(codigoOptimizado);
                System.out.println("Código optimizado generado en 'codigo_optimizado.txt'.");
            } catch (IOException e) {
                System.err.println("Error al escribir el archivo de codigo optimizado.");
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de errores: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
