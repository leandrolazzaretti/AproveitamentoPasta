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
public class MovimentacaoDto {

    private String tipo;
    private int codigo;
    private String descricao;
    private String data;
    private String quantidade;

    public MovimentacaoDto(String tipo, int codigo, String descricao, String data, String quantidade) {
        this.tipo = tipo;
        this.codigo = codigo;
        this.descricao = descricao;
        this.data = data; 
        this.quantidade = quantidade;
    }

    public MovimentacaoDto() {

    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }
}
