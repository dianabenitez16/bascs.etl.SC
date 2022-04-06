/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

/**
 *
 * @author User
 */
public class CuotaVictoria {
    private Integer numero;
    private Integer precio_cuota;
    private Integer precio_contado;
    private Integer precio_credito;

    public CuotaVictoria() {
    }

    

    public CuotaVictoria(Integer numero, Integer precio_cuota, Integer precio_contado, Integer precio_credito) {
        try{
            this.numero = Integer.valueOf(numero);
            this.precio_cuota = Integer.valueOf(precio_cuota);
            this.precio_contado = Integer.valueOf(precio_contado);
            this.precio_credito = Integer.valueOf(precio_credito);
        }catch (NumberFormatException ex){
            System.out.println("E R R O R");
        }
        
    }

    
    
    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getPrecio_cuota() {
        return precio_cuota;
    }

    public void setPrecio_cuota(Integer precio_cuota) {
        this.precio_cuota = precio_cuota;
    }

    public Integer getPrecio_contado() {
        return precio_contado;
    }

    public void setPrecio_contado(Integer precio_contado) {
        this.precio_contado = precio_contado;
    }

    public Integer getPrecio_credito() {
        return precio_credito;
    }

    public void setPrecio_credito(Integer precio_credito) {
        this.precio_credito = precio_credito;
    }
    
    
}
