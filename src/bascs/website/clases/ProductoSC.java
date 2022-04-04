package bascs.website.clases;

import bascs.website.clases.CuotasSC;
import etl.bascs.impala.clases.MarcasSC;
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
    private Double precio;
    private Integer stock;
    
    public Boolean cargado;
    
    private String rubro;
    private CuotasSC[] cuotas;
    private MarcasSC[] marcasSC;
    private RubrosSC[] rubrosSC;
    
     public ProductoSC(Properties propiedades) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.marca = marca;
        this.rubro = rubro;
 
    }     

    public ProductoSC() {
    }
     
    
    public void loadJSONConsulta(JSONObject productoJ){
        try{
   //         setCodigo((getId() == null ? productoJ.optInt("id"):getId()));
            setId((getId() == null ? productoJ.optInt("id"):getId()));
            setCodigo((getCodigo() == null ? productoJ.optString("codigo_interno_ws"):getCodigo()));
            setNombre((getNombre() == null ? productoJ.optString("nombre") : getNombre()));
            setDescripcion((getDescripcion() == null ? productoJ.optString("descripcion") : getDescripcion()));
            setPrecio((getPrecio() == null ? productoJ.optDouble("precio") : getPrecio()));
            setStock((getStock() == null ? productoJ.optInt("stock") : getStock()));
            JSONObject marcaJ = new JSONObject();
            marcaJ = productoJ.getJSONObject("marca");
            setMarca((getMarca() == null ? marcaJ.optString("nombre") : getMarca()));
            JSONObject rubroJ = new JSONObject();
            rubroJ = productoJ.getJSONObject("rubro");
            setRubro((getRubro() == null ? rubroJ.optString("nombre") : getRubro()));
              
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
     public MarcasSC obtenerMarca(String codigo){
        for (MarcasSC marcasSC1 : marcasSC) {
            if(marcasSC1.getCodigo().equals(codigo)){
           
                return marcasSC1;
            }
        }
        return null;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
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

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }

  
           
}
