/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.InsumoDto;
import dto.MovimentacaoDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import telas.TelaCadInsumo;
import telas.TelaMovimentacaoEstoque;
import util.SoNumeros;
import util.Util;

/**
 *
 * @author Leandro
 */
public class InsumoDao {

    private Connection conexao = null;
    private Util util = new Util();
    private final MovimentacaoDao movDao = new MovimentacaoDao();
    private final MovimentacaoDto movDto = new MovimentacaoDto();

    public InsumoDao() {
        this.conexao = ModuloConexao.conector();
    }

    public void adicionarInsumos(InsumoDto insumo) {
        String sql = "insert into tbinsumos(codigo,descricao,UM,quantidade,preco,codigoTipoInsumo) values(?,?,?,?,?,?)";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setInt(1, insumo.getCodigo());
            pst.setString(2, insumo.getDescricao());
            pst.setString(3, insumo.getUM());
            pst.setString(4, insumo.getQuantidade());
            pst.setString(5, insumo.getPreco());
            pst.setInt(6, insumo.getCodigoTipoInsumo());
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
        String sql = "update tbinsumos set descricao=?, UM=?, preco=?, codigoTipoInsumo=? where codigo=?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, insumo.getDescricao());
            pst.setString(2, insumo.getUM());
            pst.setString(3, insumo.getPreco());
            pst.setInt(4, insumo.getCodigoTipoInsumo());
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

    public void pesquisarInsumos(int codigo) {

        String sql = "select i.descricao, ti.nome, i.UM, i.quantidade, i.preco from tbinsumos as i"
                + " inner join tbTipoInsumo as ti on ti.codigo = i.codigoTipoInsumo"
                + " where i.codigo ='" + codigo + "'";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                TelaCadInsumo.txtCadInsCodigo.setEnabled(false);
                TelaCadInsumo.txtCadInsDes.setText(rs.getString(1));
                TelaCadInsumo.cbTipoInsumo.setSelectedItem(rs.getString(2));
                TelaCadInsumo.cbCadInsUm.setSelectedItem(rs.getString(3));
                TelaCadInsumo.txtCadInsQuant.setText(this.util.formatadorQuant(rs.getDouble(4)));
                TelaCadInsumo.txtCadInsPreco.setDocument(new SoNumeros());
                //TelaCadInsumo.txtCadInsPreco.setValue("");
                TelaCadInsumo.txtCadInsPreco.setText(this.util.formatadorQuant(rs.getDouble(5)));

            } else {
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void pesquisarInsumos2(int codigo) {

        String sql = "select descricao, UM from tbinsumos where codigo ='" + codigo + "'";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                TelaMovimentacaoEstoque.txtCodigo.setEnabled(false);
                TelaMovimentacaoEstoque.txtDescricao.setEnabled(false);
                TelaMovimentacaoEstoque.txtEstUM.setEnabled(false);
                TelaMovimentacaoEstoque.txtEstQuantidade.setEnabled(true);
                TelaMovimentacaoEstoque.txtEstData.setEnabled(true);
                TelaMovimentacaoEstoque.txtEstQuantidade.requestFocus();
                TelaMovimentacaoEstoque.txtDescricao.setText(rs.getString(1));
                TelaMovimentacaoEstoque.txtEstUM.setText(rs.getString(2));
                MovimentacaoEstoqueDao movDao = new MovimentacaoEstoqueDao();
                TelaMovimentacaoEstoque.txtEstData.setText(movDao.inverterData(movDao.dataAtual()).replace("-", "/"));

            } else {
                JOptionPane.showMessageDialog(null, "Código inválido.");
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
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
    public boolean saidaInsumo(double quantidade, int codigo) {
        boolean confirmar = true;
        String sql = "update tbinsumos set quantidade = quantidade - ? where codigo = ?";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            pst.setString(1, this.util.formatador(quantidade));
            pst.setInt(2, codigo);

            if (pst.executeUpdate() > 0) {
            } else {
                confirmar = false;
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return confirmar;
    }

    // faz a saida de insumos passando dois parametros como referência
    public void saidaInsumo2(int codigo, double quantidade) {
        String resultadoFormt = this.util.formatador(quantidade);
        String sql = "update tbinsumos set quantidade = quantidade - '" + resultadoFormt + "' where codigo = '" + codigo + "'";
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

        String sql = "SELECT i.UM, cr.consumo, cr.codigoInsumo, i.descricao"
                + " FROM tbReceitaInsumo as cr"
                + " inner join tbinsumos as i on cr.codigoInsumo = i.codigo"
                + " where cr.codigoReceita ='" + codRec + "'";
        PreparedStatement pst;
        try {
            pst = conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                resultado = conversaoUMInsumos(rs.getString(1), (rs.getDouble(2) * 100), Double.parseDouble(telas.TelaMovimentacaoEstoque.txtEstQuantidade.getText().replace(",", ".")));
                saidaInsumo2(rs.getInt(3), this.util.formatador6(resultado));
                movimentarInsumos(rs, resultado);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    private void movimentarInsumos(ResultSet rs, double quantidade) throws SQLException {
        this.movDto.setTipo("Insumo");
        this.movDto.setCodigoID(rs.getInt(3));
        this.movDto.setDescricao(rs.getString(4));
        this.movDto.setQuantidade("-" + quantidade);
        this.movDto.setData(this.util.dataAtual());
        this.movDao.movimentacao(movDto);
    }

    //Converte a UM dos insumos para kg, e calcula a quantida de cada insumo para cada Kg da minha pasta
    public double conversaoUMInsumos(String UM, double consumo, double quantidade) {

        double total = quantidade * (consumo / 100);

        if (UM.equals("g")) {
            total = total * 1000;
        } else {
            if (UM.equals("mg")) {
                total = total * 1000000;
            }
        }
        return total;
    }

    public boolean contInsumo(int codigo) {
        String sql = "select count (codigoInsumo) as total from tbReceitaInsumo where codigoInsumo = '" + codigo + "'";
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
}
