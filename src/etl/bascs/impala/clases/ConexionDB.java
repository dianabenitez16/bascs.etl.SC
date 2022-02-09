
package etl.bascs.impala.clases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author ackerman
 */

public class ConexionDB {
    public static void main(String[] args) throws Exception {
 
    
    }
    
    public static Connection conectar(){
        Connection con = null;
        
        String url = "jdbc:sqlserver://192.168.25.5:1433;DataBase=SARA_COMERCIAL";
        String username = "ws";
	String password= "w3bs3rv1c3*";
        
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {

        System.out.print(e);
        }
        System.out.println("Conectado!");
		
        return con;
    
    }
    /*
    public static void test(){
        JSONObject jo = new JSONObject();
        JSONArray ja = new JSONArray();
        JSONObject mainObj = new JSONObject();
        
        String codigo = "EBA"; 

        String sql ="DECLARE @CODIGO CHAR(3)\n" +
"SET @CODIGO = '"+codigo+"'\n" +
"\n" +
"SELECT A.CODATRVAL, A.DESCRIPCION\n" +
"FROM ATRIBUTOSVAL AS A \n" +
"WHERE \n" +
"A.CODATR IN ('AP1', 'AP2', 'AP3') AND\n" +
"A.CODATRVAL = @CODIGO";
		
        try {
            Statement st = conectar().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                jo = new JSONObject();
                Marcas mar = new Marcas();

                mar.setCodigo(rs.getString(1));
                mar.setDescripcion(rs.getString(2));
          

                jo.put("codigo", mar.getCodigo());
                jo.put("descripcion", mar.getDescripcion());
  
                ja.put(jo);
            }
            
            mainObj.put("brand", ja);
            mainObj.put("total", ja.length());
            
            System.out.println(mainObj);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonElement je = JsonParser.parseString​(mainObj.toString());
            String prettyJsonString = gson.toJson(je);
            System.out.println(prettyJsonString);
            
        } catch (Exception e) {

                System.out.print(e);
        }
    }
    
    */
    }
        
   
                