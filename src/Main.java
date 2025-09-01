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
            Token token;
            do{
                analizadorLexico.setLexema("");
                try{
                    token = analizadorLexico.proximoToken();
                    System.out.println(token);
                }catch(ExcepcionLexica ex){
                    System.out.println("Error lexico en Linea "
                            + sourceManager.getLineNumber()+" | Columna "
                            + sourceManager.getLineIndexNumber()
                            + ": "+analizadorLexico.getLexema()+" "
                            + ex.getDescrip());
                    System.out.println(ex.mostrarDetalle(
                            sourceManager.getCurrentLine(),
                            analizadorLexico.getLexema(),
                            sourceManager.getLineIndexNumber()));
                    System.out.println(ex.getMessage());
                    token = null;
                    test = "";
                    analizadorLexico.actualizarCaracterActual();
                    analizadorLexico.setLexema("");
                }
            } while(token == null || !token.getId().equals("EOF"));
            sourceManager.close();
            if (!test.isEmpty()) System.out.println("[SinErrores]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}