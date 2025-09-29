package proyectocompiladores;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import proyectocompiladores.compilador.compiladoresBaseVisitor;
import proyectocompiladores.compilador.compiladoresParser;

import proyectocompiladores.contexto.TipoDato;

public class Caminante extends compiladoresBaseVisitor<String> {
    private final StringBuilder codigoTresDirecciones = new StringBuilder();
    private int tempCounter = 1; // comenzar en t1
    private int labelCounter = 0; // contar etiquetas
    private String currentFunction = null;
    private int instruccionCounter = 0;

    private String nuevaTemporal() {
        return "t" + (tempCounter++);
    }

    private String nuevaEtiqueta() {
        return "L" + (labelCounter++);
    }

    public String getCodigoGenerado() {
        return codigoTresDirecciones.toString();
    }

    @Override
    public String visitPrograma(compiladoresParser.ProgramaContext ctx) {
        appendInstruccion("// Código de tres direcciones generado");
        appendInstruccion("PROGRAMA_INICIO:");
        appendInstruccion("// Declaración de variables globales");

        // Procesar declaraciones globales y funciones
        for (compiladoresParser.InstruccionContext instruccion : ctx.instruccion()) {
            if (instruccion.declaracion() != null && currentFunction == null) {
                visit(instruccion.declaracion());
            } else if (instruccion.declaracionFuncion() != null) {
                visit(instruccion.declaracionFuncion());
            }
        }

        // Procesar funciones (si parsed as cuerpoFuncion)
        for (compiladoresParser.CuerpoFuncionContext cuerpoFuncion : ctx.cuerpoFuncion()) {
            visit(cuerpoFuncion);
        }

        appendInstruccion("PROGRAMA_FIN:");
        return null;
    }

    @Override
    public String visitDeclaracion(compiladoresParser.DeclaracionContext ctx) {
        String tipo = ctx.tipo().getText();
        for (compiladoresParser.DeclaradorContext decl : ctx.declarador()) {
            String nombre = decl.ID().getText();
            Integer arraySize = decl.CORCHETE() != null ? Integer.parseInt(decl.NUMERO().getText()) : null;
            appendInstruccion("DECLARE " + nombre + (arraySize != null ? "[" + arraySize + "]" : "") + " " + tipo);
            if (decl.expresion() != null) {
                String expresion = visit(decl.expresion());
                appendInstruccion(nombre + " = " + expresion);
            }
        }
        return null;
    }

    @Override
    public String visitAsignacion(compiladoresParser.AsignacionContext ctx) {
        System.out.println("🔍 DEBUG visitAsignacion: " + ctx.getText());
        
        String nombre = ctx.ID().getText();
        System.out.println("🔍 DEBUG: Variable destino: " + nombre);
        
        String index = ctx.CORCHETE() != null ? "[" + visit(ctx.expresion(0)) + "]" : "";
        System.out.println("🔍 DEBUG: Índice de array: " + index);
        
        String expresion = visit(ctx.expresion(ctx.CORCHETE() != null ? 1 : 0));
        System.out.println("🔍 DEBUG: Expresión calculada: " + expresion);
        
        // Generar la asignación siempre (sin optimización)
        appendInstruccion(nombre + index + " = " + expresion);
        return nombre + index;
    }

    Map<String, String> cacheExpresiones = new HashMap<>();

    @Override
    public String visitExpresion(compiladoresParser.ExpresionContext ctx) {
        String expresionTexto = ctx.getText();
        if (cacheExpresiones.containsKey(expresionTexto)) {
            return cacheExpresiones.get(expresionTexto);
        }
        String resultado = visit(ctx.expresionLogica());
        cacheExpresiones.put(expresionTexto, resultado);
        return resultado;
    }

    @Override
    public String visitExpresionLogica(compiladoresParser.ExpresionLogicaContext ctx) {
        String expresionTexto = ctx.getText();
        if (cacheExpresiones.containsKey(expresionTexto)) {
            return cacheExpresiones.get(expresionTexto);
        }

        String temp = visit(ctx.expresionComparacion(0));

        for (int i = 1; i < ctx.expresionComparacion().size(); i++) {
            String temp2 = visit(ctx.expresionComparacion(i));
            String operador = ctx.op_logicas(i - 1).getText();
            String nuevaTemp = nuevaTemporal();
            appendInstruccion(nuevaTemp + " = " + temp + " " + operador + " " + temp2);
            temp = nuevaTemp;
        }

        cacheExpresiones.put(expresionTexto, temp);
        return temp;
    }

