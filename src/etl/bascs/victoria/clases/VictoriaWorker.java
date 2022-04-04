/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.ProductoVictoria;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.SwingWorker;

/**
 *
 * @author User
 */
public class VictoriaWorker extends SwingWorker<ProductoVictoria[], String> implements PropertyChangeListener {
    private Integer hilosMaximo;
    private Integer hilosCorriendo;
    private Integer hilosIniciados;
    private Integer hilosFinalizados;
    private Integer hilosConError;
    private Integer hilosACorrer;
    public Boolean paused = false;
    
    private String codigosConError;
    public Boolean limit_reached;
    public Properties propiedades;
    
    public ProductoVictoria[] productosFinalizados;
    public ProductoVictoria[] productosCargados;
    
    public ProductosVictoriaWorker productosWorker;
    public ProductoVictoriaWorker[] productosDetalleWorker;
    public CuotasVictoriaWorker[] productosCuotasWorker;
    
    public JLabel estado;
    public JLabel progress;
   
    public VictoriaWorker(Properties prop){
        productosFinalizados = new ProductoVictoria[0];
        productosDetalleWorker = new ProductoVictoriaWorker[0];
        productosCuotasWorker = new CuotasVictoriaWorker[0];
        
        propiedades = prop;
    }
    
    public void iniciar(){
      productosWorker = new ProductosVictoriaWorker(propiedades);  
      productosWorker.addPropertyChangeListener(this);
      
      hilosMaximo = Integer.valueOf(propiedades.getProperty("hilos"));
      hilosCorriendo = 0;
      hilosIniciados = 0;
      hilosFinalizados = 0;
      hilosConError = 0;
      hilosACorrer = 0;
      codigosConError = "";
     
    }
  
