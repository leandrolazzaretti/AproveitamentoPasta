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
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Leandro
 */
public class InsumoDao {

    Connection conexao = null;

    
    
    public InsumoDao() {
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
            pst.setString(4, insumo.getQuantidade().toString());
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

    public void atualizarInsumos(InsumoDto insumo, int codigo) {
        String sql = "update tbinsumos set descricao=?, UM=?, quantidade=?, preco=? where codigo=?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, insumo.getDescricao());
            pst.setString(2, insumo.getUm());
            pst.setString(3, insumo.getQuantidade().toString());
            pst.setDouble(4, insumo.getPreco());
            pst.setInt(5, codigo);
            //Atualiza a tabela insumos
            int adicionado = pst.executeUpdate();
            //Linha abaixo serve de apoio
            //System.out.println(adicionado);
            //confirma se realmente foi atualizada
            if (adicionado > 0) {
                JOptionPane.showMessageDialog(null, "Insumo Atualizado com sucesso!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void deletarInsumos(int codigo) {

        int confirmar = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja Deletar este insumo?", "Atenção", JOptionPane.YES_NO_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {

            String sql = "delete from tbinsumos where codigo=?";

            PreparedStatement pst;

            try {
                pst = this.conexao.prepareStatement(sql);
                pst.setInt(1, codigo);
                int deletado = pst.executeUpdate();

                if (deletado > 0) {
                    JOptionPane.showMessageDialog(null, "Insumo deletado com sucesso!");
                }
                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    //faz um update na tabela de insumos aumentando a quantidade
    public void entradaInsumo(double quantidade, int codigo) {
        String sql = "update tbinsumos set quantidade = quantidade + ? where codigo = ?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setDouble(1, quantidade);
            pst.setInt(2, codigo);
            int confirmar = pst.executeUpdate();

            if (confirmar > 0) {

                JOptionPane.showMessageDialog(null, "Entrada de insumo realizada com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //faz um update na tabela de insumos diminuindo a quantidade
    public void saidaInsumo(double quantidade, int codigo) {
        String sql = "update tbinsumos set quantidade = quantidade - ? where codigo = ?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setDouble(1, quantidade);
            pst.setInt(2, codigo);
            int confirmar = pst.executeUpdate();

            if (confirmar > 0) {

                JOptionPane.showMessageDialog(null, "Saida de insumo realizada com sucesso.");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    // faz a saida de insumos passando dois parametros como referência
    public void saidaInsumo2(int codigo, double quantidade) {
        String sql = "update tbinsumos set quantidade = quantidade - '" + quantidade + "' where codigo = '" + codigo + "'";
        PreparedStatement pst;
        try {
            pst = conexao.prepareStatement(sql);
            int confirma = pst.executeUpdate();
            if (confirma > 0) {
                //System.out.println("deu boa!");
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    // busca o codigo da tabela de insumos através da descrição
    public int buscaCodigoInsumo(String descricao) {
        String sql = "select codigo from tbinsumos where descricao =?";
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
    
        //retorna as UM/Consumo dos insumos da receita a dar entrada, e da saida dos insumos através da entrada da receita
    public void retirarInsumo(int codRec) {
        double resultado;
        String sql = "SELECT i.UM, cr.consumo, cr.codigoInsumo"
                + " FROM tbReceitaInsumo as cr"
                + " inner join tbinsumos as i on cr.codigoInsumo = i.codigo"
                + " where cr.codigoReceita ='" + codRec + "'";
        PreparedStatement pst;
        try {
            pst = conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                resultado = conversaoUMInsumos(rs.getString(1), rs.getDouble(2));
                saidaInsumo2(rs.getInt(3), resultado);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }

    }

    //Converte a UM dos insumos para kg, e calcula a quantida de cada insumo para cada Kg da minha pasta
    private double conversaoUMInsumos(String UM, double consumo) {
        double nes = Double.parseDouble(telas.TelaMovimentacaoEstoque.txtEstQuantidade.getText().replace(",","."));
        double total = nes * (consumo / 100);

        if (UM.equals("g")) {
            total = total * 1000;
        } else {
            if (UM.equals("mg")) {
                total = total * 1000000;
            }
        }
        return total;
    }
}