    @Override
    public String visitExpresionComparacion(compiladoresParser.ExpresionComparacionContext ctx) {
        String temp = visit(ctx.expresionAritmetica(0));

        if (ctx.COMP() != null) {
            String temp2 = visit(ctx.expresionAritmetica(1));
            String operador = ctx.COMP().getText();
            String nuevaTemp = nuevaTemporal();
            appendInstruccion(nuevaTemp + " = " + temp + " " + operador + " " + temp2);
            temp = nuevaTemp;
        }

        return temp;
    }

    @Override
    public String visitExpresionAritmetica(compiladoresParser.ExpresionAritmeticaContext ctx) {
        System.out.println("🔍 DEBUG visitExpresionAritmetica: " + ctx.getText());
        System.out.println("🔍 DEBUG: Número de términos: " + ctx.termino().size());
        
        String temp = visit(ctx.termino(0));
        System.out.println("🔍 DEBUG: Primer término: " + temp);
    
        for (int i = 1; i < ctx.termino().size(); i++) {
            String temp2 = visit(ctx.termino(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            System.out.println("🔍 DEBUG: Término " + i + ": " + temp2 + ", Operador: " + operador);
            
            String nuevaTemp = nuevaTemporal();
            appendInstruccion(nuevaTemp + " = " + temp + " " + operador + " " + temp2);
            temp = nuevaTemp;
        }
    
        return temp;
    }

    @Override
    public String visitTermino(compiladoresParser.TerminoContext ctx) {
        System.out.println("🔍 DEBUG visitTermino: " + ctx.getText());
        System.out.println("🔍 DEBUG: Número de factores: " + ctx.factor().size());
        
        String temp = visit(ctx.factor(0));
        System.out.println("🔍 DEBUG: Primer factor: " + temp);
    
        for (int i = 1; i < ctx.factor().size(); i++) {
            String temp2 = visit(ctx.factor(i));
            String operador = ctx.getChild(2 * i - 1).getText();
            System.out.println("🔍 DEBUG: Factor " + i + ": " + temp2 + ", Operador: " + operador);
            
            String nuevaTemp = nuevaTemporal();
            appendInstruccion(nuevaTemp + " = " + temp + " " + operador + " " + temp2);
            temp = nuevaTemp;
        }
    
        return temp;
    }

    @Override
    public String visitFactor(compiladoresParser.FactorContext ctx) {
        System.out.println("🔍 DEBUG visitFactor: " + ctx.getText());
        
        // PRIORIDAD 1: Verificar acceso a array ANTES que paréntesis
        if (ctx.ID() != null && ctx.CORCHETE() != null) {
            String id = ctx.ID().getText();
            System.out.println("🔍 DEBUG: ID encontrado: " + id);
            System.out.println("🔍 DEBUG: Acceso a array detectado");
            String index = visit(ctx.expresion());
            System.out.println("🔍 DEBUG: Índice calculado: " + index);
            String result = id + "[" + index + "]";
            System.out.println("🔍 DEBUG: Resultado final: " + result);
            return result;
        }
        // PRIORIDAD 2: Verificar expresión entre paréntesis
        else if (ctx.expresion() != null) {
            System.out.println("🔍 DEBUG: Procesando expresión entre paréntesis");
            return visit(ctx.expresion());
        }
        // PRIORIDAD 3: Variable simple
        else if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            System.out.println("🔍 DEBUG: Variable simple: " + id);
            return id;
        } else if (ctx.STRING() != null) {
            return ctx.STRING().getText();
        } else if (ctx.CHAR_LITERAL() != null) {
            return ctx.CHAR_LITERAL().getText();
        } else if (ctx.llamadaPrints() != null) {
            return visit(ctx.llamadaPrints());
        } else if (ctx.llamadaFuncion() != null) {
            return visit(ctx.llamadaFuncion());
        } else if (ctx.incrementoDecremento() != null) {
            return visit(ctx.incrementoDecremento());
        } else if (ctx.NUMERO() != null) {
            String num = (ctx.RESTA() != null ? "-" : "") + ctx.NUMERO().getText();
            System.out.println("🔍 DEBUG: Número encontrado: " + num);
            return num;
        } else if (ctx.DOUBLE_LITERAL() != null) {
            return ctx.DOUBLE_LITERAL().getText();
        } else if (ctx.booleano() != null) {
            return ctx.booleano().getText();
        }
        return null;
    }

    @Override
    public String visitEstructuraControl(compiladoresParser.EstructuraControlContext ctx) {
        if (ctx.ifElse() != null) {
            return visit(ctx.ifElse());
        } else if (ctx.whileLoop() != null) {
            return visit(ctx.whileLoop());
        } else if (ctx.forLoop() != null) {
            return visit(ctx.forLoop());
        }
        return null;
    }

    @Override
    public String visitIfElse(compiladoresParser.IfElseContext ctx) {
        String condicion = visit(ctx.expresion());
        String etiquetaThen = nuevaEtiqueta();
        String etiquetaEnd = nuevaEtiqueta();
        String etiquetaElse = ctx.ELSE() != null ? nuevaEtiqueta() : etiquetaEnd;

        appendInstruccion("if " + condicion + " goto " + etiquetaThen);
        appendInstruccion("goto " + etiquetaElse);
        appendInstruccion(etiquetaThen + ":");
        visit(ctx.bloque(0));
        if (ctx.ELSE() != null) {
            appendInstruccion("goto " + etiquetaEnd);
            appendInstruccion(etiquetaElse + ":");
            visit(ctx.bloque(1));
        }
        appendInstruccion(etiquetaEnd + ":");
        return null;
    }

    @Override
    public String visitWhileLoop(compiladoresParser.WhileLoopContext ctx) {
        String etiquetaStart = nuevaEtiqueta();
        String etiquetaBody = nuevaEtiqueta();
        String etiquetaEnd = nuevaEtiqueta();

        appendInstruccion(etiquetaStart + ":");
        String condicion = visit(ctx.expresion());
        appendInstruccion("if " + condicion + " goto " + etiquetaBody);
        appendInstruccion("goto " + etiquetaEnd);
        appendInstruccion(etiquetaBody + ":");
        visit(ctx.bloque());
        appendInstruccion("goto " + etiquetaStart);
        appendInstruccion(etiquetaEnd + ":");
        return null;
    }

    @Override
    public String visitForLoop(compiladoresParser.ForLoopContext ctx) {
        if (ctx.inicializacion() != null) {
            visit(ctx.inicializacion());
        }

        String etiquetaStart = nuevaEtiqueta();
        appendInstruccion(etiquetaStart + ":");

        String condicion;
        if (ctx.condicion() != null) {
            condicion = visit(ctx.condicion());
        } else {
            condicion = "1";
        }

        String etiquetaBody = nuevaEtiqueta();
        String etiquetaEnd = nuevaEtiqueta();

        appendInstruccion("if " + condicion + " goto " + etiquetaBody);
        appendInstruccion("goto " + etiquetaEnd);
        appendInstruccion(etiquetaBody + ":");
        visit(ctx.bloque());

        if (ctx.actualizacion() != null) {
            visit(ctx.actualizacion());
        }

        appendInstruccion("goto " + etiquetaStart);
        appendInstruccion(etiquetaEnd + ":");
        return null;
    }

    @Override
    public String visitInicializacion(compiladoresParser.InicializacionContext ctx) {
        // comprobar si es una declaracion o una asignacion
        if (ctx.declaracion() != null) {
            // es una declaracion
            return visit(ctx.declaracion());
        } else if (ctx.asignacion() != null) {
            // es una asignacion
            return visit(ctx.asignacion());
        } else {
            // caso inesperado
            return null;
        }
    }

    @Override
    public String visitActualizacion(compiladoresParser.ActualizacionContext ctx) {
        for (compiladoresParser.AsignacionContext asign : ctx.asignacion()) {
            visit(asign);
        }
        for (compiladoresParser.IncrementoDecrementoContext incdec : ctx.incrementoDecremento()) {
            String expr = visit(incdec);
            appendInstruccion(expr);
        }
        return null;
    }

    @Override
    public String visitIncrementoDecremento(compiladoresParser.IncrementoDecrementoContext ctx) {
        String id = ctx.ID().getText();
        String op = ctx.INCREMENTO() != null ? "+" : "-";
        String temporal = nuevaTemporal();
        appendInstruccion(temporal + " = " + id);
        String temporal2 = nuevaTemporal();
        appendInstruccion(temporal2 + " = " + temporal + " " + op + " 1");
        appendInstruccion(id + " = " + temporal2);
        return temporal;
    }

    @Override
    public String visitBloque(compiladoresParser.BloqueContext ctx) {
        StringBuilder bloqueCodigo = new StringBuilder();

        // comprobar si hay instrucciones dentro del bloque
        if (ctx.instrucciones() != null) {
            for (compiladoresParser.InstruccionContext instruccion : ctx.instrucciones().instruccion()) {
                bloqueCodigo.append(visit(instruccion)).append("\n"); // visitar cada instrucción
            }
        }

        return bloqueCodigo.toString(); // devolver el codigo generado para el bloque
    }

    @Override
    public String visitCondicion(compiladoresParser.CondicionContext ctx) {
        if (ctx.expresion() != null) {
            // si existe una expresion en la condicion, la procesamos
            return visit(ctx.expresion()); // visitar la expresión de la condición
        } else {
            // si no hay expresión, retornamos un valor predeterminado (por ejemplo, "1")
            return "1"; // condicion siempre verdadera
        }
    }

    @Override
    public String visitDeclaracionFuncion(compiladoresParser.DeclaracionFuncionContext ctx) {
        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();
        appendInstruccion("func_" + id + ":");
        if (ctx.parametros() != null) {
            for (compiladoresParser.ParametroContext param : ctx.parametros().parametro()) {
                appendInstruccion("PARAM " + param.ID().getText() + " " + param.tipo().getText());
            }
        }
        if (ctx.bloque() != null) {
            currentFunction = id;
            visit(ctx.bloque());
            currentFunction = null;
        }
        return null;
    }

    private TipoDato obtenerTipoDato(String tipo) {
        switch (tipo) {
            case "int":
                return TipoDato.INT;
            case "double":
                return TipoDato.DOUBLE;
            case "bool":
                return TipoDato.BOOL;
            case "void":
                return TipoDato.VOID;
            default:
                throw new RuntimeException("Tipo no soportado: " + tipo);
        }
    }

    @Override
    public String visitLlamadaFuncion(compiladoresParser.LlamadaFuncionContext ctx) {
        String nombreFuncion = ctx.ID().getText();
        List<String> argumentos = new ArrayList<>();
        for (compiladoresParser.ExpresionContext exp : ctx.expresion()) {
            argumentos.add(visit(exp));
        }
        appendInstruccion("CALL func_" + nombreFuncion + ", " + String.join(", ", argumentos));
        return "RETURN_VALUE";
    }

    @Override
    public String visitCuerpoFuncion(compiladoresParser.CuerpoFuncionContext ctx) {
        String id = ctx.ID().getText();
        currentFunction = id;
        appendInstruccion("func_" + id + ":");
        if (ctx.parametros() != null) {
            for (compiladoresParser.ParametroContext param : ctx.parametros().parametro()) {
                appendInstruccion("PARAM " + param.ID().getText() + " " + param.tipo().getText());
            }
        }
        if (ctx.bloque() != null) {
            visit(ctx.bloque());
        }
        currentFunction = null;
        return null;
    }

    @Override
    public String visitReturn(compiladoresParser.ReturnContext ctx) {
        if (ctx.expresion() != null) {
            String resultado = visit(ctx.expresion());
            appendInstruccion("return " + resultado);
        } else {
            appendInstruccion("return");
        }
        return null;
    }

    private void appendInstruccion(String instruccion) {
        codigoTresDirecciones.append(instruccionCounter++).append(": ").append(instruccion).append("\n");
    }

}
