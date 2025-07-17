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
    // private int tempCounter = 0;
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
        super.visitPrograma(ctx);
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
        String nombre = ctx.ID().getText();
        String index = ctx.CORCHETE() != null ? "[" + visit(ctx.expresion(0)) + "]" : "";
        String expresion = visit(ctx.expresion(ctx.CORCHETE() != null ? 1 : 0));
        appendInstruccion(nombre + index + " = " + expresion);
        return nombre + index + " = " + expresion;
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
            codigoTresDirecciones.append(nuevaTemp).append(" = ").append(temp).append(" ").append(operador).append(" ")
                    .append(temp2).append(";\n");
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
            codigoTresDirecciones.append(nuevaTemp).append(" = ").append(temp).append(" ").append(operador).append(" ")
                    .append(temp2).append(";\n");
            temp = nuevaTemp;
        }

        return temp;
    }

    @Override
    public String visitExpresionAritmetica(compiladoresParser.ExpresionAritmeticaContext ctx) {
        String temp = visit(ctx.termino(0));

        for (int i = 1; i < ctx.termino().size(); i++) {
            String temp2 = visit(ctx.termino(i));
            String operador = ctx.getChild(2 * i - 1).getText(); // suma o resta
            String nuevaTemp = nuevaTemporal();
            codigoTresDirecciones.append(nuevaTemp).append(" = ").append(temp).append(" ").append(operador).append(" ")
                    .append(temp2).append(";\n");
            temp = nuevaTemp;
        }

        return temp;
    }

    @Override
    public String visitTermino(compiladoresParser.TerminoContext ctx) {
        String temp = visit(ctx.factor(0));

        for (int i = 1; i < ctx.factor().size(); i++) {
            String temp2 = visit(ctx.factor(i));
            String operador = ctx.getChild(2 * i - 1).getText(); // mult, div o modul
            String nuevaTemp = nuevaTemporal();
            codigoTresDirecciones.append(nuevaTemp).append(" = ").append(temp).append(" ").append(operador).append(" ")
                    .append(temp2).append(";\n");
            temp = nuevaTemp;
        }

        return temp;
    }

    @Override
    public String visitFactor(compiladoresParser.FactorContext ctx) {
        if (ctx.expresion() != null) {
            return visit(ctx.expresion());
        } else if (ctx.ID() != null) {
            String id = ctx.ID().getText();
            String index = ctx.CORCHETE() != null ? "[" + visit(ctx.expresion()) + "]" : "";
            return id + index;
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
            return (ctx.RESTA() != null ? "-" : "") + ctx.NUMERO().getText();
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
        String etiquetaInicio = nuevaEtiqueta();
        String etiquetaCondicion = nuevaEtiqueta();
        String etiquetaFin = nuevaEtiqueta();

        String condicion = visit(ctx.expresion());

        // generacion de codigo de en tres direcciones
        codigoTresDirecciones.append(etiquetaInicio).append(":\n");
        codigoTresDirecciones.append("if ").append(condicion).append(" goto ").append(etiquetaCondicion).append(";\n");
        codigoTresDirecciones.append("goto ").append(etiquetaFin).append(";\n");
        codigoTresDirecciones.append(etiquetaCondicion).append(":\n");
        visit(ctx.bloque());
        codigoTresDirecciones.append("goto ").append(etiquetaInicio).append(";\n");
        codigoTresDirecciones.append(etiquetaFin).append(":\n");

        return null;
    }

    @Override
    public String visitForLoop(compiladoresParser.ForLoopContext ctx) {
        String etiquetaInicio = nuevaEtiqueta();
        String etiquetaCondicion = nuevaEtiqueta();
        String etiquetaFin = nuevaEtiqueta();

        // inicializar comprobar si existe antes de visitar
        StringBuilder inicializacion = new StringBuilder();

        String resultadoInicializacion = visit(ctx.inicializacion());
        inicializacion.append(resultadoInicializacion).append("\n");

        // condicion (si no hay, se asume "1")
        String condicion = visit(ctx.condicion());

        // Actualización (comprobar si existe antes de visitar)
        StringBuilder actualizacion = new StringBuilder();
        actualizacion.append(visit(ctx.actualizacion())).append("\n");

        codigoTresDirecciones.append(etiquetaInicio).append(":\n");
        codigoTresDirecciones.append("if ").append(condicion).append(" goto ").append(etiquetaCondicion).append(";\n");
        codigoTresDirecciones.append("goto ").append(etiquetaFin).append(":\n");
        codigoTresDirecciones.append(etiquetaCondicion).append(":\n");
        visit(ctx.bloque());
        codigoTresDirecciones.append("goto ").append(etiquetaInicio).append(";\n");
        codigoTresDirecciones.append(etiquetaFin).append(":\n");

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
        StringBuilder actualizaciones = new StringBuilder();

        // iterar sobre los elementos separados por coma
        for (int i = 0; i < ctx.asignacion().size(); i++) {
            actualizaciones.append(visit(ctx.asignacion(i))); // visitar cada asignación
            if (i < ctx.asignacion().size() - 1) {
                actualizaciones.append(", "); // agregar coma si no es el último
            }
        }

        // procesamos los incrementos y decrementos
        for (int i = 0; i < ctx.incrementoDecremento().size(); i++) {
            actualizaciones.append(visit(ctx.incrementoDecremento(i))); // Visitar cada incremento/decremento
            if (i < ctx.incrementoDecremento().size() - 1) {
                actualizaciones.append(", "); // Agregar coma si no es el ultimo
            }
        }

        return actualizaciones.toString(); // devolver las actualizaciones generadas
    }

    @Override
    public String visitIncrementoDecremento(compiladoresParser.IncrementoDecrementoContext ctx) {
        String id = ctx.ID().getText(); // obtener el identificador
        String operador = ctx.INCREMENTO() != null ? "++" : "--"; // comprobar si es incremento o decremento
        return id + operador; // devolver el incremento o decremento como código
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

        TipoDato tipoDato = obtenerTipoDato(tipo);
        codigoTresDirecciones.append("function ").append(id).append(":\n");

        codigoTresDirecciones.append("end function;\n");
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
        String temporal = nuevaTemporal();
        appendInstruccion(temporal + " = RETURN_VALUE");
        return temporal;
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
