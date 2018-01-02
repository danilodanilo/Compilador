/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;

/**
 *
 * @author jp
 */
public class Sintatico {

    Token token = new Token();
    AnalisadorLexico lexico = new AnalisadorLexico();
    Erro comp = new Erro();
    String erro_texto;
    String erro_tipo;
    JButton botao = new JButton();
    private Arquivo arq = new Arquivo();
    private Vector<String> cod_assembly = new Vector<String>();

    private Tabela_Simbolos tabelaSimbolos;
    private int prox_endereco_var = 0;
    private int prox_endereco_func = -1;
    private int numero_func = 0;
    private int rotulo;
    private Vector<Token> expressaoOrdem = new Vector<Token>();

    public Sintatico() {
        this.rotulo = 1;
        this.tabelaSimbolos = new Tabela_Simbolos();
    }

    public Tabela_Simbolos getTabela_Simbolos() {
        return tabelaSimbolos;
    }

    public void setAnalisa(AnalisadorLexico analise) {
        this.lexico = analise;
    }

    
    public void setButton(JButton jButton1) {
        this.botao = jButton1;
    }

    public void iniciar() {

        try {

            comp.setBotao(this.botao);

            token = lexico.pegarToken();

            if (token.getSimbolo().equals("sprograma")) {
                this.gera(0, "START", "", "");

                token = lexico.pegarToken();

                if (token.getSimbolo().equals("sidentificador")) {
                    /**
                     * **SEMANTICO***
                     */
                    this.tabelaSimbolos.insere_tabela(token.getLexema(), "nomePrograma", true, "");
                    token = lexico.pegarToken();

                    if (token.getSimbolo().equals("sponto_virgula")) {

                        analisa_bloco();

                        if (token.getSimbolo().equals("sponto")) {

                            token = lexico.pegarToken();

                            if (token.getSimbolo().equals("sfim_arquivo")) {
                                this.gera(0, "DALLOC", String.valueOf(0), String.valueOf(prox_endereco_var));
                                this.gera(0, "HLT", "", "");

                                lexico.output.addElement("Compilado com Sucesso");
                                this.botao.setEnabled(false);

                                arq.geraArquivoObj(this.cod_assembly);

                            } else {
                                lexico.output.addElement("Não podem existir instruções após o ponto final '.' ");
                                this.botao.setEnabled(false);
                            }
                        } else {
                            erro_texto = "Esperado caractere '.'";
                            erro_tipo = "Sintatico";
                            comp.setErro(lexico, erro_tipo, erro_texto);
                        }
                    } else {
                        erro_tipo = "Sintatico";
                        erro_texto = "Esperado caractere ';'";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }
                } else {
                    erro_tipo = "Sintatico";
                    erro_tipo = "Esperado identificador";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }
            } else {
                erro_texto = "Esperado palavra reservada 'programa'";
                erro_tipo = "Sintatico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }

        } catch (Exception e) {
            System.out.println("Excessao Lancada " + erro_texto + "\n");
        }
    }//OK

    private void analisa_bloco() throws Exception {
        token = lexico.pegarToken();

        analisa_et_variaveis();
        analisa_subrotinas();
        analisa_comandos();
    }//OK

    private void analisa_et_variaveis() throws Exception {
        if (token.getSimbolo().equals("svar")) {
            token = lexico.pegarToken();

            if (token.getSimbolo().equals("sidentificador")) {
                
                while (token.getSimbolo().equals("sidentificador")) {
                    this.analisa_variaveis();
                    if (token.getSimbolo().equals("sponto_virgula")) {
                        token = lexico.pegarToken();
                    } else {
                        erro_texto = "Esperado caractere ';'";
                        erro_tipo = "Sintatico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }
                }
            } else {
                erro_texto = "Esperado identificador";
                erro_tipo = "Sintatico";
                comp.setErro(lexico, erro_tipo, erro_texto);

            }
        }

    }//OK

    private void analisa_variaveis() throws Exception {
        int numero_var = 0; //numero de variaveis sendo declaradas
        do {
            if (token.getSimbolo().equals("sidentificador")) {
                /**
                 * **SEMANTICO***
                 */
                if (!this.tabelaSimbolos.pesquisa_duplicvar_tabela(token.getLexema())) {
                    
                  if (!this.tabelaSimbolos.pesquisa_duplicproc2_tabela(token.getLexema())) {  
                      
                      if (!this.tabelaSimbolos.pesquisa_duplicfunc2_tabela(token.getLexema())) {
                    
                    this.tabelaSimbolos.insere_tabela(token.getLexema(), "nomeVariavel", false, String.valueOf(prox_endereco_var + numero_var));
                    
                    numero_var++;

                    token = lexico.pegarToken();

                    if (token.getSimbolo().equals("svirgula") || token.getSimbolo().equals("sdoispontos")) {

                        if (token.getSimbolo().equals("svirgula")) {

                            token = lexico.pegarToken();

                            if (token.getSimbolo().equals("sdoispontos")) {
                                erro_texto = "Caractere ':' depois de ','";
                                erro_tipo = "Sintatico";
                                comp.setErro(lexico, erro_tipo, erro_texto);
                            }
                        }
                    } else {

                        //if (token.getSimbolo().equals("svirgula")) {
                        erro_texto = "Declaracao de variaveis invalida ','";
                        erro_tipo = "Sintatico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                        }
                    
                    } 
                      else {
                    erro_texto = "Identificador com mesmo nome de funcao já utilizado " + token.getLexema();
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);

                }
                    
                } else {
                    erro_texto = "Identificador com mesmo nome de procedimento já utilizado " + token.getLexema();
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);

                }

                
                
                } else {
                    erro_texto = "Identificador já utilizado " + token.getLexema();
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);

                }

            } else {

                erro_texto = "Esperado identificador";
                erro_tipo = "Sintatico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
        } while (!token.getSimbolo().equals("sdoispontos"));
        
        this.gera(0, "ALLOC", String.valueOf(prox_endereco_var), String.valueOf(numero_var));

        prox_endereco_var += numero_var;
        
        token = lexico.pegarToken();
        
        this.analisa_tipo();

    }//OK

    private void analisa_tipo() throws Exception {
        if (!token.getSimbolo().equals("sinteiro") && !token.getSimbolo().equals("sbooleano")) {
            erro_texto = "Esperado caractere do tipo 'inteiro' ou 'booleano'";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        } else {
            /**
             * **SEMANTICO***
             */
            tabelaSimbolos.coloca_tipo(token.getLexema());
            token = lexico.pegarToken();
        }

    }//OK

    private void analisa_subrotinas() throws Exception {
        boolean flag = false;
        int rot1 = this.rotulo;

        if (token.getSimbolo().equals("sprocedimento") || token.getSimbolo().equals("sfuncao")) {
            if(token.getSimbolo().equals("sfuncao")){
                this.gera(0, "ALLOC", String.valueOf(prox_endereco_var), String.valueOf(1));
                prox_endereco_var++;
            }
            this.gera(0, "JMP", "L"+String.valueOf(rot1), "");
            this.rotulo++;
            flag = true;
        }
        while (token.getSimbolo().equals("sprocedimento") || token.getSimbolo().equals("sfuncao")) {
            if (token.getSimbolo().equals("sprocedimento")) {
                analisa_declaracao_procedimento();
            } else {
                analisa_declaracao_funcao();
            }

            if (token.getSimbolo().equals("sponto_virgula")) {
                token = lexico.pegarToken();
            } else {
                erro_texto = "Esperado caractere ';'";
                erro_tipo = "Sintatico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
        }
        if (flag) {
            this.gera(rot1, "NULL", "", "");
        }
    }//OK

    private void analisa_declaracao_procedimento() throws Exception {
        int numero_var = 0;
        token = lexico.pegarToken();
        if (token.getSimbolo().equals("sidentificador")) {
            /**
             * **SEMANTICO***
             */
            if (!tabelaSimbolos.pesquisa_duplicproc_tabela(token.getLexema())) {
                
                tabelaSimbolos.insere_tabela(token.getLexema(), "nomeProcedimento", true, "", String.valueOf(this.rotulo));
                this.gera(this.rotulo,"NULL","","");
                this.rotulo++;

                token = lexico.pegarToken();
                
                if (token.getSimbolo().equals("sponto_virgula")) {
                    analisa_bloco();
                } else {
                    erro_texto = "Esperado caractere ';'";
                    erro_tipo = "Sintatico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }
            } else {
                erro_texto = "Procedimento ja declarado anteriormente";
                erro_tipo = "Semantico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
        } else {
            erro_texto = "Esperado identificador";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
        /**
         * **SEMANTICO***
         */
        numero_var = tabelaSimbolos.volta_nivel();
        if (numero_var > 0) {
            this.gera(0, "DALLOC", String.valueOf(prox_endereco_var - numero_var), String.valueOf(numero_var));
        }
        this.gera(0, "RETURN", "", "");
        prox_endereco_var -= numero_var;
    }//OK

    private void analisa_declaracao_funcao() throws Exception {
        
        int numero_var = 0;
        numero_func++;
        
        token = lexico.pegarToken();
        
        if (token.getSimbolo().equals("sidentificador")) {
            /**
             * **SEMANTICO***
             */
            if (!tabelaSimbolos.pesquisa_duplicfunc_tabela(token.getLexema())) {

                this.gera(rotulo, "NULL", "", "");
                this.tabelaSimbolos.pesquisa_declfunc_tabela(token.getLexema()).setLabel("L"+rotulo);
               // this.gera(0, "ALLOC", String.valueOf(prox_endereco_var), String.valueOf(numero_var));
                tabelaSimbolos.insere_tabela(token.getLexema(), "", true, String.valueOf(prox_endereco_var-1), String.valueOf(this.rotulo));
                rotulo++;
                //prox_endereco_var++;
                token = lexico.pegarToken();

                if (token.getSimbolo().equals("sdoispontos")) {
                    token = lexico.pegarToken();
                    if (token.getSimbolo().equals("sinteiro") || token.getSimbolo().equals("sbooleano")) {
                        if (token.getSimbolo().equals("sinteiro")) {
                            tabelaSimbolos.setar_tipo_funcao("funcaoInteiro");
                        } else {
                            tabelaSimbolos.setar_tipo_funcao("funcaoBooleano");
                        }
                        token = lexico.pegarToken();
                        if (token.getSimbolo().equals("sponto_virgula")) {
                            analisa_bloco();
                        }
                    } else {
                        erro_texto = "Esperado caractere do tipo 'inteiro' ou 'booleano'";
                        erro_tipo = "Sintatico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }
                } else {
                    erro_texto = "Esperado caractere ':'";
                    erro_tipo = "Sintatico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }
            } else {
                erro_texto = "Função já declarada anteriormente";
                erro_tipo = "Semantico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }

        } else {
            erro_texto = "Esperado caractere 'identificador'";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
        /**
         * **SEMANTICO***
         */
        numero_var = tabelaSimbolos.volta_nivel();
        if (numero_var > 0) {
            this.gera(0, "DALLOC", String.valueOf(prox_endereco_var - numero_var), String.valueOf(numero_var));
        }
        this.gera(0, "RETURN", "", "");
        //prox_endereco_func -= numero_func;
        prox_endereco_var -= numero_var;
    }//OK

    private void analisa_comandos() throws Exception {
        if (token.getSimbolo().equals("sinicio")) {
            token = lexico.pegarToken();
            analisa_comando_simples();
            while (!token.getSimbolo().equals("sfim")) {
                if (token.getSimbolo().equals("sponto_virgula")) {
                    token = lexico.pegarToken();
                    if (!token.getSimbolo().equals("sfim")) {
                        analisa_comando_simples();
                    }
                } else {
                    erro_texto = "Esperado caractere ';'";
                    erro_tipo = "Sintatico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }
            }
            token = lexico.pegarToken();
        } else {
            erro_texto = "Esperado palavra reservada 'inicio'";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
    }//OK

    private void analisa_comando_simples() throws Exception {
        if (token.getSimbolo().equals("sidentificador")) {
            analisa_atrib_chprocedimento();
        } else if (token.getSimbolo().equals("sse")) {
            analisa_se();
        } else if (token.getSimbolo().equals("senquanto")) {
            analisa_enquanto();
        } else if (token.getSimbolo().equals("sleia")) {
            analisa_leia();
        } else if (token.getSimbolo().equals("sescreva")) {
            analisa_escreva();
        } else {
            analisa_comandos();
        }

    }//OK

    private void analisa_atrib_chprocedimento() throws Exception {//NAO TERMINADO
        String tipo_da_expressao, tipo_do_recebedor;
        Token token_recebedor = token;

        /**
         * **SEMANTICO***
         */
        if (tabelaSimbolos.pesquisa_tabela(token.getLexema()).getNome().equals("naoEncontrado")) {//identificador nao declarado
            erro_texto = token.getLexema() + "Procedimento nao declarado";
            erro_tipo = "Semantico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
        token = lexico.pegarToken();

        if (token.getSimbolo().equals("satribuicao")) {
            
            tipo_da_expressao = this.analisa_atribuicao();
            //achou a declaracao do procedimento, entao setar true para que isso seja verdade
            tabelaSimbolos.pesquisa_declvar_tabela(token_recebedor.getLexema()).setInicializado(true);
            //trechos de geracao de codigo aqui
            this.gera(0, "STR", tabelaSimbolos.pesquisa_declvarfunc_tabela(token_recebedor.getLexema()).getEndereco(), "");

            //é necessario ainda pegar o retorno da funcao analisa_atribuicao() para verificar se os tipos sao compativeis 
            tipo_do_recebedor = tabelaSimbolos.pesquisa_tipo_recebe_Expressao(token_recebedor.getLexema());
            
            if (!tipo_da_expressao.equals(tipo_do_recebedor)) {//incompatibilidade de tipos
                if (tipo_do_recebedor.equals("naoEncontrado")) {//DANDO ERRO AQUI
                    erro_texto = "Identificador " + token_recebedor.getLexema() + " nao declarado";
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                } else {
                    erro_texto = "Tipos nao compativeis";
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }

            }
        } else {
            if (tabelaSimbolos.pesquisa_declproc_tabela(token_recebedor.getLexema()).getNome().equals("naoEncontrado")) {
                erro_texto = token_recebedor.getLexema() + "Procedimento nao declarado";
                erro_tipo = "Semantico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
            this.gera(0, "CALL", "L" + tabelaSimbolos.pesquisa_declproc_tabela(token_recebedor.getLexema()).getLabel(), "");
            this.chamada_procedimento();
        }
    }//OK

    private void analisa_leia() throws Exception {
        String endereco = new String();
        String var = new String();

        token = lexico.pegarToken();
        if (token.getSimbolo().equals("sabre_parenteses")) {
            token = lexico.pegarToken();
            if (token.getSimbolo().equals("sidentificador")) {
                if (!tabelaSimbolos.pesquisa_declvar_tabela(token.getLexema()).getNome().equals("naoEncontrado")) {
                    tabelaSimbolos.pesquisa_declvar_tabela(token.getLexema()).setInicializado(true);
                    var = token.getLexema();
                    token = lexico.pegarToken();


                    if (token.getSimbolo().equals("sfecha_parenteses")) {
                        
                        this.gera(0, "RD", "", "");
                        /**
                         * **SEMANTICO***
                         */
                        endereco = tabelaSimbolos.pesquisa_tabela(var).getEndereco();
                        //trechos da geracao de codigo aqui
                        this.gera(0, "STR", endereco, "");
                        token = lexico.pegarToken();
                    } else {

                        erro_texto = "Esperado caractere ')'";
                        erro_tipo = "Sintatico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }
                } else {
                    /**
                     * **SEMANTICO***
                     */
                    erro_texto = token.getLexema() + " Variavel nao declarada";
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }
            } else {
                erro_texto = "Esperado identificador";
                erro_tipo = "Sintatico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
        } else {
            erro_texto = "Esperado caractere '('";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
    }//OK

    private void analisa_escreva() throws Exception {
        String endereco = new String();
        String var = new String();

        token = lexico.pegarToken();

        if (token.getSimbolo().equals("sabre_parenteses")) {
            token = lexico.pegarToken();
            if (token.getSimbolo().equals("sidentificador")) {

                if (!tabelaSimbolos.pesquisa_declvarfunc_tabela(token.getLexema()).getNome().equals("naoEncontrado")) {

                    //if (tabelaSimbolos.pesquisa_declvarfunc_tabela(token.getLexema()).getTipo().equals("inteiro")
                      //      || tabelaSimbolos.pesquisa_declvarfunc_tabela(token.getLexema()).getTipo().equals("booleano")) {//verifica se a variavel ja esta tipada
                    if(tabelaSimbolos.pesquisa_declvarfunc_tabela(token.getLexema()).getTipo().equals("inteiro")){
                             
                        if (!tabelaSimbolos.pesquisa_declvar_tabela(token.getLexema()).isInicializado()) {
                                erro_texto = "Variavel" + token.getLexema() + "nao inicializada"; 
                                erro_tipo = "Semantico";
                                comp.setErro(lexico, erro_tipo, erro_texto);
                            }

                    }else{
                        if(tabelaSimbolos.pesquisa_declvarfunc_tabela(token.getLexema()).getTipo().equals("booleano")){
                            erro_texto = "Não é permitido escrever variavel booleana";
                            erro_tipo = "Semantico";
                            comp.setErro(lexico, erro_tipo, erro_texto);
                        }
                        
                    }
                    var = token.getLexema();
                    token = lexico.pegarToken();
                    if (token.getSimbolo().equals("sfecha_parenteses")) {
                        //só pode escrever uma funcao inteira ou variavel inteira ****TESTAR*****                         
                        if (tabelaSimbolos.pesquisa_declvarfunc_tabela(var).getTipo().equals("funcaoInteiro")) {
                            this.gera(0, "CALL", "L" + tabelaSimbolos.pesquisa_declvarfunc_tabela(var).getLabel(), "");
                        }
                        else{
                            if(tabelaSimbolos.pesquisa_declvarfunc_tabela(var).getTipo().equals("funcaoBooleano")){
                                erro_texto = "Não é permitido escrever uma funcao Booleana";
                                erro_tipo = "Semantico";
                                comp.setErro(lexico, erro_tipo, erro_texto);
                            }
                        }
                        endereco = tabelaSimbolos.pesquisa_tabela(var).getEndereco();
                        this.gera(0, "LDV", endereco, "");
                        this.gera(0, "PRN", "", "");
                        token = lexico.pegarToken();
                    } else {
                        erro_texto = "Esperado caracetere ')'";
                        erro_tipo = "Sintatico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }

                } else {
                    erro_texto = token.getLexema() + " Variavel nao declarada";
                    erro_tipo = "Semantico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }

            } else {
                erro_texto = "Esperado 'identificador'";
                erro_tipo = "Sintatico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
        } else {
            erro_texto = "Esperado caractere '('";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
    }//OK

    private void analisa_enquanto() throws Exception {
        int rot1, rot2;
        rot1 = this.rotulo;
        this.gera(rot1, "NULL", "", "");
        this.rotulo++;

        token = lexico.pegarToken();
        
        expressaoOrdem.removeAllElements();
        
        this.analisa_expressao();
        if (!this.verificaTipo(this.posOrdem(expressaoOrdem)).equals("booleano")) {
            erro_texto = "Variavel, funcao ou expressao nao booleana para o comando 'enquanto'";
            erro_tipo = "Semantico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
        if (token.getSimbolo().equals("sfaca")) {
           
            rot2 = this.rotulo;
            this.gera(0, "JMPF", "L" + String.valueOf(rot2), "");
            this.rotulo++;
            
            token = lexico.pegarToken();
            
            analisa_comando_simples();
            
            this.gera(0, "JMP", "L"+String.valueOf(rot1), "");
            this.gera(rot2, "NULL", "", "");

        } else {
            erro_texto = "Esperado palavra reservada 'faca'";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
    }//OK

    private void analisa_se() throws Exception {
        int rot1 = rotulo;
        rotulo++;
        int rot2 = rotulo;
        rotulo++;
        expressaoOrdem.removeAllElements();
        
        token = lexico.pegarToken();
        this.analisa_expressao();
        
        if(!this.verificaTipo(this.posOrdem(expressaoOrdem)).equals("booleano")){
              erro_texto = "Expressão não booleana para comando Se";
              erro_tipo = "Semantico";
              comp.setErro(lexico, erro_tipo, erro_texto);
        }
          
        
        if (token.getSimbolo().equals("sentao")) {
            //Pula para o else caso a condicao seja falsa
            this.gera(0, "JMPF", "L"+String.valueOf(rot1), "");
            
            token = lexico.pegarToken();
            analisa_comando_simples();

            this.gera(0, "JMP", "L" + String.valueOf(rot2), "");
            this.gera(rot1, "NULL", "", "");
            if (token.getSimbolo().equals("ssenao")) {
                
                
                //Pula o else se executou o if
                //this.gera(0, "JMP", "L" + String.valueOf(rot2), "");
                //gerar o rotulo
                //this.gera(rot1, "NULL", "", "");
            
                token = lexico.pegarToken();
                analisa_comando_simples();
            }
            this.gera(rot2, "NULL", "", "");
        } else {
            erro_texto = "Esperado palavra reservada 'entao'";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }
    }//OK

    private void analisa_expressao() throws Exception {//nao possui nada relacionado a tabela de simbolos, soh a geracao
        analisa_expressao_simples();
        if (token.getSimbolo().equals("smaior") || token.getSimbolo().equals("smaiorig") || token.getSimbolo().equals("sig") || token.getSimbolo().equals("smenor") || token.getSimbolo().equals("smenorig") || token.getSimbolo().equals("sdif")) {
            expressaoOrdem.add(token);
            token = lexico.pegarToken();
            analisa_expressao_simples();
        }
    }//OK

    private void analisa_expressao_simples() throws Exception {//nao possui nada relacionado a tabela de simbolos, soh a geracao
        if (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos")) {
            expressaoOrdem.add(token);
            token = lexico.pegarToken();
        }

        this.analisa_termo();

        while (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos") || token.getSimbolo().equals("sou")) {
            expressaoOrdem.add(token);
            token = lexico.pegarToken();
            this.analisa_termo();

        }
    }//OK

    private void analisa_termo() throws Exception {//nao possui nada relacionado a tabela de simbolos, soh a geracao
        this.analisa_fator();

        while (token.getSimbolo().equals("smult") || token.getSimbolo().equals("sdiv") || token.getSimbolo().equals("se")) {
            expressaoOrdem.add(token);
            token = lexico.pegarToken();
            this.analisa_fator();
        }

    }//OK

    private void analisa_fator() throws Exception {

        if (token.getSimbolo().equals("sidentificador"))  //Variavel ou funcao
        {  
     
            /**
             * **SEMANTICO***
             */
            Simbolos simbolo = tabelaSimbolos.pesquisa_tabela(token.getLexema());
            if (simbolo.getNome().equals("naoEncontrado")) {
                erro_texto = token.getLexema() + "não foi declarado";
                erro_tipo = "Semantico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            } else {
                if (simbolo.getTipo().equals("funcaoInteiro")
                        ||simbolo.getTipo().equals("funcaoBooleano") ) {
                    analisa_chamada_funcao();

                } else {//é variavel
                    if (!tabelaSimbolos.pesquisa_declvar_tabela(token.getLexema()).isInicializado()) {//verifica se a variavel foi inicializada
                        erro_texto = "Variavel" +token.getLexema()+ "nao Inicializada";
                        erro_tipo = "Semantico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }
                    
                    expressaoOrdem.add(token);
                    token = lexico.pegarToken();
                }

            }
        } else {
            if (token.getSimbolo().equals("snumero")) {
                expressaoOrdem.add(token);
                token = lexico.pegarToken();
            } else if (token.getSimbolo().equals("snao")) {
                expressaoOrdem.add(token);
                token = lexico.pegarToken();
                this.analisa_fator();

            } else if (token.getSimbolo().equals("sabre_parenteses")) {
                expressaoOrdem.add(token);
                token = lexico.pegarToken();
                analisa_expressao();

                if (token.getSimbolo().equals("sfecha_parenteses")) {
                    expressaoOrdem.add(token);
                    token = lexico.pegarToken();
                } else {

                    erro_texto = "Esperado caractere ')'";
                    erro_tipo = "Sintatico";
                    comp.setErro(lexico, erro_tipo, erro_texto);
                }

            } else {
                if (token.getLexema().equals("verdadeiro") || token.getLexema().equals("falso")) {
                    expressaoOrdem.add(token);
                    token = lexico.pegarToken();
                } else {
                    erro_texto = "Fator Invalido";
                    erro_tipo = "Sintatico";
                    comp.setErro(lexico, erro_tipo, erro_texto);

                }
            }
        }
    }//OK

    private String analisa_atribuicao() throws Exception {//nao possui nada relacionado a tabela de simbolos, soh a geracao
        token = lexico.pegarToken();
        //aqui sera necessario gerar a expressao pos-ordem
        expressaoOrdem.removeAllElements();//limpa o vetor para uma nova atribuicao
        this.analisa_expressao();

        //aqui é necessario retornar o tipo
        return this.verificaTipo(this.posOrdem(expressaoOrdem));
    }//OK

    private void chamada_procedimento() throws Exception {   //talvez esse if nao seja necessario.
        if (!token.getSimbolo().equals("sponto_virgula") && !token.getSimbolo().equals("ssenao") && !token.getSimbolo().equals("sfim")) {
            erro_texto = "Chamada de procedimento invalida";
            erro_tipo = "Sintatico";
            comp.setErro(lexico, erro_tipo, erro_texto);
        }

    }//OK

    private void analisa_chamada_funcao() throws Exception {
        String endereco = new String();
        
        if (token.getSimbolo().equals("sidentificador")) {
            /**
             * **SEMANTICO***
             */
            if (tabelaSimbolos.pesquisa_declfunc_tabela(token.getLexema()).getNome().equals("naoEncontrado")) {//verifica se a funcao foi declarada
                erro_texto = "Funcao" + token.getLexema() + "nao declarada";
                erro_tipo = "Semantico";
                comp.setErro(lexico, erro_tipo, erro_texto);
            }
            endereco = tabelaSimbolos.pesquisa_declfunc_tabela(token.getLexema()).getLabel();
            this.gera(0, "CALL", "L"+endereco, "");

            expressaoOrdem.add(token);
            token = lexico.pegarToken();
        }
    }//OK

    public void gera(int rotulo, String comando, String argumento1, String argumento2) {
        if (rotulo == 0) {
            if(comando.contentEquals("ALLOC")) {
                this.cod_assembly.add("     " + comando + "     " + argumento1 + "      " + argumento2 + "\n");
            }
            else
            {
                this.cod_assembly.add("     " + comando + "    " + argumento1 + "      " + argumento2 + "\n");
            }
        } else {
            this.cod_assembly.add("L" + rotulo + "   " + comando + "      " + argumento1 + "      " + argumento2 + "\n");
        }
    }

    public void geraExpressao(Vector<Token> posOrdem) {
        for (Iterator<Token> it = posOrdem.iterator(); it.hasNext();) {
            Token exp = it.next();

            if (exp.getSimbolo().equals("snumero")) {
                this.gera(0, "LDC", exp.getLexema(), "");
            } else if (exp.getSimbolo().equals("smaior")) {
                this.gera(0, "CMA", "", "");
            } else if (exp.getSimbolo().equals("smaiorig")) {
                this.gera(0, "CMAQ", "", "");
            } else if (exp.getSimbolo().equals("sig")) {
                this.gera(0, "CEQ", "", "");
            } else if (exp.getSimbolo().equals("smenor")) {
                this.gera(0, "CME", "", "");
            } else if (exp.getSimbolo().equals("smenorig")) {
                this.gera(0, "CMEQ", "", "");
            } else if (exp.getSimbolo().equals("sdif")) {
                this.gera(0, "CDIF", "", "");
            } else if (exp.getSimbolo().equals("smais")) {
                this.gera(0, "ADD", "", "");
            } else if (exp.getSimbolo().equals("smenos")) {
                this.gera(0, "SUB", "", "");
            } else if (exp.getSimbolo().equals("sdiv")) {
                this.gera(0, "DIVI", "", "");
            } else if (exp.getSimbolo().equals("smult")) {
                this.gera(0, "MULT", "", "");
            } else if (exp.getSimbolo().equals("se")) {
                this.gera(0, "AND", "", "");
            } else if (exp.getSimbolo().equals("sou")) {
                this.gera(0, "OR", "", "");
            } else if (exp.getSimbolo().equals("snao")) {
                this.gera(0, "NEG", "", "");
            } else if (exp.getSimbolo().equals("sverdadeiro")) {
                this.gera(0, "LDC", "1", "");
            } else if (exp.getSimbolo().equals("sfalso")) {
                this.gera(0, "LDC", "0", "");
            }else if(exp.getSimbolo().equals("smaisu")){
                //nao gera nada
            }else if(exp.getSimbolo().equals("smenosu")){
                this.gera(0,"INV","","");
            }
            else if (exp.getSimbolo().equals("sidentificador")) {
                this.gera(0, "LDV", tabelaSimbolos.pesquisa_declvarfunc_tabela(exp.getLexema()).getEndereco(), "");
            }
        }
    }


    public boolean prioridade(Token a, Token b) {
        Vector<String> l1 = new Vector<String>();
        Vector<String> l2 = new Vector<String>();
        Vector<String> l3 = new Vector<String>();
        Vector<String> l4 = new Vector<String>();
        Vector<String> l5 = new Vector<String>();
        Vector<String> l6 = new Vector<String>();

        int na = 0, nb = 0;

        if (a.getSimbolo().equals("sabre_parenteses")) {
            return false;
        } else if (b.getSimbolo().equals("sabre_parenteses")) {
            return true;
        }

        l1.add("smaisu");
        l1.add("smenosu");
        l1.add("snao");

        l2.add("smult");
        l2.add("sdiv");

        l3.add("smais");
        l3.add("smenos");

        l4.add("smaior");
        l4.add("smaiorig");
        l4.add("sig");
        l4.add("smenor");
        l4.add("smenorig");
        l4.add("sdif");

        l5.add("se");

        l6.add("sou");

        if (l1.contains(a.getSimbolo())) {
            na = 1;
        } else if (l2.contains(a.getSimbolo())) {
            na = 2;
        } else if (l3.contains(a.getSimbolo())) {
            na = 3;
        } else if (l4.contains(a.getSimbolo())) {
            na = 4;
        } else if (l5.contains(a.getSimbolo())) {
            na = 5;
        } else if (l6.contains(a.getSimbolo())) {
            na = 6;
        }

        if (l1.contains(b.getSimbolo())) {
            nb = 1;
        } else if (l2.contains(b.getSimbolo())) {
            nb = 2;
        } else if (l3.contains(b.getSimbolo())) {
            nb = 3;
        } else if (l4.contains(b.getSimbolo())) {
            nb = 4;
        } else if (l5.contains(b.getSimbolo())) {
            nb = 5;
        } else if (l6.contains(b.getSimbolo())) {
            nb = 6;
        }

        return (na < nb); //se a eh mais prioritario ou igual a b, retorna true

    }

   
    public String verificaTipo(Vector<Token> posOrdem) throws Exception {
{
        
        
        Vector<Token> laux = new Vector<Token>();
        
        Token tk;
        
        //lista de operadores
        Vector<String> l = new Vector();
        l.add("smaisu");
        l.add("smenosu");
        l.add("snao");
        l.add("smult");
        l.add("sdiv");
        l.add("smais");
        l.add("smenos");
        l.add("smaior");
        l.add("smaiorig");
        l.add("sig");
        l.add("smenor");
        l.add("smenorig");
        l.add("sdif");
        l.add("se");
        l.add("sou");
        
        //lista de operadores relacionais
        Vector<String> lintint = new Vector<String>();
        lintint.add("smult");
        lintint.add("sdiv");
        lintint.add("smais");
        lintint.add("smenos");
        
        
        //lista de operadores aritmeticos
        Vector<String> lintboo = new Vector<String>();
        lintboo.add("smaior");
        lintboo.add("smaiorig");
        lintboo.add("sig");
        lintboo.add("smenor");
        lintboo.add("smenorig");
        lintboo.add("sdif");
        
        
        //lista de operadores aritmeticos
        Vector<String> lbooboo = new Vector<String>();
        lbooboo.add("se");
        lbooboo.add("sou");
        
        // troca operandos por tipos primarios
        for (int i = 0; i < posOrdem.size(); i++) {

            tk = posOrdem.get(i);
            
            if (tk.getSimbolo().equals("snumero")) { // token é numero
                posOrdem.set(i, new Token("inteiro", ""));
                
            } else if (tabelaSimbolos.pesquisa_tipo(posOrdem.get(i).getLexema()).equals("inteiro") ) { // token é variavel inteira
                posOrdem.set(i, new Token("inteiro", ""));

            } else if (tabelaSimbolos.pesquisa_tipo(posOrdem.get(i).getLexema()).equals("funcaoInteiro") ) { // token é funcao inteira
                posOrdem.set(i, new Token("inteiro", ""));
                
            } else if (tabelaSimbolos.pesquisa_tipo(posOrdem.get(i).getLexema()).equals("booleano") ) { // token é variavel booleana
                posOrdem.set(i, new Token("booleano", ""));
                
            } else if (tabelaSimbolos.pesquisa_tipo(posOrdem.get(i).getLexema()).equals("funcaoBooleano") ) { // token é funcao booleana
                posOrdem.set(i, new Token("booleano", ""));
                
            } else if (tk.getSimbolo().equals("sverdadeiro") || tk.getSimbolo().equals("sfalso")) { // token é booleano
                posOrdem.set(i, new Token("booleano", ""));
                
            }
        }

        // verifica tipos
        for (int i = 0; i < posOrdem.size(); i++) {
            tk = posOrdem.get(i);


            if (l.contains(tk.getSimbolo())) { //é operador

                if (tk.getSimbolo().equals("smaisu") || tk.getSimbolo().equals("smenosu")) { //é unário + ou -

                    if(posOrdem.get(i-1).getSimbolo().equals("inteiro")) {
                        
                        posOrdem.removeElementAt(i);
                        i--; // decrementa i para compensar o elemento removido do vetor

                    } else {
                        //erro
                        
                        erro_texto = "Expressão inválida, tipos incompatíveis - unário '" + tk.getLexema() +"' requer inteiro";
                        erro_tipo = "Semântico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                    }
                    
                } else if (tk.getSimbolo().equals("snao")) { //é unário NAO
                    
                    if(posOrdem.get(i-1).getSimbolo().equals("booleano")) {
                        
                        posOrdem.removeElementAt(i);
                        i--; // decrementa i para compensar o elemento removido do vetor

                    } else {
                        //erro
                        
                        erro_texto = "Expressão inválida, tipos incompatíveis - unário '"+ tk.getLexema() +"' requer booleano";
                        erro_tipo = "Semântico";
                        comp.setErro(lexico, erro_tipo, erro_texto);
                        
                   //     System.out.println("Expressão inválida, tipos incompatíveis - unário '"+ token.getLexema() +"' requer booleano");
                        
                    }
                    
                } else { // não é unario
                    
                    if (lintint.contains(tk.getSimbolo())) { // operacao dois operandos inteiros, resultado inteiro
                        
                        if (posOrdem.get(i-1).getSimbolo().equals("inteiro") && posOrdem.get(i-2).getSimbolo().equals("inteiro")) {
                            
                            posOrdem.removeElementAt(i);
                            posOrdem.removeElementAt(i-1);
                            i-=2;
                            
                        } else {
                            //erro
                            erro_texto = "Expressão inválida, tipos incompatíveis - operador '"+ tk.getLexema() +"' requer dois inteiros";
                            erro_tipo = "Semântico";
                            comp.setErro(lexico, erro_tipo, erro_texto);
                            //System.out.println("Expressão inválida, tipos incompatíveis - operador '"+ token.getLexema() +"' requer dois inteiros");
                            
                        }
                        
                    } else if (lintboo.contains(tk.getSimbolo())) { // operacao dois operandos inteiros, resultado booleano
                        
                        if (posOrdem.get(i-1).getSimbolo().equals("inteiro") && posOrdem.get(i-2).getSimbolo().equals("inteiro")) {
                            
                            posOrdem.removeElementAt(i);
                            posOrdem.removeElementAt(i-1);
                            posOrdem.set(i-2, new Token("booleano", ""));
                            i-=2;
                            
                        } else {
                            //erro
                            
                            erro_texto = "Expressão inválida, tipos incompatíveis - operador '"+ tk.getLexema() +"' requer dois inteiros";
                            erro_tipo = "Semântico";
                            comp.setErro(lexico, erro_tipo, erro_texto);

                            //System.out.println("Expressão inválida, tipos incompatíveis - operador '"+ token.getLexema() +"' requer dois inteiros");
                            
                        }
                        
                    } else if (lbooboo.contains(tk.getSimbolo())) { // operacao dois operandos booleanos, resultado booleano
                        
                        if (posOrdem.get(i-1).getSimbolo().equals("booleano") && posOrdem.get(i-2).getSimbolo().equals("booleano")) {
                            
                            posOrdem.removeElementAt(i);
                            posOrdem.removeElementAt(i-1);
                            i-=2;
                            
                        } else {
                            //erro
                            
                            erro_texto = "Expressão inválida, tipos incompatíveis - operador '"+ tk.getLexema() +"' requer dois booleanos";
                            erro_tipo = "Semântico";
                            comp.setErro(lexico, erro_tipo, erro_texto);
                            
          //                  System.out.println("Expressão inválida, tipos incompatíveis - operador '"+ token.getLexema() +"' requer dois booleanos");
                            
                        }   
                    }
                }
            } 
        }
        
        return posOrdem.get(0).getSimbolo();
        
    }
        
       
       
      
    }

     public Vector<Token> posOrdem (Vector<Token> ordem) {
        
        Vector<Token> posOrdem = new Vector<Token>();
        Vector<Token> pilha = new Vector<Token>();
        Token token = new Token();
        
        for (int i = 0; i < ordem.size(); i++) {
            token = ordem.get(i);
            
            if (i == 0) {
                if (token.getSimbolo().equals("smais")) {
                    ordem.get(i).setSimbolo("smaisu");
                } else if (token.getSimbolo().equals("smenos")) {
                    ordem.get(i).setSimbolo("smenosu");
                }
            } else if (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos") ) {
                
                if (ordem.get(i - 1).getSimbolo().equals("sabre_parenteses")
                || ordem.get(i - 1).getSimbolo().equals("sdif")
                || ordem.get(i - 1).getSimbolo().equals("sig")
                || ordem.get(i - 1).getSimbolo().equals("smenor")
                || ordem.get(i - 1).getSimbolo().equals("smenorig")
                || ordem.get(i - 1).getSimbolo().equals("smaior")
                || ordem.get(i - 1).getSimbolo().equals("smaiorig")
                ) {
                    
                    if (token.getSimbolo().equals("smais")) {
                        ordem.get(i).setSimbolo("smaisu");
                    } else if (token.getSimbolo().equals("smenos")) {
                        ordem.get(i).setSimbolo("smenosu");
                    }
                }
            }
        }
        
        
        for (int i = 0; i < ordem.size(); i++) {
            token = ordem.get(i);
            
            if(token.getSimbolo().equals("sabre_parenteses")) { //se o token encontrado eh abre parenteses entao empilha token
                pilha.add(token);
            } else if(token.getSimbolo().equals("sfecha_parenteses")) {//se o token encontrado eh fecha parenteses entao desempilha topo da pilha ateh que abre parenteses seja encontrado e o remove
                
                while(!pilha.lastElement().getSimbolo().equals("sabre_parenteses")) {
                    posOrdem.add(pilha.lastElement());
                    pilha.remove(pilha.lastElement());
                }
                
                pilha.remove(pilha.lastElement()); //remove abre parenteses
                
            } else if (token.getSimbolo().equals("sidentificador") || token.getSimbolo().equals("snumero") || token.getSimbolo().equals("sfalso") || token.getSimbolo().equals("sverdadeiro")) { //se token encontrado eh operando entao imprime
                posOrdem.add(token);
            } else if(pilha.size() == 0) { //se pilha esta vazia entao empilha token
                pilha.add(token);
             } else if ( this.prioridade(pilha.lastElement(),token) ) { //se topo da pilha eh mais prioritario que o token encontrado entao desempilha topo ate que o topo seja menos prioritario e empilha o token encontrado
                
                do {
                    posOrdem.add(pilha.lastElement());
                    pilha.remove(pilha.lastElement());
                } while(!pilha.isEmpty() && prioridade(pilha.lastElement(),token));
                
                pilha.add(token);
            } else { //se topo da pilha eh menos prioritario que o token encontrado entao empilha o token
                pilha.add(token);
            }
        }
        
        while(!pilha.isEmpty()) {
            posOrdem.add(pilha.lastElement());
            pilha.remove(pilha.lastElement());
        }
        
        System.out.print("Expressao em ordem: ");
        for(int i = 0; i < ordem.size(); i++)
            System.out.print(ordem.get(i).getLexema());
        
        System.out.println();
        
        System.out.print("Expressao pos ordem: ");
        for(int i = 0; i < posOrdem.size(); i++)
            System.out.print(posOrdem.get(i).getLexema());
        
        System.out.println();
        
// Faz toda a geraçao da expressao
        this.geraExpressao(posOrdem);
        
        return posOrdem;
    }
}