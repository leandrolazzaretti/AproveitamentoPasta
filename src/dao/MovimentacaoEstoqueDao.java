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
import dto.ReceitaInsumoDto;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

/**
 *
 * @author Leandro
 */
public class MovimentacaoEstoqueDao {

    private Connection conexao = null;
    private Util util = new Util();
    private List<ReceitaInsumoDto> pastaProduzir = new ArrayList<>();
    private List<ReceitaInsumoDto> pastaEstoque = new ArrayList<>();
    private List<ReceitaInsumoDto> pastasTemp = new ArrayList<>();

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

    private void buscarInsumos(JTable tabela, int codigo, boolean confirmaOpc, double v) {
        String sql = "select i.codigo, i.descricao, i.quantidade, i.UM, ri.consumo from tbReceitaInsumo as ri"
                + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
                + " where ri.codigoReceita = '" + codigo + "'";

        PreparedStatement pst;
//        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
//        modelo.setNumRows(0);

        String pastaInsumo = "insumo";
        InsumoDao insDao = new InsumoDao();
        int contador = 0;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
                recInsDto.setCodigoInsumo(rs.getInt(1));
                recInsDto.setConsumo(formatador3(insDao.conversaoUMInsumos(rs.getString(4), rs.getDouble(5), v)));
                this.pastaProduzir.add(recInsDto);
//                System.out.println(this.pastaProduzir.get(contador).getCodigoInsumo());
//                System.out.println(this.pastaProduzir.get(contador).getConsumo());
                contador++;
//                System.out.println(insDao.conversaoUMInsumos(rs.getString(4), rs.getDouble(5), Double.parseDouble(telas.TelaEstoquePasta.txtQuantidade.getText().replace(",", "."))));
//                if (confirmaOpc == true) {
//                    modelo.addRow(new Object[]{
//                        pastaInsumo,
//                        rs.getInt(1),
//                        rs.getString(2),
//                        rs.getString(4),
//                        this.util.formatadorQuant(rs.getDouble(3))
//                    });
//                }
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

    public void producaoPasta(JTable tabela, String insumos, boolean confirmaOpc, int codigo) {
//        String sql = "select distinct tb.receita, r.descricao, ep.quantidade, ep.ID"
//                + " from"
//                + " (SELECT codigoReceita as receita"
//                + "  FROM tbReceitaInsumo"
//                + "  where codigoInsumo in(" + insumos + ")"
//                + " )"
//                + " as tb"
//                + " inner join tbEstoquePasta as ep on ep.codigoReceita = tb.receita"
//                + " inner join tbreceita as r on r.codigorec =  tb.receita"
//                + " where receita not in (SELECT codigoReceita"
//                + "  FROM tbReceitaInsumo"
//                + "  where codigoReceita = tb.receita"
//                + "  and codigoInsumo not in(" + insumos + ")"
//                + ")"
//                + " order by ep.dataVencimento, ep.data";

        String sql = " select tb0.codigo, tb0.descricao, tb0.quantidade, tb0.vencimento, tb0.ID"
                + " from ("
                + "    select ep.codigoReceita as codigo, r.descricao as descricao, ep.quantidade quantidade,ep.dataVencimento as vencimento, ep.ID as ID"
                + "    from tbEstoquePasta as ep"
                + "    inner join tbreceita as r on r.codigorec = ep.codigoReceita"
                + "    where ep.codigoReceita =  " + codigo + ""
                + "    ORDER BY vencimento"
                + " ) as tb0"
                + " union all"
                + " select tb1.codigo, tb1.descricao, tb1.quantidade, tb1.vencimento, tb1.ID"
                + " from ("
                + " select distinct tb.receita as codigo, r.descricao as descricao, ep.quantidade as quantidade, ep.dataVencimento as vencimento, ep.ID as ID"
                + " from ("
                + "    SELECT codigoReceita as receita"
                + "    FROM tbReceitaInsumo"
                + " where codigoInsumo in(" + insumos + ")"
                + " ) as tb"
                + " inner join tbEstoquePasta as ep on ep.codigoReceita = tb.receita"
                + " inner join tbreceita as r on r.codigorec =  tb.receita"
                + " where receita not in  ("
                + "    SELECT codigoReceita"
                + "    FROM tbReceitaInsumo"
                + "    where codigoReceita = tb.receita"
                + "    and codigoInsumo not in(" + insumos + ")"
                + " )\n"
                + " and receita not in ("
                + "    select codigoReceita"
                + "    from tbReceitaInsumo"
                + "    where codigoReceita = tb.receita"
                + "    and codigoReceita = (" + codigo + ")"
                + " )"
                + " order by vencimento"
                + " ) as tb1"
                + ";";

        PreparedStatement pst;
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0);

        //pastaEstoque(modelo, codigo);
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if ((rs.getInt(3) != 0)) {
                    modelo.addRow(new Object[]{
                        rs.getInt(5),
                        rs.getInt(1),
                        rs.getString(2),
                        this.util.formatadorQuant(rs.getDouble(3)),
                        null,
                        null,
                        inverterData(rs.getString(4)).replace("-", "/")
                    });
                }
            }
            //buscarInsumos(modelo, codigo, confirmaOpc);
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public double[] quantoUsarOpc1(JTable tabela, int codigo, boolean confirmaOpc) {
        //double v1, v2, v3, v4, V5;
        double[] v = new double[7];
        int in = 0;
        v[4] = Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", "."));
        System.out.println(v[4]);
        v[5] = v[4];
        String conf = tabela.getModel().getValueAt(in, 1).toString();
        for (int i = 0; (codigo == Integer.parseInt(conf)) && (v[4] > 0) && (i < tabela.getRowCount()); i++) {
            v[6] = i;
            in++;
            v[0] = in;
            conf = tabela.getModel().getValueAt(in, 1).toString();
            v[1] = v[4];
            String valor = tabela.getModel().getValueAt(i, 3).toString().replace(",", ".");
            v[2] = Double.parseDouble(valor);
            v[3] = v[1] - v[2];
            if (v[3] <= 0) {
                v[4] = v[3];
                tabela.getModel().setValueAt(this.util.formatadorQuant(v[3] + v[2]), i, 4);
                tabela.getModel().setValueAt(this.util.formatadorQuant(regraDeTres1(v[3] + v[2], v[5])) + "%", i, 5);
                break;
            } else {
                tabela.getModel().setValueAt(this.util.formatadorQuant(v[2]), i, 4);
                tabela.getModel().setValueAt(this.util.formatadorQuant(regraDeTres1(v[2], v[5])) + "%", i, 5);
                v[4] = v[3];
            }
        }
        System.out.println(v[4]);
        buscarInsumos(tabela, codigo, confirmaOpc, v[4]);
        return v;
    }
    
