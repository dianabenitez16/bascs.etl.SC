/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.sqllite.clases;

import java.util.Date;

/**
 *
 * @author User
 */
public class Precios {
    
   private String codAlternativo;
   private Integer precio;
   private Date fechaVigencia_desde;
   private Date fechaVigencia_hasta; 
   private String today;

    public String getCodAlternativo() {
        return codAlternativo;
    }

    public void setCodAlternativo(String codAlternativo) {
        this.codAlternativo = codAlternativo;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Date getFechaVigencia_desde() {
        return fechaVigencia_desde;
    }

    public void setFechaVigencia_desde(Date fechaVigencia_desde) {
        this.fechaVigencia_desde = fechaVigencia_desde;
    }

    public Date getFechaVigencia_hasta() {
        return fechaVigencia_hasta;
    }

    public void setFechaVigencia_hasta(Date fechaVigencia_hasta) {
        this.fechaVigencia_hasta = fechaVigencia_hasta;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }
    
}
