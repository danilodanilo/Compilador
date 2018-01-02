/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.util.Vector;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author jp
 */
class AnalisadorLexico {

    private String codigoFonte;
    public int numero_linha;
    public int numero_coluna;
    private String string;
    private int posicao;
    private Vector<Token> tokens;
    private char Char;
    boolean fim_arquivo;
    public DefaultListModel output = new DefaultListModel();
    private JList jList2 = new JList();
    public JList jList1 = new JList();
    int controle = 0;
    private String str;
    public int erro;
    //Token token = new Token();

    public AnalisadorLexico() {
        tokens = new Vector<Token>();
    }

    public void setPosicaoInicial() {
        this.posicao = 0;
    }
    
    public void setErro() {
        this.erro = 0;
    }

    
    public void setFimArquivo() {
        this.fim_arquivo = false;
    }

    public void setInicioArquivo() {
        this.numero_linha = 1;
        this.numero_coluna = 1;
    }

    public void setJlist(DefaultListModel output, JList jList1, JList jList2) {
        this.output = output;
        this.jList2 = jList2;
        this.jList1 = jList1;
        jList2.setModel(output);
    }

    public void setCodigoFonte(String str) {
        this.codigoFonte = str;
    }

    public String getCodigoFonte() {
        return codigoFonte;
    }
    

    private void lerChar() {
        Char = codigoFonte.charAt(posicao);
        posicao = posicao + 1;
        if (Char == '\n') {
            if (posicao < codigoFonte.length()) {
                this.numero_linha++;
                this.numero_coluna = 0;
                lerChar();
            } else 
            {
                Arquivo escrita = new Arquivo();
                escrita.EscritaArquivo(tokens);

            }
        }
    }

     public Token pegarToken() {
        
        Token tk = new Token();
        
        if (posicao == 0) {
            lerChar();
            this.numero_coluna++;
        }

        if (codigoFonte.length() > posicao) {

            while (codigoFonte.length() > posicao) {
                while ((Char == '{' || Char == ' ' || Char == '\t') && codigoFonte.length() > posicao) {
                    if (Char == '{') {
                        while (Char != '}' || Char == '\n') // LEMBRAR DO \n
                        {
                            this.numero_coluna++;
                            if (codigoFonte.length() <= posicao) {
                                //chegou no fim do arquivo e nao achou o fecha comentario '}'
                                this.output.addElement("Erro lexical: " + this.numero_linha + " Caractere } nao encontrado\n");
                                break;

                            } else {
                                lerChar();
                            }
                        }
                        if (codigoFonte.length() > posicao) {
                            lerChar();
                            this.numero_coluna++;
                        }

                    }
                   
                    while ((Char == ' ' || Char == '\t') && codigoFonte.length() > posicao) {
                        lerChar();
                        this.numero_coluna++;
                    }
                }
                if (codigoFonte.length() > posicao) {
                    tk = pegarString();
                    return tk;
                }
            }
        }
        tk.setLexema("fim_arquivo");
        tk.setSimbolo("sfim_arquivo");
        fim_arquivo = true;
        return tk;
       
    }

