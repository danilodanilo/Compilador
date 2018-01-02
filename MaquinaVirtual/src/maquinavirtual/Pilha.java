/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinavirtual;

public class Pilha {
    
    private int endereco;
    private int valor;
    
    public Pilha(int endereco,int valor)
    {
        this.endereco = endereco;
        this.valor = valor;
    }
    public int getEnd(){
        return endereco;
    }
    public void setEnd(){
        this.endereco = endereco;
    }
    public int getValor(){
        return valor;
    }
    public void setValor(){
        this.valor = valor;
    }
}