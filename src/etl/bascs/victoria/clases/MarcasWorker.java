/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.MarcasVictoria;
import etl.bascs.impala.json.ConsultaHttpSC;
import etl.bascs.impala.json.ConsultaHttpVictoria;
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
    public class MarcasWorker extends SwingWorker<MarcasVictoria[], String> implements PropertyChangeListener {

    public ConsultaHttpVictoria consultaV;
    public ConsultaHttpSC consultaS;
    public Properties propiedades;
    private Integer cantidad;
    public Boolean get = true;
    
    public MarcasVictoria[] marcasV;
    public MarcasVictoria marcaV;
    public JSONObject marcasJ;
    
    public MarcasWorker(Properties prop){
       marcasV = new MarcasVictoria[0];
       propiedades = prop;
    }

    public MarcasWorker() {
     //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected MarcasVictoria[] doInBackground() {
    if(get){
        try{
             setProgress(0);
             consultaV = new ConsultaHttpVictoria("http",
             propiedades.getProperty("servidor"),
             propiedades.getProperty("puerto"),
             propiedades.getProperty("metodoGET"),
             propiedades.getProperty("marcas")
             );
        if(!consultaV.getError()){
                if(consultaV.getJson().has("total")){
                    cantidad = consultaV.getJson().getInt("total");
                }else{
                    publish("No se encontraron marcas en el maestro.");
                }
                if(cantidad > 0){
                    if(consultaV.getJson().has("items")){
                        JSONArray respuesta = consultaV.getJson().getJSONArray("items");
                        marcasV = new MarcasVictoria[respuesta.length()];
                        Integer i = 0;
                        Iterator keys = respuesta.getJSONObject(i).keys();
                            while(keys.hasNext()) {
                            String key = keys.next().toString();
                            marcasJ = respuesta.getJSONObject(i).getJSONObject(key);
                            marcaV = new MarcasVictoria(propiedades);
                            marcaV.loadJSONConsulta(marcasJ);
                            marcasV[i] = marcaV;
                            setProgress(((i+1)*100)/cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                       //     publish(rubroV.getCodigo());
                                System.out.println("MARCAS " + marcasJ.toString());
                        i++;
                        get = true;
                            }
                    }else{
                        publish("No se encontraron rubros en el maestro.");
                    }
                }else{
                    publish("No se encontraron rubros en el maestro.");
                }
            }else{
                publish("Error");
                System.out.println("_ERROR");
            }
        } catch (Exception ex) {
            Logger.getLogger(RubrosWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
        return marcasV;
    }else{
        get = false;
        try{
             setProgress(0);
             consultaS = new ConsultaHttpSC("http",
             propiedades.getProperty("servidor"),
             propiedades.getProperty("metodoPOST"),
             propiedades.getProperty("marcas")
             );
             
        
             
       } catch (Exception ex) {
            Logger.getLogger(RubrosWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
      
       }
    }
        return null;
    }
    @Override
    protected void done() {
        System.out.println("Marcas obtenido. Se encontraron "+marcasV.length+" marcas.");
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

    public Boolean getGet() {
        return get;
    }

    public void setGet(Boolean get) {
        this.get = get;
    }

    public JSONObject getMarcasJ() {
        return marcasJ;
    }

    public void setMarcasJ(JSONObject marcasJ) {
        this.marcasJ = marcasJ;
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

