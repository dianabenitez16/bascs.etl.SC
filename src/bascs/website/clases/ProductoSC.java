package bascs.website.clases;

import bascs.website.clases.CuotasSC;
import etl.bascs.impala.main;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author User
 */
public class ProductoSC{
    private Integer id;
    private String codigo;
    private String descripcion;
    private String nombre;
    private String marca;
    public Boolean cargado;
    
    private String rubro;
    private CuotasSC[] cuotas;
    
     public ProductoSC(Properties propiedades) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.rubro = rubro;
 
    }     
    public void loadJSONConsulta(JSONObject productoJ){
        try{
   //         setCodigo((getId() == null ? productoJ.optInt("id"):getId()));
            setCodigo((getCodigo() == null ? productoJ.optString("codigo_interno_ws"):getCodigo()));
            setNombre((getNombre() == null ? productoJ.optString("nombre") : getNombre()));
            setDescripcion((getDescripcion() == null ? productoJ.optString("descripcion") : getDescripcion()));
            setMarca((getMarca() == null ? productoJ.optString("marca") : getMarca()));
            setRubro((getRubro() == null ? productoJ.optString("rubro") : getRubro()));
              
            cargado = true;
            } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
 }
     public void loadJSONMaestro(JSONObject productoJ){
        try {
            setId(productoJ.optInt("id"));
            setCodigo(productoJ.optString("codigo_interno_ws"));
            setNombre(productoJ.optString("nombre"));
            setDescripcion(productoJ.optString("descripcion"));
            setMarca(productoJ.optString("marca"));
            setRubro(productoJ.optString("rubro"));
            cargado = true;
            System.out.println(" " + cargado);
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

 
    
           
}
