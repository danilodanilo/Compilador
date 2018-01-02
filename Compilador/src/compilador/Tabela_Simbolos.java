/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Vector;

/**
 *
 * @author desenvJP
 */
public class Tabela_Simbolos {

    Vector<Simbolos> simbolos;

    public Tabela_Simbolos() {
        simbolos = new Vector<>();
    }

    public void insere_tabela(String lexema, String tipo, boolean escopo, String endereco) {
        Simbolos simb = new Simbolos(lexema, escopo, tipo, endereco);

        this.simbolos.add(simb);
    }

    public void insere_tabela(String lexema, String tipo, boolean escopo, String endereco, String rotulo) {
        Simbolos simb = new Simbolos(lexema, escopo, tipo, endereco, rotulo);

        this.simbolos.add(simb);
    }

    public Simbolos pesquisa_tabela(String lexema) {
        int i;
               
        for (i = simbolos.size()-1; i>=0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)) {
                
                return simbolos.elementAt(i);
            }
        }
        return new Simbolos("naoEncontrado", false, "", lexema);
    }

    public boolean pesquisa_duplicvar_tabela(String lexema) {
        int i;
        for (i = simbolos.size() - 1; i >= 0 && simbolos.elementAt(i).getEscopo() != true; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)) {
                return true;//existe duplicidade
            }
        }
        return false;
    }
    
    

    public void coloca_tipo(String lexema) {
        int i;
        for (i = simbolos.size() - 1; i >= 0 && simbolos.elementAt(i).getTipo().equals("nomeVariavel"); i--) {
            this.simbolos.elementAt(i).setTipo(lexema);
        }
    }

    public Simbolos pesquisa_declvar_tabela(String lexema) {
        int i;

        for (i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && (simbolos.elementAt(i).getTipo().equals("inteiro") || simbolos.elementAt(i).getTipo().equals("booleano"))) {
                return simbolos.elementAt(i);
            }
        }
        return new Simbolos("naoEncontrado", false, "", "");

    }

    public Simbolos pesquisa_declproc_tabela(String lexema) {
        int i;

        for (i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && (simbolos.elementAt(i).getTipo().equals("nomeProcedimento"))) {
                return simbolos.elementAt(i);
            }
        }
        return new Simbolos("naoEncontrado", false, lexema, lexema);
    }

    public Simbolos pesquisa_declvarfunc_tabela(String lexema) {

        for (int i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && (simbolos.elementAt(i).getTipo().equals("inteiro")
                    || simbolos.elementAt(i).getTipo().equals("booleano")
                    || simbolos.elementAt(i).getTipo().equals("funcaoBooleano")
                    || simbolos.elementAt(i).getTipo().equals("funcaoInteiro"))) {
                return simbolos.elementAt(i);
            }
        }
        return new Simbolos("naoEncontrado", false, "", "");
    }
    
    public boolean pesquisa_duplicproc_tabela(String lexema) {
        int i;
        for (i = simbolos.size() - 1; i >= 0 && simbolos.elementAt(i).getEscopo() != true; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && simbolos.elementAt(i).getTipo().equals("nomeProcedimento")) {
                return true;
            }
        }
        if (simbolos.elementAt(i).getNome().equals(lexema)
                && simbolos.elementAt(i).getTipo().equals("nomeProcedimento")) {
            return true;
        }
        return false;
    }
    
        public boolean pesquisa_duplicproc2_tabela(String lexema) {
        int i;
        for (i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && simbolos.elementAt(i).getTipo().equals("nomeProcedimento")) {
                return true;
            }
        }
        return false;
    }
        
        
    public boolean pesquisa_duplicfunc2_tabela(String lexema) {
        int i;
        for (i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && (simbolos.elementAt(i).getTipo().equals("funcaoInteiro") || simbolos.elementAt(i).getTipo().equals("funcaoBooleano"))) {
                return true;
            }
        }
        return false;
    }
    
    
    public int volta_nivel() {//retorna o numero de variaveis desalocadas
        int i;
        int num_var = 0;

        for (i = simbolos.size() - 1; i >= 0 && simbolos.elementAt(i).getEscopo() != true; i--) {
            if (simbolos.elementAt(i).getTipo().equals("inteiro")
                    || simbolos.elementAt(i).getTipo().equals("booleano")
                    || simbolos.elementAt(i).getTipo().equals("funcaoBooleano")
                    || simbolos.elementAt(i).getTipo().equals("funcaoInteiro")) {
                num_var++;
            }
            simbolos.removeElementAt(i);

        }
        simbolos.elementAt(i).setEscopo(false);

        return num_var;
    }

    public boolean pesquisa_duplicfunc_tabela(String lexema) {
        int i;
        for (i = simbolos.size() - 1; i >= 0 && simbolos.elementAt(i).getEscopo() != true; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && (simbolos.elementAt(i).getTipo().equals("funcaoInteiro") || simbolos.elementAt(i).getTipo().equals("funcaoBooleano"))) {
                return true;
            }
        }

        if (simbolos.elementAt(i).getNome().equals(lexema) &&
                (simbolos.elementAt(i).getTipo().equals("funcaoInteiro") || simbolos.elementAt(i).getTipo().equals("funcaoBooleano"))) {
            return true;

        }
        return false;
    }

    public Simbolos pesquisa_declfunc_tabela(String lexema) {
        int i;

        for (i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)
                    && (simbolos.elementAt(i).getTipo().equals("funcaoInteiro") || simbolos.elementAt(i).getTipo().equals("funcaoBooleano"))) {
                return simbolos.elementAt(i);
            }
        }
        return new Simbolos("naoEncontrado", false, "", "");
    }

    public void setar_tipo_funcao(String tipo) {
        simbolos.lastElement().setTipo(tipo);
    }

    public String pesquisa_tipo(String lexema) {
        int i;

        for (i = simbolos.size() - 1; i >= 0; i--) {
            if (simbolos.elementAt(i).getNome().equals(lexema)) {
                return simbolos.elementAt(i).getTipo();
            }
        }
        return "";
    }

    public String pesquisa_tipo_recebe_Expressao(String lexema) {//retorna tipo da funcao ou variavel que recebera o resultado da expressao
        int i;
        for (i = simbolos.size() - 1; i >= 0; i--) {
            //percorre tabela ate o escopo, verifica se é funcao e se sim retorno o tipo de retorno da funcao
             if (simbolos.elementAt(i).getNome().equals(lexema)) {
                if (simbolos.elementAt(i).getEscopo() == true) {
                    if (simbolos.elementAt(i).getTipo().equals("funcaoInteiro")) {
                        return "inteiro";
                    } else if (simbolos.elementAt(i).getTipo().equals("funcaoBooleano")) {
                        return "booleano";
                    }
                } else {
                    break; //parar no escopo
                }
            }
        }
        for(i=simbolos.size()-1; i>=0; i--){
            //percorre toda a tabela a procura de uma variavel igual ao do lexema, se encontrar retorna o tipo da variavel
            if(simbolos.elementAt(i).getNome().equals(lexema) &&
              (simbolos.elementAt(i).getTipo().equals("inteiro") || simbolos.elementAt(i).getTipo().equals("booleano"))){
                return simbolos.elementAt(i).getTipo();
            }
        }
        return "naoEncontrado";
    }
}
