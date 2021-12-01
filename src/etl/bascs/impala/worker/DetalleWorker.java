/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.worker;

import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.json.ConsultaHttp;
import etl.bascs.impala.main;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.json.JSONObject;

/**
 *
 * @author Juan Bogado
 */
public class DetalleWorker extends SwingWorker<Producto, String> implements PropertyChangeListener {
    public ConsultaHttp consulta;
    public Properties propiedades;
    public Integer id;
    public Producto producto;
    public Boolean error;
    
    public DetalleWorker(Producto producto, Properties propImpala) {
        this.producto = producto;
        this.propiedades = propImpala;
        this.error = false;
    }  
    
    @Override
    protected Producto doInBackground() {
        //producto = new Producto();
        try {
            setProgress(0);
            consulta = new ConsultaHttp("http", 
                propiedades.getProperty("servidor"), 
                propiedades.getProperty("puerto"), 
                propiedades.getProperty("metodo"), 
                propiedades.getProperty("usuario"), 
                propiedades.getProperty("clave"), 
                propiedades.getProperty("detalle"), 
                "user="+propiedades.getProperty("cliente")+"&product_id="+producto.getCodigo());
            if(!consulta.getError()){
                if(consulta.getJson().has("data")){
                    JSONObject productoJ = consulta.getJson().getJSONObject("data");
                    producto.loadJSONConsulta(productoJ);
                    setProgress(100);
                }else{
                    error = true;
                    publish("No se obtuvo información al consultar: "+producto.getCodigo());
                    //System.out.println("_No se obtubo información al consultar: "+producto.getCodigo());
                }
            }else{
                error = true;
                publish(consulta.getErrorMessage()+": "+producto.getCodigo());
                //System.out.println("_"+consulta.getErrorMessage()+": "+producto.getCodigo());
            }
            //Thread.sleep(5000); //JUST FOR TESTING
        } catch (Exception e) {
            error = true;
            //System.out.println("_Error desconocido al consultar: "+producto.getCodigo());
            publish("Error desconocido al consultar: "+producto.getCodigo());
            //Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        return producto;
    }
        
    @Override
    protected void process(List<String> chunks) {
        for (String key : chunks) {
            System.out.println(key);
        }
    }

    public Properties getPropImpala() {
        return propiedades;
    }

    public void setPropImpala(Properties propImpala) {
        this.propiedades = propImpala;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
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
