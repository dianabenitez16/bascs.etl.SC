/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.clases.ProductoVictoria;
import etl.bascs.impala.json.ConsultaHttpSC;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
public class ProductoDetalleWorkerSC extends SwingWorker<ProductoSC, String> implements PropertyChangeListener {

    public ConsultaHttpSC consulta;
    public ProductoSC productosSC;
    public Properties propiedades;
    public String codigo;
    public Integer id;
    public ProductoSC productoSC;
    public Boolean error;
    public JSONObject productoJ;
    private Integer cantidad;

    public ProductoDetalleWorkerSC(ProductoSC producto, Properties propSC) {
        this.productoSC = producto;
        this.propiedades = propSC;
        this.error = false;
    }

    public ProductoDetalleWorkerSC() {
    }

    @Override
    protected ProductoSC doInBackground() throws Exception {
        try {
            setProgress(0);
            consulta = new ConsultaHttpSC("http",
                    propiedades.getProperty("servidor"),
                    propiedades.getProperty("metodoGET"),
                    propiedades.getProperty("detalle")+productoSC.getId()
            );
            System.out.println("SER: " + propiedades.getProperty("servidor"));
            System.out.println("METODO: " + propiedades.getProperty("metodoGET"));
            System.out.println("PRODUCTO: " + propiedades.getProperty("detalle")+ productoSC.getId());

                        JSONArray respuesta = consulta.getJason();
            cantidad = consulta.getJason().length();
            for (int i = 0; i < respuesta.length(); i++) {

                JSONObject object = respuesta.getJSONObject(i);
                productoSC.loadJSONConsulta(object);
                setProgress(((i + 1) * 100) / cantidad);
                //Thread.sleep(50); //JUST FOR TESTING
                //publish(producto.getCodigo());
            }

        } catch (Exception e) {
            error = true;

            publish("Error desconocido al consultar: " + productoSC.getCodigo());
            System.out.println("EX " + e);
        }
        return productosSC;

    }

    @Override
    protected void process(List<String> chunks) {
        for (String key : chunks) {
            System.out.println(key);
        }
    }

    public Properties getPropSC() {
        return propiedades;
    }

    public void setPropVictoria(Properties propSC) {
        this.propiedades = propSC;
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
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".") + 1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".") + 1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);

        System.out.println(clase + ">> " + source + " > " + value);
    }
}
