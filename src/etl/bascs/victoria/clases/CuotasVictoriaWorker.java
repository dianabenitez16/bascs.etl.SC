/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.CuotasVictoria;
import etl.bascs.impala.clases.ProductoCuotasVictoria;
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
public class CuotasVictoriaWorker extends SwingWorker<ProductoCuotasVictoria[], String > implements PropertyChangeListener{
    public ConsultaHttpVictoria consultaV;
    public Properties propiedades;
    public Integer cantidad;
    public Boolean get = true;
    public String codigo;
    public ProductoCuotasVictoria cuotaV;
    public ProductoCuotasVictoria[] cuotasV;
    public Boolean error;
    
    public CuotasVictoriaWorker(ProductoCuotasVictoria cuota, Properties propVictoria){
       this.cuotaV = cuota;
       this.propiedades = propVictoria;
       this.error = false;
    }

    public CuotasVictoriaWorker(Properties prop) {
     cuotasV = new ProductoCuotasVictoria[0];
     propiedades = prop;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ProductoCuotasVictoria[] doInBackground() throws Exception {
        
        try {
            setProgress(0);
           consultaV = new ConsultaHttpVictoria("http",
                   propiedades.getProperty("servidor"),
                    propiedades.getProperty("puerto"),
                  propiedades.getProperty("metodoGET"),
                    propiedades.getProperty("cuotas")+cuotaV.getCodigo());
                       if(!consultaV.getError()){
                if(consultaV.getJson().has("cuotas")){ 
                    JSONObject cuotaJ = consultaV.getJson().getJSONObject("cuotas");
                    cuotaV.loadJSONConsulta(cuotaJ);
                    System.out.println("cuotaJ " + cuotaJ.toString());
                    setProgress(100);
                }else{
                    error = true;
                    publish("No se obtuvo información al consultar: " + cuotaV.getCodigo());
         //           System.out.println("_No se obtubo información al consultar: "+producto.getCodigo());
                }
            }else{
                error = true;
                publish(consultaV.getErrorMessage()+": "+cuotaV.getCodigo());
                //System.out.println("_"+consulta.getErrorMessage()+": "+producto.getCodigo());
            }
            //Thread.sleep(5000); //JUST FOR TESTING
        } catch (Exception e) {
            error = true;
            //System.out.println("_Error desconocido al consultar: "+producto.getCodigo());
            publish("Error desconocido al consultar: "+cuotaV.getCodigo());
            //Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        return cuotasV; //To change body of generated methods, choose Tools | Templates.
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

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
     public ProductoCuotasVictoria obtenerRubro(String codigo){
        for (ProductoCuotasVictoria rubrosSC1 : cuotasV) {
            if(rubrosSC1.getCodigo().equals(codigo)){
                return rubrosSC1;
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
