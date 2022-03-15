/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ProductoCuotasVictoria {
    private Integer cuota;
    private Double precio_contado;
    private Double precio_cuota;
    private Double precio_credito;

    public ProductoCuotasVictoria(Integer cuota, Double precio_contado, Double precio_cuota, Double precio_credito) {
       try{
            this.cuota = Integer.valueOf(cuota);
            this.precio_contado = Double.valueOf(precio_cuota);
            this.precio_cuota = Double.valueOf(precio_contado);
            this.precio_credito = Double.valueOf(precio_credito);
            
        }catch (NumberFormatException ex){
            System.out.println("ERROR " +ex);
        }
    }

   public Integer getNumero() {
        return cuota;
    }

    public void setNumero(Integer numero) {
        this.cuota = numero;
    }

    public Double getPrecio_contado() {
        return precio_contado;
    }

    public void setPrecio_contado(Double precio_contado) {
        this.precio_contado = precio_contado;
    }

    public Double getPrecio_cuota() {
        return precio_cuota;
    }

    public void setPrecio_cuota(Double precio_cuota) {
        this.precio_cuota = precio_cuota;
    }

    public Double getPrecio_credito() {
        return precio_credito;
    }

    public void setPrecio_credito(Double precio_credito) {
        this.precio_credito = precio_credito;
    }
    
    
}
