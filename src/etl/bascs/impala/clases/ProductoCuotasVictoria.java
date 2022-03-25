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
    private String codigo;
    private Double precio_contado;
    private Double precio_cuota;
    private Double precio_credito;
    public Boolean cargado;
    public Integer cantidad;

    
    public ProductoCuotasVictoria(Properties propiedades) {
       try{
            this.numero = Integer.valueOf(numero);
            this.precio_contado = Double.valueOf(precio_cuota);
            this.precio_cuota = Double.valueOf(precio_contado);
            this.precio_credito = Double.valueOf(precio_credito);
            
        }catch (NumberFormatException ex){
            System.out.println("ERROR " +ex);
        }
    }

    ProductoCuotasVictoria(int numero, double precio_contado, double precio_cuota, double precio_credito) {
            this.numero = numero;
            this.precio_contado = precio_cuota;
            this.precio_cuota = precio_contado;
            this.precio_credito = precio_credito;
           
    }
    
   public void loadJSONConsulta(JSONObject cuotasJ){
      
       try{
          setCodigo((getCodigo() == null ? cuotasJ.optString("codigo_interno_ws"):getCodigo())); 
          setNumero((getNumero() == null ? cuotasJ.optInt("numero"):getNumero())); 
          setPrecio_cuota((getPrecio_cuota() == null ? cuotasJ.optDouble("precio_cuota"):getPrecio_cuota())); 
          setPrecio_contado((getPrecio_contado() == null ? cuotasJ.optDouble("precio_contado"):getPrecio_contado())); 
          setPrecio_credito((getPrecio_credito() == null ? cuotasJ.optDouble("precio_credito"):getPrecio_credito())); 
          
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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    
}
