package proyectocompiladores.contexto;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Contexto {
    public String nombre; // global o nombre de la función
    Map<String, Identificador> identificadores;

    public Contexto() {
        this.nombre = "global";
        this.identificadores = new LinkedHashMap<>();
    }

    public Contexto(String nombre) {
        this.nombre = nombre;
        this.identificadores = new LinkedHashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void addIdentificador(Identificador identificador) {
        identificadores.put(identificador.getNombre(), identificador);
    }    

    public Identificador buscarIdentificador(Identificador id) {
        for (Identificador identificador : identificadores.values()) {
            if (identificador.getNombre().equals(id.getNombre()) &&
                (id.getTipoDato() == null || identificador.getTipoDato() == id.getTipoDato())) {
                return identificador;
            }
        }
        return null;
    }

    public Identificador buscarIdentificadorPorNombre(String nombre){
        return identificadores.get(nombre);
    }

    public Map<String, Identificador> getIdentificadores() {
        return identificadores;
    }
}

