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

    private int codigoReceita;
    private String descRec;
    private String descTP;
    private String pantone;
    private int dataVencimento;
    private String descIns;
    private String UM;
    private double consumo;

    public RelatorioReceitaDto() {

    }

    public RelatorioReceitaDto(int codigoReceita, String descRec, String descTP, String pantone, int dataVencimento, String descIns, String UM, double consumo) {
        this.codigoReceita = codigoReceita;
        this.descRec = descRec;
        this.descTP = descTP;
        this.pantone = pantone;
        this.dataVencimento = dataVencimento;
        this.descIns = descIns;
        this.UM = UM;
        this.consumo = consumo;
    }

    public int getCodigoReceita() {
        return codigoReceita;
    }

    public void setCodigoReceita(int codigoReceita) {
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

    public int getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(int dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDescIns() {
        return descIns;
    }

    public void setDescIns(String descIns) {
        this.descIns = descIns;
    }

    public String getUM() {
        return UM;
    }

    public void setUM(String UM) {
        this.UM = UM;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }
}
