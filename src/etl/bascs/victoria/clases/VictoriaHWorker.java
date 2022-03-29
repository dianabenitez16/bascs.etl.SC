/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;

import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.ProductosVictoria;
import etl.bascs.impala.worker.DetalleWorker;
import etl.bascs.impala.worker.PrestashopWorker;
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
public class VictoriaHWorker extends SwingWorker<ProductosVictoria[], String> implements PropertyChangeListener {
    private Integer hilosMaximo;
    private Integer hilosCorriendo;
    private Integer hilosIniciados;
    private Integer hilosFinalizados;
    private Integer hilosConError;
    private Integer hilosACorrer;
    
    private String codigosConError;
    
    public Properties propiedades;
    public ProductosVictoria[] productos;
    public ProductoWorkerDetalle[] detalleV;
    
    public ProductosWorker productoW;
   
    public JLabel estado;
    public JLabel progress;
   
    public VictoriaHWorker(Properties prop){
        productos = new ProductosVictoria[0];
        propiedades = prop;
        detalleV = new ProductoWorkerDetalle[0];
    }
    
    public void iniciar(){
      productoW = new ProductosWorker(propiedades);  
      productoW.addPropertyChangeListener(this);
      
      hilosMaximo = Integer.valueOf(propiedades.getProperty("hilos"));
      hilosCorriendo = 0;
      hilosIniciados = 0;
      hilosFinalizados = 0;
      hilosConError = 0;
      hilosACorrer = 0;
      codigosConError = "";
     
    }

    @Override
    protected ProductosVictoria[] doInBackground() throws Exception {
     publish("LOADING");
     productoW.execute();
     hilosACorrer = productoW.get().length;
     
     productos = new ProductosVictoria[hilosACorrer];
     detalleV = new ProductoWorkerDetalle[hilosACorrer];
     
      setProgress(0);
        while (hilosIniciados < hilosACorrer) {   
            
            if(hilosCorriendo < hilosMaximo){
                publish("RUNNING");
                
                detalleV[hilosIniciados] = new ProductoWorkerDetalle(productoW.get()[hilosIniciados],propiedades);
                detalleV[hilosIniciados].addPropertyChangeListener(this);
                detalleV[hilosIniciados].setId(hilosIniciados);
                detalleV[hilosIniciados].execute();
                
                
                hilosIniciados++;
                hilosCorriendo++;
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
           return productos;
    }
@Override
    protected void done() {
        publish((isCancelled()?"CANCELED":"FINISHED"));
    }
    
    public void cancelar(Boolean mayInterruptIfRunning){
        super.cancel(mayInterruptIfRunning);
        productoW.cancel(mayInterruptIfRunning);
        for (ProductoWorkerDetalle detalleW : detalleV) {
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
        
         if("ProductoWorkerDetalle".equals(source)){
            ProductoWorkerDetalle detalle = (ProductoWorkerDetalle) evt.getSource();

            //System.out.println(clase+">> "+source+" > "+value+" | ID: "+detalle.id);

            if(value.equals("DONE")){
                try {
                    if(detalleV[detalle.id].isCancelled()){
                        
                    }else{
                        productos[hilosFinalizados] = detalleV[detalle.id].get();
                        
                        System.out.println("Producto: "+detalleV[detalle.id].get().getCodigo());
                        if(detalleV[detalle.id].getError()){
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
                    Logger.getLogger(VictoriaHWorker.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
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
