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
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import telas.TelaCadReceita;
import util.Util;

/**
 *
 * @author Leandro
 */
public class ReceitaInsumoDao {

    Connection conexao = null;
    Util util = new Util();
    public static double consumoTotal = 0, consumoTotalTemp = 0;
    public static List<Double> consumoTotal2 = new ArrayList<>();
    public static List<Double> consumoTotal3 = new ArrayList<>();

    public ReceitaInsumoDao() {
        this.conexao = ModuloConexao.conector();
    }

    //verifica se o valor total do consumo ultrapassa 1kg enquanto estamos no adicionar
    public boolean verificaConsumoTotalAdd(double consumo){
        return consumoTotal + consumo <= 1;
    }
    
    //verifica se o valor total do consumo ultrapassa 1kg enquanto estamos no atualizar
    public boolean verificaConsumoTotalUpdate(int linha, double consumo){
        this.consumoTotalTemp = 0;
        this.consumoTotal2.set(linha, consumo);
        for(int i = 0; i < this.consumoTotal2.size(); i++){
            this.consumoTotalTemp += this.consumoTotal2.get(i);
        }
        return this.consumoTotalTemp <= 1;
    }
    
    //retorna o valor anterior do consumo, antes da auteração
    public double retornaValorConsumo(int i){
        this.consumoTotal2.set(i, this.consumoTotal3.get(i));        
        return this.consumoTotal3.get(i);
    }
    
    //seta a posição removida com o valor "0"
    public void removerConsumoDaList(int index, double consumo){
        this.consumoTotal2.remove(index);
        this.consumoTotal3.remove(index);
        this.consumoTotal -= consumo;
        this.consumoTotal = this.util.formatador4(consumoTotal);
    }
    
    //limpa algumas variáveis 
    public void limparVariaveis(){
        this.consumoTotal = 0;
        this.consumoTotal2.clear();
        this.consumoTotal3.clear();
    }
    
    //Adiciona componente
    public void adicionarComponentes(int codReceita, int codigo, double consumo, double custo) {
       
        String sql = "insert into tbReceitaInsumo(codigoReceita,codigoInsumo,consumo, custoPorKg)values(?,?,?,?)";

        PreparedStatement pst;
        try {

            pst = this.conexao.prepareStatement(sql);

            int rec = codReceita;

            pst.setInt(1, rec);
            pst.setInt(2, codigo);
            pst.setDouble(3, consumo);
            pst.setDouble(4, custo);

            int adicionado = pst.executeUpdate();
            if (adicionado > 0) {
                //System.out.println("Deu boa.");
                this.consumoTotal += consumo;
                this.consumoTotal2.add(consumo);
                this.consumoTotal3.add(consumo);
            }
            pst.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }

    //busca a UM e o preço por kg/g/mg/L do insumo desejado e chama o metodo para calcular o custo por kg
    public double custoPorComponenteKg(int codigo, double consumo) {
        double custo = 0;
        String UM = null, sql = "select UM, preco from tbinsumos where codigo = '" + codigo + "'";
        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                UM = rs.getString(1);
                custo = rs.getDouble(2);
            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        custo = calcularCustoComponente(UM, custo, consumo);
        return custo;
    }

    //calcula o custo do componente com base no seu consumo dentro de 1Kg
    private double calcularCustoComponente(String UM, double custo, double consumo) {
        consumo = this.util.conversaoKGparaUM(UM, consumo);
        custo = this.util.formatador3(consumo * custo);
        return custo;
    }

    // atualiza todos os componente
    public void atualizarComponentes(int codReceita) {
        String sql = "update tbReceitaInsumo"
                + " set consumo =?, custoPorKg =?"
                + " where codigoReceita =? and codigoInsumo =?";
        //int count = contarTabela();

        PreparedStatement pst;
        try {
            pst = this.conexao.prepareStatement(sql);

            int linha = TelaCadReceita.tblCadRecComponentes.getSelectedRow();
            String cons = TelaCadReceita.tblCadRecComponentes.getModel().getValueAt(linha, 2).toString();
            if (cons.equals("")) {
                cons = "0";
            }
            int codg = Integer.parseInt(TelaCadReceita.tblCadRecComponentes.getModel().getValueAt(linha, 0).toString());
            double custo = custoPorComponenteKg(codg, Double.parseDouble(cons.replace(".", "").replace(",", ".")));

            pst.setString(1, cons.replace(",", "."));
            pst.setDouble(2, custo);
            pst.setInt(3, codReceita);
            pst.setInt(4, codg);

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

    //confirma se o insumo existe através da descrição
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

    public boolean confirmaCodigo(int codigo) {
        String sql = "select codigorec from tbreceita where codigorec = '" + codigo + "'";
        PreparedStatement pst;
        boolean retorno = false;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                retorno = true;
            } else {
                retorno = false;
            }

            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return retorno;
    }
}