    private void verificarArray(List<ReceitaInsumoDto> produzir){
        for(int i = 0; i < produzir.size(); i++){
            if(produzir.get(i).getConsumo() <= 0.001){
                produzir.remove(i);
                i--;
            }
        }
        this.pastaProduzir = produzir;
        System.out.println(this.pastaProduzir.size());
    }

    //retorna apenas as pastas do estoque que são iguais
    public void pastaEstoque(DefaultTableModel modelo, int codigo) {
        String sql = "select ep.codigoReceita, r.descricao, ep.quantidade, ep.ID from tbEstoquePasta as ep"
                + " inner join tbreceita as r on r.codigorec = ep.codigoReceita"
                + " where ep.codigoReceita = '" + codigo + "'"
                + " order by ep.dataVencimento, ep.quantidade desc";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt(3) != 0) {
                    modelo.addRow(new Object[]{
                        rs.getInt(4),
                        rs.getString(1),
                        rs.getString(2),
                        "kg",
                        this.util.formatadorQuant(rs.getDouble(3))
                    });
                }
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //formata os valores para 2 casas decimais
    private double formatador2(double valor) {
        DecimalFormat df = new DecimalFormat("#.00");
        double resultado = 0;
        String resultado2 = df.format(valor);
        resultado = Double.parseDouble(resultado2.replace(",", "."));
        return resultado;
    }

    //formata os valores para 3 casas decimais
    private double formatador3(double valor) {
        DecimalFormat df = new DecimalFormat("#.000");
        double resultado = 0;
        String resultado2 = df.format(valor);
        resultado = Double.parseDouble(resultado2.replace(",", "."));
        return resultado;
    }

    //retorna o valor em porcentagem 
    private double regraDeTres1(double produzir, double estoque) {
        double retorno = 0;
        retorno = (produzir * 100) / estoque;

        return retorno;
    }

    //calcula a quantidade da % desejada
    private double regraDeTres2(double porcentoAtual, double quantidadePro) {
        double retorno = 0;
        retorno = (quantidadePro * porcentoAtual) / 100;

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
