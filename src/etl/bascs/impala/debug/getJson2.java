/*
 * @author junjuis
 */
package etl.bascs.impala.debug;

import etl.bascs.impala.clases.Producto;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.JSONObject;

/**
 *
 * @author junju
 */
public class getJson2 {
    
    public URL url;
    public URLConnection con;
    public JTextArea ta;
    public JSONObject json;
    public String responseCode;

    public getJson2(String link, String usuario, String clave, JTextArea ta) {
        try {
            url = new URL(link);
            con = url.openConnection();
            con.setRequestProperty("Access-Control-Request-Method", "POST");
            con.setRequestProperty ("Authorization", "Basic " + new String(Base64.getEncoder().encode((usuario+":"+clave).getBytes())));
            con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64; rv:27.0) Gecko/20100101 Firefox/27.0.2 Waterfox/27.0");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String urlParameters = "user=Ang0ras4&product_id=EDWLWI10";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            //System.out.println(con.get);
            
            responseCode = con.getHeaderField(0);
            
            
            
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + con.getHeaderField(0));
            System.out.println(con.getHeaderFields());
            BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
             response.append(inputLine);
            }
            in.close();
            //print result
            System.out.println(response.toString());
            
            


            } catch (Exception e) {
                Logger.getLogger(getJson1.class.getName()).log(Level.SEVERE, null, e);
                this.ta.setText("Error al conectar");
                this.responseCode = "null";
            }
    }
    
}
