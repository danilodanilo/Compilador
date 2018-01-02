/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package maquinavirtual;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
/**
 *
 * @author jp
 */
public class ExecutarInstrucoes 
{

    private int i = 0;
    private JButton botaoexec;
               
    private int s;
    int m[] = new int [200];


    public ExecutarInstrucoes(JButton j) 
    {
        botaoexec = j;
    }
    
    public int getS() 
    {
        return s;
    }

    public int [] getM() 
    {
        return m;
    }
    
    public void incrementaI()
    {
        i++;
    }

    public int getI()
    {
        return i;
    }

    public void executar(ArrayList<Pilha> pilha, String[][] tabelaCodigoMaquina, int numero_linha, JTextArea JTextArea2)
    {
             
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("LDC"))
        {
            //S:=s + 1 ; M [s]: = k
            s = s + 1;
            m[s] = Integer.parseInt(tabelaCodigoMaquina[numero_linha][2]);  
            incrementaI();
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("LDV"))
        {
            //S:=s + 1 ; M [s]: = k
            s = s + 1;
            m[s] = m[Integer.parseInt(tabelaCodigoMaquina[numero_linha][2])];      
            incrementaI();
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("ADD"))
        {
            //S:=s + 1 ; M [s]: = k
            m[s-1] = m[s-1] + m[s]; 
            s = s - 1;
            incrementaI();
        }
            
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("SUB"))
        {
            //S:=s + 1 ; M [s]: = k
            m[s-1] = m[s-1] - m[s]; 
            s = s - 1;
            incrementaI();
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("MULT"))
        {
            //S:=s + 1 ; M [s]: = k
            m[s-1] = m[s-1] * m[s]; 
            s = s - 1;
            incrementaI();
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("DIVI"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s] == 0){
                m[s-1] = 0;
            }
            else{
                m[s-1] = m[s-1] / m[s]; 
                s = s - 1;
            }
            incrementaI();
        }        
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("INV"))
        {
            //S:=s + 1 ; M [s]: = k
            m[s] = -m[s]; 
            incrementaI();
        }   
          
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("AND"))
        {
            //S:=s + 1 ; M [s]: = k     
            if(m[s-1] == 1 && m[s] == 1)
                m[s-1] = 1;
            else
                m[s-1] = 0;

            s = s - 1;
            incrementaI();
        }   

        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("OR"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s-1] == 1 || m[s] == 1)
                m[s-1] = 1;
            else
                m[s-1] = 0;

            s = s - 1;
            incrementaI();
        }   

        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("NEG"))
        {
            //S:=s + 1 ; M [s]: = k            
            m[s] = 1 - m[s];
            incrementaI();
        }

        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CME"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s-1] < m[s])
                m[s-1] = 1;
            else
                m[s-1] = 0 ;

            s = s - 1;
            incrementaI();
        }

        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CMA"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s-1] > m[s])
                m[s-1] = 1;
            else
                m[s-1] = 0 ;
            
            s = s - 1;
            incrementaI();
        }        
                
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CEQ"))
        {
            //S:=s + 1 ; M [s]: = k
            if (m[s-1] == m[s])
                m[s-1] = 1;
            else
                m[s-1] = 0 ;

            s = s - 1;
            incrementaI();
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CDIF"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s-1] != m[s])
                m[s-1] = 1;
            else
                m[s-1] = 0 ;

            s = s - 1;
            incrementaI();
        }        
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CMEQ"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s-1] <= m[s])
                m[s-1] = 1;
            else
                m[s-1] = 0 ;

            s = s - 1;
            incrementaI();
        }      
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CMAQ"))
        {
            //S:=s + 1 ; M [s]: = k
            if(m[s-1] >= m[s])
                m[s-1] = 1;
            else
                m[s-1] = 0;

            s = s - 1;
            incrementaI();
        }    
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("START"))
        {
            //S:=s + 1 ; M [s]: = k
            s = -1;
            incrementaI();
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("HLT"))
        {
            botaoexec.setEnabled(false);           
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("STR"))
        {
           
            m[Integer.parseInt(tabelaCodigoMaquina[numero_linha][2])] = m[s];
            s = s - 1;
            incrementaI();
        }     
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("JMP"))
        {
            //m[Integer.parseInt(tabelaCodigoMaquina[numero_linha][2])] = m[s];
            i = Integer.parseInt(tabelaCodigoMaquina[numero_linha][2]);  
        }
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("JMPF"))
        {
            if(m[s] == 0)
            {
                i =   Integer.parseInt(tabelaCodigoMaquina[numero_linha][2]);
            } 
            else
                incrementaI();
          
            s = s - 1;
        }        
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("NULL"))
        {
            incrementaI();
        }        
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("RD"))
        {
             //S:=s + 1; M[s]:= “próximo valor de entrada”. 
            s = s + 1;
            String valor_entrada = JOptionPane.showInputDialog(null, "Entrada", "Entre com um valor", JOptionPane.QUESTION_MESSAGE);
            m[s] = Integer.parseInt(valor_entrada);
            incrementaI();
        }           
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("PRN"))
        {
            
            JTextArea2.append(String.valueOf(m[s] +"\n"));
            s = s - 1;
            incrementaI();
        }   
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("ALLOC"))
        {
            int n = Integer.parseInt(tabelaCodigoMaquina[numero_linha][3]);
            int m1 = Integer.parseInt(tabelaCodigoMaquina[numero_linha][2]);
            for (int k = 0; k <= n - 1 ; k++)
            {
                s = s + 1;
                m[s] = m[m1 + k];
            }
             incrementaI();
        }   

        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("DALLOC"))
        {
            
            for (int k = Integer.parseInt(tabelaCodigoMaquina[numero_linha][3]) -1 ; k >= 0; k--)
            {
           
                m[Integer.parseInt(tabelaCodigoMaquina[numero_linha][2]) + k] = m[s];
                s = s - 1;
                
            }
            incrementaI();
            //System.out.println(m[s])
        }           
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("CALL"))
        {
            s = s + 1;
            m[s] = getI() + 1;
            i = Integer.parseInt(tabelaCodigoMaquina[numero_linha][2]);
            //System.out.println(m[s])
        }    
        
        if(tabelaCodigoMaquina[numero_linha][1].contentEquals("RETURN"))
        {
            i = m[s];
            s = s - 1;
        }
    }        
}