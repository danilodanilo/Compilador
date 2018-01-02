
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author jp
 */
public class Token {
    
    private String lexema;
    private String simbolo;
   
    public Token () {
        this.lexema  = new String();
        this.simbolo = new String();
    }
    
    public Token (String simbolo, String lexema) {
        this.lexema  = lexema;
        this.simbolo = simbolo;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getLexema() {
        return lexema;
    }

    public String getSimbolo() {
        return simbolo;
    }
}