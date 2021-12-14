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
public class ProductosBASCs {
public String coditm;
public String descripcion;
public String descripcionlarga;

    public String getCoditm() {
        return coditm;
    }

    public void setCoditm(String coditm) {
        this.coditm = coditm;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcionlarga() {
        return descripcionlarga;
    }

    public void setDescripcionlarga(String descripcionlarga) {
        this.descripcionlarga = descripcionlarga;
    }        
}
