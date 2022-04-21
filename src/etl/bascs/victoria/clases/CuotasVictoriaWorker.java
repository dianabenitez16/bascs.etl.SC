/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.ProductoCuotasVictoria;
import etl.bascs.impala.clases.ProductoVictoria;
import etl.bascs.impala.json.ConsultaHttpVictoria;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CuotasVictoriaWorker extends SwingWorker<ProductoCuotasVictoria[], String > implements PropertyChangeListener{
    public ConsultaHttpVictoria consultaV;
    public Properties propiedades;
    public Integer cantidad;
    public Boolean get = true;
    public String codigo;
    public Integer id;
    public ProductoVictoria producto;
    public ProductoCuotasVictoria cuotaV;
    public ProductoCuotasVictoria[] cuotasV;
    public Boolean error;
    public Boolean cargado;
    
    public CuotasVictoriaWorker(ProductoVictoria producto, Properties propVictoria){
       this.producto = producto;
       //this.cuotaV.setProducto(producto);
       this.propiedades = propVictoria;
       this.error = false;
    }

    public CuotasVictoriaWorker() {
    }
    
    public CuotasVictoriaWorker(Properties prop) {
        cuotasV = new ProductoCuotasVictoria[0];
        propiedades = prop;
    }

    @Override
    protected ProductoCuotasVictoria[] doInBackground(){
        try {
            setProgress(0);
            consultaV = new ConsultaHttpVictoria("http",
                    propiedades.getProperty("servidor"),
                    propiedades.getProperty("puerto"),
                    propiedades.getProperty("metodoGET"),
                    propiedades.getProperty("cuotas"));
            
              
                if(consultaV.getJson().has("total")){
                    cantidad = consultaV.getJson().getInt("total");
                }else{
                    publish("No se encontraron productos en el maestro.");
                }
               if(cantidad > 0){
                    if(consultaV.getJson().has("cuotas")){
                        JSONArray respuesta = consultaV.getJson().getJSONArray("cuotas");
                        cuotasV = new ProductoCuotasVictoria[respuesta.length()];
                       for (int i = 0; i < respuesta.length(); i++) {
                            JSONObject cuotasJ = respuesta.getJSONObject(i);
                            cuotaV = new ProductoCuotasVictoria();
                            cuotaV.loadJSONConsulta(cuotasJ);
                            cuotasV[i] = cuotaV;
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
          
            //Thread.sleep(5000); //JUST FOR TESTING
        } catch (Exception e) {
            error = true;
            //System.out.println("_Error desconocido al consultar: "+producto.getCodigo());
            System.out.println("ERROR" +e);
            publish("Error desconocido al consultar: "+cuotaV.getProducto().getCodigo());
            //Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        return cuotasV; //To change body of generated methods, choose Tools | Templates.
    }

   @Override
     protected void done() {
        System.out.println("Cuotas obtenidas. Se encontraron "+cuotasV.length+"");
        
    }
  
  
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

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
    public ProductoCuotasVictoria obtenerCuota(Integer numeroCuota){
        for (ProductoCuotasVictoria cuotaV : cuotasV) {
            if(cuotaV.getNumero().equals(numeroCuota)){
                return cuotaV;
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
