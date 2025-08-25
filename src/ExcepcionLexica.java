public class ExcepcionLexica extends Exception{
    private String lexema;
    private int numLine;

    public ExcepcionLexica(String lexema, int numLine){
        super("Error lexico: simbolo inesperado: "+lexema+"en linea "+numLine);
        this.lexema = lexema;
        this.numLine = numLine;
    }
    public String getLexema(){
        return lexema;
    }
    public int getNumLine(){
        return numLine;
    }
}
