/*
 * @author junjuis
 */
package etl.bascs.impala.json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author junju
 */
public class ConsultaHttpSC {
    private URL url;
    public URLConnection con;
    private DataOutputStream wr;
    private BufferedReader in;
    private StringBuilder response;
    private JSONArray jason ;
    private JSONObject json ;
    
    public Boolean get = true;
    
    private Boolean error;
    private String errorMessage;
    private String debugMessage;
    
    private String protocolo;
    private String servidor;
    private String metodo;
    private String clave;
    private String productos;
    private String marcas;
    private String rubros;

    
    public ConsultaHttpSC(String protocolo, String servidor, String metodo, String recurso) {
        this.protocolo = protocolo;
        this.servidor = servidor;
        this.metodo = metodo;
        this.marcas = recurso;
         
        conectar();
    }
    
       
    private void conectar() {
        try {
            url = new URL(protocolo+"://"+servidor+marcas);
            System.out.println("URL " + url.toString());
            
            con = url.openConnection();
            con.setRequestProperty("Access-Control-Request-Method", metodo);
            //System.out.println("METODO " + metodo);
            con.setRequestProperty("Authorization", "Bearer 3|GJSYrWbgAtXlJ7COevCZgC9ecCFhCNAtmCujZ8RE");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            //con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
             
            }
            in.close();
        //    System.out.println("RESPONSE " + response.toString());
            jason = new JSONArray(response.toString());
           
            error = false;
          
            debugMessage = con.getHeaderField(0);
            
        } catch (MalformedURLException ex) {
            error = true;
            errorMessage = "Error en el armado de URL";
            debugMessage = ex.getMessage();
            System.out.println("EX: "+ex.getMessage());
            Logger.getLogger(ConsultaHttpSC.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            error = true;
            errorMessage = "Error al abrir la conexion";
            debugMessage = ex.getMessage();
            if(ex.getMessage().contains("HTTP response code: 401")){
                errorMessage = "Error de autenticación. Verifique credenciales. [401]";
            }
            if(ex.getMessage().contains("HTTP response code: 500")){
                errorMessage = "Error Interno de Servidor. [500]";
            }
            //System.out.println("EX: "+ex.getMessage());
        }
        }
    
    
    public JSONArray getJason() {
        return jason;
    }

    public void setJason(JSONArray jason) {
        this.jason = jason;
    }
     public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public URLConnection getCon() {
        return con;
    }

    public void setCon(URLConnection con) {
        this.con = con;
    }

    public DataOutputStream getWr() {
        return wr;
    }

    public void setWr(DataOutputStream wr) {
        this.wr = wr;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public StringBuilder getResponse() {
        return response;
    }

    public void setResponse(StringBuilder response) {
        this.response = response;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public String getServidor() {
        return servidor;
    }

    public void setServidor(String servidor) {
        this.servidor = servidor;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getMarcas() {
        return marcas;
    }

    public void setMarcas(String marcas) {
        this.marcas = marcas;
    }


    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
    
     public static void main(String[] args) {
         
     }
}
