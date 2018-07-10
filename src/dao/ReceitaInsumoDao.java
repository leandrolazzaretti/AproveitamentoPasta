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
import javax.swing.JOptionPane;
import telas.TelaCadReceita;

/**
 *
 * @author Leandro
 */
public class ReceitaInsumoDao {

    Connection conexao = null;

    public ReceitaInsumoDao() {
        this.conexao = ModuloConexao.conector();
    }

    //Adiciona componente
    public void adicionarComponentes(int codReceita, int codigo, String consumo) {

        String sql = "insert into tbReceitaInsumo(codigoReceita,codigoInsumo,consumo)values(?,?,?)";

        PreparedStatement pst;
        try {

            pst = this.conexao.prepareStatement(sql);

            int rec = codReceita;
            
            String cons = consumo;
            pst.setInt(1, rec);
            pst.setInt(2, codigo);
            pst.setString(3, cons);

            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                //System.out.println("Deu boa.");
            }
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    // atualiza todos os componente
    public void atualizarComponentes(int codReceita) {
        String sql = "update tbReceitaInsumo"
                + " set consumo =?"
                + " where codigoReceita =? and codigoInsumo =?";
        //int count = contarTabela();

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            int linha = TelaCadReceita.tblCadRecComponentes.getSelectedRow();
            String cons = (String) TelaCadReceita.tblCadRecComponentes.getModel().getValueAt(linha, 2);
            int codg = (int) TelaCadReceita.tblCadRecComponentes.getModel().getValueAt(linha, 0);
            //int ins = Integer.parseInt(codg);
            int rec = codReceita;

            pst.setString(1, cons.replace(",", "."));
            pst.setInt(2, rec);
            pst.setInt(3, codg);

            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                //System.out.println("Deu boa.");
            }

            pst.close();
        } catch (ArrayIndexOutOfBoundsException e2) {
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    // deleta o componente selecionado
    public void deletarComponente(int codReceita) {
        String sql = "delete from tbReceitaInsumo"
                + " where codigoReceita =? and "
                + " codigoInsumo =?";

        PreparedStatement pst;

        int setar = TelaCadReceita.tblCadRecComponentes.getSelectedRow();
        int ins = Integer.parseInt(TelaCadReceita.tblCadRecComponentes.getModel().getValueAt(setar, 0).toString());
        //System.out.println(ins);
        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, codReceita);
            pst.setInt(2, ins);
            pst.executeUpdate();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // deleta todos componente 
    public void deletarTodos(int codReceita) {
        String sql = "delete from tbReceitaInsumo"
                + " where codigoReceita =?";
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, codReceita);
            int deletado = pst.executeUpdate();
            if (deletado > 0) {
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // verifica se existe algum insumo duplicado na tabela de componentes
    public boolean verificaInsumo(int codReceita, int codigo) {
        String sql = "select count (codigoReceita) as total from tbReceitaInsumo where codigoReceita =? and codigoInsumo =?";
        
        int total = 0;
        PreparedStatement pst;

        try {
            pst = conexao.prepareStatement(sql);
            pst.setInt(1, codReceita);
            pst.setInt(2, codigo);
            ResultSet rs = pst.executeQuery();
            total = Integer.parseInt(rs.getString(1));
            //System.out.println(total);

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return total <= 0;
    }

    //confirma se o insumo existe
    public boolean confirmaInsumo(String insumo) {
        String sql = "select count (descricao) as total from tbinsumos where descricao ='" + insumo + "';";
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
    
    //confirma se o insumo existe através do codigo
    
    public boolean confirmaInsumoCodigo(String codigo) {
        String sql = "select count (codigo) as total from tbinsumos where codigo ='" + codigo + "';";
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
    
    
    //seleciona o codigo do insumo atraves da descrição do mesmo
    public int codIns(String descIns) {
        String sql = "select i.codigo from tbinsumos as i where descricao ='" + descIns + "'";
        int retorno = 0;

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                retorno = rs.getInt(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return retorno;
    }
    //seleciona o descricao do insumo atraves da codigo do mesmo
    public String codIns(int codIns) {
        String sql = "select i.descricao from tbinsumos as i where codigo ='" + codIns + "'";
        String retorno = null;

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                retorno = rs.getString(1);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return retorno;
    }
    
    
    
    public boolean confirmaCodigo(int codigo){
        String sql = "select codigorec from tbreceita where codigorec = '"+codigo+"'";
        PreparedStatement pst;
        boolean retorno = false;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                retorno = true;
            }else{
                retorno = false;
            }
            
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return retorno;
    }
}
