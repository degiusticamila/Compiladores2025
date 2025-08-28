import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class AnalizadorLexico {
    private char caracterActual;
    private String lexema;
    SourceManager sourceManager;
    private String[] palabrasClave;
    public AnalizadorLexico(SourceManager sourceManager) throws IOException {

        this.sourceManager = sourceManager;
        actualizarCaracterActual();
        inicializarPalabrasClave();
    }
    public Token proximoToken() throws ExcepcionLexica, IOException {
        return estadoInicial();
    }
    public Token estadoInicial() throws ExcepcionLexica, IOException {

        // Ignorar espacios y tabulaciones
        if (Character.isSpaceChar(caracterActual) || Character.isWhitespace(caracterActual)) {
            actualizarCaracterActual();
            return estadoInicial();
        }

        // Letras
        if (Character.isLetter(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            if (Character.isUpperCase(caracterActual)) {
                return scanIdentificadorClase();
            } else {
                return scanIdentificadorVarOMetodo();
            }
        }
        if (Character.isDigit(caracterActual)) {
            actualizarLexema();
            actualizarCaracterActual();
            return scanLiteralEntero();
        }
        // Caracteres
        if (caracterActual == '\''){
            actualizarLexema();
            actualizarCaracterActual();
            return scanCharInicio();
        }
        if (caracterActual == '"'){
            actualizarLexema();
            actualizarCaracterActual();
            return e7();
        }
        if (caracterActual == '>'){
            actualizarLexema();
            actualizarCaracterActual();
            return e13();
        }
        if (caracterActual == '<'){
            actualizarLexema();
            actualizarCaracterActual();
            return e14();
        }
        if (caracterActual == '='){
            actualizarLexema();
            actualizarCaracterActual();
            return e15();
        }
        if (caracterActual == '!'){
            actualizarLexema();
            actualizarCaracterActual();
            return e16();
        }
        // Símbolos
        if (caracterActual == '+') {
            actualizarLexema();
            actualizarCaracterActual();
            return e17();
        }

        if (caracterActual == '-') {
            actualizarLexema();
            actualizarCaracterActual();
            return e19();
        }
        if (caracterActual == '*') {
            actualizarLexema();
            actualizarCaracterActual();
            return scanOperadorMult();
        }

        if (caracterActual == '|') {
            actualizarLexema();
            actualizarCaracterActual();
            return e22();
        }
        if(caracterActual == '&'){
            actualizarLexema();
            actualizarCaracterActual();
            return e37();
        }
        if (caracterActual == '%') {
            actualizarLexema();
            actualizarCaracterActual();
            return scanOperadorMod();
        }
        if (caracterActual == '{') {
            actualizarLexema();
            actualizarCaracterActual();
            return e25();
        }
        if (caracterActual == '}') {
            actualizarLexema();
            actualizarCaracterActual();
            return e26();
        }
        if (caracterActual == '(') {
            actualizarLexema();
            actualizarCaracterActual();
            return e27();
        }
        if (caracterActual == ')') {
            actualizarLexema();
            actualizarCaracterActual();
            return e28();
        }
        if (caracterActual == ';') {
            actualizarLexema();
            actualizarCaracterActual();
            return e29();
        }
        if (caracterActual == ',') {
            actualizarLexema();
            actualizarCaracterActual();
            return e30();
        }
        if (caracterActual == '.') {
            actualizarLexema();
            actualizarCaracterActual();
            return e31();
        }
        if (caracterActual == ':') {
            actualizarLexema();
            actualizarCaracterActual();
            return e32();
        }
        if (caracterActual == '/'){
            //primero q nada tiene que ser finalizador pq es una division ?
            actualizarLexema();
            actualizarCaracterActual();
            return e11();
        }
        if (caracterActual == sourceManager.END_OF_FILE){
            return new Token("EOF",lexema,sourceManager.getLineNumber());
        }
        // Cualquier otro carácter no válido
        actualizarLexema();
        throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
    }
    public Token scanIdentificadorClase() throws IOException {
        if(Character.isLetter(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_'){
            actualizarLexema();
            actualizarCaracterActual();
            return scanIdentificadorClase();
        }
        else{
            return new Token("idClase", lexema,sourceManager.getLineNumber());
        }
    }
    public Token scanIdentificadorVarOMetodo() throws ExcepcionLexica, IOException {
        //primero que nada me fijo si lexema pertenece a palabrasReservadas, si matchea devuelvo
        //un token con ese ID, sino hago todo esto
        if(Arrays.asList(palabrasClave).contains(lexema)){
            return generarTokenPalabraClave();
        } else if(Character.isLetter(caracterActual) || Character.isDigit(caracterActual) || caracterActual == '_'){
                actualizarLexema();
                actualizarCaracterActual();
                return scanIdentificadorVarOMetodo();
        } else{
            return new Token("idMetVar", lexema,sourceManager.getLineNumber());
        }

    }
    public Token scanLiteralEntero() throws ExcepcionLexica, IOException {
        if(lexema.length() > 9){
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
        if(Character.isDigit(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return scanLiteralEntero();
        }
        else{
            return new Token("intLiteral",lexema,sourceManager.getLineNumber());
        }
    }
    public Token scanCharInicio() throws ExcepcionLexica, IOException {
        if(caracterActual == '\\'){
            actualizarLexema();
            actualizarCaracterActual();
            return scanCharEscape();
        }
        else if(caracterActual != '\'' && caracterActual != '\n' && caracterActual != sourceManager.END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return scanCharCierre();
        }
        else{
            actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token scanCharCierre() throws IOException, ExcepcionLexica {
        if(caracterActual == '\''){
            actualizarLexema();
            actualizarCaracterActual();
            return finalizarCharLiteral();
        }
        else{
            actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token scanCharEscape() throws IOException, ExcepcionLexica {
        if(caracterActual != '\n' && caracterActual != sourceManager.END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return scanCharCierre();
        }
        else{
            actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token finalizarCharLiteral(){
        return new Token("charLiteral",lexema,sourceManager.getLineNumber());
    }
    public Token e7() throws IOException, ExcepcionLexica {
        if(caracterActual == '\\'){
            actualizarLexema();
            actualizarCaracterActual();
            return e9();
        }
        else if(caracterActual == '"'){
            //por un finalizador que me diga que soy un IDString
            actualizarLexema();
            actualizarCaracterActual();
            return e10();
        }
        else if (caracterActual != '\n'){ //si es cualquier otro caracter me voy para e8
            actualizarLexema();
            actualizarCaracterActual();
            return e8();
        }
        else{
            //actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token e8() throws IOException, ExcepcionLexica {
        if(caracterActual == '\\'){
            //vuelvo por E9
            actualizarLexema();
            actualizarCaracterActual();
            return e9();
        }else if(caracterActual == '"'){
            //por un finalizador que me diga que soy un IDString
            actualizarLexema();
            actualizarCaracterActual();
            return e10();
        }else if(caracterActual != '\n'){
            actualizarLexema();
            actualizarCaracterActual();
            return e8();
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    // Capaz no es necesario
    public Token e9() throws ExcepcionLexica, IOException {
        if(caracterActual == '"'){
            actualizarLexema();
            actualizarCaracterActual();
            return e8();
        }
        else{
            //actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    } //aca espero comilla doble ? y dsp vuelvo por E8 cualq cosa
    public Token e10(){
        return new Token("stringLiteral", lexema,sourceManager.getLineNumber());
    }
    public Token e11() throws IOException, ExcepcionLexica {
        if(caracterActual == '/'){
            actualizarLexema();
            actualizarCaracterActual();
            return scanComentarioSimple();
        }else if (caracterActual == '*'){
            actualizarLexema();
            actualizarCaracterActual();
            return scanComentarioMultilinea();
        }else{
            return new Token("/", lexema,sourceManager.getLineNumber());
        }
    }
    public Token scanComentarioSimple() throws IOException, ExcepcionLexica {
        //en e12 me salteo todo hasta que me venga un enter o eof?

        //si es un caracter le manda mecha y no terminador
        //GUARDA CON ESTO
        if(caracterActual != '\n'){
            actualizarLexema();
            actualizarCaracterActual();
            return scanComentarioSimple();
        }
        else{
            setLexema("");
            return estadoInicial();
        }
    }
    public Token scanComentarioMultilinea() throws IOException, ExcepcionLexica {
        // si es un * se va para otro estado a ver * y caracter
       if(caracterActual == '*'){
           //aca me fui si o si a preguntar por '/'
           actualizarLexema();
           actualizarCaracterActual();
           return e35();
       } else if (caracterActual != sourceManager.END_OF_FILE){
           actualizarLexema();
           actualizarCaracterActual();
           return scanComentarioMultilinea();
       }else{
           actualizarLexema();
           throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
       }
    }
    public Token e35() throws ExcepcionLexica, IOException {
        if(caracterActual == '/'){
            actualizarLexema();
            actualizarCaracterActual();
            return finComentarioMultilinea();
        }
        else if (caracterActual != sourceManager.END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return scanComentarioMultilinea();
        }
        else{
            actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token finComentarioMultilinea() throws ExcepcionLexica, IOException {
        setLexema("");
        return estadoInicial();
    }
    public Token e13() throws IOException {
        if(caracterActual == '='){
            actualizarLexema();
            actualizarCaracterActual();
            return e33(">");
        }
        else{
            return new Token(">",lexema,sourceManager.getLineNumber());
        }
    }
    public Token e14() throws IOException {
        if(caracterActual == '='){
            actualizarLexema();
            actualizarCaracterActual();
            return e33("<");
        }
        else{
            return new Token("<",lexema,sourceManager.getLineNumber());
        }
    }
    public Token e15() throws IOException {
        if(caracterActual == '='){
            actualizarLexema();
            actualizarCaracterActual();
            return e33("=");
        }
        else{
            return new Token("=",lexema,sourceManager.getLineNumber());
        }
    }
    public Token e16() throws IOException {
        if(caracterActual == '='){
            actualizarLexema();
            actualizarCaracterActual();
            return e33("!");
        }
        else{
            return new Token("!",lexema,sourceManager.getLineNumber());
        }
    }
    public Token e17() throws IOException {
        if(caracterActual == '+'){
            lexema = lexema + caracterActual;
            caracterActual = sourceManager.getNextChar();
            return e18();
        }
        else{
            return new Token("+",lexema,sourceManager.getLineNumber());
        }
    }
    public Token e18() throws IOException {
        return new Token("++", lexema, sourceManager.getLineNumber());
    }
    public Token e19() throws IOException {
        if(caracterActual == '-'){
            lexema = lexema + caracterActual;
            caracterActual = sourceManager.getNextChar();
            return e20();
        }
        else{
            return new Token("-", lexema, sourceManager.getLineNumber());
        }
    }
    public Token e20(){
        return new Token("--", lexema, sourceManager.getLineNumber());
    }
    public Token scanOperadorMult() throws IOException {
        return new Token("*", lexema, sourceManager.getLineNumber());
    }
    public Token e22() throws ExcepcionLexica, IOException {
        if(caracterActual == '|'){
            actualizarLexema();
            actualizarCaracterActual();
            return e23();
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token e23(){
        return new Token("||", lexema, sourceManager.getLineNumber());
    }
    public Token scanOperadorMod(){
        return new Token("%", lexema, sourceManager.getLineNumber());
    }
    public Token e25(){
        return new Token("{", lexema, sourceManager.getLineNumber());
    }
    public Token e26(){
        return new Token("}", lexema, sourceManager.getLineNumber());
    }
    public Token e27(){
        return new Token("(", lexema, sourceManager.getLineNumber());
    }
    public Token e28(){
        return new Token(")", lexema, sourceManager.getLineNumber());
    }
    public Token e29(){
        return new Token(";",  lexema, sourceManager.getLineNumber());
    }
    public Token e30(){
        return new Token(",",  lexema, sourceManager.getLineNumber());
    }
    public Token e31(){
        return new Token(".", lexema, sourceManager.getLineNumber());
    }
    public Token e32(){
        return new Token(":", lexema, sourceManager.getLineNumber());
    }
    public Token e33(String id){
        return new Token(id+"=", lexema, sourceManager.getLineNumber());
    }
    public Token e37() throws ExcepcionLexica, IOException {
        if(caracterActual == '&'){
            actualizarLexema();
            actualizarCaracterActual();
            return e38();
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
    public Token e38(){
        return new Token("&",lexema,sourceManager.getLineNumber());
    }
    public void setLexema(String lexema) {
        this.lexema = lexema;
    }
    public char getCaracterActual() {
        return caracterActual;
    }
    public void actualizarCaracterActual() throws IOException {
        caracterActual = sourceManager.getNextChar();
    }
    public void actualizarLexema(){
        lexema = lexema + caracterActual;
    }
    public void inicializarPalabrasClave(){
        palabrasClave = new String[] {
                "class","extends","public","static",
                "void","boolean","char","int",
                "abstract","final","if","else",
                "while","return","var","this",
                "new","null","true","false"
        };
    }
    public Token generarTokenPalabraClave() throws ExcepcionLexica {
        if(lexema.equals("class")){
            return new Token("pr_class", lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("extends")){
            return new Token("pr_extends",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("public")){
            return new Token("pr_public",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("static")){
            return new Token("pr_static",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("void")){
            return new Token("pr_void",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("boolean")){
            return new Token("pr_boolean",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("char")){
            return new Token("pr_char",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("int")){
            return new Token("pr_int",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("abstract")){
            return new Token("pr_abstract",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("final")){
            return new Token("pr_final",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("if")){
            return new Token("pr_if",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("else")){
            return new Token("pr_else",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("while")){
            return new Token("pr_while",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("return")){
            return new Token("pr_return",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("var")){
            return new Token("pr_var",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("this")){
            return new Token("pr_this",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("new")){
            return new Token("pr_new",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("null")){
            return new Token("pr_null",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("true")){
            return new Token("pr_true",lexema,sourceManager.getLineNumber());
        }
        if(lexema.equals("false")){
            return new Token("pr_false",lexema,sourceManager.getLineNumber());
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber());
        }
    }
}
