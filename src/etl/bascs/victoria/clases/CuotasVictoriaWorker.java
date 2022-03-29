/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.CuotasVictoria;
import etl.bascs.impala.clases.ProductoCuotasVictoria;
import etl.bascs.impala.clases.ProductoVictoria;
import etl.bascs.impala.json.ConsultaHttpVictoria;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private ProductoVictoria producto;
    public ProductoCuotasVictoria cuotaV;
    public ProductoCuotasVictoria[] cuotasV;
    public Boolean error;
    
    public CuotasVictoriaWorker(ProductoVictoria producto, Properties propVictoria){
       this.producto = producto;
       //this.cuotaV.setProducto(producto);
       this.propiedades = propVictoria;
       this.error = false;
    }

    public CuotasVictoriaWorker(Properties prop) {
        cuotasV = new ProductoCuotasVictoria[0];
        propiedades = prop;
    }

    @Override
    protected ProductoCuotasVictoria[] doInBackground() throws Exception {
        try {
            setProgress(0);
            consultaV = new ConsultaHttpVictoria("http",
                    propiedades.getProperty("servidor"),
                    propiedades.getProperty("puerto"),
                    propiedades.getProperty("metodoGET"),
                    propiedades.getProperty("cuotas") + cuotaV.getProducto().getCodigo());
            
            if(!consultaV.getError()){
                if(consultaV.getJson().has("cuotas") && consultaV.getJson().has("total")){ 
                    Integer cantidad = consultaV.getJson().getInt("total");
                    //JSONObject cuotaJ = consultaV.getJson().getJSONObject("cuotas");
                    JSONArray cuotas = consultaV.getJson().getJSONArray("cuotas"); 
                    
                    
                    cuotasV = new ProductoCuotasVictoria[cantidad];
                    for (int i = 1; i <= cantidad; i++) {
                        cuotaV.loadJSONConsulta(cuotas.getJSONObject(i));
                        cuotaV.setProducto(producto);
                        cuotasV[i] = cuotaV;
                        System.out.println("CUOTAS WORKER: PRODUCTO: "+cuotaV.getProducto().getCodigo() + " CUOTA: "+cuotaV.getNumero());
                    }
                    
                    setProgress(100);
                }else{
                    error = true;
                    publish("No se obtuvo información al consultar: " + cuotaV.getProducto().getCodigo());
         //           System.out.println("_No se obtubo información al consultar: "+producto.getCodigo());
                }
            }else{
                error = true;
                publish(consultaV.getErrorMessage()+": "+cuotaV.getProducto().getCodigo());
                //System.out.println("_"+consulta.getErrorMessage()+": "+producto.getCodigo());
            }
            //Thread.sleep(5000); //JUST FOR TESTING
        } catch (Exception e) {
            error = true;
            //System.out.println("_Error desconocido al consultar: "+producto.getCodigo());
            publish("Error desconocido al consultar: "+cuotaV.getProducto().getCodigo());
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
