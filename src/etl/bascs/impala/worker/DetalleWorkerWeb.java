/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.worker;

import etl.bascs.impala.clases.ProductosVictoria;
import etl.bascs.impala.json.ConsultaHttpVictoria;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingWorker;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class DetalleWorkerWeb extends SwingWorker<ProductosVictoria, String> implements PropertyChangeListener {
public ConsultaHttpVictoria consulta;
public Integer id;
public ProductosVictoria producto;
public Boolean error;
public Properties propiedades;

  public DetalleWorkerWeb(ProductosVictoria producto, Properties propVictoria) {
        this.producto = producto;
         this.propiedades = propVictoria;
         this.error = false;
       
    }  

    @Override
    protected ProductosVictoria doInBackground() throws Exception {
          try {
            setProgress(0);
            consulta = new ConsultaHttpVictoria("http", 
               propiedades.getProperty("servidor"), 
               propiedades.getProperty("puerto"),         
               propiedades.getProperty("metodo"),
                propiedades.getProperty("recursos"));
            if(!consulta.getError()){
                if(!consulta.getJson().isEmpty()){
                    JSONObject productoJ = consulta.getJson().getJSONObject("products");
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
        return producto; //To change body of generated methods, choose Tools | Templates.
    }
   @Override
    protected void process(List<String> chunks) {
        for (String key : chunks) {
            System.out.println(key);
        }
    }
       public Properties getPropVictoria() {
        return propiedades;
    }

    public void setPropVictoria(Properties propVictoria) {
        this.propiedades = propVictoria;
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