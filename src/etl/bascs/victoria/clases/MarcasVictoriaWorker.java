/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.MarcaVictoria;
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
import org.json.JSONObject;

/**
 *
 * @author User
 */
    public class MarcasVictoriaWorker extends SwingWorker<MarcaVictoria[], String> implements PropertyChangeListener {

    public ConsultaHttpVictoria consultaV;
    public ConsultaHttpSC consultaS;
    public Properties propiedades;
    public Integer cantidad;
    public Boolean get = true;
    
    public MarcaVictoria[] marcasV;
    public MarcaVictoria marcaV;
    public JSONObject marcasJ;
    
    public MarcasVictoriaWorker(Properties prop){
       marcasV = new MarcaVictoria[0];
       propiedades = prop;
    }

    public MarcasVictoriaWorker() {
     //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected MarcaVictoria[] doInBackground() {
  
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
                        marcasV = new MarcaVictoria[respuesta.length()];
                         for (int i = 0; i < respuesta.length(); i++) {
                            Iterator keys = respuesta.getJSONObject(i).keys();
                            String key = keys.next().toString();
                            JSONObject productoJ = respuesta.getJSONObject(i).getJSONObject(key);
                            marcaV = new MarcaVictoria(propiedades);
                            marcaV.loadJSONConsulta(productoJ);
                            marcasV[i] = marcaV;
                            setProgress(((i+1)*100)/cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                            //publish(producto.getCodigo());
                        }
                    }else{
                        publish("No se encontraron marcas en el maestro.");
                    }
                }else{
                    publish("No se encontraron marcas en el maestro.");
                }
            }else{
                publish("Error");
                System.out.println("_ERROR");
            }
        } catch (Exception ex) {
            Logger.getLogger(RubrosVictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
        return marcasV;
    
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

