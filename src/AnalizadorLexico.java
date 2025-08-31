import java.io.IOException;
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

        if (Character.isSpaceChar(caracterActual) || Character.isWhitespace(caracterActual)) {
            actualizarCaracterActual();
            return estadoInicial();
        }

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
            actualizarLexema();
            actualizarCaracterActual();
            return e11();
        }
        if (caracterActual == sourceManager.END_OF_FILE){
            return new Token("EOF",lexema,sourceManager.getLineNumber());
        }
        actualizarLexema();
        throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Simbolo desconocido");
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Literal entero demasiado largo");
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Caracter mal formado");
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Caracter sin cierre");
        }
    }
    public Token scanCharEscape() throws IOException, ExcepcionLexica {
        if(caracterActual == 'u'){
            actualizarLexema();
            actualizarCaracterActual();
            return scanUnicode1();
        }
        if(caracterActual != '\n' && caracterActual != sourceManager.END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return scanCharCierre();
        }
        else{
            actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Caracter invalido");
        }
    }
    public Token scanUnicode1() throws IOException, ExcepcionLexica {
        if(Character.isDigit(caracterActual) || Character.isLetter(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return scanUnicode2();
        }
        else throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Secuencia Unicode invalida");
    }
    public Token scanUnicode2() throws IOException, ExcepcionLexica {
        if(Character.isDigit(caracterActual) || Character.isLetter(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return scanUnicode3();
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Secuencia Unicode invalida");
        }
    }
    public Token scanUnicode3() throws IOException, ExcepcionLexica {
        if(Character.isDigit(caracterActual) || Character.isLetter(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return scanUnicode4();
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Secuencia Unicode invalida");
        }
    }
    public Token scanUnicode4() throws IOException, ExcepcionLexica {
        if(Character.isDigit(caracterActual) || Character.isLetter(caracterActual)){
            actualizarLexema();
            actualizarCaracterActual();
            return scanUnicode5();
        }
        else{
            actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Secuencia Unicode invalida");
        }
    }
    public Token scanUnicode5() throws IOException, ExcepcionLexica {
        if(caracterActual == '\''){
            actualizarLexema();
            actualizarCaracterActual();
            return finalizarCharLiteral();
        }
        else throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Secuencia Unicode invalida");
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
            actualizarLexema();
            actualizarCaracterActual();
            return e10();
        }
        else if (caracterActual != '\n'){
            actualizarLexema();
            actualizarCaracterActual();
            return e8();
        }
        else{
            //actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"String sin cerrar");
        }
    }
    public Token e8() throws IOException, ExcepcionLexica {
        if(caracterActual == '\\'){
            actualizarLexema();
            actualizarCaracterActual();
            return e9();
        }else if(caracterActual == '"'){
            actualizarLexema();
            actualizarCaracterActual();
            return e10();
        }else if(caracterActual != '\n' && caracterActual != sourceManager.END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return e8();
        }
        else{
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"String invalido");
        }
    }
    public Token e9() throws ExcepcionLexica, IOException {
        if(caracterActual != '\n' && caracterActual != sourceManager.END_OF_FILE){
            actualizarLexema();
            actualizarCaracterActual();
            return e8();
        }
        else{
            //actualizarLexema();
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"String invalido");
        }
    }
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
        if(caracterActual != '\n' && caracterActual != sourceManager.END_OF_FILE){
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
       if(caracterActual == '*'){
           actualizarLexema();
           actualizarCaracterActual();
           return e35();
       } else if (caracterActual != sourceManager.END_OF_FILE){
           actualizarLexema();
           actualizarCaracterActual();
           return scanComentarioMultilinea();
       }else{
           actualizarLexema();
           throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Comentario multilinea no cerrado");
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Comentario multilinea no cerrado");
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
            actualizarLexema();
            actualizarCaracterActual();
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
            actualizarLexema();
            actualizarCaracterActual();
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Operador logico incompleto, se esperaba '||'");
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Operador logico incompleto, se esperaba '&&'");
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
            throw new ExcepcionLexica(lexema,sourceManager.getLineNumber(),"Identificador o palabra clave invalida");
        }
    }
    public String getLexema() {
        return lexema;
    }
}
