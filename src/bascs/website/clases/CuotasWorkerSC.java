/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bascs.website.clases;

import etl.bascs.impala.json.ConsultaHttpSC;
import etl.bascs.impala.main;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class CuotasWorkerSC extends SwingWorker<CuotaSC[], String> implements PropertyChangeListener {

    public ConsultaHttpSC consulta;
    private Properties propiedades;
    private Integer cantidad;
    private Integer id;
    private ArrayList<String> codigos;
    public String[] codigo;
    public main main;
    private ProductoSC[] productosSC;
    private CuotaSC[] cuotasSC;
    private ProductoSC productoSC;
    private CuotaSC cuotaSC;
    private JSONObject jsonCodigo;
    public JSONObject jsonResponse;

    public CuotasWorkerSC(ArrayList codigo, Properties propSC) {
        this.codigos = codigo;
        this.propiedades = propSC;
    }

    public CuotasWorkerSC(String[] producto, Properties propSC) {
        this.codigo = producto;
        this.propiedades = propSC;
    }

    public CuotasWorkerSC() {
    }

    @Override
    protected CuotaSC[] doInBackground() {

        try {

            setProgress(0);

            String url = "http://www.saracomercial.com/panel/api/loader/cuotas/filtrar?page=1";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("Authorization", propiedades.getProperty("clave"));

            jsonCodigo = new JSONObject();
            jsonCodigo.put("productos_codigo_interno_ws", codigos);  //BODY DEL POST, DENTRO VA LA LISTA DE CODIGOS, SEPARADOS POR COMA

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(jsonCodigo.toString());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + jsonCodigo.toString());
            System.out.println("Response Code : " + responseCode);
            System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
            System.out.println("Accept: " + con.getRequestProperty("Accept"));
            System.out.println("Authorization: " + propiedades.getProperty("clave"));
            System.out.println("Method: " + con.getRequestMethod());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            jsonResponse = new JSONObject(response.toString());
            if (jsonResponse.has("data")) {
                JSONArray respuesta = jsonResponse.getJSONArray("data");
                cuotasSC = new CuotaSC[respuesta.length()];
                System.out.println("TAMANO DE ARRAY:"+cuotasSC.length);
                for (int i = 0; i < response.length(); i++) {
                    JSONObject cuotasJ = respuesta.getJSONObject(i);
                    //System.out.println("CANTIDAD " + cuotasJ.length());
                    cuotaSC = new CuotaSC();
                    cuotaSC.loadJSONConsulta(cuotasJ);
                    cuotasSC[i] = cuotaSC;
                    // productoSC.setCuotas(cuotasSC);

                    setProgress(((i + 1) * 100) / cantidad);
                    //Thread.sleep(50); //JUST FOR TESTING
                    //publish(producto.getCodigo());
                    //print result

                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }

        return cuotasSC;

    }

    @Override
    protected void done() {
        System.out.println("Productos obtenidos. Se encontraron " + cuotasSC.length + " cuotas del producto ");
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public JSONObject getJsonCodigo() {
        return jsonCodigo;
    }

    public void setJsonCodigo(JSONObject jsonCodigo) {
        this.jsonCodigo = jsonCodigo;
    }

    public String[] getCodigo() {
        return codigo;
    }

    public void setCodigo(String[] codigo) {
        this.codigo = codigo;
    }

    public ArrayList getCodigos() {
        return codigos;
    }

    public void setCodigos(ArrayList codigos) {
        this.codigos = codigos;
    }

    public ProductoSC obtenerRubro(String codigo) {
        for (ProductoSC productoRubro : productosSC) {
            if (productoRubro.getCodigo().equals(codigo)) {
                return productoRubro;
            }
        }
        return null;
    }

    public ProductoSC obtenerMarca(String codigo) {
        for (ProductoSC productoMarca : productosSC) {
            if (productoMarca.getCodigo().equals(codigo)) {
                return productoMarca;
            }
        }
        return null;
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
