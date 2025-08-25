import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ExcepcionLexica {
        SourceManagerImpl sourceManager = new SourceManagerImpl();
        String path = "C:\\Users\\Cami\\Desktop\\Compi\\Etapa1\\primerPrueba.txt";
        sourceManager.open(path);

        AnalizadorLexico analizadorLexico = new AnalizadorLexico(sourceManager);


        while(analizadorLexico.getCaracterActual() != sourceManager.END_OF_FILE){

            analizadorLexico.setLexema(""); // Reset
            Token token = analizadorLexico.proximoToken();
            System.out.println("Token: "+token.getId()+" Lexema: "+token.getLexema()+" Linea: "+token.getNroLinea());

        }
        sourceManager.close();
    }
}