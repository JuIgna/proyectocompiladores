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

    public Optimizador(String codigoOriginal) {
        this.codigoOriginal = codigoOriginal;
        this.codigoOptimizado = new StringBuilder();
        this.copias = new HashMap<>();
        this.variablesVivas = new HashSet<>();
        this.variablesPrograma = new HashSet<>();
    }

    public String optimizar() {
        String codigoActual = codigoOriginal;
        String codigoAnterior = "";
        while (!codigoActual.equals(codigoAnterior)) {
            codigoAnterior = codigoActual;
            String[] lineas = codigoActual.split("\n");
            variablesPrograma.clear();
            variablesVivas.clear();
            copias.clear();
            codigoOptimizado = new StringBuilder();
            for (String linea : lineas) {
                String lineaReal = linea.trim();
                if (lineaReal.matches("\\d+:\\s+.*")) {
                    lineaReal = lineaReal.substring(lineaReal.indexOf(":") + 1).trim();
                }
                if (lineaReal.isEmpty() || lineaReal.startsWith("//") || lineaReal.endsWith(":"))
                    continue;
                if (lineaReal.startsWith("DECLARE")) {
                    String var = lineaReal.split(" ")[1].trim();
                    if (var.contains("[")) {
                        var = var.substring(0, var.indexOf("["));
                    }
                    variablesPrograma.add(var);
                } else if (lineaReal.contains("=") && !lineaReal.startsWith("if") && !lineaReal.startsWith("goto")
                        && !lineaReal.startsWith("CALL") && !lineaReal.startsWith("return")) {
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
            variablesVivas.addAll(variablesPrograma);
            int i = 0;
            while (i < lineas.length) {
                String linea = lineas[i].trim();
                if (linea.isEmpty()) {
                    i++;
                    continue;
                }
                String lineaReal = linea;
                if (lineaReal.matches("\\d+:\\s+.*")) {
                    lineaReal = lineaReal.substring(lineaReal.indexOf(":") + 1).trim();
                }
                if (lineaReal.startsWith("//") || lineaReal.endsWith(":")) {
                    codigoOptimizado.append(linea).append("\n");
                    i++;
                    continue;
                }
                if (lineaReal.startsWith("DECLARE")) {
                    String var = lineaReal.split(" ")[1].trim();
                    String varBase = var.contains("[") ? var.substring(0, var.indexOf("[")) : var;
                    if (!variablesVivas.contains(varBase) && !variablesVivas.contains(var)) {
                        System.out.println("🔍 Eliminando DECLARE dead: " + linea);
                        i++;
                        continue; // DCE solo para DECLARE no usada
                    }
                    codigoOptimizado.append(linea).append("\n");
                    i++;
                    continue;
                }
                if (lineaReal.contains("=") && !lineaReal.startsWith("if") && !lineaReal.startsWith("goto")
                        && !lineaReal.startsWith("CALL") && !lineaReal.startsWith("return")) {
                    String izquierda = lineaReal.split("=")[0].trim();
                    String derecha = lineaReal.split("=")[1].trim().replace(";", "");
                    String fusedDerecha = derecha;
                    String fusedIzquierda = izquierda;
                    int skipCount = 0;
                    boolean fused = false;
                    while (i + 2 < lineas.length) {
                        String sigLinea = lineas[i + 1].trim();
                        String sigLineaReal = sigLinea.matches("\\d+:\\s+.*")
                                ? sigLinea.substring(sigLinea.indexOf(":") + 1).trim()
                                : sigLinea;
                        String sigLinea2 = lineas[i + 2].trim();
                        String sigLinea2Real = sigLinea2.matches("\\d+:\\s+.*")
                                ? sigLinea2.substring(sigLinea2.indexOf(":") + 1).trim()
                                : sigLinea2;
                        if (sigLineaReal.contains("=") && sigLinea2Real.contains("=")) {
                            String izqSig = sigLineaReal.split("=")[0].trim();
                            String derSig = sigLineaReal.split("=")[1].trim().replace(";", "");
                            String izqSig2 = sigLinea2Real.split("=")[0].trim();
                            String derSig2 = sigLinea2Real.split("=")[1].trim().replace(";", "");
                            System.out.println("Debug derSig: " + derSig);
                            int opIndex = derSig.indexOf(izquierda);
                            if (opIndex != -1 && izqSig.startsWith("t") && derSig.contains(izquierda)
                                    && izqSig2.equals(izquierda) && derSig2.equals(izqSig)) {
                                String operadorPart = derSig.substring(opIndex + izquierda.length()).trim();
                                String[] opParts = operadorPart.split(" ", 2);
                                if (opParts.length == 2) {
                                    String operador = opParts[0];
                                    String operando = opParts[1];
                                    fusedDerecha = "(" + fusedDerecha + ") " + operador + " " + operando;
                                    skipCount += 2;
                                    fused = true;
                                    System.out.println("🔍 Fusando chain: " + fusedIzquierda + " = " + fusedDerecha);
                                    i += 2;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                    if (fused) {
                        String nuevaLineaReal = fusedIzquierda + " = " + fusedDerecha;
                        String prefijo = lineas[i - skipCount].substring(0, lineas[i - skipCount].indexOf(":") + 1);
                        codigoOptimizado.append(prefijo + " " + nuevaLineaReal).append("\n");
                        i++; // Avanzar después de fuse
                        continue;
                    }
                    String lineaOptimizadaReal = optimizarAsignacion(lineaReal, copias, variablesVivas);
                    if (lineaOptimizadaReal != null) {
                        String prefijo = linea.substring(0, linea.indexOf(":") + 1);
                        codigoOptimizado.append(prefijo + " " + lineaOptimizadaReal).append("\n");
                    } else {
                        System.out.println("🔍 Eliminando asignación: " + linea);
                    }
                    i++;
                } else {
                    String lineaOptimizadaReal = aplicarPropagacionCopias(lineaReal, copias);
                    String prefijo = linea.substring(0, linea.indexOf(":") + 1);
                    codigoOptimizado.append(prefijo + " " + lineaOptimizadaReal).append("\n");
                    i++;
                }
            }
            codigoActual = codigoOptimizado.toString();
        }
        // Agregar estadísticas
        int instruccionesOriginales = contarInstrucciones(codigoOriginal);
        int instruccionesOptimizadas = contarInstrucciones(codigoActual);
        System.out.println("\n=== 6. OPTIMIZACIÓN DE CÓDIGO ===");
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
        System.out.println(formatTAC(codigoActual)); // Usa formatTAC para numerar

        return codigoActual;
    }

    private String optimizarAsignacion(String linea, Map<String, String> copias, Set<String> variablesVivas) {
        String[] partes = linea.split("=");
        if (partes.length != 2)
            return linea;

        String izquierda = partes[0].trim();
        String derecha = partes[1].trim().replace(";", "");

        String izquierdaBase = izquierda.contains("[") ? izquierda.substring(0, izquierda.indexOf("[")) : izquierda;

        derecha = aplicarPropagacionCopias(derecha, copias);

        if (derecha.matches("[a-zA-Z0-9_\\[\\]]+")) {
            copias.put(izquierda, derecha);
        }

        // Optimizar constantes
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
                    default -> throw new IllegalArgumentException("Operador no soportado: " + operador);
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

    private String aplicarPropagacionCopias(String linea, Map<String, String> copias) {
        String resultado = linea;
        for (Map.Entry<String, String> copia : copias.entrySet()) {
            resultado = resultado.replaceAll("\\b" + copia.getKey() + "\\b", copia.getValue());
        }
        return resultado;
    }

}