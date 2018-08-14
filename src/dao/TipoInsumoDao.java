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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import telas.TelaOrdenacaoInsumo;

/**
 *
 * @author Leandro
 */
public class TipoInsumoDao {

    private Connection conexao = null;
    public static List<Integer> listaTipoInsumo = new ArrayList<>();

    public TipoInsumoDao() {
        this.conexao = ModuloConexao.conector();
    }

    //busca o codigo do tipo de insumo
    public int buscaCodTipoInsumo(String tipoInsumo) {
        int codigo = 0;
        String sql = "select codigo from tbTipoInsumo where nome=?";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, tipoInsumo);
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

    //Adiciona um novo tipo de insumo
    public void addTipoInsumo(String descricao) {
        String sql = "insert into tbTipoInsumo(nome) values(?)";

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
            JOptionPane.showMessageDialog(null, "Tipo de insumo já existe.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    //Seta o combobox tipo de pastas com os dados do banco
    public void setarComboBox(JComboBox cbox) {
        String sql = "select nome from tbTipoInsumo";

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

    //Remove um tipo de pasta no combobox
    public void removeTipoInsumo(String nome) {
        String sql = "delete from tbTipoInsumo where nome=?";

        PreparedStatement pst = null;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, nome);
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

    //conta a quantidade de tipo insumo 
    public boolean contTipoInsumo(int codigo) {
        String sql = "select count (codigoTipoInsumo) as total from tbinsumos where codigoTipoInsumo = '" + codigo + "'";
        int total = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total > 0;
    }

    //conta a quantidade de tipo insumo com base no nome do mesmo 
    public boolean contTipoInsumoNome(String nome) {
        String sql = "select count (nome) as total from tbTipoInsumo where nome = '" + nome + "'";
        int total = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total > 0;
    }

    // deleta o tipo de insumo selecionado
    public void deletarTipoInsumo() {
        String sql = "delete from tbTipoInsumo"
                + " where codigo =?";

        PreparedStatement pst;

        int setar = TelaOrdenacaoInsumo.tblOrdenacao.getSelectedRow();
        int ins = Integer.parseInt(TelaOrdenacaoInsumo.tblOrdenacao.getModel().getValueAt(setar, 0).toString());
        //System.out.println(ins);
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, ins);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void atualizarTipoInsumo(int codigo, String nome, int ordenacao) {
        String sql = "update tbTipoInsumo"
                + " set nome=?, ordenacao =?"
                + " where codigo =?";
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, nome);
            pst.setInt(2, ordenacao);
            pst.setInt(3, codigo);

            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Atualizado com sucesso.");
            }

            pst.close();
        } catch (SQLException e2) {
            JOptionPane.showMessageDialog(null, "Descrição já existe.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
