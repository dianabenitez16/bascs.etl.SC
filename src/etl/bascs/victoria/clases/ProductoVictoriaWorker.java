package etl.bascs.victoria.clases;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.ProductoVictoria;
import etl.bascs.impala.json.ConsultaHttpVictoria;
import etl.bascs.impala.main;
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

public class ProductoVictoriaWorker extends SwingWorker<ProductoVictoria, String> implements PropertyChangeListener{
    public ConsultaHttpVictoria consulta;
    public ProductoVictoria productosV;
    public Properties propiedades;
    public String codigo;
    public Integer id;
    public ProductoVictoria producto;
    public Boolean error;
    public JSONObject productoJ;
    
    public ProductoVictoriaWorker(ProductoVictoria producto,Properties propVictoria) {
        this.producto = producto;
        this.propiedades = propVictoria;
        this.error = false;
    }

    
    @Override
    protected ProductoVictoria doInBackground(){
 
        try {
            setProgress(0);
            consulta = new ConsultaHttpVictoria("http",
                    propiedades.getProperty("servidor"),
                    propiedades.getProperty("puerto"),
                    propiedades.getProperty("metodoGET"),
                    propiedades.getProperty("detalle")+producto.getCodigo());
                
            if(main.DEBUG){
                /*
                System.out.println("SER: " + propiedades.getProperty("servidor"));
                System.out.println("PUER: " + propiedades.getProperty("puerto"));
                System.out.println("MET: " + propiedades.getProperty("metodoGET"));
                System.out.println("DETALLE: " + propiedades.getProperty("detalle"));
*/
            }
           
            if (!consulta.getError()) {
                if (consulta.getJson().has("items")) {
                    productoJ = consulta.getJson().getJSONObject("items");
                    producto.loadJSONDetalle(productoJ);
                    setProgress(100);
                } else {
                    error = true;
                    publish("No se obtubo información al consultar: " + producto.getCodigo());
                    //System.out.println("_No se obtubo información al consultar: "+producto.getCodigo());
                }
            } else {
                error = true;
                publish(consulta.getErrorMessage() + ": " + producto.getCodigo());
                //System.out.println("_"+consulta.getErrorMessage()+": "+producto.getCodigo());
            }
            //Thread.sleep(5000); //JUST FOR TESTING
        } catch (Exception e) {
            error = true;
            //System.out.println("_Error desconocido al consultar: "+producto.getCodigo());
            publish("Error desconocido al consultar: " + producto.getCodigo() + e);
            //Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        return producto;
    }
    @Override
    protected void process(List<String> chunks) {
        for (String key : chunks) {
            System.out.println(key);
        }
    }
    public Properties getPropImpala() {
        return propiedades;
    }

    public void setPropVictoria(Properties propVictoria) {
        this.propiedades = propVictoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
    public JSONObject getJSON(){
        JSONObject object;
        object = new JSONObject();
      ProductoVictoria  pro = new ProductoVictoria();
        for (int i = 0; i < productoJ.length(); i++) {
        
        object.put("codigo_interno_ws", getCodigo());
        
        System.out.println("CODIGO DE WORKER " + getCodigo());
        object.put("nombre", pro.getNombre());
        object.put("marca_id", pro.getMarca_id());
        object.put("rubro_id", pro.getRubroSC().getId());
        if(productoJ.has("cuotas")){
            JSONArray cuota = productoJ.optJSONArray("cuotas");
        object.put("cuotas", cuota.optJSONObject(i).getInt("precio_contado"));
        }
        System.out.println("rubro_id del json " + pro.getRubroSC().getId());
        System.out.println("marca_id del json " + pro.getMarca_id());
       
        }
        
        return object;
    }
}
  
    