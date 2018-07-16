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
public class EstoquePastaDao {

    Connection conexao = null;

    private Util util = new Util();

    private List<EstoquePastaDto> listTemp = new ArrayList<>();
    private List<EstoquePastaDto> listFinal = new ArrayList<>();
    private List<ReceitaInsumoDto> pastaProduzir = new ArrayList<>();
    private List<ReceitaInsumoDto> pastaEstoque = new ArrayList<>();

    private String insumosDeletados;
    private double quantidade;

    public EstoquePastaDao() {
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

                for (int i = 0; i < this.pastaProduzir.size(); i++) {
                    System.out.println(this.pastaProduzir.get(i).getId());
                    System.out.println(this.pastaProduzir.get(i).getCodigoReceita());
                    System.out.println(this.pastaProduzir.get(i).getCodigoInsumo());
                    System.out.println(this.pastaProduzir.get(i).getUm());
                    System.out.println(this.pastaProduzir.get(i).getConsumo());
                }
            } else {
                while (rs.next()) {
                    setaPastaEstoque(rs, quantidade);
                }

                for (int i = 0; i < this.pastaEstoque.size(); i++) {
                    System.out.println(this.pastaEstoque.get(i).getId());
                    System.out.println(this.pastaEstoque.get(i).getCodigoReceita());
                    System.out.println(this.pastaEstoque.get(i).getCodigoInsumo());
                    System.out.println(this.pastaEstoque.get(i).getUm());
                    System.out.println(this.pastaEstoque.get(i).getConsumo());
                }
            }

            pst.close();
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
    public void pastaCompativel(String insumos, int codigo) {
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

    /**
     * faz a subtração dos insimos semelhantes entre a pasta que está em estoque
     * e a pasta que deseja produzir, caso a quantidade o insumo chegue a "0"
     * ele ira chamar o metodo para eliminar o insumo, e fará uma nova pesquiza
     * setando o listTemp com as novas pastas compatíveis
     */
    public void subtrairInsumos(int codigo) {
        double porcentoAtual = 100, porcentoTemp = 0, usadoDaPastaEstoque = 0;
        int iS = 0;
        //entra dentro dos insumos das listaTemporária, e verifica se ainda existe insumos dentro da  pastaProduzir
        for (int i = 0; (this.pastaProduzir.size() != 0) && (i < this.listTemp.size()); i++) {
            //chama método para setar lista pastaEstoque com os insumos da pasta que esta na listaTemporária
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
                        if (porcentoTemp <= porcentoAtual) {
                            porcentoAtual = porcentoTemp;
                        }
                        break;
                    }
                }
            }
            //chama o metodo que fará a real subtração dos insumos
            usadoDaPastaEstoque = subtrairPelaPorcentagem(porcentoAtual);
            setaListFinal(usadoDaPastaEstoque, iS, i);
            iS++;
            //chama o metodo para verificar se o insumo é menor ou igual a "0"
            if (verificarArray(this.pastaProduzir) == true) {
                this.listTemp.clear();
                pastaCompativel(this.insumosDeletados, codigo);
                buscarInsumos(codigo, this.quantidade, true);
                i = 0;
            }
        }
    }

    //subtrai os insumos baseados na porcentagem maxima permitida da determinada pasta do estoque
    private double subtrairPelaPorcentagem(double porcentoAtual) {
        double porcentoDaPasta = 0, usadoDaPastaEstoque = 0;
        //Subtrai direto do array pastaProduzir
        for (int inde = 0; inde < this.pastaProduzir.size(); inde++) {
            for (int index = 0; index < this.pastaEstoque.size(); index++) {
                //verifica se os insumos são compatíveis
                if (this.pastaProduzir.get(inde).getCodigoInsumo() == this.pastaEstoque.get(index).getCodigoInsumo()) {
                    //faz a subtração do insumo que queremos produzir pelo insumo da pasta em estoque
                    double teste = this.pastaProduzir.get(index).getConsumo();
                    porcentoDaPasta = this.util.formatador3(this.util.regraDeTres2(porcentoAtual, teste));
                    usadoDaPastaEstoque += porcentoDaPasta;
                    this.pastaProduzir.get(inde).setConsumo(this.pastaProduzir.get(inde).getConsumo() - porcentoDaPasta);
                    break;
                }
            }
        }
        return usadoDaPastaEstoque;
    }

    // seta a listfinal com os dados da pasta do estoque que foi usada 
    private void setaListFinal(double usadoDaPastaEstoque, int iF, int iT) {

        this.listTemp.get(iT).setUsar(this.util.formatador3(usadoDaPastaEstoque));
        this.listTemp.get(iT).setEquivalencia(this.util.formatador3(this.util.regraDeTres1(usadoDaPastaEstoque, Double.parseDouble(TelaEstoquePasta.txtQuantidade.getText().replace(",", ".")))));
        this.listFinal.add(this.listTemp.get(iT));
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
                this.listFinal.get(i).getEstoque(),
                this.listFinal.get(i).getUsar(),
                this.listFinal.get(i).getEquivalencia(),
                this.listFinal.get(i).getVencimento()
            });
        }
    }

    //se quantidade do insumo for menor  ou igual a "0", ele retira o insumo da lista
    private boolean verificarArray(List<ReceitaInsumoDto> produzir) {
        boolean retorno = false;
        for (int i = 0; i < produzir.size(); i++) {
            //variavel recebe a soma do restante dos insumos, gerando assim o peso restante da pasta
            this.quantidade += this.util.formatador3(produzir.get(i).getConsumo());
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

}
