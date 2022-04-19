/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package etl.bascs.victoria.clases;

import bascs.website.clases.ProductoSC;
import etl.bascs.impala.clases.ProductoCuotasVictoria;
import etl.bascs.impala.clases.ProductoVictoria;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Properties;
import javax.swing.SwingWorker;

/**
 *
 * @author junju
 */
public class SCWorker extends SwingWorker<ProductoSC[], String> implements PropertyChangeListener{
    private Integer hilosMaximo;
    private Integer hilosCorriendo;
    private Integer hilosIniciados;
    private Integer hilosFinalizados;
    private Integer hilosConError;
    private Integer hilosACorrer;
    
    private Properties propiedades;
    
    
    public ProductoVictoria[] productosVictoria;
    public ProductoSC[] productosSC;
    public ProductoSC[] productosFinalizados;
    
    
    public SCWorker(Properties prop) {
        
    }
    
    public SCWorker(Properties prop, ProductoVictoria[] pv) {
        this.productosVictoria = pv;
    }    
    
    public void iniciar(){
        
    }
    
    

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ProductoSC[] doInBackground() throws Exception {
        
        for (ProductoVictoria productoVictoria : productosVictoria) {
            for (ProductoSC productoSC : productosSC) {
                if(productoVictoria.getCodigo().equals(productoSC.getCodigo())){
                    for (ProductoCuotasVictoria cuota : productoVictoria.getCuotas()) {
                        
                        

                    }
                }
                
            }
        }
        
        return productosFinalizados;
    }
    
}
// se debe hacer un victoriaWorker para cuotas de Sara asi evitar el TOO MANY REQUEST, tambien le limite el 
//rango de productos de SC para poder hacer las pruebas de comparacion, y aparentemente funciona.