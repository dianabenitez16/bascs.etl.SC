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
    private Integer parent_id;
    private String parent_codigo;
    public Boolean cargado;
    public RubrosVictoria(Properties propiedades) {
        this.codigo = codigo;
        this.nombre = nombre;
        //this.nombre = parent_id;
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

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_codigo() {
        return parent_codigo;
    }

    public void setParent_codigo(String parent_codigo) {
        this.parent_codigo = parent_codigo;
    }
    
    
    public void loadJSONConsulta(JSONObject rubroJ){
        try{
            setCodigo((getCodigo() == null ? rubroJ.optString("codigo_interno_ws"):getCodigo())); 
            setNombre((getNombre() == null ? rubroJ.optString("nombre"):getNombre())); 
            setParent_codigo((getParent_codigo() == null ? rubroJ.optString("parent_codigo"):getParent_codigo())); 
           
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
            Logger.getLogger(RubrosVictoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringLegible;
    }
    
    public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
        object.put("parent_codigo", getParent_id());
        System.out.println("parent_id del jason " + getParent_id());
        return object;
    }
    
}
