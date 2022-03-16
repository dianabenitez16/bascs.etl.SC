/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.json;

import etl.bascs.impala.clases.MarcasVictoria;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
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
    
    public Boolean get = true;
    
    private String protocolo;
    private String servidor;
    private String puerto;
    private String metodoGET;
    private String metodoPOST;
    private String rubros;
    private String marcas;
    private String productos;
    private MarcasVictoria parametros;

    public ConsultaHttpVictoria(String protocolo, String servidor, String puerto, String metodo, String recurso) {
        this.protocolo = protocolo;
        this.servidor = servidor;
        this.puerto = puerto;
        this.metodoGET = metodo;
        this.metodoPOST = metodo;
        this.marcas = recurso;
        this.productos = recurso;
        if(!get){
            this.parametros = parametros;
        }
        conectar();
    }
private void conectar() {
    if(get){
        try {
            url = new URL(protocolo+"://"+servidor+":"+puerto+productos);
            System.out.println("PROT " + protocolo);
            System.out.println("SER " + servidor);
            System.out.println("PUER " + puerto);
            System.out.println("RECUR " + productos);
            con = url.openConnection();
            
            con.setRequestProperty("Access-Control-Request-Method", metodoGET);
            System.out.println(" METODO " + metodoGET);
            con.setRequestProperty("Authorization", "None");
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Type", "application/json");
            //con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setDoOutput(true);
            
            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            
            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
            }
            in.close();
        //ENTRA EN RESPONSE
   //          System.out.println(response.toString());
        
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
            Logger.getLogger(ConsultaHttp.class.getName()).log(Level.SEVERE, null, ex);
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
            //Logger.getLogger(ConsultaHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }else{
        get = false;
        try {
            url = new URL(protocolo+"://"+servidor+marcas);
            System.out.println("PROT " + protocolo);
            System.out.println("SER " + servidor);
            System.out.println("RECUR " + marcas);
            
            con = url.openConnection();
            
            con.setRequestProperty("Access-Control-Request-Method", metodoPOST);
            System.out.println("METODO " + metodoPOST);
            con.setRequestProperty("Authorization", "Bearer 4|fRCGP9hboE5eiZPOrCu0bnpEug2IlGfIv05L7uYK");
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
            
           
           
            System.out.println("RESPONSE " + response.toString());
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
            //Logger.getLogger(ConsultaHttp.class.getName()).log(Level.SEVERE, null, ex);
        }
}
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

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
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

    public String getDebugMessage() {
        return debugMessage;
    }

    public void setDebugMessage(String debugMessage) {
        this.debugMessage = debugMessage;
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

    public String getMetodoGET() {
        return metodoGET;
    }

    public void setMetodoGET(String metodoGET) {
        this.metodoGET = metodoGET;
    }
 public String getRecurso() {
        return rubros;
    }

    public void setRecurso(String recurso) {
        this.rubros = recurso;
    }

    public String getRubros() {
        return rubros;
    }

    public void setRubros(String rubros) {
        this.rubros = rubros;
    }

    public String getProductos() {
        return productos;
    }

    public void setProductos(String productos) {
        this.productos = productos;
    }

    public String getMarcas() {
        return marcas;
    }

    public void setMarcas(String marcas) {
        this.marcas = marcas;
    }

    public Boolean getGet() {
        return get;
    }

    public void setGet(Boolean get) {
        this.get = get;
    }

    public String getMetodoPOST() {
        return metodoPOST;
    }

    public void setMetodoPOST(String metodoPOST) {
        this.metodoPOST = metodoPOST;
    }

}
