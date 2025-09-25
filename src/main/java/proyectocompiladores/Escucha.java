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
    private List<Contexto> contextosFunciones = new ArrayList<>();
    private List<Identificador> parametrosPendientes = new ArrayList<>();
    private String currentAmbito = "global";

    Escucha(PrintWriter escritorErrores) {
        this.escritorErrores = escritorErrores;
    }

    public boolean verificarErrores() {
        System.out.println("🔍 Debug: verificarErrores() - errores = " + errores);
        return errores < 5;
    }

    public boolean verificarWarnings() {
        return warnings == 0;
    }

    @Override
    public void enterPrograma(compiladoresParser.ProgramaContext ctx) {
        if (tablaSimbolos.getContextos().isEmpty()) {
            tablaSimbolos.addContexto();
        }
        currentAmbito = "global";
    }

    @Override
    public void exitParametro(compiladoresParser.ParametroContext ctx) {
        String nombre = ctx.ID().getText();
        String tipoTxt = ctx.tipo().getText();
        TipoDato tipoDato;
        try {
            tipoDato = TipoDato.valueOf(tipoTxt.toUpperCase());
        } catch (IllegalArgumentException e) {
            errores++;
            escritorErrores.println("Error semántico: Tipo inválido '" + tipoTxt + "'. En línea: " + ctx.getStart().getLine());
            return;
        }
        int linea = ctx.ID().getSymbol().getLine();
        int columna = ctx.ID().getSymbol().getCharPositionInLine();
    
        // no agregamos aún a la TS (evitamos ámbito global), guardamos temporalmente
        Identificador p = new Identificador(nombre, tipoDato, linea, columna, "parametro", null, "");
        parametrosPendientes.add(p);
    }
    
    
    

    @Override
    public void exitDeclaracion(compiladoresParser.DeclaracionContext ctx) {
        String ambito = resolveAmbito(ctx);
        System.out.println("🔍 Debug: Procesando declaración en ámbito: " + ambito);
        
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
            
            Identificador identificador = new Variable(nombre, tipoDato, linea, columna, ambito, arraySize);

            if (tablaSimbolos.buscarIdentificadorLocal(identificador) == null) {
                tablaSimbolos.addIdentificador(identificador);
                System.out.println("🔍 Debug: Agregada variable '" + nombre + "' en ámbito '" + ambito + "'");
                if (decl.expresion() != null) {
                    tablaSimbolos.identificadorInicializado(identificador);
                }
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
        String ambito = resolveAmbito(ctx);

        if (tipoDato != null) {
            Identificador identificador = new Variable(nombre, tipoDato, 0, 0, ambito, null);
            tablaSimbolos.identificadorInicializado(identificador);
            tablaSimbolos.identificadorUtilizado(identificador);
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
            errores++;
            return;
        }
    
        List<TipoDato> argumentos = new ArrayList<>();
        if (ctx.parametros() != null) {
            for (compiladoresParser.ParametroContext param : ctx.parametros().parametro()) {
                argumentos.add(TipoDato.valueOf(param.tipo().getText().toUpperCase()));
            }
        }
    
        // Crear función
        Funcion funcion = new Funcion(nombre, tipoDato, linea, columna, "global", argumentos);
    
        if (tablaSimbolos.buscarIdentificadorLocal(funcion) == null) {
            tablaSimbolos.addIdentificador(funcion);
    
            // Crear un contexto para la función y agregar parámetros allí
            Contexto contextoFuncion = new Contexto();
            for (Identificador p : parametrosPendientes) {
                p.ambito = nombre; // marcar el ámbito
                contextoFuncion.addIdentificador(p);
            }
            parametrosPendientes.clear();
    
            contextosFunciones.add(contextoFuncion);
        } else {
            errores++;
            escritorErrores.println("Error semántico: Función '" + nombre + "' ya declarada.");
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
        System.out.println("🔍 Debug: Entrando en exitPrograma");
        System.out.println("🔍 Debug: Contextos disponibles: " + tablaSimbolos.getContextos().size());
        
        // Reconstruir la lista a imprimir: global + funciones
        contextoAuxiliar.clear();
        Contexto contextoGlobal = tablaSimbolos.getContextos().get(0);
        contextoAuxiliar.add(contextoGlobal);
        contextoAuxiliar.addAll(contextosFunciones);
        System.out.println("🔍 Debug: Contexto global agregado con " + contextoGlobal.getIdentificadores().size() + " identificadores");
        System.out.println("🔍 Debug: Contextos de funciones agregados: " + contextosFunciones.size());
        
        Set<Identificador> identificadores = new HashSet<>();
        for (Contexto contexto : contextoAuxiliar) {
            identificadores.addAll(contexto.getIdentificadores().values());
        }

        for (Identificador identificador : identificadores) {
            if (!identificador.isUtilizada()) {
                warnings++;
                escritorErrores.println("Advertencia: Identificador " + identificador.getNombre() + " de tipo "
                        + identificador.getTipoDato() + " ha sido declarado pero no utilizado.");
            }
        }
        
        imprimirTablaSimbolos("output/tabla_simbolos.txt");

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
        // Solo agregar contexto si no estamos ya en una función
        if (!currentAmbito.equals("global")) {
            tablaSimbolos.addContexto();
        }
    }

    @Override
    public void exitBloque(compiladoresParser.BloqueContext ctx) {
        if (!currentAmbito.equals("global")) {
            // Merge del contexto de bloque al contexto padre (función)
            List<Contexto> lst = tablaSimbolos.getContextos();
            if (lst.size() >= 2) {
                Contexto bloque = lst.get(lst.size() - 1);
                Contexto padre  = lst.get(lst.size() - 2);
                for (Identificador id : bloque.getIdentificadores().values()) {
                    padre.addIdentificador(id);
                }
            }
            tablaSimbolos.delContexto();
        }
    }

    @Override
    public void enterCuerpoFuncion(compiladoresParser.CuerpoFuncionContext ctx) {
        // Crear un contexto nuevo para la función
        tablaSimbolos.addContexto();
    
        // Definir ámbito actual como el nombre de la función
        String nombreFuncion = ctx.getParent().getChild(1).getText(); 
        currentAmbito = nombreFuncion;
    }
    
    
    @Override
    public void exitCuerpoFuncion(compiladoresParser.CuerpoFuncionContext ctx) {
        String nombreFuncion = ctx.getParent().getChild(1).getText();
    
        // Crear o recuperar contexto de la función
        Contexto contextoFuncion = null;
        for (Contexto c : contextosFunciones) {
            // Si ya existen parámetros en este contexto, lo consideramos el contexto de la función
            if (!c.getIdentificadores().isEmpty()) {
                Identificador p = c.getIdentificadores().values().iterator().next();
                if (p.getAmbito().equals(nombreFuncion)) {
                    contextoFuncion = c;
                    break;
                }
            }
        }
        if (contextoFuncion == null) {
            contextoFuncion = new Contexto();
            contextosFunciones.add(contextoFuncion);
        }
    
        // Mover parámetros pendientes al contexto de la función
        for (Identificador p : parametrosPendientes) {
            p.ambito = nombreFuncion;
            contextoFuncion.addIdentificador(p);
        }
        parametrosPendientes.clear();
    
        // Mover variables locales declaradas en el contexto actual al contexto de la función
        Contexto ctxActual = tablaSimbolos.getContextoActual();
        List<Identificador> idsParaMover = new ArrayList<>();
        for (Identificador id : ctxActual.getIdentificadores().values()) {
            if (!id.getCategoria().equals("funcion") && !id.getCategoria().equals("parametro")) {
                id.ambito = nombreFuncion;
                idsParaMover.add(id);
            }
        }
        for (Identificador id : idsParaMover) {
            contextoFuncion.addIdentificador(id);
            ctxActual.getIdentificadores().remove(id.getNombre());
        }
    
        // Volver a global
        currentAmbito = "global";
    }
    
    
    
    
    
    

    @Override
    public void enterInstruccion(compiladoresParser.InstruccionContext ctx) {
        System.out.println("�� Debug: enterInstruccion - ámbito: " + currentAmbito + " - contenido: " + ctx.getText().substring(0, Math.min(20, ctx.getText().length())));
    }

    @Override
    public void exitInstruccion(compiladoresParser.InstruccionContext ctx) {
        System.out.println("🔍 Debug: exitInstruccion - ámbito: " + currentAmbito + " - contenido: " + ctx.getText().substring(0, Math.min(20, ctx.getText().length())));
        // No invocar manualmente otros handlers aquí. ANTLR llamará a exitDeclaracion/exitAsignacion/etc con el ámbito correcto.
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
        System.out.printf("%-15s %-10s %-15s %-10s %-10s %-15s %-20s%n",
                "NOMBRE", "TIPO", "CATEGORÍA", "LÍNEA", "COLUMNA", "ÁMBITO", "DETALLES");
        System.out.println("--------------------------------------------------------------------------------------------");
    
        int symbolCount = 0;
    
        // Contexto global
        Contexto contextoGlobal = tablaSimbolos.getContextos().get(0);
        for (Identificador id : contextoGlobal.getIdentificadores().values()) {
            System.out.printf("%-15s %-10s %-15s %-10d %-10d %-15s %-20s%n",
                    id.getNombre(),
                    id.getTipoDato().toString().toLowerCase(),
                    id.getCategoria(),
                    id.getLinea(),
                    id.getColumna(),
                    "global",
                    id.getDetalles());
            symbolCount++;
        }
    
        // Contextos de funciones
        for (Contexto funcCtx : contextosFunciones) {
            for (Identificador id : funcCtx.getIdentificadores().values()) {
                System.out.printf("%-15s %-10s %-15s %-10d %-10d %-15s %-20s%n",
                        id.getNombre(),
                        id.getTipoDato().toString().toLowerCase(),
                        id.getCategoria(),
                        id.getLinea(),
                        id.getColumna(),
                        id.getAmbito(),
                        id.getDetalles());
                symbolCount++;
            }
        }
    
        return symbolCount;
    }
    
    
    

    public void imprimirTablaSimbolos(String archivoSalida) {
        try (PrintWriter escritor = new PrintWriter(new FileWriter(archivoSalida))) {
            System.out.println("=== TABLA DE SÍMBOLOS ===");
            System.out.printf("%-15s %-10s %-12s %-8s %-8s %-15s %-20s%n",
                    "NOMBRE", "TIPO", "CATEGORÍA", "LÍNEA", "COLUMNA", "ÁMBITO", "DETALLES");
            System.out.println("--------------------------------------------------------------------------------------------");
            
            for (Contexto ctx : tablaSimbolos.getContextos()) {
                for (Identificador id : ctx.getIdentificadores().values()) {
                    System.out.printf("%-15s %-10s %-12s %-8d %-8d %-15s %-20s%n",
                            id.getNombre(),
                            id.getTipoDato(),
                            id.getCategoria(),
                            id.getLinea(),
                            id.getColumna(),
                            id.getAmbito(),
                            id.getDetalles());
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error al escribir la tabla de símbolos: " + e.getMessage());
        }
    }

    public void debugContextos() {
        System.out.println("🔍 Debug: Contextos en contextoAuxiliar:");
        for (int i = 0; i < contextoAuxiliar.size(); i++) {
            Contexto contexto = contextoAuxiliar.get(i);
            System.out.println("Contexto " + i + " tiene " + contexto.getIdentificadores().size() + " identificadores:");
            for (Identificador id : contexto.getIdentificadores().values()) {
                System.out.println("  - " + id.getNombre() + " (" + id.getAmbito() + ")");
            }
        }
    }

    private String resolveAmbito(org.antlr.v4.runtime.ParserRuleContext ctx) {
        org.antlr.v4.runtime.RuleContext p = ctx;
        while (p != null) {
            if (p instanceof compiladoresParser.CuerpoFuncionContext) {
                compiladoresParser.CuerpoFuncionContext f = (compiladoresParser.CuerpoFuncionContext) p;
                if (f.ID() != null) return f.ID().getText();
            }
            p = p.getParent();
        }
        return "global";
    }
}
