package etl.bascs.impala.debug;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.json.JSONException;
import org.json.JSONObject;

public class getJson1 {
    public String url;
    public JTextArea ta;
    public JSONObject json;
    public String id;

    public getJson1(String url, JTextArea ta) {
        this.url = url;
        this.ta = ta;
        get();
    }
    
    private void get(){
        try {
            json = readJsonFromUrl(this.url);
            this.ta.setText(json.toString());
            this.id = json.get("id").toString();
        } catch (IOException | JSONException ex) {
            Logger.getLogger(getJson1.class.getName()).log(Level.SEVERE, null, ex);
            this.ta.setText("Error al conectar");
            this.id = "null";
        }
    }
    
    
    public String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    
}
