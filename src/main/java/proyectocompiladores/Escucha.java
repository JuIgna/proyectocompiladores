package proyectocompiladores;

import java.util.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import proyectocompiladores.compilador.compiladoresBaseListener;
import proyectocompiladores.compilador.compiladoresParser;

import proyectocompiladores.contexto.Contexto;
import proyectocompiladores.contexto.Funcion;
import proyectocompiladores.contexto.TablaSimbolos;
import proyectocompiladores.contexto.Identificador;
import proyectocompiladores.contexto.TipoDato;
import proyectocompiladores.contexto.Variable;

public class Escucha extends compiladoresBaseListener {
    private TablaSimbolos tablaSimbolos = new TablaSimbolos();
    private PrintWriter escritorErrores;
    private int errores = 0;
    private int warnings = 0;
    private int BalanceLlaves = 0;
    private int BalanceParentesis = 0;
    private List<Contexto> contextoAuxiliar = new ArrayList<Contexto>();
    private String currentAmbito = "global";

    Escucha(PrintWriter escritorErrores) {
        this.escritorErrores = escritorErrores;
    }

    public boolean verificarErrores() {
        return errores == 0;
    }

    public boolean verificarWarnings() {
        return warnings == 0;
    }

    @Override
    public void enterPrograma(compiladoresParser.ProgramaContext ctx) {
        tablaSimbolos.addContexto();
    }

    @Override
    public void exitParametro(compiladoresParser.ParametroContext ctx) {
        String tipo = ctx.tipo().getText();
        TipoDato tipoDato;
        try {
            tipoDato = TipoDato.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return;
        }

        String nombre = ctx.ID().getText();
        int linea = ctx.ID().getSymbol().getLine();
        int columna = ctx.ID().getSymbol().getCharPositionInLine();
        Identificador identificador = new Variable(nombre, tipoDato, linea, columna, currentAmbito, null);

        if (tablaSimbolos.buscarIdentificadorLocal(identificador) == null) {
            tablaSimbolos.addIdentificador(identificador);
            tablaSimbolos.identificadorInicializado(identificador);
        } else {
            errores++;
            escritorErrores.println(
                    "Error semántico: Identificador '" + nombre + "' ya ha sido declarado. En línea: " + linea);
        }
    }

    @Override
    public void exitDeclaracion(compiladoresParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();
        TipoDato tipoDato;
        try {
            tipoDato = TipoDato.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            errores++;
            escritorErrores
                    .println("Error semántico: Tipo inválido '" + tipo + "'. En línea: " + ctx.getStart().getLine());
            return;
        }

        for (compiladoresParser.DeclaradorContext decl : ctx.declarador()) {
            String nombre = decl.ID().getText();
            int linea = decl.ID().getSymbol().getLine();
            int columna = decl.ID().getSymbol().getCharPositionInLine();
            Integer arraySize = decl.CORCHETE() != null ? Integer.parseInt(decl.NUMERO().getText()) : null;
            Identificador identificador = new Variable(nombre, tipoDato, linea, columna, currentAmbito, arraySize);

            if (tablaSimbolos.buscarIdentificadorLocal(identificador) == null) {
                tablaSimbolos.addIdentificador(identificador);
                if (decl.expresion() != null) {
                    tablaSimbolos.identificadorInicializado(identificador);
                }
                // No reportar error de inicialización aquí, permitir asignaciones posteriores
            } else {
                errores++;
                escritorErrores.println(
                        "Error semántico: Identificador '" + nombre + "' ya ha sido declarado. En línea: " + linea);
            }
        }
    }

    @Override
    public void exitAsignacion(compiladoresParser.AsignacionContext ctx) {
        String nombre = ctx.ID().getText();
        TipoDato tipoDato = tablaSimbolos.buscarTipoIdentificador(nombre);

        if (tipoDato != null) {
            Identificador identificador = new Variable(nombre, tipoDato, 0, 0, currentAmbito, null);
            tablaSimbolos.identificadorInicializado(identificador);
            tablaSimbolos.identificadorUtilizado(identificador); // Marcar como utilizado
            // Marcar variables usadas en la expresión
            if (ctx.expresion(ctx.CORCHETE() != null ? 1 : 0) != null) {
                marcarVariablesUsadas(ctx.expresion(ctx.CORCHETE() != null ? 1 : 0));
            }
        } else {
            errores++;
            escritorErrores.println("Error semántico: Identificador '" + nombre + "' no ha sido declarado. En línea: "
                    + ctx.ID().getSymbol().getLine());
        }
    }

