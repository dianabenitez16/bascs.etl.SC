/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.clases.RubroVictoria;
import etl.bascs.impala.json.ConsultaHttpSC;
import etl.bascs.impala.json.ConsultaHttpVictoria;
import etl.bascs.victoria.clases.RubrosVictoriaWorker;
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
public class RubrosWorkerSC extends SwingWorker<RubroSC[], String> implements PropertyChangeListener {
    public ConsultaHttpSC consulta;
    private Properties propiedades;
    private Integer cantidad;
    
    public RubroSC[] rubrosSC;
    public RubroSC rubroSC;
    public JSONObject rubroJ;
    
    public RubrosWorkerSC(Properties prop){
       rubrosSC = new RubroSC[0];
       propiedades = prop;
    }
     
    @Override
    protected RubroSC[] doInBackground(){
        try{
             setProgress(0);
             consulta = new ConsultaHttpSC("https",
             propiedades.getProperty("servidor"),
             propiedades.getProperty("metodoGET"),
             propiedades.getProperty("rubros")
             );
             System.out.println("SERVIDOR " + propiedades.getProperty("servidor"));
             System.out.println("MET " + propiedades.getProperty("metodoGET"));
             System.out.println("RUBROS " + propiedades.getProperty("rubros"));
          JSONArray respuesta = consulta.getJason();
               cantidad = consulta.getJason().length();
               rubrosSC = new RubroSC[respuesta.length()];
                         for (int i = 0; i < respuesta.length(); i++) {
                            JSONObject object = respuesta.getJSONObject(i);
                            rubroSC = new RubroSC(propiedades);
                            rubroSC.loadJSONConsulta(object);
                            rubrosSC[i] = rubroSC;
                            setProgress(((i+1)*100)/cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                            //publish(producto.getCodigo());
                           
                         }
        } catch (Exception ex) {
            Logger.getLogger(RubrosWorkerSC.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("_ERROR" + ex);
        }
        return rubrosSC;
    
    }
   @Override
    protected void done() {
        System.out.println("_Rubros obtenido. Se encontraron "+rubrosSC.length+" rubros.");
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

    public ConsultaHttpSC getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultaHttpSC consulta) {
        this.consulta = consulta;
    }
    
    public RubroSC obtenerRubro(String codigo){
        for (RubroSC rubrosSC1 : rubrosSC) {
            if(rubrosSC1.getCodigo().equals(codigo)){ //OJO, SOLO MANDA E0, PROBABLEMENTE UN ERROR DE COMPARACION
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
