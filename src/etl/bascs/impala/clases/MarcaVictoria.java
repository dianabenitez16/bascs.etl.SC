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
public class MarcaVictoria {
    private String codigo;
    private String nombre;
    public Integer cantidad;
    public Boolean cargado;
    private Integer marca_id;
    
      public MarcaVictoria(Properties propiedades) {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public MarcaVictoria() {
    }

    public MarcaVictoria(JSONObject marcaJ) {
        loadJSONConsulta(marcaJ);
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

    public Integer getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

   public void loadJSONConsulta(JSONObject marcasJ){
      
       try{
          setCodigo((getCodigo() == null ? marcasJ.optString("codigo_interno_ws"):getCodigo())); 
          setNombre((getNombre() == null ? marcasJ.optString("nombre"):getNombre())); 
          
          cargado = true;
          cantidad = marcasJ.length();
        
          } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }     
   }
    public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
        object.put("marca_id", getMarca_id());
        System.out.println("marca_id " + getMarca_id());
        return object;
    }
    
}
