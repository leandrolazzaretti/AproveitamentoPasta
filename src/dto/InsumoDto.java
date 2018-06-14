/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.math.BigDecimal;

/**
 *
 * @author Leandro
 */
public class InsumoDto {

    private int codigo;
    private String descricao;
    private String um;
    private BigDecimal quantidade;
    private double preco;

    public InsumoDto(int codigo, String descricao, String um, BigDecimal quantidade, double preco) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.um = um;
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

    public String getUm() {
        return um;
    }

    public void setUm(String um) {
        this.um = um;
    }

    public BigDecimal getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(BigDecimal quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    
    
}