    @Override
    public void exitLlamadaFuncion(compiladoresParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        Identificador idEncontrado = tablaSimbolos.buscarIdentificadorPorNombre(nombre);
        if (idEncontrado != null && idEncontrado.getCategoria().equals("funcion")) {
            tablaSimbolos.identificadorUtilizado(idEncontrado);
            // Marcar argumentos como utilizados
            for (compiladoresParser.ExpresionContext exp : ctx.expresion()) {
                marcarVariablesUsadas(exp);
            }
        } else {
            errores++;
            escritorErrores.println("Error semántico: Identificador '" + nombre
                    + "'. La función no está creada. En línea: " + ctx.ID().getSymbol().getLine());
        }
    }

    @Override
    public void exitDeclaracionFuncion(compiladoresParser.DeclaracionFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        String tipo = ctx.tipo().getText();
        int linea = ctx.ID().getSymbol().getLine();
        int columna = ctx.ID().getSymbol().getCharPositionInLine();
        TipoDato tipoDato;
        try {
            tipoDato = TipoDato.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException e) {
            return;
        }

        List<TipoDato> argumentos = new ArrayList<>();
        if (ctx.parametros() != null) {
            for (compiladoresParser.ParametroContext param : ctx.parametros().parametro()) {
                argumentos.add(TipoDato.valueOf(param.tipo().getText().toUpperCase()));
            }
        }
        Identificador identificador = new Funcion(nombre, tipoDato, linea, columna, currentAmbito, argumentos);

        if (tablaSimbolos.buscarIdentificadorLocal(identificador) == null) {
            tablaSimbolos.addIdentificador(identificador);
            tablaSimbolos.identificadorInicializado(identificador);
        } else {
            errores++;
            escritorErrores.println("Error semántico: Identificador '" + nombre + "' ya ha sido declarado. En línea: "
                    + linea);
        }
    }

    @Override
    public void exitExpresion(compiladoresParser.ExpresionContext ctx) {
        if (ctx.expresionLogica() != null) {
            TipoDato tipo = obtenerTipoExpresionLogica(ctx.expresionLogica());
            if (tipo != TipoDato.BOOL) {
                errores++;
                escritorErrores.println("Error semántico: La expresión lógica debe ser de tipo booleano. En línea: "
                        + ctx.getStart().getLine());
            }
        }
    }

    public TipoDato obtenerTipoExpresionLogica(compiladoresParser.ExpresionLogicaContext ctx) {
        TipoDato tipo = obtenerTipoExpresionComparacion(ctx.expresionComparacion(0));

        for (int i = 1; i < ctx.expresionComparacion().size(); i++) {
            TipoDato tipoDer = obtenerTipoExpresionComparacion(ctx.expresionComparacion(i));
            if (tipo != TipoDato.BOOL || tipoDer != TipoDato.BOOL) {
                return null;
            }
        }
        return TipoDato.BOOL;
    }

    public TipoDato obtenerTipoExpresionComparacion(compiladoresParser.ExpresionComparacionContext ctx) {
        TipoDato tipoIzq = obtenerTipoExpresionAritmetica(ctx.expresionAritmetica(0));

        if (ctx.COMP() != null) {
            TipoDato tipoDer = obtenerTipoExpresionAritmetica(ctx.expresionAritmetica(1));
            if (tipoIzq == null || tipoDer == null) {
                return null;
            }
            if ((tipoIzq == TipoDato.INT || tipoIzq == TipoDato.DOUBLE || tipoIzq == TipoDato.CHAR) &&
                    (tipoDer == TipoDato.INT || tipoDer == TipoDato.DOUBLE || tipoDer == TipoDato.CHAR)) {
                return TipoDato.BOOL;
            }
            return null;
        }
        return tipoIzq;
    }

