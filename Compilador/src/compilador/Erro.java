/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador;

import javax.swing.JButton;

/**
 *
 * @author jp
 */
class Erro {
    
    JButton jButton1 = new JButton();

    void setErro(AnalisadorLexico lexico, String erro_tipo, String erro_texto) throws Exception {
         lexico.output.addElement("ERRO " + erro_tipo + " linha: "+ lexico.numero_linha + " " + erro_texto +"\n" );
         lexico.jList1.ensureIndexIsVisible(lexico.numero_linha-1);
         lexico.jList1.setSelectedIndex(lexico.numero_linha-1);
         this.jButton1.setEnabled(false);
         throw new Exception();
    }   



    void setBotao(JButton botao) {
        this.jButton1 = botao;
    }
}