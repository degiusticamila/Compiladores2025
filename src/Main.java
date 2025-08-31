import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args){
        try {
            SourceManagerImpl sourceManager = new SourceManagerImpl();
            sourceManager.open(args[0]);
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(sourceManager);

            String test = "[SinErrores]";
            while (analizadorLexico.getCaracterActual() != sourceManager.END_OF_FILE) {
                analizadorLexico.setLexema("");
                try {
                    Token token = analizadorLexico.proximoToken();
                    System.out.println(token);

                } catch (ExcepcionLexica ex) {

                    System.out.println("Error lexico en Linea "+sourceManager.getLineNumber()+" | Columna "+sourceManager.getLineIndexNumber()+": "+analizadorLexico.getLexema()+" "+ex.getDescrip());
                    System.out.println(ex.mostrarDetalle(sourceManager.getCurrentLine(),analizadorLexico.getLexema(),sourceManager.getLineIndexNumber()));
                    System.out.println(ex.getMessage());
                    test = "";
                    analizadorLexico.actualizarCaracterActual();
                    analizadorLexico.setLexema("");
                }
            }
            sourceManager.close();
            if (!test.isEmpty()) System.out.println("[SinErrores]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}