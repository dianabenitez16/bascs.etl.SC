/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.json;

import etl.bascs.impala.clases.MarcasVictoria;
import etl.bascs.impala.clases.RubroVictoria;
import etl.bascs.impala.main;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
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
    try {
                URL url = new URL("");
                
                
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", "Bearer 4|fRCGP9hboE5eiZPOrCu0bnpEug2IlGfIv05L7uYK");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setUseCaches(false);

                //Start Content Wrapper
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
               MarcasVictoria postData = new MarcasVictoria();
             postData.setCodigo("ABB");
              
                bw.flush();
                bw.close();
             
                //Closing Content Wrapper and getting result
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    // OK
              
                }}catch (Exception ex) {
            Logger.getLogger(testing.class.getName()).log(Level.SEVERE, null, ex);
                         }
        }
}
