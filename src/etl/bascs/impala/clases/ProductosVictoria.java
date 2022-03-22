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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ProductosVictoria {
    private String codigo;
    private String descripcion;
    private String nombre;
    private String marca;
    public Boolean cargado;
    
    private String rubro;
    private ProductoCuotasVictoria[] cuotas;
     public ProductosVictoria(Properties propiedades) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.rubro = rubro;
 
    }     
    public void loadJSONConsulta(JSONObject productoJ){
        try{
            
            setCodigo((getCodigo() == null ? productoJ.optString("codigo_interno_ws"):getCodigo()));
            setNombre((getNombre() == null ? productoJ.optString("nombre") : getNombre()));
            setDescripcion((getDescripcion() == null ? productoJ.optString("descripcion") : getDescripcion()));
            setMarca((getMarca() == null ? productoJ.optString("marca") : getMarca()));
            setRubro((getRubro() == null ? productoJ.optString("rubro") : getRubro()));
            
            cuotas = new ProductoCuotasVictoria[0];
            if(productoJ.has("cuota")){
                JSONArray cuota = productoJ.optJSONArray("cuota");
                cuotas = new ProductoCuotasVictoria[cuota.length()];
                for (int i = 0; i < cuotas.length; i++) {
                    
            cuotas[i] = new ProductoCuotasVictoria(cuota.optJSONObject(i).getInt("cuota"),
                    cuota.optJSONObject(i).getDouble("precio_contado"),
                    cuota.optJSONObject(i).getDouble("precio_cuota"),
                    cuota.optJSONObject(i).getDouble("precio_credito"));
                    System.out.println("CUOTAS " + cuotas[i]);
                }
            }
            
           cargado = true;
            } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
 }
     public void loadJSONMaestro(JSONObject productoJ){
        try {
            setCodigo(productoJ.optString("codigo_interno_ws"));
            System.out.println("CODIGO DEL MAESTRO "  + productoJ.optString("codigo_interno_ws"));
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

    public ProductoCuotasVictoria[] getCuotas() {
        return cuotas;
    }

    public void setCuotas(ProductoCuotasVictoria[] cuotas) {
        this.cuotas = cuotas;
    }
    
    
           
}
