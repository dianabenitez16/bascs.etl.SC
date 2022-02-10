/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.worker;

import etl.bascs.impala.clases.Marcas;
import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.ProductosVictoria;
import etl.bascs.impala.json.ConsultaHttp;
import etl.bascs.impala.json.ConsultaHttpVictoria;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import static org.apache.http.protocol.HTTP.USER_AGENT;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */



public class WorkerVictoria extends SwingWorker<ProductosVictoria[], String> implements PropertyChangeListener{
  public ConsultaHttpVictoria consulta;
    private Properties propiedades;
    private Integer cantidad;
    
    private ProductosVictoria[] productos;
    private ProductosVictoria producto;
    
        public WorkerVictoria(Properties prop) {
        productos = new ProductosVictoria[0];
        propiedades = prop;
 
}
        
    @Override
    protected ProductosVictoria[] doInBackground() {
        try {
           setProgress(0);
           String url = "http://192.168.192.60:8080/WS/webapi/victoria/productos";
        
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
      
        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader( "Accept", "application/json");
        request.setHeader("Method", "GET");     
        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(
                            new InputStreamReader(response.getEntity().getContent()));                 
        StringBuilder result = new StringBuilder();        
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
              if(consulta.getJson().has("total")){
                    cantidad = consulta.getJson().getInt("total");
                }else{
                    publish("No se encontraron productos en el maestro.");
                }
                if(cantidad > 0){
                    if(consulta.getJson().has("products")){
                        JSONArray respuesta = consulta.getJson().getJSONArray("products");
                        productos = new ProductosVictoria[respuesta.length()];
                        
                        for (int i = 0; i < respuesta.length(); i++) {
                            Iterator keys = respuesta.getJSONObject(i).keys();
                            String key = keys.next().toString();
                            JSONObject productoJ = respuesta.getJSONObject(i).getJSONObject(key);
                            producto = new ProductosVictoria();
                 //           marca.loadJSONConsulta(productoJ);
                            productos[i] = producto;
                            setProgress(((i+1)*100)/cantidad);
                          
                    
                        }
                    }else{
                        publish("No se encontraron productos en el maestro.");
                    
                    }
            }else{
                publish("Error");
                System.out.println("_ERROR");
            }
        } catch (Exception ex) {
            Logger.getLogger(MaestroWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productos;
     
    }
    
    @Override
    protected void done() {
        //System.out.println("_Maestro obtenido. Se encontraron "+productos.length+" productos.");
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
      public static void main(String[] args) {
      }
    
}

