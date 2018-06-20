/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.ReceitaDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import telas.TelaCadReceita;
import telas.TelaMovimentacaoEstoque;

/**
 *
 * @author Leandro
 */
public class ReceitaDao {

    Connection conexao = null;

    public ReceitaDao() {
        this.conexao = ModuloConexao.conector();
    }

    public void adicionarReceita(ReceitaDto receita) {
        String sql = "insert into tbreceita(codigorec,descricao,pantone,codigoTipoPasta,datavencimento) values(?,?,?,?,?)";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, receita.getCodigo());
            pst.setString(2, receita.getDescricao());
            pst.setString(3, receita.getPantone());
            pst.setInt(4, receita.getTipo());
            pst.setInt(5, receita.getVencimento());
            //Atualiza a tabela receita
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Receita cadastrada com sucesso!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void atualizarReceita(ReceitaDto receita, int codigo) {
        String sql = "update tbreceita set descricao=?, pantone=?, codigoTipoPasta=?, datavencimento=? where codigorec=?";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, receita.getDescricao());
            pst.setString(2, receita.getPantone());
            pst.setInt(3, receita.getTipo());
            pst.setInt(4, receita.getVencimento());
            pst.setInt(5, codigo);
            //Atualiza a tabela Receita
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Receita Atualizada com sucesso!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public void deletarReceita(int codigo) {

        String sql = "delete from tbreceita where codigorec=?";

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, codigo);
            int deletado = pst.executeUpdate();
            if (deletado > 0) {
                JOptionPane.showMessageDialog(null, "Receita deletada com sucesso!");
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void pesquisarReceita(int codigo) {
        String sql = "select r.descricao, r.pantone, t.descricao, r.datavencimento "
                + "from tbreceita as r "
                + "inner join tbTipoPasta as t on r.codigoTipoPasta = t.codigo "
                + "where r.codigorec = '" + codigo + "'";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                TelaCadReceita.txtCadRecCodigo.setEnabled(false);
                TelaCadReceita.txtCadRecDes.setText(rs.getString(1));
                TelaCadReceita.txtCadRecPan.setText(rs.getString(2));
                TelaCadReceita.cbCadReceitaTipo.setSelectedItem(rs.getString(3));
                TelaCadReceita.txtCadRecVal.setText(rs.getString(4));
            } else {
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //confirma se o codigo da receita já existe
    public boolean confirmaCodigo(String codigo) {
        String sql = "select count (codigorec) as total from tbreceita where codigorec ='" + codigo + "'";
        int total = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            total = Integer.parseInt(rs.getString(1));
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total <= 0;
    }

    //confirma se a descrição da receita já existe
    public boolean confirmaDescricao(String descricao) {
        String sql = "select count (descricao) as total from tbreceita where descricao ='" + descricao + "';";
        int total = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            total = Integer.parseInt(rs.getString(1));
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total <= 0;
    }

    public void pesquisarReceitaMovi(int codigo) {

        String sql = "select descricao from tbreceita where codigorec ='" + codigo + "'";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                TelaMovimentacaoEstoque.txtCodigo.setEnabled(false);
                TelaMovimentacaoEstoque.txtDescricao.setEnabled(false);
                TelaMovimentacaoEstoque.txtEstQuantidade.setEnabled(true);
                TelaMovimentacaoEstoque.txtEstData.setEnabled(true);
                TelaMovimentacaoEstoque.txtDescricao.setText(rs.getString(1));
                TelaMovimentacaoEstoque.txtEstQuantidade.requestFocus();
                if (TelaMovimentacaoEstoque.cbTipo.getSelectedItem().equals("Saída")) {
                    MovimentacaoEstoqueDao movDao = new MovimentacaoEstoqueDao();
                TelaMovimentacaoEstoque.txtEstData.setText(movDao.inverterData(movDao.dataAtual()).replace("-", "/"));
                    
                }
            } else {
                JOptionPane.showMessageDialog(null, "Código inválido.");
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public String buscaCodigo(String descricao) {
        String sql = "select codigorec from tbreceita where descricao = '" + descricao + "'";
        String codigo = null;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            codigo = rs.getString(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return codigo;
    }

    public int buscaVencimento(int codigorec) {
        String sql = "select datavencimento from tbreceita where codigorec = '" + codigorec + "'";
        int codigo = 0;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            codigo = rs.getInt(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return codigo;
    }

}
