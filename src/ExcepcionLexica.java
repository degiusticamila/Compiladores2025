import java.util.ArrayList;

public class ExcepcionLexica extends Exception{
    private String lexema;
    private int numLine;
    private StringBuilder detalle;
    private String descrip;
    public ExcepcionLexica(String lexema, int numLine, String descrip){
        super("[Error:"+lexema+"|"+numLine+"]");
        this.lexema = lexema;
        this.numLine = numLine;
        this.detalle = new StringBuilder();
        this.descrip = descrip;
    }
    public String getLexema(){
        return lexema;
    }
    public int getNumLine(){
        return numLine;
    }
    public String mostrarDetalle(String currentLine, String lexema, int lineIndexNumber){

        detalle.setLength(0);

        final String prefijo = "Detalle: ";
        detalle.append(prefijo);
        detalle.append(currentLine);
        detalle.append('\n');

        int ancho = Math.min(lineIndexNumber - 1,currentLine.length()-1);
        //Busca la linea exacta donde esta el lexema desde ancho para atras.
        int startIndex = currentLine.lastIndexOf(lexema, ancho);
        for (int i = 0; i < prefijo.length() + startIndex; i++) {
            detalle.append(' ');
        }
        detalle.append('^');

        return detalle.toString();
    }
    public String getDescrip(){
        return descrip;
    }
}