    @Override
    protected synchronized ProductoVictoria[] doInBackground() {
        try {
            publish("LOADING");
            productosWorker.execute();
            
            hilosACorrer = productosWorker.get().length;   
            
            productosFinalizados = new ProductoVictoria[hilosACorrer];
            productosDetalleWorker = new ProductoVictoriaWorker[hilosACorrer];
            productosCuotasWorker = new CuotasVictoriaWorker[hilosACorrer];
            
            setProgress(0);
            while (hilosIniciados < hilosACorrer) {
                limit_reached = false;
                if(hilosCorriendo < hilosMaximo){
                    publish("RUNNING");
                    
                    productosDetalleWorker[hilosIniciados] = new ProductoVictoriaWorker(productosWorker.get()[hilosIniciados],propiedades);
                    productosDetalleWorker[hilosIniciados].addPropertyChangeListener(this);
                    productosDetalleWorker[hilosIniciados].setId(hilosIniciados);
                    productosDetalleWorker[hilosIniciados].execute();
                    
                    productosCuotasWorker[hilosIniciados] = new CuotasVictoriaWorker(productosWorker.get()[hilosIniciados],propiedades);
                    productosCuotasWorker[hilosIniciados].addPropertyChangeListener(this);
                    productosCuotasWorker[hilosIniciados].setId(hilosIniciados);
                    productosCuotasWorker[hilosIniciados].execute();
                    
                    
                    hilosIniciados++;
                    hilosCorriendo++;
                    limit_reached = false;
                }else{
                   
                    Thread.sleep(10000);
                   
                }
                
                if(isCancelled()){
                    break;
                }
            }
            while (hilosIniciados > hilosFinalizados){
                publish("FINISHING");
                Thread.sleep(500);
            }
            
        } catch (InterruptedException ex) {
            System.out.println("PROCESO INTERRUMPIDO: No se pudo ejecutar el Worker de PRODUCTOS.");
            Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println("ERROR: No se pudo ejecutar el Worker de PRODUCTOS.");
            Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                   
        return productosFinalizados;
        
    }    
    
    @Override
    protected void done() {
        publish((isCancelled()?"CANCELED":"FINISHED"));
    }
    
    public void cancelar(Boolean mayInterruptIfRunning){
        super.cancel(mayInterruptIfRunning);
        productosWorker.cancel(mayInterruptIfRunning);
        for (ProductoVictoriaWorker detalleW : productosDetalleWorker) {
            if(detalleW != null){
                detalleW.cancel(mayInterruptIfRunning);
            }
            
        }
    }
      @Override
    protected void process(List<String> chunks) {
        estado.setText("[MAX:"+hilosMaximo+"] "+"[RUN:"+hilosCorriendo+"] "+"[INI:"+hilosIniciados+"] "+"[FIN:"+hilosFinalizados+"] "+"[TOT:"+hilosACorrer+"] ["+chunks.get(chunks.size()-1)+"]");
    }
     @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".")+1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".")+1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);
        
        if("ProductoVictoriaWorker".equals(source)){
            ProductoVictoriaWorker detalle = (ProductoVictoriaWorker) evt.getSource();

            //System.out.println(clase+">> "+source+" > "+value+" | ID: "+detalle.id);

            if(value.equals("DONE")){
                try {
                    if(productosDetalleWorker[detalle.id].isCancelled()){
                        
                    }else{
                        productosFinalizados[hilosFinalizados] = productosDetalleWorker[detalle.id].get();
                        
                        System.out.println("Producto: "+productosDetalleWorker[detalle.id].get().getCodigo());
                        if(productosDetalleWorker[detalle.id].getError()){
                            hilosConError++;
                            codigosConError += detalle.producto.getCodigo()+", ";
                        }
                        hilosFinalizados ++;
                        hilosCorriendo --;
                        setProgress(((hilosFinalizados+1)*100)/hilosACorrer);
                        Integer progress = ((hilosFinalizados+1)*100)/hilosACorrer;
                        this.progress.setText("Cargando " + (progress).toString()+"%");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("EX:"+ex.getLocalizedMessage());
                    Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }else if("CuotasVictoriaWorker".equals(source)){
            /*
            CuotasVictoriaWorker cuotas = (ProductoVictoriaWorker) evt.getSource();

            //System.out.println(clase+">> "+source+" > "+value+" | ID: "+detalle.id);

            if(value.equals("DONE")){
                try {
                    if(productosDetalleWorker[detalle.id].isCancelled()){
                        
                    }else{
                        productosFinalizados[hilosFinalizados] = productosDetalleWorker[detalle.id].get();
                        
                        
                        
                        //System.out.println("Producto: "+detalleV[detalle.id].get().getCodigo());
                        if(productosDetalleWorker[detalle.id].getError()){
                            hilosConError++;
                            codigosConError += detalle.producto.getCodigo()+", ";
                        }
                        hilosFinalizados ++;
                        hilosCorriendo --;
                        setProgress(((hilosFinalizados+1)*100)/hilosACorrer);
                        Integer progress = ((hilosFinalizados+1)*100)/hilosACorrer;
                        this.progress.setText("Cargando " + (progress).toString()+"%");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("EX:"+ex.getLocalizedMessage());
                    Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        */
        }
    }

    public Integer getHilosMaximo() {
        return hilosMaximo;
    }

    public void setHilosMaximo(Integer hilosMaximo) {
        this.hilosMaximo = hilosMaximo;
    }

    public Integer getHilosCorriendo() {
        return hilosCorriendo;
    }

    public void setHilosCorriendo(Integer hilosCorriendo) {
        this.hilosCorriendo = hilosCorriendo;
    }

    public Integer getHilosIniciados() {
        return hilosIniciados;
    }

    public void setHilosIniciados(Integer hilosIniciados) {
        this.hilosIniciados = hilosIniciados;
    }

    public Integer getHilosFinalizados() {
        return hilosFinalizados;
    }

    public void setHilosFinalizados(Integer hilosFinalizados) {
        this.hilosFinalizados = hilosFinalizados;
    }

    public Integer getHilosConError() {
        return hilosConError;
    }

    public void setHilosConError(Integer hilosConError) {
        this.hilosConError = hilosConError;
    }

    public Integer getHilosACorrer() {
        return hilosACorrer;
    }

    public void setHilosACorrer(Integer hilosACorrer) {
        this.hilosACorrer = hilosACorrer;
    }

    public String getCodigosConError() {
        return codigosConError;
    }

    public void setCodigosConError(String codigosConError) {
        this.codigosConError = codigosConError;
    }
      
}
