/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

import bascs.website.clases.RubrosSC;
import etl.bascs.impala.main;
import etl.bascs.victoria.clases.ProductoWorkerDetalle;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
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
    private Double precio_contado;
    private Integer marca_id;
    private Integer rubro_id;
   
    public Boolean cargado;
    
    public ProductoWorkerDetalle[] cuotad;
    private String rubro;
    public CuotasVictoria[] cuotas;

    public ProductosVictoria() {
    }
    
   
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
            
            
            
            cuotas = new CuotasVictoria[0];
            if(productoJ.has("cuotas")){
                JSONArray cuota = productoJ.optJSONArray("cuotas");
                cuotas = new CuotasVictoria[cuota.length()];
                for (int i = 0; i < cuotas.length; i++) {
            cuotas[i] = new CuotasVictoria(cuota.optJSONObject(i).getInt("cuota"),
                    cuota.optJSONObject(i).getInt("precio_cuota"),
                    cuota.optJSONObject(i).getInt("precio_contado"),
                    cuota.optJSONObject(i).getInt("precio_credito"));
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
            setMarca_id(productoJ.optInt("marca"));
            setRubro_id(productoJ.optInt("rubro"));
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

    public CuotasVictoria[] getCuotas() {
        return cuotas;
    }

    public void setCuotas(CuotasVictoria[] cuotas) {
        this.cuotas = cuotas;
    }

    public Double getPrecio_contado() {
        return precio_contado;
    }

    public void setPrecio_contado(Double precio_contado) {
        this.precio_contado = precio_contado;
    }

    public Integer getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

    public Integer getRubro_id() {
        return rubro_id;
    }

    public void setRubro_id(Integer rubro_id) {
        this.rubro_id = rubro_id;
    }
   
    public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
        object.put("marca_id", getMarca_id());
        object.put("rubro_id", getRubro_id());
        
        object.put("precio", getPrecio_contado());
        System.out.println("rubro_id del jason " + getRubro_id());
        System.out.println("marca_id del jason " + getMarca_id());
        return object;
    }
    
           
}
