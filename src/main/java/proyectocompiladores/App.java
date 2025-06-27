package proyectocompiladores;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.antlr.v4.runtime.CharStreams;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;

// Las diferentes entradas se explicaran oportunamente
public class App {
    public static void main(String[] args) throws Exception {
        // leer el input de txt con codico C
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
                System.out
                        .println("Advertencias Detectadas, se reanuda la ejecucion, verifique el archivo errores.txt");
            }

            if (!escucha.verificarErrores() || !manejadorErrores.verificarErrores()) {
                System.out.println("Errores Detectados, se reanuda la ejecucion, verifique el archivo errores.txt");
            }

        } catch (IOException e) {
            System.out.println("Error al escribir en el archivo de errores: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
