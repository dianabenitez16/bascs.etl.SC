/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.clases.CuotaVictoria;
import etl.bascs.impala.json.ConsultaHttpSC;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CuotasWorkerSC extends SwingWorker<CuotasSC[], String> implements PropertyChangeListener{
    public ConsultaHttpSC consulta;
    private Properties propiedades;
    private Integer cantidad;
    
    private ProductoSC[] productosSC;
    private CuotasSC[] cuotasSC;
    private ProductoSC productoSC; 
    private CuotasSC cuotaSC;
    
    public CuotasWorkerSC(ProductoSC productoSC, Properties propSC){
        this.productoSC = productoSC;
        this.propiedades = propSC;
    }
 @Override
    protected CuotasSC[] doInBackground(){
        synchronized(this){
           if(productoSC.getId() <= 20){  
      try{
         
             setProgress(0);
          consulta = new ConsultaHttpSC("http",
                  propiedades.getProperty("servidor"),
                  propiedades.getProperty("metodoGET"),
                  "/panel/api/loader/productos/" + productoSC.getId() + "/cuotas"
          );
        System.out.println("CUOTA:" + "/panel/api/loader/productos/" + productoSC.getId() + "/cuotas/");

          JSONArray respuesta = consulta.getJason();
          cantidad = consulta.getJason().length();
          ProductoSC prodSC = new ProductoSC();
          cuotasSC = new CuotasSC[respuesta.length()];
          for (int i = 0; i < cuotasSC.length; i++) {
              cuotasSC[i] = new CuotasSC(respuesta.optJSONObject(i).getInt("producto_id"),
                      respuesta.optJSONObject(i).getInt("numero"),
                      respuesta.optJSONObject(i).getInt("importe_cuota"));
            //      System.out.println("PRODUCTO ID: " +respuesta.optJSONObject(i).getInt("importe_cuota"));
              prodSC.setCuotas(cuotasSC);
              setProgress(((i + 1) * 100) / cantidad);
              //Thread.sleep(50); //JUST FOR TESTING
              //publish(producto.getCodigo());
          }

      } catch (Exception ex) {
            Logger.getLogger(CuotasWorkerSC.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
      return cuotasSC;
         }else{
               try {
                   Thread.sleep(60000);
               } catch (InterruptedException ex) {
                   Logger.getLogger(CuotasWorkerSC.class.getName()).log(Level.SEVERE, null, ex);
               }
           }}
        return null;
        
    }
    
  
@Override
    protected void done() {
        System.out.println("Productos obtenidos. Se encontraron "+cuotasSC.length+" cuotas del producto "+productoSC.getId()+" en la website.");
    }
    
    @Override
    protected void process(List<String> prods) {
        for (String prod : prods) {
            System.out.println(prod);
        }
    }
    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

      public ProductoSC obtenerRubro(String codigo){
        for (ProductoSC productoRubro : productosSC) {
            if(productoRubro.getCodigo().equals(codigo)){
                return productoRubro;
            }
        }
        return null;
    }
      public ProductoSC obtenerMarca(String codigo){
        for (ProductoSC productoMarca : productosSC) {
            if(productoMarca.getCodigo().equals(codigo)){
                return productoMarca;
            }
        }
        return null;
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".")+1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".")+1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);
        
        System.out.println(clase+">> "+source+" > "+value);
    }
    
}