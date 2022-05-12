/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.main;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class RubroSC {
    private Integer id;
    private String codigo;
    private String nombre;
    private Integer parent_id;
    public Integer cantidad;
    public Boolean cargado;

    public RubroSC(Properties propiedades) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public RubroSC(JSONObject rubroJ) {
         loadJSONConsulta(rubroJ);
    }

    public RubroSC() {
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
        this.nombre = decodeUFT(nombre);
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

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

     public void loadJSONConsulta(JSONObject rubrosJ){
      
       try{
          setId((getId() == null ? rubrosJ.optInt("id"):getId()));
          setCodigo((getCodigo() == null ? rubrosJ.optString("codigo_interno_ws"):getCodigo())); 
          setNombre((getNombre() == null ? rubrosJ.optString("nombre"):getNombre())); 
          setParent_id((getParent_id() == null ? rubrosJ.optInt("parent_id"):getParent_id())); 
          
          cargado = true;
          cantidad = rubrosJ.length();
        
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
            Logger.getLogger(RubroSC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringLegible;
    }
     
    public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
        object.put("parent_codigo", getParent_id());
  //      System.out.println("parent_id del jason " + getParent_id());
        return object;
    }
    
}
