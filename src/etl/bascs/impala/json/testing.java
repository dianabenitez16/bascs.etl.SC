/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.json;

import etl.bascs.impala.clases.RubrosVictoria;
import etl.bascs.impala.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author User
 */
public class testing {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
  HttpClient httpClient = new DefaultHttpClient();
HttpPost httpPost = new HttpPost("http://www.saracomercial.com/panel/api/loader/rubros");
// Request parameters and other properties.
 //       httpPost.setHeader("User-Agent", USER_AGENT);
        httpPost.setHeader( "Accept", "application/json");
        httpPost.setHeader( "Content-Type", "application/json");
        httpPost.setHeader("Authorization", "Bearer 4|fRCGP9hboE5eiZPOrCu0bnpEug2IlGfIv05L7uYK");
        httpPost.setHeader("Method", "POST");
        
List<NameValuePair> params = new ArrayList<>();
params.add(new BasicNameValuePair("codigo_interno_ws", "0"));
params.add(new BasicNameValuePair("nombre", "No Posee"));
try {
    httpPost.setEntity(new UrlEncodedFormEntity(params));
} catch (UnsupportedEncodingException e) {
    // writing error to Log
    e.printStackTrace();
}
/*
 * Execute the HTTP Request
 */
try {
    HttpResponse response = httpClient.execute(httpPost);
    HttpEntity respEntity = response.getEntity();

    if (respEntity != null) {
        // EntityUtils to get the response content
        String content =  EntityUtils.toString(respEntity);
        System.out.println("CONTENT " + content);
    }
} catch (ClientProtocolException e) {
    // writing exception to log
    e.printStackTrace();
} catch (IOException e) {
    // writing exception to log
    e.printStackTrace();
}
    }
}
