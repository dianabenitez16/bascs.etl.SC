/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

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
public class RubrosSC {
    private Integer id;
    private String codigo;
    private String nombre;
    private String parent_id;
    public Integer cantidad;
    public Boolean cargado;

    public RubrosSC(Properties propiedades) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
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
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }

     public void loadJSONConsulta(JSONObject rubrosJ){
      
       try{
          setId((getId() == null ? rubrosJ.optInt("id"):getId()));
          setCodigo((getCodigo() == null ? rubrosJ.optString("codigo_interno_ws"):getCodigo())); 
          setNombre((getNombre() == null ? rubrosJ.optString("nombre"):getNombre())); 
          
          cargado = true;
          cantidad = rubrosJ.length();
        
          } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }     
   }
    
    
}
