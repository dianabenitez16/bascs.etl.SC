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
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author User
 */
public class RubrosVictoria {
    private String codigo;
    private String nombre;
    private String parent_id;
    public Boolean cargado;
    public RubrosVictoria(Properties propiedades) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nombre = parent_id;
    }

    public RubrosVictoria() {
      
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
        this.nombre = decodeUFT(nombre);
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

   public void loadJSONConsulta(JSONObject rubroJ){
      
       try{
          setCodigo((getCodigo() == null ? rubroJ.optString("codigo_interno_ws"):getCodigo())); 
          setNombre((getNombre() == null ? rubroJ.optString("nombre"):getNombre())); 
   //       setParent_id((getParent_id() == null ? rubroJ.optString("parent_id"):getParent_id())); 
          
          cargado = true;
           
        
          } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }     
   }
   
    public String decodeUFT(String rawString){
        if(rawString == null || rawString.isEmpty()) {
            return "";
        }
        
        String stringLegible = rawString;
        try {
            stringLegible = new String(rawString.getBytes("UTF-8"));
                       
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringLegible;
    }
}
