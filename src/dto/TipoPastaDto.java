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
public class TipoPastaDto {
    private int codigo;
    private String descricao;
    
    public TipoPastaDto(int codigo, String descricao){
        this.codigo = codigo;
        this.descricao = descricao;       
    }
    
    public TipoPastaDto(){
        
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
    
}
