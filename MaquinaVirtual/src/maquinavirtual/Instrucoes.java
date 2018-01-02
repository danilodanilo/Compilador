/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinavirtual;

/**
 *
 * @author jp
 */
public class Instrucoes {
    
    private String operacao;
    private String atributo1;
    private String atributo2;
    private String comentario;

    Instrucoes(String operacao, String atributo1, String atributo2, String comentario) {
        this.operacao = operacao;
        this.atributo1 = atributo1;
        this.atributo2 = atributo2;
        this.comentario = comentario;
    }
    
    public String getOperacao() {
        return operacao;
    }
    
    public String getParam1()
    {
        return atributo1;
    }
    
    public String getParam2()
    {
        return atributo2;
    }
    
    public String getComentario()
    {
        return comentario;
    }
    public void setOperacao(String operacao)
    {
        this.operacao = operacao;
    }
    
    public void setParam1(String atributo1)
    {
        this.atributo1 = atributo1;
    }
    
    public void setParam2(String atributo2)
    {
        this.atributo2 = atributo2;
    }   
    
    public void setComentario(String comentario)
    {
        this.comentario = comentario;
    }
    public String toString(){
        return "Instrucao{" + "operacao=" + operacao + "atributo1=" + atributo1 + "atributo2=" + atributo2 + "comentario=" + comentario + "}";
    }
}