/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.json.ConsultaHttpSC;
import etl.bascs.victoria.clases.RubrosWorker;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class ProductoWorkerSC extends SwingWorker<ProductoSC[], String> implements PropertyChangeListener{
    public ConsultaHttpSC consulta;
    private Properties propiedades;
    private Integer cantidad;
    
    private ProductoSC[] productosSC;
    public ProductoSC productoSC; 
   
    
    public ProductoWorkerSC(Properties prop){
        productosSC = new ProductoSC[0];
        propiedades = prop;
    }

   
    @Override
    protected ProductoSC[] doInBackground(){
      try{
             setProgress(0);
             consulta = new ConsultaHttpSC("http",
             propiedades.getProperty("servidor"),
             propiedades.getProperty("metodoGET"),
             propiedades.getProperty("productos")
             );
        
                JSONArray respuesta = consulta.getJason();
               cantidad = consulta.getJason().length();
               productosSC = new ProductoSC[respuesta.length()];
                         for (int i = 0; i < respuesta.length(); i++) {
                            JSONObject object = respuesta.getJSONObject(i);
                            productoSC = new ProductoSC(propiedades);
                            productoSC.loadJSONConsulta(object);
                            productosSC[i] = productoSC;
                            setProgress(((i+1)*100)/cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                            //publish(producto.getCodigo());
                        }
                         
                } catch (Exception ex) {
            Logger.getLogger(RubrosWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
        return productosSC;
    
    }
@Override
    protected void done() {
        System.out.println("Productos obtenidos. Se encontraron "+productosSC.length+" producto de la website.");
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

    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".")+1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".")+1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);
        
        System.out.println(clase+">> "+source+" > "+value);
    }
    
}