public class Token {
    private String id;
    private String Lexema;
    private int nroLinea;

    public Token(String id, String Lexema, int nroLinea) {
        this.id = id;
        this.Lexema = Lexema;
        this.nroLinea = nroLinea;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getLexema() {
        return Lexema;
    }
    public void setLexema(String Lexema) {
        this.Lexema = Lexema;
    }
    public int getNroLinea() {
        return nroLinea;
    }
    public void setNroLinea(int nroLinea) {
        this.nroLinea = nroLinea;
    }

}
