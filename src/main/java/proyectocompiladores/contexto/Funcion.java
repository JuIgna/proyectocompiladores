package proyectocompiladores.contexto;

import java.util.List;

public class Funcion extends Identificador{

    List<TipoDato> argumentos;

    public Funcion(String nombre, TipoDato tipoDato, List<TipoDato> argumentos) {
        super(nombre, tipoDato);  // Llamamos al constructor de Identificador
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
