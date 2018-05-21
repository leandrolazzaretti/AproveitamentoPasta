/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 */
public class ModuloConexao {

  
    public static Connection conector(){
        Connection conexao = null;
        
        String url = "jdbc:sqlite:/Users/Leandro/Documents/NetBeansProjects/prjAproveitamentoPastas/DataBase/prjAproveitamento.db"; 
         String driver = "org.sqlite.JDBC";       
       
         try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url);
            //JOptionPane.showMessageDialog(null,"conexao ok");
            return conexao;

        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null,
                    "Ocorreu um erro de conexão. Verifique a Base de Dados indicada !" + "\n" + erro.getMessage(), "Conexão", 3);
            erro.printStackTrace();
            return null;
        }

    }

}