    public TipoDato obtenerTipoExpresionAritmetica(compiladoresParser.ExpresionAritmeticaContext ctx) {
        TipoDato tipo = obtenerTipoTermino(ctx.termino(0));

        for (int i = 1; i < ctx.termino().size(); i++) {
            TipoDato tipoDer = obtenerTipoTermino(ctx.termino(i));
            if (tipo == null || tipoDer == null) {
                return null;
            }
            if (tipo == TipoDato.INT && tipoDer == TipoDato.INT) {
                tipo = TipoDato.INT;
            } else if ((tipo == TipoDato.INT || tipo == TipoDato.DOUBLE || tipo == TipoDato.CHAR) &&
                    (tipoDer == TipoDato.INT || tipoDer == TipoDato.DOUBLE || tipoDer == TipoDato.CHAR)) {
                tipo = TipoDato.DOUBLE;
            } else {
                return null;
            }
        }
        return tipo;
    }

    public TipoDato obtenerTipoTermino(compiladoresParser.TerminoContext ctx) {
        TipoDato tipo = obtenerTipoFactor(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++) {
            TipoDato tipoDer = obtenerTipoFactor(ctx.factor(i));
            if (tipo == null || tipoDer == null) {
                return null;
            }
            if (tipo == TipoDato.INT && tipoDer == TipoDato.INT) {
                tipo = TipoDato.INT;
            } else if ((tipo == TipoDato.INT || tipo == TipoDato.DOUBLE || tipo == TipoDato.CHAR) &&
                    (tipoDer == TipoDato.INT || tipoDer == TipoDato.DOUBLE || tipoDer == TipoDato.CHAR)) {
                tipo = TipoDato.DOUBLE;
            } else {
                return null;
            }
        }
        return tipo;
    }

    public TipoDato obtenerTipoFactor(compiladoresParser.FactorContext ctx) {
        if (ctx.ID() != null) {
            return tablaSimbolos.buscarTipoIdentificador(ctx.ID().getText());
        } else if (ctx.NUMERO() != null) {
            return TipoDato.INT;
        } else if (ctx.DOUBLE_LITERAL() != null) {
            return TipoDato.DOUBLE;
        } else if (ctx.CHAR_LITERAL() != null) {
            return TipoDato.CHAR;
        } else if (ctx.expresion() != null) {
            return obtenerTipoExpresionLogica(ctx.expresion().expresionLogica());
        }
        return null;
    }

    @Override
    public void exitPrograma(compiladoresParser.ProgramaContext ctx) {
        Set<Identificador> identificadores = new HashSet<>();
        for (Contexto contexto : tablaSimbolos.getContextos()) {
            identificadores.addAll(contexto.getIdentificadores().values());
        }

        for (Identificador identificador : identificadores) {
            if (!identificador.isUtilizada()) {
                warnings++;
                escritorErrores.println("Advertencia: Identificador " + identificador.getNombre() + " de tipo "
                        + identificador.getTipoDato() + " ha sido declarado pero no utilizado.");
            }
        }
        Contexto contexto = tablaSimbolos.getContextoActual();
        contextoAuxiliar.add(contexto);
        imprimirTablaSimbolos("output/tabla_simbolos.txt");
        tablaSimbolos.delContexto();

        if (BalanceLlaves != 0) {
            errores++;
            escritorErrores.println("ERROR: Las llaves no están balanceadas");
        }
        if (BalanceParentesis != 0) {
            errores++;
            escritorErrores.println("ERROR: Los paréntesis no están balanceados");
        }
        currentAmbito = "global";
    }

    @Override
    public void enterBloque(compiladoresParser.BloqueContext ctx) {
        tablaSimbolos.addContexto();
    }

    @Override
    public void exitBloque(compiladoresParser.BloqueContext ctx) {
        Contexto contexto = tablaSimbolos.getContextoActual();
        contextoAuxiliar.add(contexto);
        tablaSimbolos.delContexto();
    }

    @Override
    public void enterCuerpoFuncion(compiladoresParser.CuerpoFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        currentAmbito = nombre; // Establecer el ámbito para las variables dentro de la función
        tablaSimbolos.addContexto();
    }

