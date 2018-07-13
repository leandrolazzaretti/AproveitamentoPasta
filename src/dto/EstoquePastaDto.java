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
public class EstoquePastaDto {

    private int id;
    private int codigo;
    private String descricao;
    private double estoque;
    private double usar;
    private double equivalencia;
    private String vencimento;

    public EstoquePastaDto() {
    }

    public EstoquePastaDto(int id, int codigo, String descricao, double estoque, double usar, double equivalencia, String vencimento) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.estoque= estoque;
        this.usar = usar;
        this.equivalencia = equivalencia;
        this.vencimento = vencimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getEstoque() {
        return estoque;
    }

    public void setEstoque(double estoque) {
        this.estoque = estoque;
    }

    public double getUsar() {
        return usar;
    }

    public void setUsar(double usar) {
        this.usar = usar;
    }

    public double getEquivalencia() {
        return equivalencia;
    }

    public void setEquivalencia(double equivalencia) {
        this.equivalencia = equivalencia;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }
}
