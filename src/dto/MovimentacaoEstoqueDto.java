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
public class MovimentacaoEstoqueDto {

    private int ID;
    private int codigoReceita;
    private String UM;
    private double quantidade;
    private String data;
    private String dataVencimento;

    public MovimentacaoEstoqueDto(int ID, int codigoReceita, String UM, double quantidade, String data, String dataVencimento) {
        this.ID = ID;
        this.codigoReceita = codigoReceita;
        this.UM = UM;
        this.quantidade = quantidade;
        this.data = data;
        this.dataVencimento = dataVencimento;
    }

    public MovimentacaoEstoqueDto() {

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

    public String getUM() {
        return UM;
    }

    public void setUM(String UM) {
        this.UM = UM;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
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
}
