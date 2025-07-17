package proyectocompiladores.contexto;

public class Variable extends Identificador {
    private Integer arraySize;

    public Variable(String nombre, TipoDato tipoDato, int linea, int columna, String ambito, Integer arraySize) {
        super(nombre, tipoDato, linea, columna, "variable", ambito, "[private]" + (arraySize != null ? " [arr:" + arraySize + "]" : ""));
        this.arraySize = arraySize;
    }

    public Integer getArraySize () {
        return arraySize;
    }

}
