/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author jp
 */
class Prioridade {
    
    private Token token;
    private int prioridade;
    
    public Prioridade(Token token, int prioridade){
        this.token = token;
        this.prioridade = prioridade;
    }
    
    public Token getToken()
    {
        return token;
    }
    
    public int getPrioridade()
    {
        return prioridade;
    }
    
    public void setPrioridade(int prioridade)
    {
        this.prioridade = prioridade;
    }
          
    
}
