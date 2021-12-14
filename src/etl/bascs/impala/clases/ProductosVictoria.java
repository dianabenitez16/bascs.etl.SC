/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;
import etl.bascs.impala.main;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ProductosVictoria {
    private Properties propiedades;
    
    private Integer id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer stock;
    
    public Boolean cargado;
    
      public ProductosVictoria(Properties propiedades) {
        this.id = null;
        this.codigo = null;
        this.nombre = null;
        this.descripcion = null;
        this.stock = null;
        this.cargado = false;
    }

 public void loadJSONConsulta(JSONObject productoJ){
        try {
            setCodigo((getCodigo() == null ? productoJ.optString("product_code"):getCodigo()));
            setNombre((getNombre() == null ? productoJ.optString("descripcion") : getNombre()));
            // Se rediseña este tipo de linea, para considerar que previamente se le haya asignado un valor, desdel el maestro por ejemplo
            setDescripcion((getDescripcion() == null ? productoJ.optString("description") : (getDescripcion().isEmpty() ? productoJ.optString("description") : getDescripcion())));
          
            
            cargado = true;
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    public Properties getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(Properties propiedades) {
        this.propiedades = propiedades;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = this.codigo = codigo.replace("/", "");
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
            
            this.nombre = nombre;
        
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
         
            this.descripcion = descripcion;
        }
    

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }
      

     public static void main(String[] args) {
     
     }
}
