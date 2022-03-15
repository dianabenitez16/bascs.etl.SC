/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.worker;

import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.json.ConsultaHttp;
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
 * @author Juan Bogado
 */
public class MaestroWorker extends SwingWorker<Producto[], String> implements PropertyChangeListener{
    public ConsultaHttp consulta;
    private Properties propiedades;
    private Integer cantidad;
    
    private Producto[] productos;
    private Producto producto;

    public MaestroWorker(Properties prop) {
        productos = new Producto[0];
        propiedades = prop;
        
    }  
    
    @Override
    protected Producto[] doInBackground() {
        try {
            setProgress(0);
            consulta = new ConsultaHttp("http",
                    propiedades.getProperty("servidor"),
                    propiedades.getProperty("puerto"),
                    propiedades.getProperty("metodo"),
                    propiedades.getProperty("usuario"),
                    propiedades.getProperty("clave"),
                    propiedades.getProperty("maestro"),
                    "user="+propiedades.getProperty("cliente"));
            //Thread.sleep(5000); //JUST FOR TESTING
            if(!consulta.getError()){
                if(consulta.getJson().has("count")){
                    cantidad = consulta.getJson().getInt("count");
                }else{
                    publish("No se encontraron productos en el maestro.");
                }
                if(cantidad > 0){
                    if(consulta.getJson().has("data")){
                        JSONArray respuesta = consulta.getJson().getJSONArray("data");
                        productos = new Producto[respuesta.length()];
                        
                        for (int i = 0; i < respuesta.length(); i++) {
                            Iterator keys = respuesta.getJSONObject(i).keys();
                            String key = keys.next().toString();
                            JSONObject productoJ = respuesta.getJSONObject(i).getJSONObject(key);
                            producto = new Producto(propiedades);
                            producto.loadJSONConsulta(productoJ);
                            productos[i] = producto;
                            setProgress(((i+1)*100)/cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                            //publish(producto.getCodigo());
                        }
                    }else{
                        publish("No se encontraron productos en el maestro.");
                    }
                }else{
                    publish("No se encontraron productos en el maestro.");
                }
            }else{
                publish("Error");
                System.out.println("_ERROR");
            }
        } catch (Exception ex) {
            Logger.getLogger(MaestroWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productos;
    }
    
    @Override
    protected void done() {
        //System.out.println("_Maestro obtenido. Se encontraron "+productos.length+" productos.");
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
