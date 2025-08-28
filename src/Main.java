import java.io.IOException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args){
        try {
            SourceManagerImpl sourceManager = new SourceManagerImpl();
            //String path = "C:\\Users\\Cami\\Desktop\\Compi\\Etapa1\\primerPrueba.txt";
            //sourceManager.open(path);

            sourceManager.open(args[0]);
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(sourceManager);

            boolean hayErrores = false;

            while (analizadorLexico.getCaracterActual() != sourceManager.END_OF_FILE) {

                analizadorLexico.setLexema(""); // Reset
                try {
                    Token token = analizadorLexico.proximoToken();
                    System.out.println(token);

                } catch (ExcepcionLexica ex) {
                    // Caso: se detecta un error lexico
                    System.out.println(ex.getMessage());
                    hayErrores = true;
                    break;
                }

            }
            sourceManager.close();
            if (!hayErrores) System.out.println("[SinErrores]");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}