package proyectocompiladores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static proyectocompiladores.App.formatTAC;
import static proyectocompiladores.App.contarInstrucciones;

public class Optimizador {
    private final String codigoOriginal;
    private StringBuilder codigoOptimizado;
    private final Map<String, String> copias;
    private final Set<String> variablesVivas;
    private final Set<String> variablesPrograma;
    private final Map<String, String> ultimasAsignaciones;
    private String ultimaVariable = null;

    public Optimizador(String codigoOriginal) {
        this.codigoOriginal = codigoOriginal;
        this.codigoOptimizado = new StringBuilder();
        this.copias = new HashMap<>();
        this.variablesVivas = new HashSet<>();
        this.variablesPrograma = new HashSet<>();
        this.ultimasAsignaciones = new HashMap<>();
    }

    public String optimizar() {
        String codigoActual = codigoOriginal;
        String codigoAnterior = "";
        int iteraciones = 0;
        int maxIteraciones = 1;

        while (!codigoActual.equals(codigoAnterior) && iteraciones < maxIteraciones) {
            iteraciones++;
            codigoAnterior = codigoActual;
            String[] lineas = codigoActual.split("\n");

            variablesPrograma.clear();
            variablesVivas.clear();
            copias.clear();
            ultimaVariable = null;
            ultimasAsignaciones.clear();
            codigoOptimizado = new StringBuilder();

            analizarVariablesVivas(lineas);

            int i = 0;
            while (i < lineas.length) {
                String linea = lineas[i].trim();
                if (linea.isEmpty()) {
                    i++;
                    continue;
                }

                String lineaReal = extraerLineaReal(linea);

                if (lineaReal.isEmpty() || lineaReal.startsWith("//") || lineaReal.endsWith(":")) {
                    codigoOptimizado.append(linea).append("\n");
                    i++;
                    continue;
                }

                if (lineaReal.startsWith("DECLARE")) {
                    if (procesarDeclare(linea, lineaReal)) {
                        i++;
                        continue;
                    }
                    codigoOptimizado.append(linea).append("\n");
                    i++;
                    continue;
                }

                if (lineaReal.startsWith("CALL")) {
                    ultimaVariable = null;
                    String lineaOptimizada = aplicarPropagacionCopias(lineaReal, copias);
                    String prefijo = linea.substring(0, linea.indexOf(":") + 1);
                    codigoOptimizado.append(prefijo + " " + lineaOptimizada).append("\n");
                    i++;
                    continue;
                }

                if (lineaReal.startsWith("return")) {
                    String lineaOptimizada = procesarReturn(lineaReal);
                    String prefijo = linea.substring(0, linea.indexOf(":") + 1);
                    codigoOptimizado.append(prefijo + " " + lineaOptimizada).append("\n");
                    i++;
                    continue;
                }

                if (lineaReal.contains("=") && !lineaReal.startsWith("if") && !lineaReal.startsWith("goto")) {
                    int skipCount = procesarAsignacion(lineas, i);
                    i += skipCount;
                    continue;
                }

                String lineaOptimizada = aplicarPropagacionCopias(lineaReal, copias);
                String prefijo = linea.substring(0, linea.indexOf(":") + 1);
                codigoOptimizado.append(prefijo + " " + lineaOptimizada).append("\n");
                i++;
            }

            codigoActual = codigoOptimizado.toString();
        }

        int instruccionesOriginales = contarInstrucciones(codigoOriginal);
        int instruccionesOptimizadas = contarInstrucciones(codigoActual);

        // System.out.println("\n=== 6. OPTIMIZACIÓN DE CÓDIGO ===");
        System.out.println("   🔧 Aplicando optimizaciones al código intermedio...");
        System.out.println("✅ Optimización completada:");
        System.out.println("   📊 Instrucciones originales: " + instruccionesOriginales);
        System.out.println("   📊 Instrucciones optimizadas: " + instruccionesOptimizadas);
        System.out.println("   📊 Instrucciones eliminadas: " + (instruccionesOriginales - instruccionesOptimizadas));
        System.out.printf("   📊 Reducción de código: %.2f%%\n",
                (instruccionesOriginales > 0
                        ? (instruccionesOriginales - instruccionesOptimizadas) * 100.0 / instruccionesOriginales
                        : 0));
        System.out.println("   📝 Código optimizado:");
        System.out.println(formatTAC(codigoActual));

        return codigoActual;
    }

