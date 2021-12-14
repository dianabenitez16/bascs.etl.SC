/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.json;

import etl.bascs.impala.clases.ProductosBASCs;
import etl.bascs.impala.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;



public class ConsultaPrueba {
private final String USER_AGENT = "Mozilla/5.0";
private String codigo_interno_ws;
private String nombre;
private int stock;
private String descripcion;
private boolean habilitado;

private ConsultaPrueba consulta;

private String codigoBAS;
private String nombreBAS;
private String desBAS;

    public String getCodigoBAS() {
        return codigoBAS;
    }

    public void setCodigoBAS(String codigoBAS) {
        this.codigoBAS = codigoBAS;
    }

    public String getNombreBAS() {
        return nombreBAS;
    }

    public void setNombreBAS(String nombreBAS) {
        this.nombreBAS = nombreBAS;
    }

    public String getDesBAS() {
        return desBAS;
    }

    public void setDesBAS(String desBAS) {
        this.desBAS = desBAS;
    }



    public String getCodigo_interno_ws() {
        return codigo_interno_ws;
    }

    public void setCodigo_interno_ws(String codigo_interno_ws) {
        this.codigo_interno_ws = codigo_interno_ws;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
    
public static void main(String[] args) throws Exception {

        ConsultaPrueba http = new ConsultaPrueba();

        System.out.println("Testing 1 - Send Http GET request");
        http.sendGet();

        System.out.println("\nTesting 2 - Send Http POST request");
//        http.sendPost();

    }

 private void sendGet() throws Exception {
try{
        String url = "http://192.168.192.137:8080/victoria/webapi/productos/";

        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);

        // add request header
        
        request.setHeader("User-Agent", USER_AGENT);
        request.setHeader( "Accept", "application/json");
        request.setHeader("Authorization", "None ");
        request.setHeader("Method", "GET");
        
        

        HttpResponse response = client.execute(request);
        System.out.println("Reqiest " + request );

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + 
                       response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                       new InputStreamReader(response.getEntity().getContent()));
                   
        StringBuilder result = new StringBuilder();
        
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println("ESTE ES EL JSON A UTILIZAR "+result.toString());
 
        JSONObject productos = new JSONObject(result.toString());
        System.out.println("coditm " + productos.getString("coditm"));
        System.out.println("descripcion " + productos.getString("descripcion"));
        System.out.println("descripcionlarga " + productos.getString("descripcionlarga"));

 }catch (Exception ex) {
            Logger.getLogger(ConsultaPrueba.class.getName()).log(Level.SEVERE, null, ex);
}
 }
 /*
 private void sendPost() throws Exception {

        String url = "http://www.saracomercial.com/panel/api/loader/productos";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        // add header
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader( "Accept", "application/json");
        post.setHeader("Authorization", "Bearer " + "4|fRCGP9hboE5eiZPOrCu0bnpEug2IlGfIv05L7uYK");
      
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("codigo_interno_ws", "PROD2019"));
        urlParameters.add(new BasicNameValuePair("nombre", "Televisor Prueba 50/"));
        urlParameters.add(new BasicNameValuePair("stock", "100"));
        urlParameters.add(new BasicNameValuePair("descripcion", "Televisor grande"));
        urlParameters.add(new BasicNameValuePair("marca_id", "1"));
        urlParameters.add(new BasicNameValuePair("rubro_id", "1"));
     
   

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " + 
                                    response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());

}  
*/
 }