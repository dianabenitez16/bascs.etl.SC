/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

import bascs.website.clases.RubroSC;
import etl.bascs.impala.main;
import etl.bascs.victoria.clases.CuotasVictoriaWorker;
import etl.bascs.victoria.clases.ProductoVictoriaWorker;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ProductoVictoria {
    
    
    private String codigo;
    private String descripcion;
    private String nombre;
    private String marca;
    private Integer precio_contado;
    private Integer marca_id;
    private Integer stock ;
    private Integer producto_id;
    public Boolean cargado = false;
    
    public ProductoVictoriaWorker[] cuotad;
    
    public CuotaVictoria[] cuotas;
    public CuotasVictoriaWorker cuotasW;
   
    private RubroVictoria rubroVictoria; 
    private RubroSC rubroSC; 
    
    private MarcasVictoria marcaVictoria;
    private MarcasSC marcaSC;
    

    public ProductoVictoria() {
    }

   
    
    public void loadJSONDetalle(JSONObject productoJ){
        try{
            
            setCodigo((getCodigo() == null ? productoJ.optString("codigo_interno_ws"):getCodigo()));
            setNombre((getNombre() == null ? productoJ.optString("nombre") : getNombre()));
            setDescripcion((getDescripcion() == null ? productoJ.optString("descripcion") : getDescripcion()));
            setMarca((getMarca() == null ? productoJ.optString("marca") : getMarca()));
            
            setRubroVictoria(new RubroVictoria(productoJ.getJSONObject("rubro")));
            
            setPrecio_contado((getPrecio_contado() == null ? productoJ.optInt("precio") : getPrecio_contado()));
         
          
             
            cuotas = new CuotaVictoria[0];
            if(productoJ.has("cuotas")){
                JSONArray cuota = productoJ.optJSONArray("cuotas");
                cuotas = new CuotaVictoria[cuota.length()];
                for (int i = 0; i < cuotas.length; i++) {
            cuotas[i] = new CuotaVictoria(cuota.optJSONObject(i).getInt("numero"),
                    cuota.optJSONObject(i).getInt("precio_cuota"),
                    cuota.optJSONObject(i).getInt("precio_contado"),
                    cuota.optJSONObject(i).getInt("precio_credito"));
                    //System.out.println("CUOTAS " + cuotas[i]);
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
            //System.out.println("CODIGO DEL MAESTRO "  + productoJ.optString("codigo_interno_ws"));
            setNombre(productoJ.optString("nombre"));
            setDescripcion(productoJ.optString("descripcion"));
            setMarca_id(productoJ.optInt("marca"));
            setRubroVictoria(new RubroVictoria(productoJ.getJSONObject("rubro")));
            /*
            RubroSC rubroSC = new RubroSC();
            rubroSC.setId(productoJ.optInt("rubro"));
            setRubroSC(rubroSC);
            */
            cargado = true;
            System.out.println(" " + cargado);
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

     
    public RubroVictoria getRubroVictoria() {
        return rubroVictoria;
    }

    public void setRubroVictoria(RubroVictoria rubroVictoria) {
        this.rubroVictoria = rubroVictoria;
    }

    public RubroSC getRubroSC() {
        return rubroSC;
    }

    public void setRubroSC(RubroSC rubroSC) {
        this.rubroSC = rubroSC;
    }
     
     
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = decodeUFT(codigo);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = decodeUFT(descripcion);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = decodeUFT(nombre);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    
    
    public CuotaVictoria[] getCuotas() {
        return cuotas;
    }

    public void setCuotas(CuotaVictoria[] cuotas) {
        this.cuotas = cuotas;
    }

    public Integer getPrecio_contado() {
        return precio_contado;
    }

    public void setPrecio_contado(Integer precio_contado) {
        this.precio_contado = precio_contado;
    }

   
    public Integer getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

    

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
    
    
    public String decodeUFT(String rawString){
        if(rawString == null || rawString.isEmpty()) {
            return "";
        }
        
        String stringLegible = rawString;
        try {
            stringLegible = new String(rawString.getBytes("UTF-8"));
                       
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ProductoVictoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringLegible;
    }
    
    public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
        
        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
    //    object.put("descripcion", getDescripcion());
        object.put("marca_id", getMarca_id());
        object.put("rubro_id", getRubroSC().getId());
        object.put("precio", getPrecio_contado());
        object.put("stock",  "10");
        
        
        return object;
    }
    
           
}
