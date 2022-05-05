/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.sqllite.clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
/**
 *
 * @author User
 */
public class SQLite {

  
    public SQLite() {
        conectar();
        crearTabla();
 
       
 //    insertar();
    }
    
      public Connection conectar(){
          
        Connection c = null;
      
                    try {
                       Class.forName("org.sqlite.JDBC");
                       c = DriverManager.getConnection("jdbc:sqlite:Sara_comercial.db");
                    } catch ( Exception e ) {
                       System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                       System.exit(0);
                    }

       //             System.out.println("Opened database successfully");
                      return c;
        
      }
      
      public void crearTabla(){
          
      Connection c = null;
      Statement stmt = null;
  //    boolean hasdata = false;
         try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:Sara_comercial.db");
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
                String sql = "CREATE TABLE IF NOT EXISTS PRECIOS " +
                                "(codigoAlternativo VARCHAR NOT NULL, " +
                                "precio  INTEGER NOT NULL,  "+
                                "fechaVigencia_desde DATE NOT NULL, " +
                                "fechaVigencia_hasta DATE NOT NULL) ";
               stmt.executeUpdate(sql);
           //     hasdata = true;
                 stmt.close();
                  c.close();
             } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
             }
      System.out.println("Table created successfully"); 
      
      }

 
      public void insertar(){
          
      Connection c = null;
      Statement stmt = null;
  //    boolean hasdata = false;
         try {
                Class.forName("org.sqlite.JDBC");
                c = DriverManager.getConnection("jdbc:sqlite:Sara_comercial.db");
                System.out.println("Opened database successfully");

                stmt = c.createStatement();
          //      String sql ="INSERT INTO TALONARIOS (tipo_com, sucursal, punto_exp,nro_desde,nro_hasta, fecha_des, fecha_has, timbrado ) VALUES (1,1,1,'262150','280000','2020-01-01','2023-1-01',123456)";
                String sqln ="INSERT INTO COMPROBANTES VALUES(2, 'Nota de Crédito') ";
                 //       + "INSERT INTO COMPROBANTES VALUES(2, 'Nota de Crédito')";
                                
             
                stmt.executeUpdate(sqln);
           //     stmt.executeUpdate(sql);
           //     hasdata = true;
                 stmt.close();
                  c.close();
             } catch ( Exception e ) {
                System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                System.exit(0);
             }
      System.out.println("RECORDS created successfully"); 
      
        }
}