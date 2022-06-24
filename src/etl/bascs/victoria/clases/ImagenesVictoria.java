/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.main;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ImagenesVictoria {
    
    String codigo;
    String imagen;
    Boolean cargado;

    public ImagenesVictoria() {
    }
     public ImagenesVictoria(JSONObject imagenJ) {
      loadJSONDetalle(imagenJ);
    }
    
    
       public void loadJSONDetalle(JSONObject imagenJ) {
        try {
            setImagen((getImagen() == null ? imagenJ.optString("imagen") : getImagen()));
            setCodigo((getCodigo() == null ? imagenJ.optString("codigo_interno_ws") : getCodigo()));
            cargado = true;
            
          } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
          
      }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }
    public JSONObject getJSON() {
        JSONObject object;
        object = new JSONObject();

        object.put("codigo_interno_ws", getCodigo());
        object.put("imagen", getImagen());
      

        return object;
    }
}
