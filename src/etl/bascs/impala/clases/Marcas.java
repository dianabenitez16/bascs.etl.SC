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
public class Marcas {
    private Properties propiedades;
       public String codigo;
	public String nombre;
        public Boolean cargado;

    public Marcas(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }
	
        
        
        public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return nombre;
	}
	public void setDescripcion(String descripcion) {
		this.nombre = descripcion;
	}

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }
        
	public Marcas(){
            
        }
	public Marcas(Properties propiedades){
            this.codigo = null;
            this.nombre = null;
            this.cargado = false;
        }
           public void loadJSONMaestro(JSONObject productoJ){
        try {
            setCodigo(productoJ.optString("codigo_interno_ws"));
            setDescripcion(productoJ.optString("nombre"));
  
            System.out.println(" json " + productoJ);
            cargado = true;
            System.out.println(" " + cargado);
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
}