    public Token pegarString() {
         Token tk2 = new Token();
        if (Character.isDigit(Char)) { //trata digito

            String num = new String();
           // Token tk2 = new Token();

            num += Char;
            this.lerChar();
            this.numero_coluna++;

            while (Character.isDigit(Char)) {
                num += Char;
                this.lerChar();
                this.numero_coluna++;
            }
            tk2.setSimbolo("snumero");
            tk2.setLexema(num);
            tokens.add(tk2);
            return tk2;

        } else if (Character.isLetter(Char)) { //trata identificador e palavra reservada

            String strin = new String();
            Token tk = new Token();
            //A-Z, 0-9, a-z
            while ((Character.isLetterOrDigit(Char) || Char == '_') && controle == 0) {
                controle = 0;
                strin += Char;
                
                this.numero_coluna++;
                if (codigoFonte.charAt(posicao) == '\n') {//ALTERACAO AQUI PARA VERIFICAR SE O PROXIMO CARACTERE E \n
                    //se for barra \n, nao volta para o while
                    controle = 1;
                }
                this.lerChar();
            }
            controle = 0;
            tk.setLexema(strin);
            tk.setSimbolo(procuraReservada(strin));
            tokens.add(tk);
            return tk;

        } else if (Char == ':') { // tratar atribuição
            String operador = new String();
            //Token tk2 = new Token();
            operador += Char;

            this.lerChar();
            this.numero_coluna++;

            if (Char == '=') {
                operador += Char;
                tk2.setLexema(":=");
                tk2.setSimbolo("satribuicao");
                this.lerChar();
                this.numero_coluna++;
            } else {
                tk2.setLexema(String.valueOf(operador));
                tk2.setSimbolo("sdoispontos");
            }

            tokens.add(tk2);
            return tk2;

        } else if (Char == '+' || Char == '-' || Char == '*') { //tratar operador aritmetico
            char operador;
            Token tk = new Token();
            operador = Char;

            this.lerChar();
            tk.setLexema(String.valueOf(operador));

            if (operador == '+') {
                tk.setSimbolo("smais");
            } else if (operador == '-') {
                tk.setSimbolo("smenos");
            } else if (operador == '*') {
                tk.setSimbolo("smult");
            }
            tokens.add(tk);
            return tk;
        } else if (Char == '<' || Char == '>' || Char == '!' || Char == '=') { // tratar operador relacional
            String operador = new String();
            

            operador += Char;
            this.lerChar();
            this.numero_coluna++;

            if (operador.equals("=")) {
                tk2.setSimbolo("sig");
            } else if (operador.equals(">")) {
                if (Char == '=') {
                    operador += Char;
                    tk2.setSimbolo("smaiorig");
                    this.lerChar();
                    this.numero_coluna++;
                } else {
                    tk2.setSimbolo("smaior");
                }

            } else if (operador.equals("<") && Char != '>') {
                if (Char == '=') {
                    operador += Char;
                    tk2.setSimbolo("smenorig");
                    this.lerChar();
                    this.numero_coluna++;
                } else {
                    tk2.setSimbolo("smenor");
                }
            } else if (operador.equals("<") && Char == '>') {
                operador += Char;
                tk2.setSimbolo("sdif");
                this.lerChar();
                this.numero_coluna++;
            }
            tk2.setLexema(operador);
            this.tokens.add(tk2);
            return tk2;

        } else if (Char == ';' || Char == ',' || Char == ')' || Char == '(' || Char == '.') { // tratar pontuação
            char operador;
           // Token tk = new Token();
            operador = Char;

            if (operador == '.') {
                tk2.setSimbolo("sponto");
            } else if (operador == ',') {
                tk2.setSimbolo("svirgula");
            } else if (operador == ';') {
                tk2.setSimbolo("sponto_virgula");
            } else if (operador == '(') {
                tk2.setSimbolo("sabre_parenteses");
            } else {
                tk2.setSimbolo("sfecha_parenteses");
            }

            tk2.setLexema(String.valueOf(operador));
            this.tokens.add(tk2);
            this.lerChar();
            this.numero_coluna++;
            return tk2;

        } else { //trata erro

            //System.out.println("Erro aqui!");
            this.erro = 1;
            this.output.addElement("Erro Lexical: linha " + this.numero_linha + " coluna " + this.numero_coluna + " caractere invalido " + Char + "\n");
            this.numero_coluna++;
            tk2.setLexema(String.valueOf(Char));
            tk2.setSimbolo("erro_lexico");
            return tk2;
        }
    }

    private String procuraReservada(String str) {
        if (str.equals("programa")) {
            return "sprograma";
        } else if (str.equals("inicio")) {
            return "sinicio";
        } else if (str.equals("fim")) {
            return "sfim";
        } else if (str.equals("procedimento")) {
            return "sprocedimento";
        } else if (str.equals("funcao")) {
            return "sfuncao";
        } else if (str.equals("se")) {
            return "sse";
        } else if (str.equals("entao")) {
            return "sentao";
        } else if (str.equals("senao")) {
            return "ssenao";
        } else if (str.equals("enquanto")) {
            return "senquanto";
        } else if (str.equals("faca")) {
            return "sfaca";
        } else if (str.equals("escreva")) {
            return "sescreva";
        } else if (str.equals("leia")) {
            return "sleia";
        } else if (str.equals("var")) {
            return "svar";
        } else if (str.equals("inteiro")) {
            return "sinteiro";
        } else if (str.equals("booleano")) {
            return "sbooleano";
        } else if (str.equals("div")) {
            return "sdiv";
        } else if (str.equals("e")) {
            return "se";
        } else if (str.equals("ou")) {
            return "sou";
        } else if (str.equals("nao")) {
            return "snao";
        } else if (str.equals("verdadeiro")) {
            return "sverdadeiro";
        } else if (str.equals("falso")) {
            return "sfalso";
        } else { //nao é palavra reservada entao é um identificador qualquer
            return "sidentificador";
        }
    }
}
