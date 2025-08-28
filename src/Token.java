public class Token {
    private String id;
    private String lexema;
    private int nroLinea;

    public Token(String id, String lexema, int nroLinea) {
        this.id = id;
        this.lexema = lexema;
        this.nroLinea = nroLinea;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLexema() {
        return lexema;
    }
    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
    public int getNroLinea() {
        return nroLinea;
    }
    public void setNroLinea(int nroLinea) {
        this.nroLinea = nroLinea;
    }
    public String toString() {
        return ("("+id+", "+lexema+", "+nroLinea+")");
    }
}
