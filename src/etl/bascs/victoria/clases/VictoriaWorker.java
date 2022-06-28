/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.victoria.clases;


import bascs.website.clases.ProductoSC;
import bascs.website.clases.ProductosWorkerSC;
import etl.bascs.impala.clases.ProductoVictoria;
import etl.bascs.impala.main;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
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
public class VictoriaWorker extends SwingWorker<ProductoVictoria[], String> implements PropertyChangeListener {
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
    public ProductoSC[] productosACompararSC;
    public Properties propVic;
    public Properties propiedades;
    
    
    public ProductoVictoria[] productosFinalizados;
    public ProductoVictoria productosAComparar;
    
    public ProductosWorkerSC productosWorkerSC;
    public ProductosVictoriaWorker productosWorker;
    public ProductoVictoriaWorker[] productosDetalleWorker;
    public ProductoVictoriaWorker productoDetalleWorker;
    public ProductoSC[] productoSC;
    //public CuotasVictoriaWorker[] productosCuotasWorker; // se deshabilita porque el worker de DETALLE ya trae las cuotas
    
    public JLabel estado;
    public JLabel progress;
    
   
    public VictoriaWorker(Properties prop){
        productosFinalizados = new ProductoVictoria[0];
        productosDetalleWorker = new ProductoVictoriaWorker[0];
        proSCW1 = new ProductosWorkerSC[0];
        productoSC = new ProductoSC[0];
        //productosCuotasWorker = new CuotasVictoriaWorker[0];
        
        propiedades = prop;
    }
     
   
    public void iniciar(){
      
        
      
      productosWorker = new ProductosVictoriaWorker(propiedades);  
      productosWorker.addPropertyChangeListener(this);
      
      proSCW = new ProductosWorkerSC(propiedades);  
  //    productosWorker.addPropertyChangeListener(this);
     
      
      hilosMaximo = 2; //Integer.valueOf(propiedades.getProperty("hilos"));
      hilosCorriendo = 0;
      hilosIniciados = 0;
      hilosFinalizados = 0;
      hilosConError = 0;
      hilosACorrer = 0;
      codigosConError = "";
   
    }

    @Override
    protected ProductoVictoria[] doInBackground() {
        try {
            publish("LOADING");
            productosWorker.execute();
            proSCW.execute();
            hilosACorrer = productosWorker.get().length;   
            //hilosACorrer = 20;   
            
            productosFinalizados = new ProductoVictoria[hilosACorrer];
            productosDetalleWorker = new ProductoVictoriaWorker[hilosACorrer];
            //productosCuotasWorker = new CuotasVictoriaWorker[hilosACorrer];
            
            setProgress(0);
            while (hilosIniciados < hilosACorrer) {
                
                if(hilosCorriendo < hilosMaximo){
                    publish("RUNNING");
                    
                    productosDetalleWorker[hilosIniciados] = new ProductoVictoriaWorker(productosWorker.get()[hilosIniciados],propiedades);
                    productosDetalleWorker[hilosIniciados].addPropertyChangeListener(this);
                    productosDetalleWorker[hilosIniciados].setId(hilosIniciados);
                    productosDetalleWorker[hilosIniciados].execute();
                    
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
            Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            System.out.println("ERROR: No se pudo ejecutar el Worker de PRODUCTOS.");
            Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
        }
        jsArray = new JSONArray();
        test = new JSONObject();
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

            System.out.println(clase+">> "+source+" > "+value+" | ID: "+detalle.id);

            if(value.equals("DONE")){
             
                try {
                    if(productosDetalleWorker[detalle.id].isCancelled()){
                        
                    }else{
                        productosFinalizados[hilosFinalizados] = productosDetalleWorker[detalle.id].get();
                        
                        
                        if(productosDetalleWorker[detalle.id].getError()){
                            hilosConError++;
                            codigosConError += detalle.producto.getCodigo()+", ";
                            System.out.println("Producto ERROR: "+productosDetalleWorker[detalle.id].get());
                        }else{
                           
                           Boolean nuevo = true;
                          
                           productoDetalleWorker = productosDetalleWorker[detalle.id];
                           productosAComparar = productoDetalleWorker.get() ; 
                           productosACompararSC = proSCW.get();
                                for (ProductoSC productoSC : productosACompararSC ) {
                                    
                                 if (productosAComparar.getCodigo().equals(productoSC.getCodigo())) {
                                     if(!productoSC.imagen){
                                         ImagenesWSPOST(productosAComparar, productoSC.getId());
                                     System.out.println("LA IMAGEN VA ACÁ");
                                     }
                                }
                                 
                                 
                                }
                               
                            
                            System.out.println("Producto OK: "+productosDetalleWorker[detalle.id].get().getCodigo());
                        }
                        
                        hilosFinalizados ++;
                        hilosCorriendo --;
//                        setProgress(((hilosFinalizados+1)*100)/hilosACorrer);
                        Integer progress = ((hilosFinalizados+1)*100)/hilosACorrer;
                        
                    }
                    this.progress.setText("Cargando " + (progress).toString()+"%");
                    
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("EX:"+ex.getLocalizedMessage());
                    Logger.getLogger(VictoriaWorker.class.getName()).log(Level.SEVERE, null, ex);
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

  
    
    public void ProductosWSPOST(ProductoVictoria productoVT) {
       System.out.println("PRODUCTOS A INSERTAR " + productoVT.toString());
       }
     public void ImagenesWSPOST(ProductoVictoria productoVT, Integer id) {
     // System.out.println("IMAGENES A INSERTAR " + productoVT.getImagenJSON() + "CON EL ID " + id);
       
       try {

                String url = "https://portal.saracomercial.com/api/loader/productos/"+id+"/imagenes";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", "Bearer 5|qWJSOFV23sfTloGxkvkT0KUym0gCnEfkGQuipq9k");

                String urlParameters = productoVT.getJSON().toString();

                // Send post request
                con.setDoOutput(true);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(con.getOutputStream(), "UTF-8"));
                bw.write(urlParameters);
                bw.flush();
                bw.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
          //      System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
                System.out.println("Accept: " + con.getRequestProperty("Accept"));
                System.out.println("Authorization: " +"Bearer 5|qWJSOFV23sfTloGxkvkT0KUym0gCnEfkGQuipq9k");
                System.out.println("Method: " + con.getRequestMethod());
                System.out.println("Date: " + con.getDate());

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
            } catch (MalformedURLException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ProtocolException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
   
       }
}
