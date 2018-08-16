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
public class RelatorioReceitaDto {

    private String codigoReceita;
    private String descRec;
    private String descTP;
    private String pantone;
    private String dataVencimento;
    private String custoPorKg;
    private String descIns;
    private String valor;
    private String consumo;

    public RelatorioReceitaDto() {

    }

    public RelatorioReceitaDto(String codigoReceita, String descRec, String descTP, String pantone, String dataVencimento, String custoPorKg, String descIns, String valor, String consumo) {
        this.codigoReceita = codigoReceita;
        this.descRec = descRec;
        this.descTP = descTP;
        this.pantone = pantone;
        this.dataVencimento = dataVencimento;
        this.custoPorKg = custoPorKg;
        this.descIns = descIns;
        this.valor = valor;
        this.consumo = consumo;
    }

    public String getCodigoReceita() {
        return codigoReceita;
    }

    public void setCodigoReceita(String codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    public String getDescRec() {
        return descRec;
    }

    public void setDescRec(String descRec) {
        this.descRec = descRec;
    }

    public String getDescTP() {
        return descTP;
    }

    public void setDescTP(String descTP) {
        this.descTP = descTP;
    }

    public String getPantone() {
        return pantone;
    }

    public void setPantone(String pantone) {
        this.pantone = pantone;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescIns() {
        return descIns;
    }

    public void setDescIns(String descIns) {
        this.descIns = descIns;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }

    public String getCustoPorKg() {
        return custoPorKg;
    }

    public void setCustoPorKg(String custoPorKg) {
        this.custoPorKg = custoPorKg;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
}
