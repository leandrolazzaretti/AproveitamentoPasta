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

    private int codigo;
    private String descricao;
    private String pantone;
    private int tipo;
    private int vencimento;

    public ReceitaDto(int codigo, String descricao, String pantone, int tipo, int vencimento) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.pantone = pantone;
        this.tipo = tipo;
        this.vencimento = vencimento;
    }
    
    public ReceitaDto(){
        
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

    public String getPantone() {
        return pantone;
    }

    public void setPantone(String pantone) {
        this.pantone = pantone;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getVencimento() {
        return vencimento;
    }

    public void setVencimento(int vencimento) {
        this.vencimento = vencimento;
    }
}
