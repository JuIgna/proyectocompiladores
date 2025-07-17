package proyectocompiladores.contexto;

import java.util.List;
import java.util.stream.Collectors;

public class Funcion extends Identificador {

    List<TipoDato> argumentos;

    public Funcion(String nombre, TipoDato tipoDato, int linea, int columna, String ambito, List<TipoDato> argumentos) {
        super(nombre, tipoDato, linea, columna, "funcion", ambito, "[private]" + (argumentos.isEmpty() ? ""
                : " [" + argumentos.stream().map(t -> t.toString().toLowerCase()).collect(Collectors.joining(", "))
                        + "]"));
        this.argumentos = argumentos;
    }

    public List<TipoDato> getArgumentos() {
        return argumentos;
    }

    @Override
    public String toString() {
        return super.toString() + ", Argumentos: " + argumentos;
    }

}
