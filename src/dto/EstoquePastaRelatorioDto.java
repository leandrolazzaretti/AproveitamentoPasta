/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author Leandro
 */
public class EstoquePastaRelatorioDto {

    private int ID;
    private int codigoReceita;
    private String descricao;
    private String quantidade;
    private String custoPorKg;
    private String data;
    private String dataVencimento;
    private String totalQuantidade;
    private String totalValor;

    public EstoquePastaRelatorioDto() {
    }

    public EstoquePastaRelatorioDto(int ID, int codigoReceita, String descricao, String quantidade, String custoPorKg, String data, String dataVencimento, String totalQuantidade, String totalValor) {
        this.ID = ID;
        this.codigoReceita = codigoReceita;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.custoPorKg = custoPorKg;
        this.data = data;
        this.dataVencimento = dataVencimento;
        this.totalQuantidade = totalQuantidade;
        this.totalValor = totalValor;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCodigoReceita() {
        return codigoReceita;
    }

    public void setCodigoReceita(int codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getCustoPorKg() {
        return custoPorKg;
    }

    public void setCustoPorKg(String custoPorKg) {
        this.custoPorKg = custoPorKg;
    }

    public String getTotalQuantidade() {
        return totalQuantidade;
    }

    public void setTotalQuantidade(String totalQuantidade) {
        this.totalQuantidade = totalQuantidade;
    }

    public String getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(String totalValor) {
        this.totalValor = totalValor;
    }
}