    @Override
    public void exitCuerpoFuncion(compiladoresParser.CuerpoFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        String tipo = ctx.tipo().getText();
        TipoDato tipoDato = TipoDato.valueOf(tipo.toUpperCase());
        int linea = ctx.ID().getSymbol().getLine();
        int columna = ctx.ID().getSymbol().getCharPositionInLine();
        List<TipoDato> argumentos = new ArrayList<>();
        if (ctx.parametros() != null) {
            for (compiladoresParser.ParametroContext param : ctx.parametros().parametro()) {
                argumentos.add(TipoDato.valueOf(param.tipo().getText().toUpperCase()));
            }
        }
        currentAmbito = nombre;
        Identificador identificador = new Funcion(nombre, tipoDato, linea, columna, currentAmbito, argumentos);
        if (tablaSimbolos.buscarIdentificadorLocal(identificador) == null) {
            tablaSimbolos.addIdentificador(identificador);
            tablaSimbolos.identificadorInicializado(identificador);
        }
        tablaSimbolos.addContexto();
    }

    @Override
    public void enterInstruccion(compiladoresParser.InstruccionContext ctx) {
        // System.out.println("Instruccion: " + ctx.getText());
    }

    @Override
    public void exitInstruccion(compiladoresParser.InstruccionContext ctx) {
        // System.out.println("Instruccion exit: " + ctx.getText());
    }

    @Override
    public void exitForLoop(compiladoresParser.ForLoopContext ctx) {
        if (ctx.bloque() == null) {
            System.err.println("ERROR: Falta el bloque de código para el FOR.");
            errores++;
        }

        String bloqueCodigo = ctx.bloque().getText();
        if (!bloqueCodigo.startsWith("{")) {
            System.err.println("ERROR: El bloque del FOR debe estar encerrado entre '{' y '}'");
            BalanceLlaves++;
            errores++;
        } else if (!bloqueCodigo.endsWith("}")) {
            System.err.println("ERROR: El bloque del FOR debe estar encerrado entre '{' y '}'");
            BalanceLlaves--;
            errores++;
        }

        if (ctx.PA() == null || ctx.PA().getText().contains("missing")) {
            System.err.println("ERROR: Paréntesis desbalanceados o faltantes en el bloque FOR.");
            BalanceParentesis++;
            errores++;
        } else if (ctx.PC() == null || ctx.PC().getText().contains("missing")) {
            System.err.println("ERROR: Paréntesis desbalanceados o faltantes en el bloque FOR.");
            BalanceParentesis--;
            errores++;
        }
    }

    @Override
    public void exitWhileLoop(compiladoresParser.WhileLoopContext ctx) {
        String bloqueCodigo = ctx.bloque().getText();
        if (!bloqueCodigo.startsWith("{")) {
            System.err.println("ERROR: El bloque del WHILE debe estar encerrado entre '{' y '}'.");
            BalanceLlaves++;
            errores++;
        } else if (!bloqueCodigo.endsWith("}")) {
            System.err.println("ERROR: El bloque del WHILE debe estar encerrado entre '{' y '}'.");
            BalanceLlaves--;
            errores++;
        }

        if (ctx.PA() == null || ctx.PA().getText().contains("missing")) {
            System.err.println("ERROR: Paréntesis desbalanceados o faltantes en el bloque WHILE.");
            BalanceParentesis++;
            errores++;
        } else if (ctx.PC() == null || ctx.PC().getText().contains("missing")) {
            System.err.println("ERROR: Paréntesis desbalanceados o faltantes en el bloque WHILE.");
            BalanceParentesis--;
            errores++;
        }
    }

