/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

/**
 *
 * @author User
 */
public class CuotasSC {
    private Integer id;
    private Integer producto_id;
    private Integer numero;
    private Integer posee_descuento;
    private Integer importe_cuota;
    private Integer porcentaje_descuento;

    public CuotasSC( Integer producto_id, Integer cuotas, Integer importe_cuota) {
       
        this.producto_id = producto_id;
        this.numero = cuotas;
        this.importe_cuota = importe_cuota;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer cuotas) {
        this.numero = cuotas;
    }

    public Integer getPosee_descuento() {
        return posee_descuento;
    }

    public void setPosee_descuento(Integer posee_descuento) {
        this.posee_descuento = posee_descuento;
    }

    public Integer getImporte_cuota() {
        return importe_cuota;
    }

    public void setImporte_cuota(Integer importe_cuota) {
        this.importe_cuota = importe_cuota;
    }

    public Integer getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(Integer porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }
    
    
}
