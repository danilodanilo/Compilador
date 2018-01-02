/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author jp
 */
class Arquivo {
    private String str;
    private BufferedReader buffer2;

    Arquivo() {
//        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    public void EscritaArquivo(Vector<Token> tokens)
    {
                      
                String simbolo = "";
                String lexema = "";
                
                File file = new File("tokens.txt");
                File arquivo;
                
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
                }
                for(int i=0; i<tokens.size(); i++){
                    simbolo = tokens.get(i).getSimbolo();
                    lexema = tokens.get(i).getLexema();
                    String tab = "\t";
                    String pular_linha = System.getProperty("line.separator");
                     try {
                        fos.write(lexema.toString().getBytes());//escreve lexema
                    } catch (IOException ex) {
                        Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        fos.write(tab.toString().getBytes());//escreve tab
                    } catch (IOException ex) {
                        Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     try {
                        fos.write(simbolo.toString().getBytes());//escreve simbolo
                    } catch (IOException ex) {
                        Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                   
                    try {
                        fos.write(pular_linha.toString().getBytes());
                    } catch (IOException ex) {
                        Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
                    }
                     
                }
                
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
                }
    }
    
    public String LeituraArquivo(File arquivo, DefaultListModel lista, JList jList1) throws FileNotFoundException, IOException
    {
        
        String codigoFonte = "";
        BufferedReader buffer = null;
        boolean leitura = false;
        
        lista.removeAllElements();
        jList1.removeAll();
        jList1.setModel(lista);
        
        buffer = new BufferedReader(new FileReader(arquivo));
      
        leitura = buffer.ready();
        
        while(leitura)
        {
           // codigoFonte = codigoFonte + buffer.readLine() + "\n";
            str = buffer.readLine();
            codigoFonte = codigoFonte + str + "\n";
            leitura = buffer.ready();
            lista.addElement(str);
        }
    
        buffer.close();
        return codigoFonte;
        
    }
     public void geraArquivoObj (Vector<String> assembly) {
        //MODIFICAR ESSA FUNCAO PARA ESCREVER O ARQUIVO EM UM LUGAR ESPECIFICADO
        String s =  new String();
        
        for (int i=0; i < assembly.size(); i++) {
            s += assembly.get(i);
        }
        
        try {
            FileWriter writer = new FileWriter(new File("assembly.obj"),true);
            PrintWriter saida = new PrintWriter(writer);
            saida.print(s);
            saida.close();
            writer.close();
            System.out.println("Arquivo OBJ criado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}