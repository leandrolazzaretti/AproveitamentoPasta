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
            } else {
                while (rs.next()) {
                    setaPastaEstoque(rs, quantidade);
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
    public void subtrairInsumos() {
        for (int i = 0; (this.pastaProduzir.size() != 0) && (i < this.listTemp.size()); i++) {
                buscarInsumos(this.listTemp.get(i).getCodigo(), this.listTemp.get(i).getEstoque(), false);
            for(int in = 0; in < this.pastaProduzir.size(); in++){
                for(int ind = 0; ind < this.pastaEstoque.size(); ind++){
                    if (this.pastaProduzir.get(in).getCodigoInsumo() == this.pastaEstoque.get(ind).getCodigoInsumo()) {
                        
                    }
                }
                
            }
            
            if (verificarArray(pastaProduzir) == true) {
                i = 0;
            }
        }
    }

    private boolean verificarArray(List<ReceitaInsumoDto> produzir) {
        boolean retorno = false;
        for (int i = 0; i < produzir.size(); i++) {
            if (produzir.get(i).getConsumo() <= 0.001) {
                produzir.remove(i);
                retorno = true;
                i--;
            }
        }
        this.pastaProduzir = produzir;
        return retorno;
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
