/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.worker;

import etl.bascs.impala.clases.MarcasSC;
import etl.bascs.impala.clases.MarcasVictoria;
import etl.bascs.impala.json.ConsultaHttpSC;
import etl.bascs.impala.json.ConsultaHttpVictoria;
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
        if(!consulta.getError()){
                JSONArray respuesta = consulta.getJason();
                cantidad = consulta.getJason().length();
                
                if(cantidad > 0){   
                    System.out.println("CANTIDAD " + cantidad);
                          
                  Integer i = 0;
                        Iterator keys = respuesta.getJSONObject(i).keys();
                            while(keys.hasNext() && i > 0) {
                            String key = keys.next().toString();
                            JSONObject marcasJ = respuesta.getJSONObject(i);
                            marcaSC = new MarcasSC(propiedades);
                            marcaSC.loadJSONConsulta(marcasJ);
                            marcasSC[i] = marcaSC;
                                System.out.println("jsonObject" + marcasJ.toString());
                              i++;      
                    }
                                  
                            setProgress(((i+1)*100)/cantidad);
                          
                         
                }}} catch (Exception ex) {
            Logger.getLogger(RubrosWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
        return marcasSC;
    
    }
 @Override
    protected void done() {
        System.out.println("Marcas obtenido. Se encontraron "+marcasSC.length+" marcas.");
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