    public void analizarVariablesVivas(String[] lineas) {
        String funcionActual = null;
        Map<String, String> returnVarPorFuncion = new HashMap<>();

        for (int i = 0; i < lineas.length; i++) {
            String lineaReal = extraerLineaReal(lineas[i].trim());
            if (lineaReal.isEmpty() || lineaReal.startsWith("//"))
                continue;

            if (lineaReal.startsWith("func_")) {
                funcionActual = lineaReal.replace(":", "").trim();
            } else if (lineaReal.startsWith("return") && funcionActual != null) {
                String[] partes = lineaReal.split(" ");
                if (partes.length > 1) {
                    String varReturn = partes[1].trim();
                    returnVarPorFuncion.put(funcionActual, varReturn);
                }
            }
        }

        for (String linea : lineas) {
            String lineaReal = extraerLineaReal(linea.trim());
            if (lineaReal.isEmpty() || lineaReal.startsWith("//") || lineaReal.endsWith(":"))
                continue;

            if (lineaReal.startsWith("DECLARE")) {
                String var = lineaReal.split(" ")[1].trim();
                if (var.contains("[")) {
                    var = var.substring(0, var.indexOf("["));
                }
                variablesPrograma.add(var);
            } else if (lineaReal.contains("=") && !lineaReal.startsWith("if") &&
                    !lineaReal.startsWith("goto") && !lineaReal.startsWith("CALL") &&
                    !lineaReal.startsWith("return")) {
                String derecha = lineaReal.split("=")[1].trim().replace(";", "");
                for (String var : derecha.split("[+\\-*/% ]+")) {
                    String trimmedVar = var.trim();
                    if (!trimmedVar.isEmpty()) {
                        variablesVivas.add(trimmedVar);
                    }
                }
            } else if (lineaReal.startsWith("if")) {
                String condicion = lineaReal.split(" ")[1];
                variablesVivas.add(condicion);
            } else if (lineaReal.startsWith("CALL")) {
                String[] partes = lineaReal.split(",");
                for (int j = 1; j < partes.length; j++) {
                    variablesVivas.add(partes[j].trim());
                }
            } else if (lineaReal.startsWith("return")) {
                if (lineaReal.split(" ").length > 1) {
                    variablesVivas.add(lineaReal.split(" ")[1]);
                }
            }
        }

        variablesVivas.addAll(returnVarPorFuncion.values());
        variablesVivas.addAll(variablesPrograma);
    }

    public boolean procesarDeclare(String linea, String lineaReal) {
        String var = lineaReal.split(" ")[1].trim();
        String varBase = var.contains("[") ? var.substring(0, var.indexOf("[")) : var;
        if (!variablesVivas.contains(varBase) && !variablesVivas.contains(var)) {
            // System.out.println("🔍 Eliminando DECLARE dead: " + linea);
            return true;
        }
        return false;
    }

    public int procesarAsignacion(String[] lineas, int i) {
        String linea = lineas[i].trim();
        String lineaReal = extraerLineaReal(linea);

        String[] partes = lineaReal.split("=");
        if (partes.length != 2) {
            String lineaOpt = aplicarPropagacionCopias(lineaReal, copias);
            String prefijo = linea.substring(0, linea.indexOf(":") + 1);
            codigoOptimizado.append(prefijo + " " + lineaOpt).append("\n");
            return 1;
        }

        String izquierda = partes[0].trim();
        String derecha = partes[1].trim().replace(";", "");

        if (derecha.equals("RETURN_VALUE")) {
            ultimaVariable = izquierda;
            copias.put("RETURN_VALUE", izquierda);
        }

        int skipCount = intentarFusionCadena(lineas, i, izquierda, derecha);
        if (skipCount > 1) {
            return skipCount;
        }

        String lineaOptimizada = optimizarAsignacion(lineaReal, copias, variablesVivas);
        if (lineaOptimizada != null) {
            String prefijo = linea.substring(0, linea.indexOf(":") + 1);
            codigoOptimizado.append(prefijo + " " + lineaOptimizada).append("\n");

            if (variablesPrograma.contains(izquierda)) {
                ultimasAsignaciones.put(izquierda, derecha);
            }
        } else {
            // System.out.println("🔍 Eliminando asignación: " + linea);
        }

        return 1;
    }

