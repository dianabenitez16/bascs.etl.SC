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
public class CuotasVictoria {
    private Integer cuota;
    private Integer precio_cuota;
    private Integer precio_contado;
    private Integer precio_credito;

    public CuotasVictoria(Integer cuota, Integer precio_cuota, Integer precio_contado, Integer precio_credito) {
        this.cuota = cuota;
        this.precio_cuota = precio_cuota;
        this.precio_contado = precio_contado;
        this.precio_credito = precio_credito;
    }

    public CuotasVictoria(String cuota, String precio_cuota, String precio_contado, String precio_credito) {
        try{
            this.cuota = Integer.valueOf(cuota);
            this.precio_contado = Integer.valueOf(precio_cuota);
            this.precio_credito = Integer.valueOf(precio_contado);
            this.precio_credito = Integer.valueOf(precio_credito);
        }catch (NumberFormatException ex){
            System.out.println("E R R O R");
        }
        
    }

    
    
    public Integer getCuota() {
        return cuota;
    }

    public void setCuota(Integer cuota) {
        this.cuota = cuota;
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
