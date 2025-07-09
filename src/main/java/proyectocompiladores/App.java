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

public class App {
    public static void main(String[] args) throws Exception {
        ParseTree tree = null;
        Caminante visitor = null;
        String codTresDir = null;
        compiladoresParser parser = null;

        boolean inputParseado = false;
        boolean codTresDirGenerado = false;
        boolean errorParseo = false;
        int opcion = 0;

        while (opcion != 4) {
            opcion = menu();
            switch (opcion) {
                case 1:
                    // opcion para leer el archivo y parsearlo
                    try (PrintWriter escritorErrores = new PrintWriter(new FileWriter("output/errores.txt"))) {
                        CharStream input = CharStreams.fromFileName("input/entrada.txt");
                        compiladoresLexer lexer = new compiladoresLexer(input);
                        CommonTokenStream tokens = new CommonTokenStream(lexer);
                        parser = new compiladoresParser(tokens);

                        ManejadorErrores manejadorErrores = new ManejadorErrores(escritorErrores);
                        parser.removeErrorListeners();
                        parser.addErrorListener(manejadorErrores);

                        Escucha escucha = new Escucha(escritorErrores);
                        parser.addParseListener(escucha);

                        tree = parser.programa();

                        if (!escucha.verificarWarnings()) {
                            System.out.println("Advertencias detectadas, verifique el archivo errores.txt");
                        }
                        if (!escucha.verificarErrores() || !manejadorErrores.verificarErrores()) {
                            System.out.println("Errores detectados, verifique el archivo errores.txt");
                            errorParseo = true;
                        } else {
                            System.out.println("Parseo exitoso.");
                            inputParseado = true;
                            codTresDirGenerado = false;
                            errorParseo = false;
                        }
                    } catch (IOException e) {
                        System.out.println("Error al escribir en el archivo de errores: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    // Opcion para generar el codigo de tres direcciones
                    if (errorParseo) {
                        System.out.println("Por favor, primero verifique y corrija los errores de parseo (opción 1).");
                        break;
                    }
                    if (!inputParseado) {
                        System.out.println("Por favor, primero parsea el código de entrada (opción 1).");
                        break;
                    }
                    visitor = new Caminante();
                    visitor.visit(tree);
                    codTresDir = visitor.getCodigoGenerado();
                    try (PrintWriter escritorCodigo = new PrintWriter(
                            new FileWriter("output/codigo_3direcciones.txt"))) {
                        escritorCodigo.print(codTresDir);
                        System.out.println("Código de tres direcciones generado en 'codigo_3direcciones.txt'.");
                        codTresDirGenerado = true;
                    } catch (IOException e) {
                        System.err.println("Error al escribir el archivo de código de tres direcciones.");
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    // Opcion para optimizar el codigo de tres direcciones generado
                    if (!codTresDirGenerado) {
                        System.out.println("Por favor, primero genere el código de tres direcciones (opción 2).");
                        break;
                    }
                    Optimizador optimizador = new Optimizador(codTresDir);
                    String codigoOptimizado = optimizador.optimizar();
                    try (PrintWriter escritorOptimizado = new PrintWriter(
                            new FileWriter("output/codigo_optimizado.txt"))) {
                        escritorOptimizado.print(codigoOptimizado);
                        System.out.println("Código optimizado generado en 'codigo_optimizado.txt'.");
                    } catch (IOException e) {
                        System.err.println("Error al escribir el archivo de código optimizado.");
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Por favor, intente nuevamente.");
            }
        }
    }

    public static int menu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("-------------------------------- Compilador --------------------------------");
        System.out.println("Seleccione una opción:");
        System.out.println("1-> Leer el codigo de entrada desde un archivo");
        System.out.println("2-> Generar el Codigo de Tres Direcciones");
        System.out.println("3-> Generar el Codigo de Tres Direcciones Optimizado");
        System.out.println("4-> Salir");
        System.out.print("Ingrese su opción: ");

        int opcion = scanner.nextInt();
        return opcion;
    }

}
