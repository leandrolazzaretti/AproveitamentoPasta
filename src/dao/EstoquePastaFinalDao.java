/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import conexao.ModuloConexao;
import dto.EstoquePastaDto;
import dto.ReceitaInsumoDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import telas.TelaEstoquePasta;
import util.Util;

/**
 *
 * @author Leandro
 */
public class EstoquePastaFinalDao {

    Connection conexao = null;

    private final Util util = new Util();

    private  List<EstoquePastaDto> listTemp = new ArrayList<>();
    private  List<EstoquePastaDto> listFinal = new ArrayList<>();
    private  List<ReceitaInsumoDto> pastaProduzir = new ArrayList<>();
    private  List<ReceitaInsumoDto> pastaEstoque = new ArrayList<>();

    private String insumosDeletados;
    private double usadoDaPastaEstoque, pesoRestantePastaProduzir;

    public EstoquePastaFinalDao() {
        this.conexao = ModuloConexao.conector();
    }

    //Busca os insumos que compoem a pasta (pode ser do estoque ou que queremos produzir) true: Produzir  false: Estoque
    public void buscarInsumos(int codigo, double quantidade, boolean confirma) {
        String sql = "select i.codigo, i.descricao, i.quantidade, i.UM, ri.consumo from tbReceitaInsumo as ri"
                + " inner join tbinsumos as i on i.codigo = ri.codigoInsumo"
                + " where ri.codigoReceita = '" + codigo + "'";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            if (confirma == true) {
                while (rs.next()) {
                    setaPastaProduzir(rs, quantidade);
                }
            } else {
                while (rs.next()) {
                    setaPastaEstoque(rs, quantidade);
                }
            }

            pst.close();
            printInfo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.out.println(e);
        }
    }
    //seta o List pastaProduzir, com os dados do formulário

    private void setaPastaProduzir(ResultSet rs, double quantidade) throws SQLException {
        ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
        recInsDto.setCodigoInsumo(rs.getInt(1));
        recInsDto.setConsumo(this.util.formatador3(this.util.conversaoUMInsumos(rs.getString(4), rs.getDouble(5), quantidade)));
        recInsDto.setUm(rs.getString(4));
        this.pastaProduzir.add(recInsDto);
    }

    //seta o List pastaEstoque, com os dados contidos no List listTemp
    private void setaPastaEstoque(ResultSet rs, double quantidade) throws SQLException {
        ReceitaInsumoDto recInsDto = new ReceitaInsumoDto();
        recInsDto.setCodigoInsumo(rs.getInt(1));
        recInsDto.setConsumo(this.util.formatador3(this.util.conversaoUMInsumos(rs.getString(4), rs.getDouble(5), quantidade)));
        recInsDto.setUm(rs.getString(4));
        this.pastaEstoque.add(recInsDto);
    }

    //verifica as pastas compatíveis com a receita desejada 
    public void pastaCompativel(String insumos) {

        String sql = "select tb1.codigo, tb1.descricao, tb1.quantidade, tb1.vencimento, tb1.ID"
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
                + " )"
                + " and quantidade > 0"
                + " ) as tb1"
                + " order by tb1.vencimento"
                + ";";

        PreparedStatement pst;

        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if ((rs.getInt(3) != 0)) {
                    EstoquePastaDto estPastDto = new EstoquePastaDto();
                    estPastDto.setId(rs.getInt(5));
                    estPastDto.setCodigo(rs.getInt(1));
                    estPastDto.setDescricao(rs.getString(2));
                    estPastDto.setEstoque(this.util.formatador3(rs.getDouble(3)));
                    estPastDto.setVencimento(this.util.inverterData(rs.getString(4)).replace("-", "/"));

                    this.listTemp.add(estPastDto);
                }
            }
            pst.close();
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void subtrairInsumos(int codigo) {
        //chama método para setar lista pastaEstoque com os insumos da pasta que esta na listaTemporária
        double quantDoInsumoEst = 0, porcentoTotal = 100, porcentoTemp;
        for (int i = 0; (!this.pastaProduzir.isEmpty()) && (i < this.listTemp.size()); i++) {
             buscarInsumos(this.listTemp.get(i).getCodigo(), this.listTemp.get(i).getEstoque(), false);
            //entra dentro da lista pastaProduzir, para verificar compatibilidade dos insumos
            for (int in = 0; in < this.pastaProduzir.size(); in++) {
                //entra dentro da pastaProduzir para varrer os insumos para verificar a compatibilidade
                for (int ind = 0; ind < this.pastaEstoque.size(); ind++) {
                    //verifica compatibilidade dos insumos através do código dos insumos
                    if (this.pastaProduzir.get(in).getCodigoInsumo() == this.pastaEstoque.get(ind).getCodigoInsumo()) {
                        //verifica a % que pode ser usada em determinado insumo
                        porcentoTemp = this.util.formatador3(this.util.regraDeTres1(this.pastaProduzir.get(in).getConsumo(), this.pastaEstoque.get(ind).getConsumo()));
                        //se a porcentagem temporária for maior que a Atual então é realizado a troca
                        if (porcentoTemp <= porcentoTotal) {
                            porcentoTotal = porcentoTemp;
                        }
                        break;
                    }
                }
            }
            //Recalcula o maximo a ser usada de todos os insumos da pasta estoque, com base na % total que this.util.formatador3(this.util.regraDeTres2(porcentoTotal, this.pastaEstoque.get(inde).getConsumo()))pode ser uasda
            for (int inde = 0; inde < this.pastaEstoque.size(); inde++) {
                this.pastaEstoque.get(inde).setConsumo(this.util.formatador3(this.util.regraDeTres2(porcentoTotal, this.pastaEstoque.get(inde).getConsumo())));
            }
            printInfo();
            //chama o metodo que ira fazer a subtração
            subtrairPelaQuantidade();
            setaListFinal(usadoDaPastaEstoque, i);
            //chama o metodo para verificar se o insumo é menor ou igual a "0"
            if (verificarArray(pastaProduzir) == true) {
                this.listTemp.clear();
                //se a lista for diferente de vazio, então chama o metodo para refazer a pesquisa com base nos insumos deletados
                if (!this.pastaProduzir.isEmpty()) {
                    pastaCompativel(this.insumosDeletados);
                    //buscarInsumos(codigo, this.quantidade, true);
                    i = -1;
                }
            }
        }
    }
    
    // seta a listfinal com os dados da pasta do estoque que foi usada 
    private void setaListFinal(double usadoDaPastaEstoque, int iT) {
        
        this.listTemp.get(iT).setUsar(this.util.formatador3(usadoDaPastaEstoque));
        double porcent = this.util.formatador3(this.util.regraDeTres1(usadoDaPastaEstoque, Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(".", "").replace(",", "."))));
        //verifica se a % ultrapassa os 100%, caso isso acontece o objeto recebe 100%
        if (porcent > 100) {
            this.listTemp.get(iT).setEquivalencia(100);
        } else {
            this.listTemp.get(iT).setEquivalencia(porcent);
        }

        this.listFinal.add(this.listTemp.get(iT));
        this.pastaEstoque.clear();
        this.usadoDaPastaEstoque = 0;
    }

    //seta a tabela que vem por parâmetro
    public void setarTabela(JTable tabela) {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setNumRows(0);

        for (int i = 0; i < this.listFinal.size(); i++) {
            modelo.addRow(new Object[]{
                this.listFinal.get(i).getId(),
                this.listFinal.get(i).getCodigo(),
                this.listFinal.get(i).getDescricao(),
                this.util.formatadorQuant(this.listFinal.get(i).getEstoque()),
                this.util.formatadorQuant(this.listFinal.get(i).getUsar()),
                this.util.formatadorQuant(this.listFinal.get(i).getEquivalencia()) + "%",
                this.listFinal.get(i).getVencimento()
            });
        }
    }
    
    //subtrai os insumos baseados na porcentagem maxima permitida da determinada pasta do estoque
    private boolean subtrairPelaQuantidade() {
        double totalUsado = 0, totalTemp;
        boolean confirma = false;
        //Subtrai direto do array pastaProduzir
        for (int i = 0; i < this.pastaProduzir.size(); i++) {
            for (int in = 0; in < this.pastaEstoque.size(); in++) {
                //verifica se os insumos são compatíveis
                if (this.pastaProduzir.get(i).getCodigoInsumo() == this.pastaEstoque.get(in).getCodigoInsumo()) {
                    //faz a subtração do insumo que queremos produzir pelo insumo da pasta em estoque
                    double insuProd = this.pastaProduzir.get(i).getConsumo();
                    double insuEstoq = this.pastaEstoque.get(in).getConsumo();
                    totalTemp = insuProd - insuEstoq;
                    totalUsado = insuProd - totalTemp;
                    this.usadoDaPastaEstoque += totalUsado;
                    if (totalTemp < 0.001) {
                        totalTemp = 0;
                        confirma = true;
                    }
                    this.pastaProduzir.get(i).setConsumo(totalTemp);
                    break;
                }
            }
        }
        return confirma;
    }
    
     //se quantidade do insumo for menor  ou igual a "0", ele retira o insumo da lista
    private boolean verificarArray(List<ReceitaInsumoDto> produzir) {
        boolean retorno = false;
        this.pesoRestantePastaProduzir = 0;
        for (int i = 0; i < produzir.size(); i++) {
            //variavel recebe a soma do restante dos insumos, gerando assim o peso restante da pasta
            this.pesoRestantePastaProduzir += this.util.formatador3(produzir.get(i).getConsumo());
            //verifica se é = "0", se for verdadeiro o insumo será eliminado da lista
            if (produzir.get(i).getConsumo() <= 0.001) {
                produzir.remove(i);
                retorno = true;
                i--;
            }
        }
        this.pastaProduzir = produzir;
        setarUrlInsu();
        return retorno;
    }
    
     //cria uma nova URL com com os códigos dos insumos que ainda não foram zerados
    private void setarUrlInsu() {
        String insu = null;
        for (int i = 0; (!this.pastaProduzir.isEmpty()) && (i < this.pastaProduzir.size()); i++) {
            insu += this.pastaProduzir.get(i).getCodigoInsumo() + ",";
        }
        if (insu != null) {
            this.insumosDeletados = insu.substring(0, insu.length() - 1);
            this.insumosDeletados = this.insumosDeletados.replace("null", "");
        }
    }


    //busca os códigos dos insumos contidos na Receita que deseja produzir, e retorna em forma de uma String(URL)
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

    /**
     * Busca o maior consumo entre os insumos da minha pasta em estoque e
     * retorna o valor em kg do aproveitamento máximo da minha pasta em estoque.
     * Atualiza a lista
     */
    private int maxInsumoEstoqueUsar(int codigo) {
        String sql = "select ri.codigoInsumo, max(ri.consumo) as total  from tbReceitaInsumo as ri"
                + " where ri.codigoReceita = " + codigo;
        PreparedStatement pst;
        int insumo = 0;
        try {
            pst = this.conexao.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                insumo = rs.getInt(1);

            }
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return insumo;
    }

    //busca o insumo e o consumo 
    //MOSTRA AS INFORMAÇÕES ATRAVÉS DO SYSTEM.OUT
    public void printInfo() {

        System.out.println("PASTA MAIS ANTIGA COMPATÍVEL: ");
        System.out.println("ID: " + this.listTemp.get(0).getId());
        System.out.println("CÓDIGO: " + this.listTemp.get(0).getCodigo());
        System.out.println("DESCRIÇÃO: " + this.listTemp.get(0).getDescricao());
        System.out.println("ESTOQUE: " + this.listTemp.get(0).getEstoque());
        System.out.println("USAR: " + this.listTemp.get(0).getUsar());
        System.out.println("EQUIVALÊNCIA %: " + this.listTemp.get(0).getEquivalencia());
        System.out.println("VENCIMENTO: " + this.listTemp.get(0).getVencimento());

        System.out.println("INSUMO DA PASTA MAIS ANTIGA: ");
        for (int i = 0; i < this.pastaEstoque.size(); i++) {
            System.out.print("ID: " + this.pastaEstoque.get(i).getId() + ", ");
            System.out.print("INSUMO: " + this.pastaEstoque.get(i).getCodigoInsumo() + ", ");
            System.out.print("UM: " + this.pastaEstoque.get(i).getUm() + ", ");
            System.out.println("CONSUMO: " + this.pastaEstoque.get(i).getConsumo());
        }

        System.out.println("INSUMO DA PASTA QUE DESEJO PRODUZIR: ");
        for (int i = 0; i < this.pastaProduzir.size(); i++) {
            System.out.print("ID: " + this.pastaProduzir.get(i).getId() + ", ");
            System.out.print("INSUMO: " + this.pastaProduzir.get(i).getCodigoInsumo() + ", ");
            System.out.print("UM: " + this.pastaProduzir.get(i).getUm() + ", ");
            System.out.println("CONSUMO: " + this.pastaProduzir.get(i).getConsumo());
        }
    }

    public void limparVariaveis() {
        this.listTemp.clear();
        this.listFinal.clear();
        this.pastaProduzir.clear();
        this.pastaEstoque.clear();

        this.insumosDeletados = null;
        this.usadoDaPastaEstoque = 0;
    }
}
