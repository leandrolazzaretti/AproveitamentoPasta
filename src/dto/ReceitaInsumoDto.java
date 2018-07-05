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
public class ReceitaInsumoDto {
    private int id;
    private int codigoReceita;
    private int codigoInsumo;
    private double consumo;
    
    public ReceitaInsumoDto(int id, int codigoReceita,  int codigoInsumo, double consumo){
        this.id = id;
        this.codigoReceita = codigoReceita;
        this.codigoInsumo = codigoInsumo;
        this.consumo = consumo;
    }
    
    public ReceitaInsumoDto(){
        
    }

    public int getCodigoReceita() {
        return codigoReceita;
    }

    public void setCodigoReceita(int codigoReceita) {
        this.codigoReceita = codigoReceita;
    }

    public int getCodigoInsumo() {
        return codigoInsumo;
    }

    public void setCodigoInsumo(int codigoInsumo) {
        this.codigoInsumo = codigoInsumo;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
}