    public int intentarFusionCadena(String[] lineas, int i, String izquierda, String derecha) {
        if (!izquierda.startsWith("t")) {
            return 1; // solo aplicar a temporales
        }

        if (i + 1 < lineas.length) {
            String sigLinea = extraerLineaReal(lineas[i + 1].trim());
            if (sigLinea.contains("=")) {
                String[] partesSig = sigLinea.split("=");
                String izqSig = partesSig[0].trim();
                String derSig = partesSig[1].trim();

                if (derSig.equals(izquierda)) {
                    // fusionar: en vez de t1 = ..., x = t1 → x = ...
                    String nuevaLinea = izqSig + " = " + derecha;
                    String prefijo = lineas[i].substring(0, lineas[i].indexOf(":") + 1);
                    codigoOptimizado.append(prefijo + " " + nuevaLinea).append("\n");
                    return 2; // nos saltamos dos líneas
                }
            }
        }
        return 1; // no fusiona
    }

    public String optimizarAsignacion(String linea, Map<String, String> copias, Set<String> variablesVivas) {
        String[] partes = linea.split("=");
        if (partes.length != 2)
            return linea;

        String izquierda = partes[0].trim();
        String derecha = partes[1].trim().replace(";", "");
        String izquierdaBase = izquierda.contains("[") ? izquierda.substring(0, izquierda.indexOf("[")) : izquierda;

        if (!derecha.equals("RETURN_VALUE")) {
            derecha = aplicarPropagacionCopias(derecha, copias);
        }

        if (derecha.matches("[a-zA-Z0-9_\\[\\]]+") && !derecha.equals("RETURN_VALUE")) {
            copias.put(izquierda, derecha);
        }

        if (derecha.matches("\\d+\\s*[+\\-*/% ]+\\d+")) {
            try {
                String[] tokens = derecha.split("\\s+");
                int a = Integer.parseInt(tokens[0]);
                String operador = tokens[1];
                int b = Integer.parseInt(tokens[2]);
                int resultado = switch (operador) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> b != 0 ? a / b : a;
                    case "%" -> a % b;
                    default -> throw new IllegalArgumentException("Operador no soportado");
                };
                copias.put(izquierda, String.valueOf(resultado));
                return izquierda + " = " + resultado;
            } catch (Exception e) {
            }
        }

        if (derecha.equals(izquierda)) {
            return null;
        }

        if (!variablesVivas.contains(izquierda) && !variablesPrograma.contains(izquierdaBase)
                && !izquierda.startsWith("t")) {
            return null;
        }

        return izquierda + " = " + derecha;
    }

    public String aplicarPropagacionCopias(String linea, Map<String, String> copias) {
        String resultado = linea;

        Map<String, String> copiasFiltradas = new HashMap<>(copias);
        if (ultimaVariable != null) {
            copiasFiltradas.remove("RETURN_VALUE");
        }

        for (Map.Entry<String, String> copia : copiasFiltradas.entrySet()) {
            resultado = resultado.replaceAll("\\b" + copia.getKey() + "\\b", copia.getValue());
        }
        return resultado;
    }

    public String procesarReturn(String lineaReal) {
        if (!lineaReal.contains(" ")) {
            return lineaReal;
        }

        String[] partes = lineaReal.split(" ", 2);
        String varReturn = partes[1].trim();

        if (varReturn.matches("t\\d+")) {
            for (Map.Entry<String, String> entry : ultimasAsignaciones.entrySet()) {
                if (entry.getValue().equals(varReturn)) {
                    return "return " + entry.getKey();
                }
            }

            for (Map.Entry<String, String> entry : copias.entrySet()) {
                if (entry.getValue().equals(varReturn) && variablesPrograma.contains(entry.getKey())) {
                    return "return " + entry.getKey();
                }
            }
        }

        String resultado = aplicarPropagacionCopias(varReturn, copias);
        return "return " + resultado;
    }

    public String extraerLineaReal(String linea) {
        if (linea.matches("\\d+:\\s+.*")) {
            return linea.substring(linea.indexOf(":") + 1).trim();
        }
        return linea;
    }
}