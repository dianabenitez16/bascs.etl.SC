/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.clases.ProductoCuotasVictoria;
import etl.bascs.impala.main;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CuotaSC {
    private Integer id;
    private Integer producto_id;
    private Integer numero;
    private String codigo;
    public CuotasWorkerSC cantidad1;
    private Integer posee_descuento;
    private Integer importe_cuota;
    private Boolean bandera = false;
    private Integer porcentaje_descuento;
    public ArrayList cuotas;
    public Integer cantidad;
    
    public Boolean cargado = false;
    public CuotaSC( int producto_id, int cuotas, int importe_cuota, int posee_descuento, int porcentaje_descuento) {
       
        this.producto_id = producto_id;
        this.numero = cuotas;
        this.importe_cuota = importe_cuota;
        this.porcentaje_descuento = porcentaje_descuento;
        this.posee_descuento = posee_descuento;
    }

    public CuotaSC() {
    }
     public void loadJSONConsulta(JSONObject cuotasJ){
      
        try{
           CuotaSC cuota;
            //setCodigo((getCodigo() == null ? cuotasJ.optString("codigo_interno_ws"):getCodigo())); 
          
            setNumero((getNumero() == null ? cuotasJ.optInt("numero"):getNumero())); 
            setId((getId() == null ? cuotasJ.optInt("id"):getId()));
            setProducto_id((getProducto_id() == null ? cuotasJ.optInt("producto_id"):getProducto_id()));
            setImporte_cuota((getImporte_cuota() == null ? cuotasJ.optInt("importe_cuota"):getImporte_cuota())); 
            setPorcentaje_descuento((getPorcentaje_descuento() == null ? cuotasJ.optInt("porcentaje_descuento"):getPorcentaje_descuento())); 
            setPosee_descuento((getPosee_descuento() == null ? cuotasJ.optInt("posee_descuento"):getPosee_descuento())); 
           
            cuotas = new ArrayList<>();
            cantidad = cuotasJ.length();
       //     System.out.println("CANTIDAD " + cantidad);
            if (cuotasJ.has("data")) {
               JSONArray cuotaJ = cuotasJ.optJSONArray("data");
                bandera = true;
                System.out.println("BANDERA " + bandera);
                for (int i = 0; i < cuotaJ.length(); i++) {
                    cuota = new CuotaSC(
                            cuotaJ.optJSONObject(i).getInt("producto_id"),
                             cuotaJ.optJSONObject(i).getInt("numero"),
                            cuotaJ.optJSONObject(i).getInt("importe_cuota"),
                            cuotaJ.optJSONObject(i).getInt("posee_descuento"),
                            cuotaJ.optJSONObject(i).getInt("porcentaje_descuento"));
                    cuotas.add(cuota);
                   
                //    System.out.println("CUOTAS " + cuota.getCodigo());
                }
            }
            
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

    public ArrayList getCuotas() {
        return cuotas;
    }

    public void setCuotas(ArrayList cuotas) {
        this.cuotas = cuotas;
    }

    public CuotasWorkerSC getCantidad1() {
        return cantidad1;
    }

    public void setCantidad1(CuotasWorkerSC cantidad1) {
        this.cantidad1 = cantidad1;
    }
    
    
}
