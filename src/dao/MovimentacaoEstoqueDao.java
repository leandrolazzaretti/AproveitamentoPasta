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
import javax.swing.table.DefaultTableModel;
import telas.TelaEstoquePasta;
import util.Util;

/**
 *
 * @author Leandro
 */
public class MovimentacaoEstoqueDao {

    Connection conexao = null;
    Util util = new Util();

    public MovimentacaoEstoqueDao() {
        this.conexao = ModuloConexao.conector();
    }

    //da entrada no estoque de pasta
    public void entradaPasta(MovimentacaoEstoqueDto movEstoqueDto) {
        String sql = "insert into tbEstoquePasta(codigoReceita,UM,quantidade,data,dataVencimento)"
                + " values(?,?,?,?,?)";
        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, movEstoqueDto.getCodigoReceita());
            pst.setString(2, movEstoqueDto.getUM());
            pst.setDouble(3, movEstoqueDto.getQuantidade());
            pst.setString(4, movEstoqueDto.getData());
            pst.setString(5, movEstoqueDto.getDataVencimento());
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

    public int somaPastas(String descricao) {
        String sql = "select  sum(ep.quantidade) from tbEstoquePasta as ep"
                + " inner join tbreceita as r on ep.codigoReceita = r.codigorec"
                + " where r.descricao = '" + descricao + "'";

        PreparedStatement pst;
        int soma = 0;
        try {
            pst = conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            soma = rs.getInt(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return soma;
    }

//    
//        for () {
//        x += array[i] + ","
//    }
    public String buscarInsumos(int codigo) {
        String sql = "select ri.codigoInsumo from tbReceitaInsumo as ri"
                + " where ri.codigoReceita = '" + codigo + "'";

        PreparedStatement pst;
        //List<Integer> lista = new ArrayList<>();
        String insumos = null;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                insumos += rs.getString(1) + ",";
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        String modificada = insumos.substring(0, insumos.length() - 1);
        modificada = modificada.replace("null", "");
        return modificada;
    }

    public void producaoPasta(String insumos) {
        String sql = "select distinct tb.receita, r.descricao, ep.quantidade"
                + " from"
                + "(SELECT codigoReceita as receita"
                + "  FROM tbReceitaInsumo"
                + "  where codigoInsumo in(" + insumos + ")"
                + ")"
                + " as tb"
                + " inner join tbEstoquePasta as ep on ep.codigoReceita = tb.receita"
                + " inner join tbreceita as r on r.codigorec =  tb.receita"
                + " where receita not in (SELECT codigoReceita"
                + "  FROM tbReceitaInsumo"
                + "  where codigoReceita = tb.receita"
                + "  and codigoInsumo not in(" + insumos + ")"
                + ")"
                + " order by ep.data";

        PreparedStatement pst;

        DefaultTableModel modelo = (DefaultTableModel) TelaEstoquePasta.tblProducaoPasta.getModel();
        modelo.setNumRows(0);

        TelaEstoquePasta.tblProducaoPasta.getColumnModel().getColumn(0).setPreferredWidth(40);
        TelaEstoquePasta.tblProducaoPasta.getColumnModel().getColumn(1).setPreferredWidth(40);
        TelaEstoquePasta.tblProducaoPasta.getColumnModel().getColumn(2).setPreferredWidth(40);
        TelaEstoquePasta.tblProducaoPasta.getColumnModel().getColumn(3).setPreferredWidth(40);
        TelaEstoquePasta.tblProducaoPasta.getColumnModel().getColumn(4).setPreferredWidth(40);

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt(3) != 0) {
                    modelo.addRow(new Object[]{
                        rs.getInt(1),
                        rs.getString(2),
                        this.util.formatadorQuant(rs.getString(3))
                    });
                }
            }
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

    public String dataVencimento(String data, int validade) {
        String sql = "SELECT date('" + data + "','" + validade + " day')";
        String retorno = null;
        PreparedStatement pst;
        try {
            pst = conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            retorno = rs.getString(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return retorno;
    }

    public String dataAtual() {
        String sql = "SELECT date('now')";
        String data = null;
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            data = rs.getString(1);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return data;
    }

    public boolean dataComparar(String dataAtual, String dataVencimento) {
        int data1 = Integer.parseInt(dataAtual.replace("-", ""));
        int data2 = Integer.parseInt(dataVencimento.replace("-", ""));
        return data2 <= data1;
    }

    public String inverterData(String data) {
        String[] dataInvertida = data.split("-");
        return dataInvertida[2] + "-" + dataInvertida[1] + "-" + dataInvertida[0];
    }
}
