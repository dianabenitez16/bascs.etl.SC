/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

import etl.bascs.impala.main;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ProductoCuotasVictoria {
    private Integer numero;
    private ProductoVictoria producto;
    private Double precio_contado;
    private Double precio_cuota;
    private Double precio_credito;
    private Integer porcentaje_descuento;
    private Integer posee_descuento;
    public Boolean cargado;
    public String codigo;
    public Integer cantidad;
    public Integer producto_id;

    
    public ProductoCuotasVictoria(Properties propiedades) {
      
    }

    ProductoCuotasVictoria(int numero, double precio_contado, double precio_cuota, double precio_credito, int posee_descuento, int porcentaje_descuento) {
            this.numero = numero;
            this.precio_contado = precio_cuota;
            this.precio_cuota = precio_contado;
            this.precio_credito = precio_credito;
            this.porcentaje_descuento = porcentaje_descuento;
            this.posee_descuento = posee_descuento;
           
    }

    public ProductoCuotasVictoria() {
    }
    
    public void loadJSONConsulta(JSONObject cuotasJ){
      
        try{
            //setCodigo((getCodigo() == null ? cuotasJ.optString("codigo_interno_ws"):getCodigo())); 
            setNumero((getNumero() == null ? cuotasJ.optInt("numero"):getNumero())); 
            setCodigo((getCodigo() == null ? cuotasJ.optString("codigo_interno_ws"):getCodigo()));
            setPrecio_cuota((getPrecio_cuota() == null ? cuotasJ.optDouble("precio_cuota"):getPrecio_cuota()));
            setPosee_descuento((getPosee_descuento() == null ? cuotasJ.optInt("posee_descuento"):getPosee_descuento()));
            setPorcentaje_descuento((getPorcentaje_descuento() == null ? cuotasJ.optInt("porcentaje_descuento"):getPorcentaje_descuento()));
            
            cargado = true;
            cantidad = cuotasJ.length();
        
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }     
   } 
   public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
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

    public ProductoVictoria getProducto() {
        return producto;
    }

    public void setProducto(ProductoVictoria producto) {
        this.producto = producto;
    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Integer getPorcentaje_descuento() {
        return porcentaje_descuento;
    }

    public void setPorcentaje_descuento(Integer porcentaje_descuento) {
        this.porcentaje_descuento = porcentaje_descuento;
    }

    public Integer getPosee_descuento() {
        return posee_descuento;
    }

    public void setPosee_descuento(Integer posee_descuento) {
        this.posee_descuento = posee_descuento;
    }
    
      public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
              
        object.put("importe_cuota", getPrecio_cuota());
        object.put("numero", getNumero());
        object.put("posee_descuento", getPosee_descuento());
        object.put("porcentaje_descuento", getPorcentaje_descuento());
      
        return object;
        
        
          
    }
    
}
