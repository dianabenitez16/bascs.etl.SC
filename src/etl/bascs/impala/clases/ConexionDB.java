
package etl.bascs.impala.clases;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author ackerman
 */

public class ConexionDB {
    public static  Connection conectar(){
    Connection con = null;
    {
        String url = "jdbc:sqlserver://192.168.192.53:1433;DataBase=SARA_COMERCIAL";
        String username = "sa";
	String password= "C0nsult0r14%";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con = DriverManager.getConnection(url, username, password);
			
		} catch (Exception e) {
			
		System.out.print(e);
		}
                System.out.println("Conectado!");
	}	
        return con;
    
    }
    }
        
   
                