    @Override
    public void exitIfElse(compiladoresParser.IfElseContext ctx) {
        if (ctx.bloque(0) == null) {
            System.err.println("ERROR: Falta el bloque de código para el IF.");
            errores++;
        }

        String bloqueIf = ctx.bloque(0).getText();
        if (!bloqueIf.startsWith("{")) {
            System.err.println("ERROR: El bloque del IF debe estar encerrado entre '{' y '}'.");
            BalanceLlaves++;
            errores++;
        } else if (!bloqueIf.endsWith("}")) {
            System.err.println("ERROR: El bloque del IF debe estar encerrado entre '{' y '}'.");
            BalanceLlaves--;
            errores++;
        }

        if (ctx.PA() == null || ctx.PC() == null || ctx.PA().getText().contains("missing")
                || ctx.PC().getText().contains("missing")) {
            System.err.println("ERROR: Paréntesis desbalanceados o faltantes en el bloque IF.");
            errores++;
        }

        if (ctx.ELSE() != null) {
            if (ctx.bloque(1) == null) {
                System.err.println("ERROR: Falta el bloque de código para el ELSE.");
                errores++;
            } else {
                String bloqueElse = ctx.bloque(1).getText();
                if (!bloqueElse.startsWith("{") || !bloqueElse.endsWith("}")) {
                    System.err.println("ERROR: El bloque del ELSE debe estar encerrado entre '{' y '}'.");
                    errores++;
                }
            }
        }
    }

    private void marcarVariablesUsadas(compiladoresParser.ExpresionContext ctx) {
        if (ctx.expresionLogica() != null) {
            for (compiladoresParser.ExpresionComparacionContext comp : ctx.expresionLogica().expresionComparacion()) {
                marcarVariablesUsadasEnComparacion(comp);
            }
        }
    }

    private void marcarVariablesUsadasEnComparacion(compiladoresParser.ExpresionComparacionContext ctx) {
        for (compiladoresParser.ExpresionAritmeticaContext arith : ctx.expresionAritmetica()) {
            for (compiladoresParser.TerminoContext term : arith.termino()) {
                for (compiladoresParser.FactorContext factor : term.factor()) {
                    if (factor.ID() != null) {
                        String nombre = factor.ID().getText();
                        Identificador id = tablaSimbolos.buscarIdentificadorPorNombre(nombre);
                        if (id != null) {
                            tablaSimbolos.identificadorUtilizado(id);
                        }
                    }
                    if (factor.expresion() != null) {
                        marcarVariablesUsadas(factor.expresion());
                    }
                    if (factor.llamadaFuncion() != null) {
                        String nombre = factor.llamadaFuncion().ID().getText();
                        Identificador id = tablaSimbolos.buscarIdentificadorPorNombre(nombre);
                        if (id != null) {
                            tablaSimbolos.identificadorUtilizado(id);
                        }
                    }
                }
            }
        }
    }

    public int imprimirTablaSimbolosConsola() {
        System.out.println("=== TABLA DE SÍMBOLOS ===");
        System.out.println("NOMBRE          TIPO       CATEGORÍA       LÍNEA      COLUMNA    ÁMBITO          DETALLES");
        System.out.println(
                "--------------------------------------------------------------------------------------------");

        int symbolCount = 0;
        for (Contexto contexto : contextoAuxiliar) {
            for (Identificador id : contexto.getIdentificadores().values()) {
                System.out.printf("%-15s %-10s %-15s %-10d %-10d %-15s %-20s\n",
                        id.getNombre(), id.getTipoDato().toString().toLowerCase(), id.getCategoria(),
                        id.getLinea(), id.getColumna(), id.getAmbito(), id.getDetalles());
                symbolCount++;
            }
        }
        return symbolCount;
    }

    public void imprimirTablaSimbolos(String archivoSalida) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivoSalida))) {
            escritor.println("=== TABLA DE SÍMBOLOS ===");
            escritor.println(
                    "NOMBRE          TIPO       CATEGORÍA       LÍNEA      COLUMNA    ÁMBITO          DETALLES");
            escritor.println(
                    "--------------------------------------------------------------------------------------------");

            for (Contexto contexto : contextoAuxiliar) {
                for (Identificador id : contexto.getIdentificadores().values()) {
                    escritor.printf("%-15s %-10s %-15s %-10d %-10d %-15s %-20s\n",
                            id.getNombre(), id.getTipoDato().toString().toLowerCase(), id.getCategoria(),
                            id.getLinea(), id.getColumna(), id.getAmbito(), id.getDetalles());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al escribir la tabla de símbolos: " + e.getMessage());
        }
    }
}
