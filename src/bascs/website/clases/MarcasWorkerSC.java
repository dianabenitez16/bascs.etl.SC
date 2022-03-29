/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.victoria.clases.RubrosVictoriaWorker;
import etl.bascs.impala.clases.MarcasSC;
import etl.bascs.impala.json.ConsultaHttpSC;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class MarcasWorkerSC extends SwingWorker<MarcasSC[], String> implements PropertyChangeListener{

    public ConsultaHttpSC consulta;
    private Properties propiedades;
    private Integer cantidad;
    
    private MarcasSC[] marcasSC;
    public MarcasSC marcaSC; 
   
    
    public MarcasWorkerSC(Properties prop){
        marcasSC = new MarcasSC[0];
        propiedades = prop;
    }

   
    @Override
    protected MarcasSC[] doInBackground(){
  try{
             setProgress(0);
             consulta = new ConsultaHttpSC("http",
             propiedades.getProperty("servidor"),
             propiedades.getProperty("metodoGET"),
             propiedades.getProperty("marcas")
             );
        
                JSONArray respuesta = consulta.getJason();
                cantidad = consulta.getJason().length();
               marcasSC = new MarcasSC[respuesta.length()];
                         for (int i = 0; i < respuesta.length(); i++) {
                            JSONObject object = respuesta.getJSONObject(i);
                            marcaSC = new MarcasSC(propiedades);
                            marcaSC.loadJSONConsulta(object);
                            marcasSC[i] = marcaSC;
                            setProgress(((i+1)*100)/cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                            //publish(producto.getCodigo());
                        }
                         
                } catch (Exception ex) {
            Logger.getLogger(RubrosVictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
        return marcasSC;
    
    }
 @Override
    protected void done() {
        System.out.println("Marcas obtenido. Se encontraron "+marcasSC.length+" marcas de Victoria.");
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
    public MarcasSC obtenerMarca(String codigo){
        for (MarcasSC marcasSC1 : marcasSC) {
            if(marcasSC1.getCodigo().equals(codigo)){
       //         System.out.println("CODIGO " + codigo);
                return marcasSC1;
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
