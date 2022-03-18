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
    private Integer cuotas;
    private Integer posee_descuento;
    private Double importe_cuota;
    private Double porcentaje_descuento;

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

    public Integer getCuotas() {
        return cuotas;
    }

    public void setCuotas(Integer cuotas) {
        this.cuotas = cuotas;
    }

    public Integer getPosee_descuento() {
        return posee_descuento;
    }

    public void setPosee_descuento(Integer posee_descuento) {
        this.posee_descuento = posee_descuento;
    }

    public Double getImporte_cuota() {
        return importe_cuota;
    }

    public void setImporte_cuota(Double importe_cuota) {
        this.importe_cuota = importe_cuota;
    }

    public Double getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(Double porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }
    
    
}
