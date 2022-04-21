/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.main;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CuotasSC {
    private Integer id;
    private Integer producto_id;
    private Integer numero;
    private String codigo;
    private Integer posee_descuento;
    private Integer importe_cuota;
    private Integer porcentaje_descuento;
    public CuotasSC[] cuotas;
    public CuotasSC cuota;
    public Boolean cargado = false;
    public CuotasSC( int producto_id, int cuotas, int importe_cuota, int posee_descuento, int porcentaje_descuento) {
       
        this.producto_id = producto_id;
        this.numero = cuotas;
        this.importe_cuota = importe_cuota;
        this.porcentaje_descuento = porcentaje_descuento;
        this.posee_descuento = posee_descuento;
    }

    public CuotasSC() {
    }
     public void loadJSONConsulta(JSONObject cuotasJ){
      
        try{
            //setCodigo((getCodigo() == null ? cuotasJ.optString("codigo_interno_ws"):getCodigo())); 
            setNumero((getNumero() == null ? cuotasJ.optInt("numero"):getNumero())); 
            setId((getId() == null ? cuotasJ.optInt("id"):getId()));
            setProducto_id((getProducto_id() == null ? cuotasJ.optInt("producto_id"):getProducto_id()));
            setImporte_cuota((getImporte_cuota() == null ? cuotasJ.optInt("importe_cuota"):getImporte_cuota())); 
            setPorcentaje_descuento((getPorcentaje_descuento() == null ? cuotasJ.optInt("porcentaje_descuento"):getPorcentaje_descuento())); 
            setPosee_descuento((getPosee_descuento() == null ? cuotasJ.optInt("posee_descuento"):getPosee_descuento())); 
            
            cargado = true;
            
        
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }     
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

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
