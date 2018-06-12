/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.InsumoDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 */
public class InsumoDao {
    Connection conexao = null;
    
    public InsumoDao(){
        this.conexao = ModuloConexao.conector();
    }
    public void adicionarInsumos(InsumoDto insumo) {
        String sql = "insert into tbinsumos(codigo,descricao,UM,quantidade,preco) values(?,?,?,?,?)";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, insumo.getCodigo());
            pst.setString(2, insumo.getDescricao());
            pst.setString(3, insumo.getUm());
            pst.setDouble(4, insumo.getQuantidade());
            pst.setDouble(5, insumo.getPreco());
            //Atualiza a tabela insumos
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Insumo cadastrado com sucesso!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }
}
