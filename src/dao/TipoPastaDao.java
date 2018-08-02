/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 * 
 */

public class TipoPastaDao {

    private Connection conexao = null;

    public TipoPastaDao() {
        this.conexao = ModuloConexao.conector();
    }

    //Seta o combobox tipo de pastas com os dados do banco
    public void setarComboBox(JComboBox cbox) {
        String sql = "select descricao from tbTipoPasta";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                cbox.addItem(rs.getString(1));
            }
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Adiciona um novo tipo de pasta no combobox
    public void addComboBox(String descricao) {
        String sql = "insert into tbTipoPasta(descricao) values(?)";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Adicionado com sucesso.");
            }
            pst.close();
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(null, "Tipo de pasta já existe.");
            System.out.println(e2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    //Remove um tipo de pasta no combobox
    public void removeComboBox(String descricao) {
        String sql = "delete from tbTipoPasta where descricao=?";

        PreparedStatement pst = null;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, descricao);
            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Apagado com sucesso.");
            }
            pst.close();

        } catch (SQLException sqlException) {
            JOptionPane.showMessageDialog(null, sqlException);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public int buscaCodTipoPasta(String tipoPasta) {
        int codigo = 0;
        String sql = "select codigo from tbTipoPasta where descricao=?";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, tipoPasta);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                codigo = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            //pst.close();
        }
        return codigo;
    }

    public boolean contTipoPasta(int codigo) {
        String sql = "select count (codigoTipoPasta) as total from tbreceita where codigoTipoPasta = '" + codigo + "'";
        int total = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                total = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total > 0;
    }
}
