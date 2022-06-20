/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;


import bascs.website.clases.ProductoSC;
import bascs.website.clases.ProductosWorkerSC;
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
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ImagenesVWorker extends SwingWorker<ImagenesVictoria[], String> implements PropertyChangeListener {
    private Integer hilosMaximo;
    private Integer hilosCorriendo;
    private Integer hilosIniciados;
    private Integer hilosFinalizados;
    private Integer hilosConError;
    private Integer hilosACorrer;
    
    private String codigosConError;
    public JSONObject test;
    public JSONArray jsArray;
    private ProductosWorkerSC proSCW;
    private ProductosWorkerSC[] proSCW1;
    
    public Properties propiedades;
    public Properties propSC = new Properties();
    
    public ProductoVictoria[] productosFinalizados;
    public ImagenesVictoria[] imagenesProductos;
    public ImagenVictoriaWorker imagenesProductosW;
    public ImagenesVictoria productosAComparar;
    
    public ProductosVictoriaWorker productosWorker;
    public ImagenVictoriaWorker[] imagenesVictoriaWorker;
    public ProductoVictoriaWorker productoDetalleWorker;
    public ImagenesVictoria imagenVictoria[];
    //public CuotasVictoriaWorker[] productosCuotasWorker; // se deshabilita porque el worker de DETALLE ya trae las cuotas
    
    public JLabel estado;
    public JLabel progress;
    
   
    public ImagenesVWorker(Properties prop){
        productosFinalizados = new ProductoVictoria[0];
        imagenesVictoriaWorker = new ImagenVictoriaWorker[0];
        proSCW1 = new ProductosWorkerSC[0];
        
        //productosCuotasWorker = new CuotasVictoriaWorker[0];
      //  propiedades = propSC;
        propiedades = prop;
    }
    
    public void iniciar(){
      productosWorker = new ProductosVictoriaWorker(propiedades);  
      productosWorker.addPropertyChangeListener(this);
     
        
        System.out.println("PROP " + propSC.getProperty("servidor"));
      hilosMaximo = 2; //Integer.valueOf(propiedades.getProperty("hilos"));
      hilosCorriendo = 0;
      hilosIniciados = 0;
      hilosFinalizados = 0;
      hilosConError = 0;
      hilosACorrer = 0;
      codigosConError = "";
     
    }

    @Override
    protected ImagenesVictoria[] doInBackground() {
        try {
            publish("LOADING");
            productosWorker.execute();
           
            hilosACorrer = productosWorker.get().length;   
            //hilosACorrer = 20;   
            
            productosFinalizados = new ProductoVictoria[hilosACorrer];
            imagenesVictoriaWorker = new ImagenVictoriaWorker[hilosACorrer];
            imagenesProductos = new ImagenesVictoria[hilosACorrer];
            //productosCuotasWorker = new CuotasVictoriaWorker[hilosACorrer];
            
            setProgress(0);
            while (hilosIniciados < hilosACorrer) {
                
                if(hilosCorriendo < hilosMaximo){
                    publish("RUNNING");
                    
                    imagenesVictoriaWorker[hilosIniciados] = new ImagenVictoriaWorker(productosWorker.get()[hilosIniciados],propiedades);
                    imagenesVictoriaWorker[hilosIniciados].addPropertyChangeListener(this);
                    imagenesVictoriaWorker[hilosIniciados].setId(hilosIniciados);
                    imagenesVictoriaWorker[hilosIniciados].execute();
                    
                     /*
                    productosCuotasWorker[hilosIniciados] = new CuotasVictoriaWorker(productosWorker.get()[hilosIniciados],propiedades);
                    productosCuotasWorker[hilosIniciados].addPropertyChangeListener(this);
                    productosCuotasWorker[hilosIniciados].setId(hilosIniciados);
                    productosCuotasWorker[hilosIniciados].execute();
                    */
                    
                    hilosIniciados++;
                    hilosCorriendo++;
                    
                    
                }else{
                    publish("SLEEPING");
                    
                    Thread.sleep(5000);
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
            Logger.getLogger(ImagenesVWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println("ERROR: No se pudo ejecutar el Worker de PRODUCTOS.");
            Logger.getLogger(ImagenesVWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        jsArray = new JSONArray();
        test = new JSONObject();
         
        return imagenesProductos;
         
    }
   
    @Override
    protected void done() {
        publish((isCancelled()?"CANCELED":"FINISHED"));
    }
    
    public void cancelar(Boolean mayInterruptIfRunning){
        super.cancel(mayInterruptIfRunning);
        productosWorker.cancel(mayInterruptIfRunning);
        for (ImagenVictoriaWorker imagenW : imagenesVictoriaWorker) {
            if(imagenW != null){
                imagenW.cancel(mayInterruptIfRunning);
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
        
        if("ImagenVictoriaWorker".equals(source)){
            ImagenVictoriaWorker detalle = (ImagenVictoriaWorker) evt.getSource();

            System.out.println(clase+">> "+source+" > "+value+" | ID: "+detalle.id);

            if(value.equals("DONE")){
                try {
                    if(imagenesVictoriaWorker[detalle.id].isCancelled()){
                        
                    }else{
                        imagenesProductos[hilosFinalizados] = imagenesVictoriaWorker[detalle.id].get();
                        
                        
                        if(imagenesVictoriaWorker[detalle.id].getError()){
                            hilosConError++;
                            codigosConError += detalle.producto.getCodigo()+", ";
                            System.out.println("Producto ERROR: "+imagenesVictoriaWorker[detalle.id].get());
                        }else{
                           
                           Boolean nuevo = true;
                           String[] soco = {"1000","10087"};
                           imagenesProductosW = imagenesVictoriaWorker[detalle.id];
                           productosAComparar = imagenesProductosW.get() ; 
                                for (String socos : soco ) {
                                    
                                 if (productosAComparar.getCodigo().equals(socos)) {
                                    nuevo = false;
                                   
                                }
                          } 
                              if(nuevo){
                                  ProductosWSPOST(productosAComparar);
                                  System.out.println("productosAComparar" + productosAComparar.getCodigo());
                              } 
                            
                            System.out.println("Producto OK: "+imagenesVictoriaWorker[detalle.id].get().getCodigo());
                        }
                        
                        hilosFinalizados ++;
                        hilosCorriendo --;
//                        setProgress(((hilosFinalizados+1)*100)/hilosACorrer);
                        Integer progress = ((hilosFinalizados+1)*100)/hilosACorrer;
                        
                    }
                    this.progress.setText("Cargando " + (progress).toString()+"%");
                    
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("EX:"+ex.getLocalizedMessage());
                    Logger.getLogger(ImagenesVWorker.class.getName()).log(Level.SEVERE, null, ex);
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
    public void ProductosWSPOST(ImagenesVictoria productoVT) {
       System.out.println("PRODUCTOS A INSERTAR " + productoVT.getJSON().toString());
       }

}
