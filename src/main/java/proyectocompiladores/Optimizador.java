package proyectocompiladores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Optimizador {
    private final String codigoOriginal;
    private final StringBuilder codigoOptimizado;
    private final Map<String, Integer> constantes;
    private final Set<String> variablesUtilizadas;

    public Optimizador(String codigoOriginal) {
        this.codigoOriginal = codigoOriginal;
        this.codigoOptimizado = new StringBuilder();
        this.constantes = new HashMap<>();
        this.variablesUtilizadas = new HashSet<>();
    }

    public String optimizar() {
        String[] lineas = codigoOriginal.split("\n");
        boolean enFuncion = false;
        
        for (String linea : lineas) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;
            
            // Detectar inicio de función
            if (linea.startsWith("func_") && linea.endsWith(":")) {
                enFuncion = true;
                codigoOptimizado.append(linea).append("\n");
                continue;
            }
            
            // Detectar fin de programa
            if (linea.equals("PROGRAMA_FIN:")) {
                codigoOptimizado.append(linea).append("\n");
                break;
            }
            
            // Optimizar asignaciones
            if (linea.contains("=") && !linea.startsWith("if") && !linea.startsWith("goto") && !linea.startsWith("CALL")) {
                String lineaOptimizada = optimizarAsignacion(linea);
                codigoOptimizado.append(lineaOptimizada).append("\n");
            } else {
                codigoOptimizado.append(linea).append("\n");
            }
        }
        
        return codigoOptimizado.toString();
    }

    private String optimizarAsignacion(String linea) {
        String[] partes = linea.split("=");
        if (partes.length != 2) return linea;
        
        String izquierda = partes[0].trim();
        String derecha = partes[1].trim().replace(";", "");
        
        // Optimizar expresiones constantes
        if (derecha.matches("\\d+\\s*[+\\-*/]\\s*\\d+")) {
            try {
                String[] tokens = derecha.split("\\s+");
                int a = Integer.parseInt(tokens[0]);
                int b = Integer.parseInt(tokens[2]);
                String operador = tokens[1];
                
                int resultado = switch (operador) {
                    case "+" -> a + b;
                    case "-" -> a - b;
                    case "*" -> a * b;
                    case "/" -> b != 0 ? a / b : a;
                    default -> throw new IllegalArgumentException("Operador no soportado: " + operador);
                };
                
                return izquierda + " = " + resultado;
            } catch (Exception e) {
                return linea;
            }
        }
        
        return linea;
    }
}
