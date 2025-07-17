package proyectocompiladores.contexto;

public class Identificador {
    String nombre;
    TipoDato tipoDato;
    boolean inicializada;
    boolean utilizada;
    int linea;
    int columna;
    String categoria; // "variable"
    String ambito; // "global", "main", "sumar", etc.
    String detalles; // "[private]", "[arr:3]", "[int, int]", etc

    public Identificador(String nombre, TipoDato tipoDato, int linea, int columna, String categoria, String ambito, String detalles) {
        this.nombre = nombre;
        this.tipoDato = tipoDato;
        this.inicializada = false;
        this.utilizada = false;
        this.linea = linea;
        this.columna = columna;
        this.categoria = categoria;
        this.ambito = ambito;
        this.detalles = detalles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDato getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(TipoDato tipoDato) {
        this.tipoDato = tipoDato;
    }

    public boolean isInicializada() {
        return inicializada;
    }

    public void setInicializada(boolean inicializada) {
        this.inicializada = inicializada;
    }

    public boolean isUtilizada() {
        return utilizada;
    }

    public void setUtilizada(boolean utilizada) {
        this.utilizada = utilizada;
    }

    public int getLinea (){
        return linea;
    }
    public int getColumna (){
        return columna;
    }
    public String getCategoria() {
        return categoria;
    }
    public void setCategoria (String categoria){
        this.categoria = categoria;
    }
    
    public String getAmbito (){
        return ambito;
    }
    public String getDetalles (){
        return detalles;
    }

    
}
