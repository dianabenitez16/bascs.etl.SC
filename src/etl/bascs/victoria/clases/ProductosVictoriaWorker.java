/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import bascs.website.clases.RubroSC;
import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.ProductoVictoria;
import etl.bascs.impala.json.ConsultaHttp;
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
public class ProductosVictoriaWorker extends SwingWorker<ProductoVictoria[], String> implements PropertyChangeListener {

    public ConsultaHttpVictoria consulta;
    private Properties propiedades;
    private Integer cantidad;
    public Exception errores;
    private ProductoVictoria[] productos;
    private ProductoVictoria producto;

    public ProductosVictoriaWorker(Properties prop) {
        productos = new ProductoVictoria[0];
        propiedades = prop;

    }

    @Override
    protected ProductoVictoria[] doInBackground() throws Exception {
        try {
            setProgress(0);
            consulta = new ConsultaHttpVictoria("http",
                    propiedades.getProperty("servidor"),
                    propiedades.getProperty("puerto"),
                    propiedades.getProperty("metodoGET"),
                    propiedades.getProperty("productos"));

            //Thread.sleep(5000); //JUST FOR TESTING
            if (!consulta.getError()) {
                if (consulta.getJson().has("total")) {
                    cantidad = consulta.getJson().getInt("total");
                } else {
                    publish("No se encontraron productos en el maestro.");
                }
                if (cantidad > 0) {
                    if (consulta.getJson().has("items")) {
                        JSONArray respuesta = consulta.getJson().getJSONArray("items");
                        productos = new ProductoVictoria[respuesta.length()];
                        for (int i = 0; i < respuesta.length(); i++) {
                            Iterator keys = respuesta.getJSONObject(i).keys();
                            String key = keys.next().toString();
                            JSONObject productoJ = respuesta.getJSONObject(i).getJSONObject(key);
                            producto = new ProductoVictoria();
                            producto.loadJSONMaestro(productoJ);
                            productos[i] = producto;
                            setProgress(((i + 1) * 100) / cantidad);
                            //Thread.sleep(50); //JUST FOR TESTING
                            //publish(producto.getCodigo());

                        }
                    } else {
                        publish("No se encontraron productos en el maestro.");
                    }
                } else {
                    publish("No se encontraron productos en el maestro.");
                }
            } else {
                publish("Error");
                System.out.println("_ERROR");
            }
        } catch (Exception ex) {
            Logger.getLogger(ProductosVictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return productos;//To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void done() {
        System.out.println("_Productos obtenido. Se encontraron " + productos.length + " productos de Victoria.");
    }

    @Override
    protected void process(List<String> prods) {
        for (String prod : prods) {
            //       System.out.println("EL PROD ES ETO "+prod);
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
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".") + 1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);

        System.out.println(clase + ">> " + source + " > " + value);
    }
    
    public ProductoVictoria obtenerProducto(String codigo){
        for (ProductoVictoria producto : productos) {
            if(producto.getCodigo().equals(codigo)){
                return producto;
            }
        }
        return null;
    }
}
