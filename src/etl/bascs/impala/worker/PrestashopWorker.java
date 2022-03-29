/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.worker;

import etl.bascs.impala.clases.Producto;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/**
 *
 * @author Juan Bogado
 */
public class PrestashopWorker extends SwingWorker<Producto[], String> implements PropertyChangeListener  {
    private Integer hilosMaximo;
    private Integer hilosCorriendo;
    private Integer hilosIniciados;
    private Integer hilosFinalizados;
    private Integer hilosConError;
    private Integer hilosACorrer;
    
    private String codigosConError;
    
    public Properties propiedades;
    public MaestroWorker maestroW;
    public DetalleWorker[] detalleWs;
    
    public JLabel estado;
    
    public Producto[] productos;

    public PrestashopWorker(Properties prop) {
        productos = new Producto[0];
        propiedades = prop;
        detalleWs = new DetalleWorker[0];
    }
    /********************NOTAS ******************
     AÑADIR UN AVISO AL USUSARIO AL PRINCIPIO DE RUBROS
     JAVA SINCRONYZED LO MISMO QUE INVOKE LATER
     
     */
    
    public void iniciar(){
        maestroW = new MaestroWorker(propiedades);
        maestroW.addPropertyChangeListener(this);
        
        hilosMaximo = Integer.valueOf(propiedades.getProperty("hilos"));
        hilosCorriendo = 0;
        hilosIniciados = 0;
        hilosFinalizados = 0;
        hilosConError = 0;
        hilosACorrer = 0;
        codigosConError = "";
        
    }
    
    @Override
    protected Producto[] doInBackground() throws Exception {
        publish("LOADING");
        maestroW.execute();
        hilosACorrer = maestroW.get().length;
        //hilosACorrer = 50; //just for testing
        productos = new Producto[hilosACorrer];
        detalleWs = new DetalleWorker[hilosACorrer];
        
        setProgress(0);
        while (hilosIniciados < hilosACorrer) {   
            
            if(hilosCorriendo < hilosMaximo){
                publish("RUNING");
                //codigo = maestroW.get()[hilosIniciados].getCodigo().replace("/", "");
                
                detalleWs[hilosIniciados] = new DetalleWorker(maestroW.get()[hilosIniciados],propiedades);
                detalleWs[hilosIniciados].addPropertyChangeListener(this);
                detalleWs[hilosIniciados].setId(hilosIniciados);
                detalleWs[hilosIniciados].execute();
                
                //publish("["+hilosIniciados+"/"+hilosACorrer+"] Consultando "+codigo);
                hilosIniciados++;
                hilosCorriendo++;
            }else{
                Thread.sleep(500);
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
        maestroW.cancel(mayInterruptIfRunning);
        for (DetalleWorker detalleW : detalleWs) {
            if(detalleW != null){
                detalleW.cancel(mayInterruptIfRunning);
            }
            
        }
    }
    
    @Override
    protected void process(List<String> chunks) {
        estado.setText("[MAX:"+hilosMaximo+"] "+"[RUN:"+hilosCorriendo+"] "+"[INI:"+hilosIniciados+"] "+"[FIN:"+hilosFinalizados+"] "+"[TOT:"+hilosACorrer+"] ["+chunks.get(chunks.size()-1)+"]");
    }
    

    

    /* PROPERTY CHANGE */
    /**********************************************************************************************************/
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".")+1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".")+1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);
        
        //System.out.println(clase+">> "+source+" > "+value);
        
        /*
        System.out.print("\tMAX: "+hilosMaximo);
        System.out.print("\tRUN: "+hilosCorriendo);
        System.out.print("\tINI: "+hilosIniciados);
        System.out.print("\tFIN: "+hilosFinalizados);
        System.out.print("\tTOT: "+hilosACorrer);
        System.out.println("");
        */
        
        if("DetalleWorker".equals(source)){
            DetalleWorker detalle = (DetalleWorker) evt.getSource();

            //System.out.println(clase+">> "+source+" > "+value+" | ID: "+detalle.id);

            if(value.equals("DONE")){
                try {
                    if(detalleWs[detalle.id].isCancelled()){
                        
                    }else{
                        productos[hilosFinalizados] = detalleWs[detalle.id].get();
                        
                        //System.out.println("Producto: "+detalleWs[detalle.id].get().getCodigo());
                        if(detalleWs[detalle.id].getError()){
                            hilosConError++;
                            codigosConError += detalle.producto.getCodigo()+", ";
                        }
                        hilosFinalizados ++;
                        hilosCorriendo --;
                        setProgress(((hilosFinalizados+1)*100)/hilosACorrer);
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("EX:"+ex.getLocalizedMessage());
                    Logger.getLogger(PrestashopWorker.class.getName()).log(Level.SEVERE, null, ex);
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
