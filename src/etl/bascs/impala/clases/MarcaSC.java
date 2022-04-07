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
    public class MarcaSC {
       private Integer id;
       private String codigo;
       private String nombre;
       private Boolean cargado;
       public Boolean hasid;

    public MarcaSC(Properties propiedades) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

   public MarcaSC(JSONObject marcaJ) {
        loadJSONConsulta(marcaJ);
    }

    public MarcaSC() {
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

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }

    
    public void loadJSONConsulta(JSONObject marcasJ){
      
       try{
          
          setId((getId() == null ? marcasJ.optInt("id"):getId())); 
          setCodigo((getCodigo() == null ? marcasJ.optString("codigo_interno_ws"):getCodigo())); 
          setNombre((getNombre() == null ? marcasJ.optString("nombre"):getNombre())); 
    
          cargado = true;
           
        
          } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }     
   }
}
