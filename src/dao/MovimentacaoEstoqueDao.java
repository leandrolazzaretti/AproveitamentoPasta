/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.MovimentacaoEstoqueDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 */
public class MovimentacaoEstoqueDao {

    Connection conexao = null;

    public MovimentacaoEstoqueDao() {
        this.conexao = ModuloConexao.conector();
    }

    //da entrada no estoque de pasta
    public void entradaPasta(MovimentacaoEstoqueDto movEstoqueDto) {
        String sql = "insert into tbEstoquePasta(codigoReceita,UM,quantidade,data)"
                + " values(?,?,?,?)";
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, movEstoqueDto.getCodigoReceita());
            pst.setString(2, movEstoqueDto.getUM());
            pst.setDouble(3, movEstoqueDto.getQuantidade());
            pst.setString(4, movEstoqueDto.getData());
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Entrada de pasta efetuada com sucesso.");
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // da saída na pasta desejada 
    public void saidaPasta(double quantidade, int codigo) {
        double total = quantidade;

        String sql = "select  ep.quantidade, ep.ID from tbEstoquePasta as ep"
                + " inner join tbreceita as r on ep.codigoReceita = r.codigorec"
                + " where ep.codigoReceita = '" + codigo + "'"
                + " order by ep.data asc";
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (total > 0) {
                rs.next();
                total -= rs.getDouble(1);
                if (total >= 0) {
                    updateSaidaPasta(rs.getInt(2), 0);
                } else {
                    total += rs.getDouble(1);
                    updateSaidaPasta(rs.getInt(2), rs.getDouble(1) - total);
                    total = 0;
                }
            }
            JOptionPane.showMessageDialog(null, "Saída de pasta realizada com sucesso.");
            pst.close();       
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }  
    
     private void updateSaidaPasta(int ID, double quantidade) {
        String sql = "update tbEstoquePasta set quantidade ='" + quantidade + "' where ID ='" + ID + "'";
        PreparedStatement pst;
        try {
            pst = conexao.prepareStatement(sql);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // busca o codigo da tabela de receita através da descrição
    public int buscaCodigoReceita(String descricao) {
        String sql = "select codigorec from tbreceita where descricao =?";
        int codigo = 0;
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            ResultSet rs = pst.executeQuery();

            codigo = Integer.parseInt(rs.getString(1));
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return codigo;
    }
}
