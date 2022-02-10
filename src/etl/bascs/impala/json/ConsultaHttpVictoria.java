/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.json;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ConsultaHttpVictoria {
    
    private URL url;
    private URLConnection con;
    private DataOutputStream wr;
    private BufferedReader in;
    private StringBuilder response;
    private JSONObject json ;
    
    private Boolean error;
    private String errorMessage;
    private String debugMessage;
    
    private String protocolo;
    private String servidor;
    private String puerto;
    private String metodo;
    private String parametros;
    private String recursos;
    
    public ConsultaHttpVictoria(String protocolo, String servidor, String puerto, String metodo, String recursos) {
        this.protocolo = protocolo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.recursos = recursos;
        this.parametros= parametros;
        this.metodo = metodo;
        conectar();
    }

    
       
    private void conectar() {
        try {
            url = new URL("http://192.168.192.60:8080/WS/webapi/victoria/productos");
            con = url.openConnection();
            
            con.setRequestProperty("Method", "GET");
            con.setRequestProperty("Authorization", "None");
            con.setRequestProperty("User-Agent", USER_AGENT);
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty( "Accept", "application/json");
            //con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true); 
 
    
            
            wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(parametros);
            wr.flush();
            wr.close();
            
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
          
            }
            in.close();
            
            json = new JSONObject(response.toString());
        
            if(json.has("errorCode")){
                error = false;
                errorMessage = String.valueOf(json.getInt("errorCode")) +" - "+ json.getString("errorText");
                
            }else{
                if(json.has("msgCode")){
                    error = false;
                    errorMessage = String.valueOf(json.getInt("msgCode")) +" - "+ json.getString("msgText");
                }else{
                    error = true;
                    errorMessage = con.getHeaderField(0);
                 
                }

            } 
           
            debugMessage = con.getHeaderField(0);
            
        } catch (MalformedURLException ex) {
            error = true;
            errorMessage = "Error en el armado de URL";
            debugMessage = ex.getMessage();
            System.out.println("EX: "+ex.getMessage());
            Logger.getLogger(ConsultaHttpVictoria.class.getName()).log(Level.SEVERE, null, ex);
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
            System.out.println("EX: "+ex.getMessage());
            Logger.getLogger(ConsultaHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
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

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public String getParametros() {
        return parametros;
    }

    public void setParametros(String parametros) {
        this.parametros = parametros;
    }

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
    }

    public String getRecursos() {
        return recursos;
    }

    public void setRecursos(String recursos) {
        this.recursos = recursos;
    }
    
      public static void main(String[] args) {
      }
}


 
