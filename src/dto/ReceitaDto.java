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
public class ReceitaDto {

    private int codigorec;
    private String descricao;
    private String pantone;
    private int codigoTipoPasta;
    private int datavencimento;

    public ReceitaDto(int codigorec, String descricao, String pantone, int codigoTipoPasta, int datavencimento) {
        this.codigorec = codigorec;
        this.descricao = descricao;
        this.pantone = pantone;
        this.codigoTipoPasta = codigoTipoPasta;
        this.datavencimento = datavencimento;
    }
    
    public ReceitaDto(){
        
    }

    public int getCodigorec() {
        return codigorec;
    }

    public void setCodigorec(int codigorec) {
        this.codigorec = codigorec;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPantone() {
        return pantone;
    }

    public void setPantone(String pantone) {
        this.pantone = pantone;
    }

    public int getCodigoTipoPasta() {
        return codigoTipoPasta;
    }

    public void setCodigoTipoPasta(int codigoTipoPasta) {
        this.codigoTipoPasta = codigoTipoPasta;
    }

    public int getDatavencimento() {
        return datavencimento;
    }

    public void setDatavencimento(int datavencimento) {
        this.datavencimento = datavencimento;
    }
}
