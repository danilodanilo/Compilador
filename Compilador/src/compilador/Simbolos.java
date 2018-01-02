/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

/**
 *
 * @author desenvJP
 */
public class Simbolos {
    private String nome;
    
    private boolean escopo;
    
    private String tipo;
    
    private String endereco;
    
    private  String label;
    
    private boolean inicializado;
    
    public Simbolos(String nome, boolean escopo, String tipo, String endereco){
        this.nome = nome;
        this.escopo = escopo;
        this.tipo = tipo;
        this.endereco = endereco;
        this.inicializado = true;
              
    }
    public Simbolos(String nome, boolean escopo, String tipo, String endereco, String rotulo){
        this.nome = nome;
        this.escopo = escopo;
        this.tipo = tipo;
        this.label = rotulo;
        this.endereco = endereco;
        this.inicializado = true;
    }
    
    public boolean isInicializado(){
        return inicializado;
    }
    
    public void setInicializado(boolean inicializado){
        this.inicializado = inicializado;
                
    }
    public String getLabel(){
        return label;
    }
    public void setLabel(String label){
        this.label = label;
    }
        public String getEndereco() {
        return endereco;
    }

    public boolean getEscopo() {
        return escopo;
    }

    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setEscopo(boolean escopo) {
        this.escopo = escopo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
}
