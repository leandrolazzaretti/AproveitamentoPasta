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
import dao.InsumoDao;
import dto.ReceitaInsumoDto;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Leandro
 */
public class MovimentacaoEstoqueDao {

    Connection conexao = null;
    Util util = new Util();
    List<ReceitaInsumoDto> pastaProduzir = new ArrayList<>();
    List<ReceitaInsumoDto> pastaEstoque = new ArrayList<>();
    List<ReceitaInsumoDto> pastaTemp = new ArrayList<>();

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

    private void buscarInsumos(DefaultTableModel modelo, int codigo) {
        String sql = "select i.codigo, i.descricao, i.quantidade, i.UM, ri.consumo from tbReceitaInsumo as ri"
                + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
                + " where ri.codigoReceita = '" + codigo + "'";

        PreparedStatement pst;
        String pastaInsumo = "insumo";
        InsumoDao insDao = new InsumoDao();
        int contador = 0;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
                recInsDto.setCodigoInsumo(rs.getInt(1));
                recInsDto.setConsumo(insDao.conversaoUMInsumos(rs.getString(4), rs.getDouble(5), Double.parseDouble(telas.TelaEstoquePasta.txtQuantidade.getText().replace(",", "."))));
                this.pastaProduzir.add(recInsDto);
//                System.out.println(this.pastaProduzir.get(contador).getCodigoInsumo());
//                System.out.println(this.pastaProduzir.get(contador).getConsumo());
                contador++;
//                System.out.println(insDao.conversaoUMInsumos(rs.getString(4), rs.getDouble(5), Double.parseDouble(telas.TelaEstoquePasta.txtQuantidade.getText().replace(",", "."))));
                modelo.addRow(new Object[]{
                    pastaInsumo,
                    rs.getInt(1),
                    rs.getString(2),
                    rs.getString(4),
                    this.util.formatadorQuant(rs.getString(3))
                });
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    public String buscaCodigoInsumos(int codigo) {
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
                + " order by ep.dataVencimento, ep.data";

        PreparedStatement pst;
        String pastaInsumo = "Pasta";
        DefaultTableModel modelo = (DefaultTableModel) TelaEstoquePasta.tblProducaoPasta.getModel();
        modelo.setNumRows(0);

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt(3) != 0) {
                    modelo.addRow(new Object[]{
                        pastaInsumo,
                        rs.getInt(1),
                        rs.getString(2),
                        "kg",
                        this.util.formatadorQuant(rs.getString(3))
                    });
                }
            }

            buscarInsumos(modelo, Integer.parseInt(TelaEstoquePasta.txtCodigo.getText()));
            quantoUsar();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void quantoUsar() {
        String confirma = "Pasta";
        InsumoDao insDao = new InsumoDao();
        int codigo;
        int contador = 0;
        int id = 0;
        for (int ind = 0; TelaEstoquePasta.tblProducaoPasta.getModel().getValueAt(ind, 0).toString().equals(confirma); ind++) {

            codigo = (int) TelaEstoquePasta.tblProducaoPasta.getModel().getValueAt(ind, 1);
            String sql = "select ri.consumo, i.UM, i.codigo from tbReceitaInsumo as ri"
                    + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
                    + " where ri.codigoReceita = '" + codigo + "'";
            PreparedStatement pst;

            try {
                pst = this.conexao.prepareStatement(sql);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
                    recInsDto.setId(id);
                    recInsDto.setCodigoInsumo(rs.getInt(3));
                    recInsDto.setConsumo(insDao.conversaoUMInsumos(rs.getString(2), rs.getDouble(1), Double.parseDouble(TelaEstoquePasta.tblProducaoPasta.getModel().getValueAt(ind, 4).toString().replace(",", "."))));
                    this.pastaEstoque.add(recInsDto);
//                    System.out.println(this.pastaEstoque.get(contador).getId());
//                    System.out.println(this.pastaEstoque.get(contador).getCodigoInsumo());
//                    System.out.println(this.pastaEstoque.get(contador).getConsumo());
                    contador++;
//                    System.out.println(insDao.conversaoUMInsumos(rs.getString(2), rs.getDouble(1), Double.parseDouble(TelaEstoquePasta.tblProducaoPasta.getModel().getValueAt(ind, 4).toString().replace(",", "."))));
                }

                pst.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
                System.out.println(e);
            }
            id++;
        }
        subtrairInsumos(this.pastaEstoque.get(this.pastaEstoque.size() - 1).getId());
    }

    //subtrai os insumos da pasta a produzir
    private void subtrairInsumos(int idTot) {
        double usoKg, porcentoAtual = 100, porcentoTemp;
        int idTemp, idTemp2 = 0, id = 0;
        for (int i = 0; i <= idTot; i = i) {
            idTemp = id;
            idTemp2 = idTemp;
            for (int i2 = 0; i2 < this.pastaProduzir.size(); i2++) {
                try {
                    for (int i3 = 0; i == this.pastaEstoque.get(idTemp).getId(); i3++) {
                        if (this.pastaProduzir.get(i2).getCodigoInsumo() == this.pastaEstoque.get(idTemp).getCodigoInsumo()) {
                            System.out.println(regraDeTres1(this.pastaProduzir.get(i2).getConsumo(), this.pastaEstoque.get(idTemp).getConsumo()));
                            System.out.println("");
                            porcentoTemp =  regraDeTres1(this.pastaProduzir.get(i2).getConsumo(), this.pastaEstoque.get(idTemp).getConsumo());
                            
                        }
                        //regraDeTres1(this.pastaProduzir.get(i2).getConsumo(), this.pastaEstoque.get(id).getConsumo());

                        idTemp++;
                        id = idTemp;
                    }

                } catch (Exception e) {
                    System.out.println(e);
                    break;
                }
                idTemp = idTemp2;
            }
            i++;
        }
    }

    //retorna o valor em porcentagem 
    private double regraDeTres1(double produzir, double estoque) {
        double retorno = 0;
        retorno = (produzir * 100) / estoque;

        return retorno;
    }

    //calcula a porcentagem em da porcentagem desejada
    private double regraDeTres2(double porcentoAtual, double porcentoTem) {
        double retorno = 0;
        retorno = (porcentoAtual * porcentoTem) / 100;

        return retorno;
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
