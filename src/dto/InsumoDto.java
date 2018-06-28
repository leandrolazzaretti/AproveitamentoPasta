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
public class InsumoDto {

    private int codigo;
    private String descricao;
    private String UM;
    private String quantidade;
    private String preco;

    public InsumoDto(int codigo, String descricao, String UM, String quantidade, String preco) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.UM = UM;
        this.quantidade = quantidade;
        this.preco = preco;
    }
    
    public InsumoDto(){
        
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

    public String getUM() {
        return UM;
    }

    public void setUM(String UM) {
        this.UM = UM;
    }

    public String getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(String quantidade) {
        this.quantidade = quantidade;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
    
    
}
