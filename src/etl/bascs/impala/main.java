/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala;

import bascs.website.clases.CuotasSC;
import bascs.website.clases.CuotasWorkerSC;
import com.formdev.flatlaf.IntelliJTheme;
import etl.bascs.impala.clases.MarcaVictoria;
import etl.bascs.victoria.clases.MarcasVictoriaWorker;
import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.ProductoVictoria;
import bascs.website.clases.RubroSC;
import etl.bascs.impala.clases.RubroVictoria;
import etl.bascs.impala.clases.Scalr;
import etl.bascs.impala.config.Propiedades;
import etl.bascs.impala.worker.MaestroWorker;
import bascs.website.clases.MarcasWorkerSC;
import bascs.website.clases.ProductoDetalleWorkerSC;
import bascs.website.clases.ProductoSC;
import bascs.website.clases.ProductosWorkerSC;
import bascs.website.clases.RubrosWorkerSC;
import etl.bascs.impala.clases.MarcaSC;
import etl.bascs.impala.clases.ProductoCuotasVictoria;
import etl.bascs.impala.worker.DetalleWorker;
import etl.bascs.impala.worker.PrestashopWorker;
import etl.bascs.victoria.clases.CuotasVictoriaWorker;
import etl.bascs.victoria.clases.ProductoVictoriaWorker;
import etl.bascs.victoria.clases.ProductosVictoriaWorker;
import etl.bascs.victoria.clases.RubrosVictoriaWorker;
import etl.bascs.victoria.clases.VictoriaWorker;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;


/**
 *
 * @author junju
 */
public class main extends javax.swing.JFrame implements java.beans.PropertyChangeListener {
    public static final Boolean DEBUG = true;
    
    public static DecimalFormat formatInt = new DecimalFormat("#,##0");
    public static DecimalFormat formatDec = new DecimalFormat("#,##0.##");
    
    public Propiedades propiedades;
    public Properties propGenerales = new Properties();
    public Properties propVictoria = new Properties();
    public Properties propSC = new Properties();
    public Properties propImpala = new Properties();
    public Properties propJellyfish = new Properties();
    
    public Boolean isClicked = false;
    public Boolean prendido = false;
    
    //PARA MAÑANA ARREGLAR LA PARTE DE TABLAS, EN EL WORKER, APARENTEMENTE DESPUES DE ESO YA FUNCIOONA
    
    public RubrosVictoriaWorker rubrosW;
    public ProductosVictoriaWorker productosW;
    public MarcasVictoriaWorker marcasW;
  
    
    public MarcasWorkerSC marcasSC;
    public RubrosWorkerSC rubrosSC;
    public ProductosWorkerSC productosSC;
    public ProductoDetalleWorkerSC productoSC;
    public VictoriaWorker victoriaW;
    public CuotasVictoriaWorker cuotasW;
    public CuotasWorkerSC cuotasSCW;
    public ArrayList<String> codigosSC;
    public ArrayList<String> codigo;
    public CuotasSC cuotasSC;
    public ProductoSC productoSCW;
    
    public PrestashopWorker prestashopW;
    public MaestroWorker maestroW;
    public DetalleWorker detalleW;
    public ProductoVictoriaWorker productoW;
    public ProductoVictoria productoVT;
    
    
    public RubroVictoria rubVt;
    public RubroSC rubScl;
    public Boolean rubroNuevo;
    public JFileChooser fc;
    public File fPrestashopImport;
    
    public Integer contadorVolumetrico;
    ////---CARGA DE TABLAS-----
    public String[] tablaHeaderMaestro = new String[] {"X", "ID","Codigo", "Alternativo", "EAN", "Stock", "Descripcion", "Marca", "Categoria", "Division" , "Precio"};
    public Integer[] tablaWithMaestro = new Integer[] {5,90,30,80,20,500,80,150,150,50};
    public Object[][] tablaContenidoMaestro;
    
    public String[] tablaHeaderProductos = new String[] {"X","ID","Codigo", "Nombre", "Descripción", "Marca", "Rubro", "Precio", "Stock"};
    public Integer[] tablaWithProductos = new Integer[] {5,90,150,500,150,150,150, 50, 50, 50, 15};
    public Object[][] tablaContenidoProductos;
    
    public String[] tablaHeaderProductosSC = new String[] {"X","ID","Codigo", "Nombre", "Descripción", "Rubro", "Marca", "Precio", "Stock"};
    public Integer[] tablaWithProductosSC = new Integer[] {90,150,500,150,150,150, 50, 50, 50};
    public Object[][] tablaContenidoProductosSC;
   
    public String[] tablaHeaderRubros = new String[] {"X","ID","Codigo", "Nombre", "Parent ID"};
    public Integer[] tablaRubros = new Integer[] {30,30,30,30,30};
    public Object[][] tablaContenidoRubros;
    
    public String[] tablaHeaderMarcas = new String[] {"X","ID","Codigo", "Nombre"};
    public Integer[] tablaMarcas = new Integer[] {30,30,30,30};
    public Object[][] tablaContenidoMarcas;
    
    public String[] tablaHeaderMarcasSC = new String[] {"X","ID","Codigo", "Nombre"};
    public Integer[] tablaMarcasSC = new Integer[] {30,30,30,30};
    public Object[][] tablaContenidoMarcasSC;
   
    public String[] tablaHeaderRubrosSC = new String[] {"X","ID","Codigo", "Nombre", "Parent ID"};
    public Integer[] tablaRubrosSC = new Integer[] {30,30,30,30,30};
    public Object[][] tablaContenidoRubrosSC;
  
    public String[] tablaHeaderPrestashop;
    
    public Producto productoBusqueda;
    public ProductoVictoria productoBusquedaV;
    public ProductoSC productoBusquedaSC;
    public ProductoVictoria[] productosFinalizados;
            
    /* CONSTRUCTOR */        
    /**********************************************************************************************************/
    public main() {
        initComponents();
        setLocationRelativeTo(null);
        iniciarPropiedades();
        iniciarListeners();
      
//        getVictoriaRubros();   
        contadorVolumetrico = 0;
    }
    
    /* PRODUCTO */
    /**
     * @param limpiarCodigo********************************************************************************************************/
    
    public void limpiarProducto(Boolean limpiarCodigo){
        if(limpiarCodigo){
            tProductoID.setText("");
        }else{
            tProductoID.setText(tProductoID.getText().replace("/", ""));
        }
        
        tProductoEAN.setText("");
        tProductoDescripcion.setText("");
        taProductoDescripcionLarga.setText("");
        tProductoMarca.setText("");
        tProductoCategoria.setText("");
        tProductoDivision.setText("");
        tProductoPrecioLista.setText("");
        tProductoPrecioCosto.setText("");
        tProductoPrecioVenta.setText("");
        tProductoPrecioVentaFinal.setText("");
        tProductoFactorCosto.setText("");
        tProductoFactorVenta.setText("");
        tProductoEnvioImporte.setText("");
        tProductoMoneda.setText("");
        tProductoExistencia.setSelected(false);
        tProductoStock.setText("");
        
        tbProductoDetallesTecnicos.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {},new String [] {"Atributo", "Valor"}));
        tbProductoDetallesTecnicos.setDefaultEditor(Object.class, null);
        
        tbProductoImagenes.setModel(new javax.swing.table.DefaultTableModel(new Object [][] {},new String [] {"ID", "URL"}));
        tbProductoImagenes.setDefaultEditor(Object.class, null);
        
        taProductoImagen.setIcon(null);
        
        tProductoEstado.setText("");
    }
    
    public void sincronizarVictoria(){
        Properties propiedades = new Properties();
        propiedades.putAll(propVictoria);
        propiedades.putAll(propGenerales);
        victoriaW = new VictoriaWorker(propiedades);
        
        victoriaW.estado = lVictoriaWorkerEstado;
        victoriaW.progress = lVictoriaEstado;
        victoriaW.addPropertyChangeListener(this);
        victoriaW.iniciar();
        victoriaW.execute(); 
    }  
    public void buscarProducto(Producto producto){
        if(!tProductoID.getText().isEmpty()){
            limpiarProducto(false);
            detalleW = new DetalleWorker(producto,getPropiedades());
            detalleW.addPropertyChangeListener(this);
            detalleW.execute();
        }else{
            JOptionPane.showMessageDialog(null, "Ingrese un codigo de producto válido.");
        }
    }
    public void buscarCuotas(ProductoCuotasVictoria cuotas){
        if(!tProductoIDV.getText().isEmpty()){
            limpiarProducto(false);
            cuotasW = new CuotasVictoriaWorker();
            cuotasW.addPropertyChangeListener(this);
            cuotasW.execute();
        }else{
            System.out.println("Error. No se han podido cargar las cuotas.");
        }
    }
    public void buscarCuotasSC(ArrayList<String> codigo){ 
       productosRecorrido();
        cuotasSCW = new CuotasWorkerSC(codigo, propSC);
        cuotasSCW.addPropertyChangeListener(this);
        cuotasSCW.execute();
       
      }
    
    public void buscarCuotaSC(String[] codigo){ // SE BUSCAN LAS CUOTAS POR CODIGO INTERNO DENTRO DEL WORKER
        if (!tProductoIDSC.getText().isEmpty()) { //OBS: PARA PODER HACER UN POST/OBTENCIÓN DE MÚLTIPLES CUOTAS, SE DEBEN SEPARAR POR COMAS EJ: CODIGO: ["100","704"]
            cuotasSCW = new CuotasWorkerSC(codigo, propSC);
            cuotasSCW.addPropertyChangeListener(this);
            cuotasSCW.execute();
        } else {
            System.out.println("Error. No se han podido cargar las cuotas.");
        }
    }
    public void buscarProductoVictoria (ProductoVictoria productoV){
       if(!tProductoIDV.getText().isEmpty()){
            productoW = new ProductoVictoriaWorker(productoV, getPropiedades());
            productoW.addPropertyChangeListener(this);
            productoW.execute();
            
        }else{
            JOptionPane.showMessageDialog(null, "Ingrese un codigo de producto válido.");
        }
    }
    public void buscarProductoWebsite (ProductoSC producto){
       if(!tProductoIDSC.getText().isEmpty()){
            productoSC = new ProductoDetalleWorkerSC(producto, getPropiedades());
            productoSC.addPropertyChangeListener(this);
            productoSC.execute();
        }else{
            JOptionPane.showMessageDialog(null, "Ingrese un codigo de producto válido.");
        }
    } 
   //CARGAR PRODUCTOS EN DETALLE  
    public void cargarProducto(Producto producto){
        if(producto.cargado){
            tProductoID.setText(producto.getCodigo());
            tProductoEAN.setText(producto.getCodigoEAN());
            tProductoDescripcion.setText(producto.getDescripcion());
            taProductoDescripcionLarga.setText(producto.getDescripcionLarga());
            tProductoMarca.setText(producto.getMarca());
            tProductoCategoria.setText(producto.getCategoria());
            tProductoDivision.setText(producto.getDivision());
            tProductoPrecioLista.setText(formatInt.format(producto.getPrecioLista()));
            tProductoPrecioCosto.setText(formatInt.format(producto.getPrecioCosto()));
            tProductoPrecioVenta.setText(formatInt.format(producto.getPrecioVenta()));
            tProductoPrecioVentaFinal.setText(formatInt.format(producto.getPrecioVentaFinal()));
            tProductoFactorCosto.setText(formatDec.format(((1-producto.getFactorCosto())*100))+" %");
            tProductoFactorVenta.setText(formatDec.format((producto.getFactorVenta()*100)-100)+" %");
            if(producto.getEnviosSumar())
                tProductoEnvioImporte.setText(formatInt.format(producto.getEnviosImporte()));
            tProductoMoneda.setText(producto.getMoneda());
            tProductoExistencia.setSelected(producto.getExistencia());
            tProductoStock.setText(producto.getStock().toString());
            
            
            // Carga de detalles tecnicos
            Object detallesTecnicos[][] = new Object[producto.getDetallesTecnicos().length][2];
            for (int i = 0; i < detallesTecnicos.length; i++){
                detallesTecnicos[i][0] = producto.getDetallesTecnicos()[i].getAtributo();
                detallesTecnicos[i][1] = producto.getDetallesTecnicos()[i].getValor();
            }
            cargarTablaDetallesTecnicos(detallesTecnicos);
            
            // Carga de imagenes
            Object imagenes[][] = new Object[producto.getImagenes().length][2];
            for (int i = 0; i < imagenes.length; i++){
                imagenes[i][0] = producto.getImagenes()[i].getId();
                imagenes[i][1] = producto.getImagenes()[i].getUrl();
            }
            if(imagenes.length > 0){
                cargarTablaImagenes(imagenes);
                cargarImagen(imagenes[0][1].toString());
            }
    }
    }
    public void paginator(){
        try {
            URL url = new URL("http://192.168.192.60:8080/WS/webapi/victoria/rubros/page/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestProperty("Accept", "application/json");
            
            System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
            
            http.disconnect();
        } catch (MalformedURLException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public void cargarProductosdeVictoria(ProductoVictoria producto){
        
        if(producto.cargado){
            tProductoIDV.setText(producto.getCodigo());
            tProductoNombreV.setText(producto.getNombre());
            taProductoDescripcionV.setText(producto.getDescripcion());
            tProductoMarcaVictoriaNombre.setText(producto.getMarcaVictoria().getNombre());
            tProductoMarcaVictoriaCodigo.setText(producto.getMarcaVictoria().getCodigo());
            tProductoRubroVictoriaCodigo.setText(producto.getRubroVictoria().getCodigo());
            tProductoRubroVictoriaNombre.setText(producto.getRubroVictoria().getNombre());
            
              Object cuotas[][] = new Object[producto.getCuotas().length][4];
            for (int i = 0; i < cuotas.length; i++){
                cuotas[i][0] = producto.getCuotas()[i].getNumero();
                cuotas[i][1] = producto.getCuotas()[i].getPrecio_contado();
                cuotas[i][2] = producto.getCuotas()[i].getPrecio_credito();
                cuotas[i][3] = producto.getCuotas()[i].getPrecio_cuota();
            }
             cargarTablaCuotasV(cuotas);
        
        }
    }
  public void addCheckbox(int column, JTable table){
     //CONTINUAR CON EL CHECKBOX
  }
  public void cargarProductosSC(ProductoSC producto){
      if(producto.cargado){
        
           tProductoIDSC.setText(formatInt.format(producto.getId()));
           tProductoSC.setText(producto.getCodigo());
           tProductoNombre.setText(producto.getNombre());
           taProductoDescripcionSC.setText(producto.getDescripcion());
           tProductoMarcaSCCodigo.setText(formatInt.format(producto.getMarcaSC().getId()));
           tProductoRubroSCCodigo.setText(formatInt.format(producto.getRubroSC().getId()));
           tStockSC.setText(formatInt.format(producto.getStock()));
           if(producto.getVisible() == 0){
               tVisibleSC.setSelected(false);
           }else{
               tVisibleSC.setSelected(true);
           }
           if(producto.getHabilitado() == 0){
               tHabilitadoSC.setSelected(false);
           }else{
               tHabilitadoSC.setSelected(true);
           }
           
            
      
      }else{
          JOptionPane.showInputDialog("Producto no existente, verifique ID");
      }
          
       
  }
    public void actualizarProductoSC(ProductoSC prodv){
       
        if (!prodv.equals(tProductoSC.getText())
                || !prodv.equals(tProductoNombre.getText()) || !prodv.getDescripcion().equals(taProductoDescripcionSC.getText()) || !prodv.getMarcaSC().equals(tProductoSCMarca.getText())
                || !prodv.equals(tProductoSCRubro.getText()) || !prodv.getVisible().equals(tVisibleSC.isSelected()) || !prodv.getHabilitado().equals(tHabilitadoSC.isSelected())
                || !prodv.getStock().equals(Integer.valueOf(tStockSC.getText()))) {
            prodv.setCodigo(tProductoSC.getText());
            prodv.setNombre(tProductoNombre.getText());
            prodv.setDescripcion(taProductoDescripcionSC.getText());
            if (tVisibleSC.isSelected()) {
                prodv.setVisible(1);
            } else {
                prodv.setVisible(0);
            }
            if (tHabilitadoSC.isSelected()) {
                prodv.setHabilitado(1);
            } else {
                prodv.setHabilitado(0);
            }
            PUTproductoSC(Integer.valueOf(tProductoIDSC.getText()), prodv);
            JOptionPane.showMessageDialog(null, "Datos actualizados con éxito.");
        }else{
            System.out.println("NO SE HA PODIDO ACTUALIZAR");
        }
    }
    public void cargarTablaCuotasV(Object[][] contenido){
       tbProductoCuotas.setModel(new javax.swing.table.DefaultTableModel(contenido,new String [] {"CUOTA", "PRECIO CONTADO", "PRECIO CREDITO", "PRECIO CUOTA"}));
        tbProductoCuotas.getColumnModel().getColumn(0).setPreferredWidth(130);
        tbProductoCuotas.getColumnModel().getColumn(1).setPreferredWidth(130);
        tbProductoCuotas.getColumnModel().getColumn(2).setPreferredWidth(130);
        tbProductoCuotas.getColumnModel().getColumn(3).setPreferredWidth(130);
    }
    public void cargarTablaCuotasSC(Object[][] contenido){
       tbProductoCuotasSC.setModel(new javax.swing.table.DefaultTableModel(contenido,new String [] {"CUOTA", "IMPORTE", "PRODUCTO ID"}));
        tbProductoCuotasSC.getColumnModel().getColumn(0).setPreferredWidth(130);
        tbProductoCuotasSC.getColumnModel().getColumn(1).setPreferredWidth(130);
        tbProductoCuotasSC.getColumnModel().getColumn(2).setPreferredWidth(130);
       
    }
    public void cargarTablaImagenes(Object[][] contenido){
        tbProductoImagenes.setModel(new javax.swing.table.DefaultTableModel(contenido,new String [] {"ID", "URL"}));
        tbProductoImagenes.getColumnModel().getColumn(0).setPreferredWidth(20);
        tbProductoImagenes.getColumnModel().getColumn(1).setPreferredWidth(130);
    }
    
    public void cargarTablaDetallesTecnicos(Object[][] contenido){
        tbProductoDetallesTecnicos.setModel(new javax.swing.table.DefaultTableModel(contenido,new String [] {"Atributo", "Valor"}));
    }
    
    public void cargarImagen(String imagen){
        BufferedImage img;
        
        // Se verifica que la carpeta de imagenes exista, sino se crea.
        File carpeta = new File("imagenes/");
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                appendMensaje("Se creo directorio de imagenes.");
            } else {
                appendMensaje("Error al crear directorio de imagenes.");
            }
        }
        
        // Se verifica que el archivo de la imagen exista, sino se intenta descargar.
        File archivo = new File("imagenes/"+imagen);
        if (!archivo.exists()) {
            descargarImagen("http"+"://"+getOrigen().getProperty("servidor")+":"+getOrigen().getProperty("puerto")+getOrigen().getProperty("imagenes"),imagen);
        }else{
            // Se verifica la antiguedad de la imagen, si es vieja, se descarga nuevamente.
            try {
                Instant now = Instant.now();
                FileTime ft = Files.getLastModifiedTime(archivo.toPath());
                
                Instant instant1 = now.truncatedTo(ChronoUnit.DAYS);
                Instant instant2 = ft.toInstant().truncatedTo(ChronoUnit.DAYS);

                if (!instant1.equals(instant2)) {
                    descargarImagen("http"+"://"+getOrigen().getProperty("servidor")+":"+getOrigen().getProperty("puerto")+getOrigen().getProperty("imagenes"),imagen);
                }
            } catch (IOException ex) {
                System.out.println("ERROR AL COMPARAR FECHAS");
            }
        }
        
        // Se verifica que el archivo de la imagen exista, sino se catga el logo.
        if (archivo.exists()) {
            // Se intenta cargar la imagen.
            try {
                img = ImageIO.read(archivo);
                Dimension newMaxSize = taProductoImagen.getSize();
                BufferedImage resizedImg = Scalr.resize(img, Scalr.Method.SPEED, newMaxSize.width, newMaxSize.height);
                
                taProductoImagen.setIcon(new javax.swing.ImageIcon(resizedImg));
                appendMensaje("Se cargo imagen local redimensionada.");
            } catch (IOException ex) {
                appendMensaje( "Error al acceder a la imagen local.");
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                appendMensaje( "Algun error extrano al abrir: "+archivo);
            }
        }else{
            String servidorLogo = "http://www.saracomercial.com/images/logo/";
            String imagenLogo = "logo_sara_nuevo.png";
            File archivoLogo = new File("imagenes/"+imagenLogo);
            if (!archivoLogo.exists()) {
                descargarImagen(servidorLogo, imagenLogo);
            }
            
            if (archivoLogo.exists()) {
                try {
                    img = ImageIO.read(archivoLogo);
                    Dimension newMaxSize = taProductoImagen.getSize();
                    BufferedImage resizedImg = Scalr.resize(img, Scalr.Method.SPEED, newMaxSize.width, newMaxSize.height);

                    taProductoImagen.setIcon(new javax.swing.ImageIcon(resizedImg));
                    appendMensaje("Se cargo imagen local redimensionada.");
                } catch (IOException ex) {
                    appendMensaje( "Error al acceder a la imagen local.");
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (Exception ex) {
                    appendMensaje( "Algun error extrano al abrir: "+archivo);
                }
            }else{
                appendMensaje( "Algun error extrano al cargar el logo: "+imagenLogo);
            }
            
        }
    }
    public void descargarImagen(String rutaOnline, String imagen){
        BufferedImage img;
        String extension = "";
        int i = imagen.lastIndexOf('.');
        if (i > 0) {
            extension = imagen.substring(i+1);
        }
        try {
            img = ImageIO.read(new URL(rutaOnline+imagen));
            File outputfile = new File("imagenes/"+imagen);
            ImageIO.write(img, extension, outputfile);
            appendMensaje("Se descargo una imagen online.");
        } catch (MalformedURLException ex) {
            appendMensaje("Error al acceder a la imagen online.");
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            appendMensaje("Error al descargar la imagen online. Se intenta aplicando UpperCase"); 
            try {
                img = ImageIO.read(new URL(rutaOnline+imagen.toUpperCase()));
                File outputfile = new File("imagenes/"+imagen.toUpperCase());
                ImageIO.write(img, extension, outputfile);
                appendMensaje("Se descargo una imagen online aplicando UpperCase.");
            } catch (MalformedURLException ex2) {
                appendMensaje("Error al acceder a la imagen online.");
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex2);
            } catch (IOException ex2) {
                appendMensaje("Error al descargar la imagen online. El archivo "+imagen+" no existe."); 
            }
        }
    }
    
    public Dimension getImagenEscalada(Dimension imageSize, Dimension boundary) {
        double widthRatio = boundary.getWidth() / imageSize.getWidth();
        double heightRatio = boundary.getHeight() / imageSize.getHeight();
        double ratio = Math.min(widthRatio, heightRatio);

        return new Dimension((int) (imageSize.width  * ratio),(int) (imageSize.height * ratio));
    }
    
    /*BUSCAR REGISTROS MAESTRO*/
    
    public void limpiarMaestro(){
        tMaestroCantidad.setText("0");
        cargarTablaMaestro(new Object [][] {});
    }
    
    public void limpiarProductosVictoria(){
        tVictoriaCantidad.setText("0");
        lVictoriaWorkerEstado.setText("");
        lVictoriaEstado.setText("");
        cargarTablaProductosVictoria(new Object [][] {});
        //tbVictoriaProductos
    }
    
    
    // BUSCADORES 
    
    public void buscarRubrosVictoria(){
        Properties propiedades = new Properties();
        propiedades.putAll(propVictoria);
        propiedades.putAll(propGenerales);
        
        rubrosW = new RubrosVictoriaWorker(propiedades);
        rubrosW.addPropertyChangeListener(this);
        rubrosW.execute();
    }
     public void buscarCuotas(){
        Properties propiedades = new Properties();
        propiedades.putAll(propVictoria);
        propiedades.putAll(propGenerales);
        
        cuotasW = new CuotasVictoriaWorker(propiedades);
        cuotasW.addPropertyChangeListener(this);
        cuotasW.execute();
    }
     
    public void buscarRubrosSC(){
        cargarTablaRubrosSC(new Object[0][0]);
        
        Properties propiedades = new Properties();
        propiedades.putAll(propSC);
        propiedades.putAll(propGenerales);
        
        rubrosSC = new RubrosWorkerSC(propiedades);
        rubrosSC.addPropertyChangeListener(this);
        rubrosSC.execute();
    }
     
    public void buscarMarcasSC(){
        Properties propiedades = new Properties();
        propiedades.putAll(propSC);
        propiedades.putAll(propGenerales);
        
        marcasSC = new MarcasWorkerSC(propiedades);
        marcasSC.addPropertyChangeListener(this);
        marcasSC.execute();
    }
    public void buscarMarcasVictoria(){
        limpiarMaestro();
        
        Properties propiedades = new Properties();
        propiedades.putAll(propVictoria);
        propiedades.putAll(propGenerales);
        
        marcasW = new MarcasVictoriaWorker(propiedades);
        marcasW.addPropertyChangeListener(this);
        marcasW.execute();
    }
    public void buscarMaestro(){
        limpiarMaestro();
        maestroW = new MaestroWorker(getPropiedades());
        maestroW.addPropertyChangeListener(this);
        maestroW.execute();
    }
    public void buscarProductosVictoria(){ // Listado de productos, sin precio ni cuotas.
        limpiarProductosVictoria();
        
        Properties propiedades = new Properties();
        propiedades.putAll(propVictoria);
        propiedades.putAll(propGenerales);
        
        productosW = new ProductosVictoriaWorker(propiedades);
        productosW.addPropertyChangeListener(this);
        productosW.execute();
    }
    public void buscarProductosWebsite(){
        limpiarMaestro();
        
        Properties propiedades = new Properties();
        propiedades.putAll(propSC);
        propiedades.putAll(propGenerales);
        productosSC = new ProductosWorkerSC(propiedades);
        productosSC.addPropertyChangeListener(this);
        productosSC.execute();
    }
    
    
    
    //CARGAR TABLAS DEL MAIN //
    /*******************************************************/
    public void cargarTablaMaestro(Object[][] contenido){
        tbMaestroProductos.setModel(new javax.swing.table.DefaultTableModel(contenido,tablaHeaderMaestro));
        tbMaestroProductos.getColumnModel().removeColumn(tbMaestroProductos.getColumnModel().getColumn(0));
        for (int i = 0; i < tbMaestroProductos.getColumnCount(); i++) {
            tbMaestroProductos.getColumnModel().getColumn(i).setPreferredWidth(tablaWithMaestro[i]);
        }
    }
     public void cargarTablaProductosVictoria(Object[][] contenidos){
        tbVictoriaProductos.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderProductos));
        tbVictoriaProductos.getColumnModel().removeColumn(tbVictoriaProductos.getColumnModel().getColumn(0));
        for (int i = 0; i < tbVictoriaProductos.getColumnCount(); i++) {
            tbVictoriaProductos.getColumnModel().getColumn(i).setPreferredWidth(tablaWithProductos[i]);
        }
    }
    public void cargarTablaRubrosVictoria(Object[][] contenidos){
        tRubrosVictoria.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderRubros));
        tRubrosVictoria.getColumnModel().removeColumn(tRubrosVictoria.getColumnModel().getColumn(0));
        for (int i = 0; i < tRubrosVictoria.getColumnCount(); i++) {
            tRubrosVictoria.getColumnModel().getColumn(i).setPreferredWidth(tablaRubros[i]);
        }
    }
     public void cargarTablaRubrosSC(Object[][] contenidos){
        tRubroSC.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderRubrosSC));
        tRubroSC.getColumnModel().removeColumn(tRubroSC.getColumnModel().getColumn(0));
        for (int i = 0; i < tRubroSC.getColumnCount(); i++) {
            tRubroSC.getColumnModel().getColumn(i).setPreferredWidth(tablaRubrosSC[i]);
        }
    }
    public void cargarTablaProductoSC(Object[][] contenidos){
        tbProductosSC.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderProductosSC));
        tbProductosSC.getColumnModel().removeColumn(tbProductosSC.getColumnModel().getColumn(0));
        for (int i = 0; i < tbProductosSC.getColumnCount(); i++) {
            tbProductosSC.getColumnModel().getColumn(i).setPreferredWidth(tablaWithProductosSC[i]);
        }
        
    }
    public void cargarTablaMarcas(Object[][] contenidos){
        tMarcasVictoria.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderMarcas));
        tMarcasVictoria.getColumnModel().removeColumn(tMarcasVictoria.getColumnModel().getColumn(0));
        for (int i = 0; i < tMarcasVictoria.getColumnCount(); i++) {
            tMarcasVictoria.getColumnModel().getColumn(i).setPreferredWidth(tablaMarcas[i]);
        }
    }
   public void cargarTablaMarcasSC(Object[][] contenidos){
        tMarcaSC.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderMarcasSC));
        tMarcaSC.getColumnModel().removeColumn(tMarcaSC.getColumnModel().getColumn(0));
        for (int i = 0; i < tMarcaSC.getColumnCount(); i++) {
            tMarcaSC.getColumnModel().getColumn(i).setPreferredWidth(tablaMarcasSC[i]);
        }
       
   }
   /*********************RECORRIDOS**************************************/
   
   public void rubrosRecorrido(){
         isClicked = true;        
        Boolean rubroEliminar;      
        Integer rubrosOmitidos = 0;
        
        // PUT - ACTUALIZA
        // POST - CREA
        
        try{  
            taVictoriaSincronizar.append("\n\nInicia sincronizacion de RUBROS.");
            //RECORRIDO VICTORIA
            for (RubroVictoria rubVictoria : rubrosW.get()) {
                rubroNuevo = true;
                for (RubroSC rubSC : rubrosSC.get()) {
                    if(rubVictoria.getCodigo().equals(rubSC.getCodigo())){
                        rubroNuevo = false;
                        if(!rubVictoria.getNombre().equals(rubSC.getNombre())){
                            System.out.println("RUBROS [PUT] (Nombres diferentes) \t VT: "+rubVictoria.getNombre()+"|"+"SC: "+rubSC.getNombre());
                            rubrosWSPUT(rubSC.getId(), rubSC);
                            
                        }else{
                            rubrosOmitidos++;
                        }
                    }
                }
                
                if(rubroNuevo){
                   rubrosWSPOST(rubVictoria);
                   buscarRubrosVictoria();
                   
                }
                
            }
            
            //RECORRIDO WEBSERVICE
            for (RubroSC rubSC : rubrosSC.get()) {
                rubroEliminar = true;
                for (RubroVictoria rubVictoria : rubrosW.get()) {
                    if(rubVictoria.getCodigo().equals(rubSC.getCodigo())){
                        rubroEliminar = false;
                    }
                }
                
                if(rubroEliminar){
                    rubrosWSDELETE(rubSC);
                }
            }
            
            //RECORRIDO VICTORIA PARENT ID
            for (RubroVictoria rubVictoria : rubrosW.get()) {
                for (RubroSC rubSC : rubrosSC.get()) {
                    if(rubVictoria.getCodigo().equals(rubSC.getCodigo()) && rubSC.getParent_id() == 0 || rubSC.getParent_id() == null){
                        if(rubVictoria.getParent_codigo() != null && !rubVictoria.getParent_codigo().equals("")){
                            if(rubrosSC.obtenerRubro(rubVictoria.getParent_codigo()) != null){
                                rubVictoria.setParent_id(rubrosSC.obtenerRubro(rubVictoria.getParent_codigo()).getId());
                                
                            }else{
                                taVictoriaSincronizar.append("\nEl RUBRO: "+rubVictoria.getNombre()+ "("+rubVictoria.getCodigo()+") no posee un PARENT CODIGO válido: "+rubVictoria.getParent_codigo()+ ". Verificar en BASCS.");
                                //System.out.println("PARENT ID: No se encuentra el RUBRO "+rubVictoria.getParent_codigo()+ "en el WS.");
                                rubVictoria.setParent_id(null);
                                
                            }
                            System.out.println("RUBROS [PUT] (No tiene Codigo Parent) \t VT: "+rubVictoria.getNombre()+"|"+"SC: "+rubSC.getNombre());
                            rubrosWSPUT(rubSC.getId(), rubSC);
                            
                        }
                    }
                }
            }
            
            taVictoriaSincronizar.append("\nSe omitieron "+rubrosOmitidos+" RUBROS.");    
            taVictoriaSincronizar.append("\nSe completo la sincronizacion de RUBROS.");    
          
              //System.out.println("RUBROS OMITIDOS: "+rubrosOmitidos);
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
   }
   
   public void marcasRecorrido(){
        Boolean marcaEliminar;      
        Integer marcasOmitidos = 0;
        Boolean marcaNueva = false;
        // PUT - ACTUALIZA
        // POST - CREA
        
        try{  
            taVictoriaSincronizar.append("\n\nInicia sincronizacion de MARCAS.");
            //RECORRIDO VICTORIA
            for (MarcaVictoria marVictoria : marcasW.get()) {
                marcaNueva = true;
                for (MarcaSC marSC : marcasSC.get()) {
                    if(marVictoria.getCodigo().equals(marSC.getCodigo())){
                        marcaNueva = false;
                        if(!marVictoria.getNombre().equals(marSC.getNombre())){
                            //PUT MARCAS
                            System.out.println("MARCAS [PUT] (Nombres diferentes) \t VT: "+marVictoria.getNombre()+"|"+"SC: "+marSC.getNombre());
                           }else{
                            marcasOmitidos++;
                        }
                    }
                }
                
                if(marcaNueva){
                   marcasWSPOST(marVictoria);
              //     System.out.println("Se insertara a marcas : "+marVictoria.getNombre());
                   buscarMarcasVictoria();
                }
                
            }
            
            //RECORRIDO WEBSERVICE ESTO DEBE SER PARA ELIMINAR MARCAS
            for (MarcaSC marSC : marcasSC.get()) {
                marcaEliminar = true;
                for (MarcaVictoria marVictoria : marcasW.get()) {
                    if(marVictoria.getCodigo().equals(marSC.getCodigo())){
                        marcaEliminar = false;
                    }
                }
                
                if(marcaEliminar){
                    marcasWSDELETE(marSC);
                }
            }
            
            taVictoriaSincronizar.append("\nSe omitieron "+marcasOmitidos+" MARCAS.");    
            taVictoriaSincronizar.append("\nSe completo la sincronizacion de MARCAS.");  
            //System.out.println("MARCAS OMITIDAS: "+marcasOmitidos);
            
        } catch (Exception e) {
            e.printStackTrace();
        }    
   }
   
   public void productosRecorrido(){
        Integer productosOmitidos = 0;
        Boolean productoNuevo = false;
        ProductoSC productoSC = new ProductoSC();
        // PUT - ACTUALIZA
        // POST - CREA
        try{
           //RECORRIDO VICTORIA
           for (ProductoVictoria prodVictoria : productosW.get()) {
               productoNuevo = true;
               prendido = false;
               for (ProductoSC podSC : productosSC.get()) {
                   
                     if (prodVictoria.getCodigo().equals(podSC.getCodigo())) {
                          prodVictoria.setProducto_id(podSC.getId());
                          productoNuevo = false;
                           
                           if (!prodVictoria.getNombre().trim().equals(podSC.getNombre().trim())) {
                               //        productosWSPUT(podSC.getId(), prodVictoria);

                           } else {
                               productosOmitidos++;
                           }
                       }
                   
               // SI LOS IDS Y NUMEROS DE CUOTA DE VICTORIA LOS IGUALES A LOS DE CUOTASSC NO SE INSERTAN
             }    
                   /*
                 for (ProductoCuotasVictoria cuotasVictoria : cuotasW.get()){
                     for(CuotasSC cuotasSC : cuotasSCW.get()){
                     cuotasVictoria.setProducto_id(productoSC.getId());
                 if (cuotasVictoria.getCodigo().equals(productoSC.getCodigo()) && 
                         cuotasVictoria.getProducto_id() == cuotasSC.getId()) {
                     
                     System.out.println("A INSERTAR " + cuotasVictoria.getCodigo() + "ID " + cuotasVictoria.getProducto_id());
                
                 }else{
                     productosOmitidos++;
                     
                       }
                   }
               
               }
               */      /*
                   for (ProductoCuotasVictoria cuotasVictoria : cuotasW.get()) {
                       for (CuotasSC cuotasSC : cuotasSCW.get()) {
                           if (prodVictoria.getProducto_id() == (cuotasSC.getProducto_id())
                                   && cuotasSC.getNumero() != cuotasVictoria.getNumero()) {
                               System.out.println("A INSERTAR " + cuotasVictoria.getCodigo());
                           }
                       }
                   }
                   
                    */       // SE ASIGNA A LOS PRODUCTOS VICTORIA, LOS RUBROS SC CORRESPONDIENTES
                           if (rubrosSC.obtenerRubro(prodVictoria.getRubroVictoria().getCodigo()) != null) {
                               prodVictoria.setRubroSC(rubrosSC.obtenerRubro(prodVictoria.getRubroVictoria().getCodigo()));
                           } else {
                               taVictoriaSincronizar.append("RUBRO ID: No se encuentra el RUBRO " + prodVictoria.getRubroVictoria().getNombre() + "en el WS.");
                           }

                           /*
                    for (RubroSC rubSC : rubrosSC.get()) {
                        // Verifico si el rubro del producto victoria, existe en SC
                        if(prodVictoria.getRubroVictoria().getCodigo().equals(rubSC.getCodigo())){

                            
                        }
                    }
                            */
                           // SE ASIGNA A LOS PRODUCTOS VICTORIA, LAS MARCAS SC CORRESPONDIENTES
                           if (marcasSC.obtenerMarca(prodVictoria.getMarca()) != null) {
                               prodVictoria.setMarca_id(marcasSC.obtenerMarca(prodVictoria.getMarca()).getId());
                           } else {
                               taVictoriaSincronizar.append("MARCA ID: No se encuentra la MARCA " + prodVictoria.getMarca() + "en el WS.");
                           }

                           /*
                    for (MarcasSC marSC : marcasSC.get()) {
                        if(prodVictoria.getMarca().equals(marSC.getCodigo())){
                            if(marcasSC.obtenerMarca(prodVictoria.getMarca()) != null){
                                prodVictoria.setMarca_id(marcasSC.obtenerMarca(prodVictoria.getMarca()).getId());

                            }else{
                                System.out.println("MARCA ID: No se encuentra la MARCA "+prodVictoria.getMarca()+ "en el WS.");
                            }
                        }
                    }
                            */
                       

                       if (productoNuevo) {
                           //      ProductosWSPOST(prodVictoria);
                           taVictoriaSincronizar.append("SE INSERTARON " + prodVictoria.getCodigo());
                       } else {

                       }
                 
               
           }

           taVictoriaSincronizar.append("\nSe omitieron " + productosOmitidos + " PRODUCTOS.");
           taVictoriaSincronizar.append("\nSe completo la sincronizacion de PRODUCTOS.");

       } catch (Exception e) {
           e.printStackTrace();
       }
   }
    public void cuotasRecorrido() { // RECORRE LAS CUOTASVICTORIA, Y COMPARA SU CODIGO CON EL CODIGO DE LOS PRODUCTOS DEL WS
     Integer cuotaOmitida = 0;
         try {
             for (ProductoVictoria prodVictoria : productosW.get()) {
             prendido = false;
               for (ProductoSC podSC : productosSC.get()) {
                     if (prodVictoria.getCodigo().equals(podSC.getCodigo())) {
                          prodVictoria.setProducto_id(podSC.getId());
                     }
             for (ProductoCuotasVictoria cuotasVictoria : cuotasW.get()){
                 if (prodVictoria.getProducto_id() != (cuotasSC.getProducto_id())
                         && cuotasSC.getNumero() != cuotasVictoria.getNumero()) {
                     System.out.println("A INSERTAR " + cuotasVictoria.getCodigo());
                 }else{
                     System.out.println("volver a verificar");
                 }
               // SI LOS IDS Y NUMEROS DE CUOTA DE VICTORIA LOS IGUALES A LOS DE CUOTASSC NO SE INSERTAN
             } 
                     }
               
             }
        } catch (InterruptedException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ExecutionException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }
     
    //POST - PUT - DELETE DEL WEBSERVICE*/
    private void cuotasWSPOST(Integer id, ProductoCuotasVictoria cuotas) {
   if(!DEBUG){
            try {
                ProductoCuotasVictoria cuotaVT = new ProductoCuotasVictoria();
                String url = "http://www.saracomercial.com/panel/api/loader/productos/"+id+"/cuotas";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Accept", "application/json");
                con.setRequestProperty("Authorization", propSC.getProperty("clave"));

                String urlParameters = cuotaVT.getJSON().toString();
                System.out.println("CUOTAS A INSERTAR " + urlParameters);
                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
                System.out.println("Accept: " + con.getRequestProperty("Accept"));
                System.out.println("Authorization: " + propSC.getProperty("clave"));
                System.out.println("Method: " + con.getRequestMethod());

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
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
    private void PUTproductoSC(Integer id, ProductoSC producto) {
if(!DEBUG){
        try {
           
            String url = "http://www.saracomercial.com/panel/api/loader/productos/"+id+"";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            //add reuqest header
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
   //         con.setRequestProperty("Authorization", propSC.getProperty("clave"));

            String urlParameters = producto.getJSON().toString();
            System.out.println("SE ACTUALIZARA " + producto.getJSON().toString() + " del ID: " + id);
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'PUT' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
            System.out.println("Accept: " + con.getRequestProperty("Accept"));
            System.out.println("Authorization: " + propSC.getProperty("clave"));
            System.out.println("Method: " + con.getRequestMethod());
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
    public void marcasWSPOST(MarcaVictoria marcasVT){
        if(!DEBUG){
             try {

                String url = "http://www.saracomercial.com/panel/api/loader/productos/marcas";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Accept", "application/json");
     //           con.setRequestProperty("Authorization", propSC.getProperty("clave"));

                String urlParameters = marcasVT.getJSON().toString();

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
                System.out.println("Accept: " + con.getRequestProperty("Accept"));
                System.out.println("Authorization: " + propSC.getProperty("clave"));
                System.out.println("Method: " + con.getRequestMethod());

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
  
    public void rubrosWSPUT(Integer id, RubroSC rubroSC){
        
        //ACTUALIZAR EN EL WS
        if(!DEBUG){
          try {  
        String url = "http://www.saracomercial.com/panel/api/loader/rubros/"+id+"";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            //add reuqest header
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
    //        con.setRequestProperty("Authorization", propSC.getProperty("clave"));

            String urlParameters = rubroSC.getJSON().toString();
            System.out.println("SE ACTUALIZARA " + rubroSC.getJSON().toString() + " del ID: " + id);
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'PUT' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
            System.out.println("Accept: " + con.getRequestProperty("Accept"));
            System.out.println("Authorization: " + propSC.getProperty("clave"));
            System.out.println("Method: " + con.getRequestMethod());
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
     public void productosWSPUT(Integer id, ProductoVictoria podVictoria){
        if(!DEBUG){
            
            try {  
        String url = "http://www.saracomercial.com/panel/api/loader/productos/"+id+"";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            //add reuqest header
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Accept", "application/json");
  //          con.setRequestProperty("Authorization", propSC.getProperty("clave"));

            String urlParameters = podVictoria.getJSON().toString();
            System.out.println("SE ACTUALIZARA " + podVictoria.getJSON().toString() + " del ID: " + id);
            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'PUT' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);
            System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
            System.out.println("Accept: " + con.getRequestProperty("Accept"));
            System.out.println("Authorization: " + propSC.getProperty("clave"));
            System.out.println("Method: " + con.getRequestMethod());
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
     
    public void ProductosWSPOST(ProductoVictoria productoVT){
        if(!DEBUG){
          try {

                String url = "http://www.saracomercial.com/panel/api/loader/productos";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Accept", "application/json");
   //             con.setRequestProperty("Authorization", propSC.getProperty("clave"));

                String urlParameters = productoVT.getJSON().toString();

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
                System.out.println("Accept: " + con.getRequestProperty("Accept"));
                System.out.println("Authorization: " + propSC.getProperty("clave"));
                System.out.println("Method: " + con.getRequestMethod());

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
    public void rubrosWSPOST(RubroVictoria rubroVT){
        if(!DEBUG){
            try {
               
                String url = "http://www.saracomercial.com/panel/api/loader/rubros";
                URL obj = new URL(url);
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

                con.setRequestMethod("POST");
                con.setRequestProperty("Content-type", "application/json");
                con.setRequestProperty("Accept", "application/json");
  //              con.setRequestProperty("Authorization", propSC.getProperty("clave"));

                String urlParameters = rubroVT.getJSON().toString();

                // Send post request
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();

                int responseCode = con.getResponseCode();
                System.out.println("\nSending 'POST' request to URL : " + url);
                System.out.println("Post parameters : " + urlParameters);
                System.out.println("Response Code : " + responseCode);
                System.out.println("Content-Type: " + con.getRequestProperty("Content-type"));
                System.out.println("Accept: " + con.getRequestProperty("Accept"));
                System.out.println("Authorization: " + propSC.getProperty("clave"));
                System.out.println("Method: " + con.getRequestMethod());

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
    
    public void rubrosWSDELETE(RubroSC rubroWS){
        // ELIMINAR DEL WS
        if(!DEBUG){
            
        }else{
            System.out.println("DEBUG: rubrosWSDELETE");
        }
    }
   
    public void marcasWSDELETE(MarcaSC marcaWS){
        //ELIMINAR DEL WS
        if(!DEBUG){
            
        }else{
            System.out.println("DEBUG: marcasWSDELETE");
        }
    }
    /**/
        
     
    public void generarArchivoContenido(Object[] headers, Object[][] contenido, String filename){
        File carpeta = new File("export/");
        Writer out;
                
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                appendMensaje("Se crea directorio de exportaciones.");
            } else {
                appendMensaje("Error al crear carpeta de exportaciones.");
            }
        }
        
        try {
            //Apertura de archivo
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("export/"+filename),
                            "UTF-8"
                    )
            );
            
            //Escribir encabezados
            for (Object header : headers) {
                out.write(header.toString() + ";");
            }
            out.write("\n");
            
            //Escribir cuentas
            for (Object[] linea : contenido) {
                for (int c = 0; c < linea.length; c++) {
                    //System.out.println("c:"+c+ " linea:"+linea.length);
                    //VERIFICAR PORQUE MUERE AL LLEGAR A LA COLUMNA 6TA
                    //ES LA COLUMNA DE Descuento desde (aaaa-mm-dd) QUE NO ESTA ESTABLECIDA MANUALMENTE
                    //se dispuso que si null, ponga ""
                    
                    out.write((linea[c]!=null?linea[c].toString():""));
                    if (c < linea.length) {
                        out.write(";");
                    }
                }
                out.write("\n");
            }
            
            //Cerramos archivo
            out.close();
            
        } catch (FileNotFoundException ex) {
            appendMensaje("Error al crear archivo: "+filename);
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            appendMensaje("Error IO de archivo: "+filename);
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    }
    
    public void limpiarPrestashop(){
        tPrestashopFileExport.setText("");
        tPrestashopPath.setText("");
        bPrestashopProcesar.setEnabled(true);
        bPrestashopAbrir.setEnabled(false);
        tPrestashopWorkerEstado.setText("");
        tProductoEstado.setText("");
    }
    
    public void generarArchivoPrestashop(Object[] header, Producto[] productos, String filename){
        String pathImagenes = "http://"+getOrigen().getProperty("servidor")+":"+getOrigen().getProperty("puerto")+getOrigen().getProperty("imagenes");
        
        Object[][] contenidoDefault = new Object[productos.length][header.length];
    
        int i = 0;
        for(Producto producto : productos){
            //PONER UN java.lang.ArrayIndexOutOfBoundsException
            //PORQUE SI SE ESCRIBE MAL ALGUNA COLUMNA, DA ERROR 
            
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("ID")] = "";
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Nombre")] = producto.getDescripcionFormateada();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Precio impuestos incluidos")] = producto.getPrecioVentaFinal();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("ID regla de impuestos")] = 1;
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("En oferta (0/1)")] = producto.getOferta();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Valor del descuento")] = producto.getPrecioDescuento();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Porcentaje de descuento")] = producto.getPrecioDescuento();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Referencia nº")] = producto.getCodigo();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Marca")] = producto.getMarca();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("EAN13")] = producto.getCodigoEAN();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Anchura")] = producto.getDetalleTecnicoFormateado("ANCHO");
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Altura")] = producto.getDetalleTecnicoFormateado("ALTO");
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Profundidad")] = producto.getDetalleTecnicoFormateado("LARGO");
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Peso")] = producto.getDetalleTecnicoFormateado("PESOMAYOR");
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Cantidad")] = producto.getStock();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Descripción")] = producto.getDescripcionLargaFormateada();
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("URL's de las imágenes (x,y,z...)")] = producto.getImagenesURL(pathImagenes);
            contenidoDefault[i][Arrays.asList(tablaHeaderPrestashop).indexOf("Característica (Nombre:Valor:Posición:Personalizado)")] = producto.getCaracteristicas()+"&"+producto.getDescripcionTecnica();
            if(producto.getPesoRecalculado()){
                contadorVolumetrico++;
            }
            i++;
        }
        
        
        System.out.println("Header: "+header.length);
        System.out.println("Productos: "+productos.length);
        System.out.println("Contenido: "+contenidoDefault.length);
        System.out.println("Filename: "+filename);
        

        generarArchivoContenido(header, contenidoDefault, filename);
        appendMensaje("Se generó archivo Prestashop");
        appendMensaje("Se recalcularon "+contadorVolumetrico+" pesos.");
    }
    
    public List<Producto> cargarProdutosCSV(String fileName) {
        List<Producto> productos = new ArrayList<>();
        Path pathToFile = Paths.get(fileName);

        // create an instance of BufferedReader
        // using try with resource, Java 7 feature to close resources
        try (BufferedReader br = Files.newBufferedReader(pathToFile,
                StandardCharsets.US_ASCII)) {

            // read the first line from the text file
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) {

                // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                Producto producto = new Producto(getPropiedades());

                // adding book into ArrayList
                productos.add(producto);

                // read next line before looping
                // if end of file reached, line would be null
                line = br.readLine();
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return productos;
    }
    


    
    /* PROPIEDADES */
    /**********************************************************************************************************/
    
    private void iniciarPropiedades(){
        propiedades = new Propiedades(this);
        
        tGeneralesEnviosSumar.setSelected(Boolean.valueOf(propGenerales.getProperty("envios_sumar")));
        tGeneralesEnviosImporte.setText(propGenerales.getProperty("envios_importe"));
        tGeneralesEnviosDesde.setText(propGenerales.getProperty("envios_desde"));
        tGeneralesEnviosHasta.setText(propGenerales.getProperty("envios_hasta"));
         
        tVictoriaSServidor.setText(propVictoria.getProperty("servidor"));
        tVictoriaPuerto.setText(propVictoria.getProperty("puerto"));
        tVictoriaMet.setText(propVictoria.getProperty("metodoGET"));
        tVictoriaRubro.setText(propVictoria.getProperty("rubros"));
        tVictoriaMarca.setText(propVictoria.getProperty("marcas"));
        tVictoriaProductos.setText(propVictoria.getProperty("productos"));
        tVictoriaProductosDetalles.setText(propVictoria.getProperty("detalle"));
        tVictoriaHilos.setText(propVictoria.getProperty("hilos"));
   
        tImpalaServidor.setText(propImpala.getProperty("servidor"));
        tImpalaPuerto.setText(propImpala.getProperty("puerto"));
        tImpalaMetodo.setText(propImpala.getProperty("metodo"));
        tImpalaCliente.setText(propImpala.getProperty("cliente"));
        tImpalaUsuario.setText(propImpala.getProperty("usuario"));
        tImpalaClave.setText(propImpala.getProperty("clave"));
        tImpalaHilos.setText(propImpala.getProperty("hilos"));
        tImpalaOpcionesDescuento.setText(propImpala.getProperty("descuento"));
        tImpalaOpcionesRecargo.setText(propImpala.getProperty("recargo"));
        
        tImpalaRecursoProductoDetalle.setText(propImpala.getProperty("detalle"));
        tImpalaRecursoProductoMaestro.setText(propImpala.getProperty("maestro"));
        tImpalaRecursoProductoImagenes.setText(propImpala.getProperty("imagenes"));
        
        tJellyfishServidor.setText(propJellyfish.getProperty("servidor"));
        tJellyfishPuerto.setText(propJellyfish.getProperty("puerto"));
        tJellyfishMetodo.setText(propJellyfish.getProperty("metodo"));
        tJellyfishCliente.setText(propJellyfish.getProperty("cliente"));
        tJellyfishUsuario.setText(propJellyfish.getProperty("usuario"));
        tJellyfishClave.setText(propJellyfish.getProperty("clave"));
        tJellyfishHilos.setText(propJellyfish.getProperty("hilos"));
        tJellyfishOpcionesDescuento.setText(propJellyfish.getProperty("descuento"));
        tJellyfishOpcionesRecargo.setText(propJellyfish.getProperty("recargo"));
        
        tJellyfishRecursoProductoDetalle.setText(propJellyfish.getProperty("detalle"));
        tJellyfishRecursoProductoMaestro.setText(propJellyfish.getProperty("maestro"));
        tJellyfishRecursoProductoImagenes.setText(propJellyfish.getProperty("imagenes"));
         
        Object[][] contenidoDefault;
        Object[][] contenidoCargado;
        
        contenidoDefault = new Object[propiedades.getTablaHeaderPrestashopDefault().length][2];
        for (int i = 0; i < contenidoDefault.length; i++) {
            contenidoDefault[i][0] = i;
            contenidoDefault[i][1] = propiedades.getTablaHeaderPrestashopDefault()[i];            
        }
        tbCPrestashopDefault.setModel(new javax.swing.table.DefaultTableModel(contenidoDefault,new String [] {"ID", "Columna"}));
        tbCPrestashopDefault.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbCPrestashopDefault.getColumnModel().getColumn(1).setPreferredWidth(460);
        
        if(tablaHeaderPrestashop == null){
            contenidoCargado = contenidoDefault;
            tablaHeaderPrestashop = propiedades.getTablaHeaderPrestashopDefault();
        }else{
            contenidoCargado = new Object[tablaHeaderPrestashop.length][2];
            for (int i = 0; i < contenidoCargado.length; i++) {
                contenidoCargado[i][0] = java.util.Arrays.asList(propiedades.getTablaHeaderPrestashopDefault()).indexOf(tablaHeaderPrestashop[i]);
                contenidoCargado[i][1] = tablaHeaderPrestashop[i];            
            }
        }
        
        tbCPrestashopCargado.setModel(new javax.swing.table.DefaultTableModel(contenidoCargado,new String [] {"ID", "Columna"}));
        tbCPrestashopCargado.getColumnModel().getColumn(0).setPreferredWidth(40);
        tbCPrestashopCargado.getColumnModel().getColumn(1).setPreferredWidth(460);
        
    }
    
   
    public Properties getPropiedades(){
        Properties propiedades = new Properties();
        propiedades.putAll(getOrigen());
        propiedades.putAll(propGenerales);
        return propiedades;
    }
    
    public Properties getOrigen(){
       
    switch(tpPrincipal.getSelectedIndex()){
            case 0:
                return propImpala;
            case 1:
                return propJellyfish;
            case 4:
                return propVictoria;
            case 5:
                return propSC;
            default:
                return propImpala;
        }
        
        
    }
  /* LISTENERS */
    /**********************************************************************************************************/
    
    private void iniciarListeners(){
        // TABLA MAESTRO
        tbMaestroProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    limpiarProducto(false);
                    tpConsulta.setSelectedIndex(0);
                    
                    tProductoID.setText(tablaContenidoMaestro[(int) table.getValueAt(row, 0)][2].toString());
           //         System.out.println("EL VALOR DEL DETALLE ES " + tablaContenidoMaestro[(int) table.getValueAt(row, 0)][2].toString());
                    
                    buscarProducto((Producto) tablaContenidoMaestro[(int) table.getValueAt(row, 0)][0]);
           //         System.out.println("EL VALOR DEL TABLACONTENIDO ES " + tablaContenidoMaestro[(int) table.getValueAt(row, 0)][0].toString());
                }
                
            }
        });
        tbMaestroProductos.setDefaultEditor(Object.class, null);
        tbMaestroProductos.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
        //TABLA DE VICTORIA 
          tbVictoriaProductos.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    limpiarProducto(false);
                    tpVictoria.setSelectedIndex(0);
                    tProductoIDV.setText(tablaContenidoProductos[(int) table.getValueAt(row, 0)][2].toString());
                    buscarProductoVictoria((ProductoVictoria) tablaContenidoProductos[(int) table.getValueAt(row, 0)][0]);
                    
                } 
               
            }
        });
        tbVictoriaProductos.setDefaultEditor(Object.class, null);
        tbVictoriaProductos.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
       //TABLA DE SARA COMERCIAL 
          tbProductosSC.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    limpiarProducto(false);
                    tpWebsite.setSelectedIndex(1);
                    
                    tProductoIDSC.setText(tablaContenidoProductosSC[(int) table.getValueAt(row, 0)][1].toString());
                    tProductoSCRubro.setText(tablaContenidoProductosSC[(int) table.getValueAt(row, 0)][6].toString());
                    tProductoSCMarca.setText(tablaContenidoProductosSC[(int) table.getValueAt(row, 0)][5].toString());
                    buscarProductoWebsite((ProductoSC) tablaContenidoProductosSC[(int) table.getValueAt(row, 0)][0]);
                 }
               
            }
        });
        tbProductosSC.setDefaultEditor(Object.class, null);
        tbProductosSC.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

        
        // TABLA IMAGENES 
        tbProductoImagenes.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    cargarImagen(table.getValueAt(row, 1).toString());
                }
            }
        });
        tbProductoImagenes.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    }
    
    /* DEBUG */
    /**
     * @param mensaje********************************************************************************************************/
    public void appendMensaje(String mensaje){
        //System.out.println("______"+mensaje);
        taDebug.append(mensaje+"\n");
    }
    
    
    /* SISTEMA */
    /**********************************************************************************************************/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpPrincipal = new javax.swing.JTabbedPane();
        pPrestashop = new javax.swing.JPanel();
        lMaestroCantidad3 = new javax.swing.JLabel();
        tPrestashopPath = new javax.swing.JTextField();
        bPrestashopSeleccionar = new javax.swing.JButton();
        lMaestroCantidad4 = new javax.swing.JLabel();
        tPrestashopExportLineas = new javax.swing.JTextField();
        lMaestroCantidad5 = new javax.swing.JLabel();
        tPrestashopExportColumnas = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        lMaestroCantidad6 = new javax.swing.JLabel();
        pConsulta = new javax.swing.JPanel();
        tpConsulta = new javax.swing.JTabbedPane();
        pConsultaDetalle = new javax.swing.JPanel();
        lProductoCodigo = new javax.swing.JLabel();
        lProductoDescripcionLarga = new javax.swing.JLabel();
        lProductoMarca = new javax.swing.JLabel();
        lProductoCategoria = new javax.swing.JLabel();
        lProductoDivision = new javax.swing.JLabel();
        lProductoPrecio = new javax.swing.JLabel();
        lProductoExistencia = new javax.swing.JLabel();
        lProductoDetallesTecnicos = new javax.swing.JLabel();
        bProductoLimpiar = new javax.swing.JButton();
        bProductoBuscar = new javax.swing.JButton();
        tProductoID = new javax.swing.JTextField();
        sProductoSeparador1 = new javax.swing.JSeparator();
        tProductoDescripcion = new javax.swing.JTextField();
        spProductoDescripcionLarga = new javax.swing.JScrollPane();
        taProductoDescripcionLarga = new javax.swing.JTextArea();
        tProductoMarca = new javax.swing.JTextField();
        tProductoCategoria = new javax.swing.JTextField();
        tProductoDivision = new javax.swing.JTextField();
        tProductoPrecioLista = new javax.swing.JTextField();
        tProductoMoneda = new javax.swing.JTextField();
        tProductoExistencia = new javax.swing.JCheckBox();
        spProductoDetallesTecnicos = new javax.swing.JScrollPane();
        tbProductoDetallesTecnicos = new javax.swing.JTable();
        spProductoImagenes = new javax.swing.JScrollPane();
        tbProductoImagenes = new javax.swing.JTable();
        taProductoImagen = new javax.swing.JLabel();
        lProductoPrecio1 = new javax.swing.JLabel();
        tProductoPrecioVenta = new javax.swing.JTextField();
        tProductoStock = new javax.swing.JTextField();
        lProductoExistencia1 = new javax.swing.JLabel();
        lProductoPrecio2 = new javax.swing.JLabel();
        tProductoPrecioCosto = new javax.swing.JTextField();
        lProductoExistencia2 = new javax.swing.JLabel();
        tProductoFactorVenta = new javax.swing.JTextField();
        lProductoExistencia3 = new javax.swing.JLabel();
        lProductoExistencia4 = new javax.swing.JLabel();
        tProductoFactorCosto = new javax.swing.JTextField();
        tProductoEAN = new javax.swing.JTextField();
        lProductoDetallesTecnicos1 = new javax.swing.JLabel();
        tProductoPrecioVentaFinal = new javax.swing.JTextField();
        lProductoPrecio3 = new javax.swing.JLabel();
        lProductoExistencia5 = new javax.swing.JLabel();
        tProductoEnvioImporte = new javax.swing.JTextField();
        pConsultaMaestro = new javax.swing.JPanel();
        sProductoSeparador2 = new javax.swing.JSeparator();
        bMaestroLimpiar = new javax.swing.JButton();
        bMaestroBuscar = new javax.swing.JButton();
        spMaestroProductos = new javax.swing.JScrollPane();
        tbMaestroProductos = new javax.swing.JTable();
        lMaestroCantidad = new javax.swing.JLabel();
        tMaestroCantidad = new javax.swing.JTextField();
        pConsultaPrestashop = new javax.swing.JPanel();
        bPrestashopProcesar = new javax.swing.JButton();
        bPrestashopLimpiar = new javax.swing.JButton();
        sProductoSeparador3 = new javax.swing.JSeparator();
        spPrestashop = new javax.swing.JScrollPane();
        tbPrestashop = new javax.swing.JTable();
        bPrestashopAbrir = new javax.swing.JButton();
        tPrestashopFileExport = new javax.swing.JTextField();
        lMaestroCantidad2 = new javax.swing.JLabel();
        lMaestroCantidad7 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        tPrestashopWorkerEstado = new javax.swing.JLabel();
        pConfiguracion = new javax.swing.JPanel();
        tpConfiguracion = new javax.swing.JTabbedPane();
        pCGenerales = new javax.swing.JPanel();
        lBASCSServidor2 = new javax.swing.JLabel();
        lBASCSInstancia1 = new javax.swing.JLabel();
        lBASCSUsuario1 = new javax.swing.JLabel();
        tGeneralesEnviosImporte = new javax.swing.JTextField();
        lBASCSInstancia2 = new javax.swing.JLabel();
        tGeneralesEnviosDesde = new javax.swing.JTextField();
        tGeneralesEnviosSumar = new javax.swing.JCheckBox();
        lBASCSInstancia3 = new javax.swing.JLabel();
        tGeneralesEnviosHasta = new javax.swing.JTextField();
        pCBASCS = new javax.swing.JPanel();
        lBASCSServidor = new javax.swing.JLabel();
        lBASCSPuerto = new javax.swing.JLabel();
        lBASCSInstancia = new javax.swing.JLabel();
        lBASCSBD = new javax.swing.JLabel();
        tVictoriaSServidor = new javax.swing.JTextField();
        tVictoriaPuerto = new javax.swing.JTextField();
        tVictoriaMet = new javax.swing.JTextField();
        tVictoriaRubro = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jTabbedPane4 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        lImpalaUsuario6 = new javax.swing.JLabel();
        lImpalaUsuario7 = new javax.swing.JLabel();
        tVictoriaProductos = new javax.swing.JTextField();
        tVictoriaProductosDetalles = new javax.swing.JTextField();
        lBASCSPuerto1 = new javax.swing.JLabel();
        tVictoriaMarca = new javax.swing.JTextField();
        lBASCSPuerto2 = new javax.swing.JLabel();
        tVictoriaHilos = new javax.swing.JTextField();
        pCImpala = new javax.swing.JPanel();
        lImpalaServidor = new javax.swing.JLabel();
        lImpalaPuerto = new javax.swing.JLabel();
        lImpalaMetodo = new javax.swing.JLabel();
        lImpalaCliente = new javax.swing.JLabel();
        lImpalaUsuario = new javax.swing.JLabel();
        lImpalaClave = new javax.swing.JLabel();
        tImpalaServidor = new javax.swing.JTextField();
        tImpalaPuerto = new javax.swing.JTextField();
        tImpalaMetodo = new javax.swing.JTextField();
        tImpalaCliente = new javax.swing.JTextField();
        tImpalaUsuario = new javax.swing.JTextField();
        tImpalaClave = new javax.swing.JPasswordField();
        jSeparator1 = new javax.swing.JSeparator();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        lImpalaUsuario1 = new javax.swing.JLabel();
        lImpalaUsuario2 = new javax.swing.JLabel();
        lImpalaProductosImagenes = new javax.swing.JLabel();
        tImpalaRecursoProductoDetalle = new javax.swing.JTextField();
        tImpalaRecursoProductoMaestro = new javax.swing.JTextField();
        tImpalaRecursoProductoImagenes = new javax.swing.JTextField();
        lImpalaHilos = new javax.swing.JLabel();
        tImpalaHilos = new javax.swing.JTextField();
        jTabbedPane6 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        lImpalaUsuario10 = new javax.swing.JLabel();
        lImpalaUsuario11 = new javax.swing.JLabel();
        tImpalaOpcionesDescuento = new javax.swing.JTextField();
        tImpalaOpcionesRecargo = new javax.swing.JTextField();
        pCJellyfish = new javax.swing.JPanel();
        lImpalaServidor1 = new javax.swing.JLabel();
        lImpalaPuerto1 = new javax.swing.JLabel();
        lImpalaMetodo1 = new javax.swing.JLabel();
        lImpalaCliente1 = new javax.swing.JLabel();
        lImpalaUsuario3 = new javax.swing.JLabel();
        lImpalaClave1 = new javax.swing.JLabel();
        tJellyfishServidor = new javax.swing.JTextField();
        tJellyfishPuerto = new javax.swing.JTextField();
        tJellyfishMetodo = new javax.swing.JTextField();
        tJellyfishCliente = new javax.swing.JTextField();
        tJellyfishUsuario = new javax.swing.JTextField();
        tJellyfishClave = new javax.swing.JPasswordField();
        jSeparator3 = new javax.swing.JSeparator();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        lImpalaUsuario4 = new javax.swing.JLabel();
        lImpalaUsuario5 = new javax.swing.JLabel();
        lImpalaProductosImagenes1 = new javax.swing.JLabel();
        tJellyfishRecursoProductoDetalle = new javax.swing.JTextField();
        tJellyfishRecursoProductoMaestro = new javax.swing.JTextField();
        tJellyfishRecursoProductoImagenes = new javax.swing.JTextField();
        lJellyfishHilos = new javax.swing.JLabel();
        tJellyfishHilos = new javax.swing.JTextField();
        jTabbedPane5 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        lImpalaUsuario8 = new javax.swing.JLabel();
        lImpalaUsuario9 = new javax.swing.JLabel();
        tJellyfishOpcionesDescuento = new javax.swing.JTextField();
        tJellyfishOpcionesRecargo = new javax.swing.JTextField();
        pCPrestashop = new javax.swing.JPanel();
        spCPrestashopDefault = new javax.swing.JScrollPane();
        tbCPrestashopDefault = new javax.swing.JTable();
        spCPrestashopCargado = new javax.swing.JScrollPane();
        tbCPrestashopCargado = new javax.swing.JTable();
        lImpalaServidor2 = new javax.swing.JLabel();
        lImpalaServidor3 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        pDebug = new javax.swing.JPanel();
        spDebug = new javax.swing.JScrollPane();
        taDebug = new javax.swing.JTextArea();
        pVictoria = new javax.swing.JPanel();
        tpVictoria = new javax.swing.JTabbedPane();
        pVictoriaDetalle = new javax.swing.JPanel();
        lProductoCodigo1 = new javax.swing.JLabel();
        lProductoDescripcionLarga1 = new javax.swing.JLabel();
        lProductoMarca1 = new javax.swing.JLabel();
        bProductoLimpiarV = new javax.swing.JButton();
        bProductoBuscarV = new javax.swing.JButton();
        tProductoIDV = new javax.swing.JTextField();
        sProductoSeparador4 = new javax.swing.JSeparator();
        tProductoNombreV = new javax.swing.JTextField();
        spProductoDescripcionLarga1 = new javax.swing.JScrollPane();
        taProductoDescripcionV = new javax.swing.JTextArea();
        tProductoMarcaVictoriaNombre = new javax.swing.JTextField();
        spProductoDetallesTecnicos1 = new javax.swing.JScrollPane();
        tbProductoCuotas = new javax.swing.JTable();
        tProductoStock1 = new javax.swing.JTextField();
        lProductoExistencia7 = new javax.swing.JLabel();
        lProductoDetallesTecnicos3 = new javax.swing.JLabel();
        tProductoRubroVictoriaCodigo = new javax.swing.JTextField();
        lProductoMarca3 = new javax.swing.JLabel();
        tProductoRubroVictoriaNombre = new javax.swing.JTextField();
        tProductoMarcaVictoriaCodigo = new javax.swing.JTextField();
        pVictoriaMaestro = new javax.swing.JPanel();
        sProductoSeparador5 = new javax.swing.JSeparator();
        bVictoriaLimpiar = new javax.swing.JButton();
        bVictoriaBuscar = new javax.swing.JButton();
        spMaestroProductos1 = new javax.swing.JScrollPane();
        tbVictoriaProductos = new javax.swing.JTable();
        lMaestroCantidad1 = new javax.swing.JLabel();
        tVictoriaCantidad = new javax.swing.JTextField();
        Cargar = new javax.swing.JButton();
        pVictoriaMyR = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tRubrosVictoria = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        btRubrosVTCargar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tMarcasVictoria = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        btRubrosVTPost = new javax.swing.JButton();
        pVictoriaDebug = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        debugRubros = new javax.swing.JTextArea();
        debugRubro = new javax.swing.JButton();
        pVictoriaOperaciones = new javax.swing.JPanel();
        bVictoriaSincronizar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        taVictoriaSincronizar = new javax.swing.JTextArea();
        lVictoriaEstado = new javax.swing.JLabel();
        lVictoriaWorkerEstado = new javax.swing.JLabel();
        bVictoriaCuotas = new javax.swing.JButton();
        pWebsite = new javax.swing.JPanel();
        tpWebsite = new javax.swing.JTabbedPane();
        pWebsiteMaestro = new javax.swing.JPanel();
        sProductoSeparador8 = new javax.swing.JSeparator();
        bMaestroLimpiar2 = new javax.swing.JButton();
        bMaestroBuscar2 = new javax.swing.JButton();
        spMaestroProductos2 = new javax.swing.JScrollPane();
        tbProductosSC = new javax.swing.JTable();
        lMaestroCantidad8 = new javax.swing.JLabel();
        tMaestroCantidad2 = new javax.swing.JTextField();
        bBorrarWS = new javax.swing.JButton();
        pWebsiteDetalle = new javax.swing.JPanel();
        lProductoCodigo2 = new javax.swing.JLabel();
        lProductoDescripcionLarga2 = new javax.swing.JLabel();
        lProductoMarca2 = new javax.swing.JLabel();
        lProductoExistencia12 = new javax.swing.JLabel();
        lProductoDetallesTecnicos4 = new javax.swing.JLabel();
        bProductoLimpiarSC = new javax.swing.JButton();
        bProductoBuscarSC = new javax.swing.JButton();
        tProductoSC = new javax.swing.JTextField();
        sProductoSeparador7 = new javax.swing.JSeparator();
        tProductoNombre = new javax.swing.JTextField();
        spProductoDescripcionLarga2 = new javax.swing.JScrollPane();
        taProductoDescripcionSC = new javax.swing.JTextArea();
        tProductoMarcaSCCodigo = new javax.swing.JTextField();
        tProductoSCMarca = new javax.swing.JTextField();
        tStockSC = new javax.swing.JTextField();
        tPoseeDescuentoSC = new javax.swing.JCheckBox();
        spProductoDetallesTecnicos2 = new javax.swing.JScrollPane();
        tbProductoCuotasSC = new javax.swing.JTable();
        lProductoExistencia13 = new javax.swing.JLabel();
        lProductoMarca4 = new javax.swing.JLabel();
        lProductoMarca5 = new javax.swing.JLabel();
        tProductoSCRubro = new javax.swing.JTextField();
        lProductoMarca6 = new javax.swing.JLabel();
        tProductoRubroSCCodigo = new javax.swing.JTextField();
        lProductoCodigo3 = new javax.swing.JLabel();
        tProductoIDSC = new javax.swing.JTextField();
        lProductoCodigo4 = new javax.swing.JLabel();
        tVisibleSC = new javax.swing.JCheckBox();
        tHabilitadoSC = new javax.swing.JCheckBox();
        lProductoCodigo5 = new javax.swing.JLabel();
        bProductoActualizar = new javax.swing.JButton();
        lProductoMarca7 = new javax.swing.JLabel();
        lProductoMarca8 = new javax.swing.JLabel();
        tPorcentajeDescuento = new javax.swing.JTextField();
        tProductoExistencia3 = new javax.swing.JCheckBox();
        pMyRSC = new javax.swing.JPanel();
        tRubrosSC = new javax.swing.JScrollPane();
        tRubroSC = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        btRubrosSCCargar = new javax.swing.JButton();
        tMarcasSC = new javax.swing.JScrollPane();
        tMarcaSC = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        btRubrosSCPost = new javax.swing.JButton();
        btRubrosSCDelete = new javax.swing.JButton();
        tProductoEstado = new javax.swing.JTextField();
        cbOrigen = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ETL - PrestaShop - v210617");

        tpPrincipal.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tpPrincipal.setPreferredSize(new java.awt.Dimension(1000, 750));

        pPrestashop.setPreferredSize(new java.awt.Dimension(980, 730));

        lMaestroCantidad3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad3.setText("Archivo");
        lMaestroCantidad3.setToolTipText("Seleccione el archivo generado con el Export de la plataforma Prestashop de Tienda Naranja. (products_...)");
        lMaestroCantidad3.setPreferredSize(new java.awt.Dimension(80, 20));

        tPrestashopPath.setEnabled(false);
        tPrestashopPath.setPreferredSize(new java.awt.Dimension(500, 20));

        bPrestashopSeleccionar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopSeleccionar.setText("Seleccionar archivo");
        bPrestashopSeleccionar.setPreferredSize(new java.awt.Dimension(120, 20));
        bPrestashopSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopSeleccionarActionPerformed(evt);
            }
        });

        lMaestroCantidad4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad4.setText("Lineas");
        lMaestroCantidad4.setPreferredSize(new java.awt.Dimension(80, 20));

        tPrestashopExportLineas.setEnabled(false);
        tPrestashopExportLineas.setPreferredSize(new java.awt.Dimension(50, 20));

        lMaestroCantidad5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad5.setText("Columnas");
        lMaestroCantidad5.setPreferredSize(new java.awt.Dimension(80, 20));

        tPrestashopExportColumnas.setEnabled(false);
        tPrestashopExportColumnas.setPreferredSize(new java.awt.Dimension(50, 20));

        lMaestroCantidad6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lMaestroCantidad6.setText("Tienda Naranja");
        lMaestroCantidad6.setPreferredSize(new java.awt.Dimension(100, 25));

        javax.swing.GroupLayout pPrestashopLayout = new javax.swing.GroupLayout(pPrestashop);
        pPrestashop.setLayout(pPrestashopLayout);
        pPrestashopLayout.setHorizontalGroup(
            pPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pPrestashopLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tPrestashopPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bPrestashopSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pPrestashopLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tPrestashopExportLineas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lMaestroCantidad5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tPrestashopExportColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lMaestroCantidad6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pPrestashopLayout.setVerticalGroup(
            pPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pPrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lMaestroCantidad6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lMaestroCantidad3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopPath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bPrestashopSeleccionar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lMaestroCantidad4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopExportLineas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lMaestroCantidad5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopExportColumnas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(599, Short.MAX_VALUE))
        );

        tpPrincipal.addTab("Prestashop", pPrestashop);

        pConsulta.setPreferredSize(new java.awt.Dimension(980, 730));

        tpConsulta.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tpConsulta.setPreferredSize(new java.awt.Dimension(970, 720));

        pConsultaDetalle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pConsultaDetalle.setPreferredSize(new java.awt.Dimension(960, 710));

        lProductoCodigo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo.setText("Código");
        lProductoCodigo.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDescripcionLarga.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDescripcionLarga.setText("Más información");
        lProductoDescripcionLarga.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca.setText("Marca");
        lProductoMarca.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoCategoria.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCategoria.setText("Categoria");
        lProductoCategoria.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDivision.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDivision.setText("División");
        lProductoDivision.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoPrecio.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio.setText("Precio de lista");
        lProductoPrecio.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia.setText("Existencia");
        lProductoExistencia.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDetallesTecnicos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDetallesTecnicos.setText("Detalles Técnicos");
        lProductoDetallesTecnicos.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoLimpiar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoLimpiar.setText("Limpiar");
        bProductoLimpiar.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoLimpiarActionPerformed(evt);
            }
        });

        bProductoBuscar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoBuscar.setText("Buscar");
        bProductoBuscar.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoBuscarActionPerformed(evt);
            }
        });

        tProductoID.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoID.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoDescripcion.setEditable(false);
        tProductoDescripcion.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoDescripcion.setPreferredSize(new java.awt.Dimension(400, 20));
        tProductoDescripcion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoDescripcionActionPerformed(evt);
            }
        });

        taProductoDescripcionLarga.setEditable(false);
        taProductoDescripcionLarga.setColumns(20);
        taProductoDescripcionLarga.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        taProductoDescripcionLarga.setLineWrap(true);
        taProductoDescripcionLarga.setRows(5);
        spProductoDescripcionLarga.setViewportView(taProductoDescripcionLarga);

        tProductoMarca.setEditable(false);
        tProductoMarca.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarca.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoCategoria.setEditable(false);
        tProductoCategoria.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoCategoria.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoDivision.setEditable(false);
        tProductoDivision.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoDivision.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoPrecioLista.setEditable(false);
        tProductoPrecioLista.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioLista.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoMoneda.setEditable(false);
        tProductoMoneda.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMoneda.setPreferredSize(new java.awt.Dimension(40, 20));

        tProductoExistencia.setEnabled(false);
        tProductoExistencia.setPreferredSize(new java.awt.Dimension(20, 20));

        spProductoDetallesTecnicos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoDetallesTecnicos.setPreferredSize(new java.awt.Dimension(400, 150));

        tbProductoDetallesTecnicos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoDetallesTecnicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Atributo", "Valor"
            }
        ));
        spProductoDetallesTecnicos.setViewportView(tbProductoDetallesTecnicos);

        spProductoImagenes.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoImagenes.setPreferredSize(new java.awt.Dimension(400, 100));

        tbProductoImagenes.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoImagenes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "URL"
            }
        ));
        spProductoImagenes.setViewportView(tbProductoImagenes);

        taProductoImagen.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        taProductoImagen.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        taProductoImagen.setPreferredSize(new java.awt.Dimension(400, 400));

        lProductoPrecio1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio1.setText("Precio de venta");
        lProductoPrecio1.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoPrecioVenta.setEditable(false);
        tProductoPrecioVenta.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioVenta.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoStock.setEditable(false);
        tProductoStock.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoStock.setPreferredSize(new java.awt.Dimension(40, 20));

        lProductoExistencia1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia1.setText("Stock superior a");
        lProductoExistencia1.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoPrecio2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio2.setText("Precio de costo");
        lProductoPrecio2.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoPrecioCosto.setEditable(false);
        tProductoPrecioCosto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioCosto.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoExistencia2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia2.setText("Moneda");
        lProductoExistencia2.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoFactorVenta.setEditable(false);
        tProductoFactorVenta.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoFactorVenta.setPreferredSize(new java.awt.Dimension(40, 20));

        lProductoExistencia3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia3.setText("Recargo venta");
        lProductoExistencia3.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia4.setText("Descuento costo");
        lProductoExistencia4.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoFactorCosto.setEditable(false);
        tProductoFactorCosto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoFactorCosto.setPreferredSize(new java.awt.Dimension(40, 20));

        tProductoEAN.setEditable(false);
        tProductoEAN.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEAN.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoDetallesTecnicos1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDetallesTecnicos1.setText("Detalles Técnicos");
        lProductoDetallesTecnicos1.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoPrecioVentaFinal.setEditable(false);
        tProductoPrecioVentaFinal.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioVentaFinal.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoPrecio3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio3.setText("Precio final");
        lProductoPrecio3.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia5.setText("Recargo envío");
        lProductoExistencia5.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoEnvioImporte.setEditable(false);
        tProductoEnvioImporte.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEnvioImporte.setPreferredSize(new java.awt.Dimension(40, 20));

        javax.swing.GroupLayout pConsultaDetalleLayout = new javax.swing.GroupLayout(pConsultaDetalle);
        pConsultaDetalle.setLayout(pConsultaDetalleLayout);
        pConsultaDetalleLayout.setHorizontalGroup(
            pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lProductoDescripcionLarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDetallesTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDetallesTecnicos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDivision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spProductoDescripcionLarga, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tProductoDivision, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoCategoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(55, 55, 55)
                                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                                .addComponent(lProductoExistencia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaDetalleLayout.createSequentialGroup()
                                                .addComponent(lProductoExistencia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaDetalleLayout.createSequentialGroup()
                                                .addComponent(lProductoExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)))
                                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tProductoExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoMoneda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(spProductoImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spProductoDetallesTecnicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(5, 5, 5)
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taProductoImagen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                .addComponent(lProductoPrecio3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoPrecioVentaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lProductoPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lProductoPrecio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lProductoPrecio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tProductoPrecioLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tProductoPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tProductoPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoExistencia4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tProductoFactorCosto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoExistencia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tProductoFactorVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoExistencia5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tProductoEnvioImporte, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addComponent(lProductoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tProductoEAN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tProductoDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bProductoLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bProductoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sProductoSeparador1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pConsultaDetalleLayout.setVerticalGroup(
            pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bProductoLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tProductoDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoEAN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lProductoCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spProductoDescripcionLarga, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDescripcionLarga, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(lProductoDetallesTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(135, 135, 135)
                        .addComponent(lProductoDetallesTecnicos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(spProductoDetallesTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spProductoImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(taProductoImagen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lProductoPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoPrecioLista, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoFactorCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tProductoFactorVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoPrecioCosto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoPrecio2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoDivision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoDivision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoMoneda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lProductoExistencia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tProductoEnvioImporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoPrecioVenta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoPrecio1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGap(387, 387, 387)
                        .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tProductoExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(tProductoStock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(lProductoExistencia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(tProductoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pConsultaDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(lProductoCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(pConsultaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lProductoPrecio3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoPrecioVentaFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tpConsulta.addTab("Detalle", pConsultaDetalle);

        pConsultaMaestro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pConsultaMaestro.setPreferredSize(new java.awt.Dimension(960, 710));

        sProductoSeparador2.setPreferredSize(new java.awt.Dimension(900, 10));

        bMaestroLimpiar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMaestroLimpiar.setText("Limpiar");
        bMaestroLimpiar.setPreferredSize(new java.awt.Dimension(80, 20));
        bMaestroLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMaestroLimpiarActionPerformed(evt);
            }
        });

        bMaestroBuscar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMaestroBuscar.setText("Buscar");
        bMaestroBuscar.setPreferredSize(new java.awt.Dimension(80, 20));
        bMaestroBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMaestroBuscarActionPerformed(evt);
            }
        });

        spMaestroProductos.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        spMaestroProductos.setPreferredSize(new java.awt.Dimension(900, 480));

        tbMaestroProductos.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        tbMaestroProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spMaestroProductos.setViewportView(tbMaestroProductos);

        lMaestroCantidad.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad.setText("Cantidad");
        lMaestroCantidad.setPreferredSize(new java.awt.Dimension(80, 20));

        tMaestroCantidad.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tMaestroCantidad.setText("0");
        tMaestroCantidad.setEnabled(false);
        tMaestroCantidad.setPreferredSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout pConsultaMaestroLayout = new javax.swing.GroupLayout(pConsultaMaestro);
        pConsultaMaestro.setLayout(pConsultaMaestroLayout);
        pConsultaMaestroLayout.setHorizontalGroup(
            pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sProductoSeparador2, javax.swing.GroupLayout.DEFAULT_SIZE, 1261, Short.MAX_VALUE)
                    .addGroup(pConsultaMaestroLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tMaestroCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bMaestroLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMaestroBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spMaestroProductos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        pConsultaMaestroLayout.setVerticalGroup(
            pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lMaestroCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMaestroCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bMaestroLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bMaestroBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spMaestroProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpConsulta.addTab("Maestro", pConsultaMaestro);

        pConsultaPrestashop.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pConsultaPrestashop.setPreferredSize(new java.awt.Dimension(960, 710));

        bPrestashopProcesar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopProcesar.setText("Procesar");
        bPrestashopProcesar.setPreferredSize(new java.awt.Dimension(80, 20));
        bPrestashopProcesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopProcesarActionPerformed(evt);
            }
        });

        bPrestashopLimpiar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopLimpiar.setText("Limpiar");
        bPrestashopLimpiar.setPreferredSize(new java.awt.Dimension(80, 20));
        bPrestashopLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopLimpiarActionPerformed(evt);
            }
        });

        spPrestashop.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spPrestashop.setPreferredSize(new java.awt.Dimension(900, 480));

        tbPrestashop.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbPrestashop.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spPrestashop.setViewportView(tbPrestashop);

        bPrestashopAbrir.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopAbrir.setText("Abrir archivo");
        bPrestashopAbrir.setEnabled(false);
        bPrestashopAbrir.setPreferredSize(new java.awt.Dimension(100, 20));
        bPrestashopAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopAbrirActionPerformed(evt);
            }
        });

        tPrestashopFileExport.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPrestashopFileExport.setEnabled(false);
        tPrestashopFileExport.setPreferredSize(new java.awt.Dimension(300, 20));

        lMaestroCantidad2.setText("Cantidad");
        lMaestroCantidad2.setPreferredSize(new java.awt.Dimension(100, 25));

        lMaestroCantidad7.setText("Cantidad");
        lMaestroCantidad7.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel1.setText("Ruta");
        jLabel1.setPreferredSize(new java.awt.Dimension(80, 20));

        tPrestashopWorkerEstado.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPrestashopWorkerEstado.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout pConsultaPrestashopLayout = new javax.swing.GroupLayout(pConsultaPrestashop);
        pConsultaPrestashop.setLayout(pConsultaPrestashopLayout);
        pConsultaPrestashopLayout.setHorizontalGroup(
            pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spPrestashop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                        .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(tPrestashopFileExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tPrestashopWorkerEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 383, Short.MAX_VALUE)
                                .addComponent(bPrestashopAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(bPrestashopLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(bPrestashopProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sProductoSeparador3, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(5, 5, 5))))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                    .addGap(628, 628, 628)
                    .addComponent(lMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(543, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                    .addContainerGap(553, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(618, 618, 618)))
        );
        pConsultaPrestashopLayout.setVerticalGroup(
            pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopFileExport, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopWorkerEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bPrestashopAbrir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bPrestashopLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bPrestashopProcesar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spPrestashop, javax.swing.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                    .addGap(331, 331, 331)
                    .addComponent(lMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(316, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                    .addContainerGap(326, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(321, 321, 321)))
        );

        tpConsulta.addTab("Prestashop", pConsultaPrestashop);

        javax.swing.GroupLayout pConsultaLayout = new javax.swing.GroupLayout(pConsulta);
        pConsulta.setLayout(pConsultaLayout);
        pConsultaLayout.setHorizontalGroup(
            pConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pConsultaLayout.setVerticalGroup(
            pConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, 699, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpPrincipal.addTab("Consulta", pConsulta);

        pConfiguracion.setPreferredSize(new java.awt.Dimension(980, 730));

        tpConfiguracion.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        pCGenerales.setPreferredSize(new java.awt.Dimension(755, 200));

        lBASCSServidor2.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        lBASCSServidor2.setText("Envios");
        lBASCSServidor2.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSInstancia1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSInstancia1.setText("Sumar");
        lBASCSInstancia1.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSUsuario1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSUsuario1.setText("Importe");
        lBASCSUsuario1.setPreferredSize(new java.awt.Dimension(80, 20));

        tGeneralesEnviosImporte.setEditable(false);
        tGeneralesEnviosImporte.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tGeneralesEnviosImporte.setPreferredSize(new java.awt.Dimension(150, 20));
        tGeneralesEnviosImporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tGeneralesEnviosImporteActionPerformed(evt);
            }
        });

        lBASCSInstancia2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSInstancia2.setText("Desde");
        lBASCSInstancia2.setPreferredSize(new java.awt.Dimension(80, 20));

        tGeneralesEnviosDesde.setEditable(false);
        tGeneralesEnviosDesde.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tGeneralesEnviosDesde.setPreferredSize(new java.awt.Dimension(150, 20));

        tGeneralesEnviosSumar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tGeneralesEnviosSumar.setText("Agregar precio de envio en el precio final");
        tGeneralesEnviosSumar.setEnabled(false);
        tGeneralesEnviosSumar.setPreferredSize(new java.awt.Dimension(200, 20));

        lBASCSInstancia3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSInstancia3.setText("Hasta");
        lBASCSInstancia3.setPreferredSize(new java.awt.Dimension(80, 20));

        tGeneralesEnviosHasta.setEditable(false);
        tGeneralesEnviosHasta.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tGeneralesEnviosHasta.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout pCGeneralesLayout = new javax.swing.GroupLayout(pCGenerales);
        pCGenerales.setLayout(pCGeneralesLayout);
        pCGeneralesLayout.setHorizontalGroup(
            pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCGeneralesLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pCGeneralesLayout.createSequentialGroup()
                        .addComponent(lBASCSInstancia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tGeneralesEnviosHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pCGeneralesLayout.createSequentialGroup()
                        .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBASCSInstancia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSInstancia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tGeneralesEnviosImporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tGeneralesEnviosDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tGeneralesEnviosSumar, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lBASCSServidor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(924, Short.MAX_VALUE))
        );
        pCGeneralesLayout.setVerticalGroup(
            pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCGeneralesLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lBASCSServidor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lBASCSInstancia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tGeneralesEnviosSumar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tGeneralesEnviosImporte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSInstancia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tGeneralesEnviosDesde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCGeneralesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSInstancia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tGeneralesEnviosHasta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(547, Short.MAX_VALUE))
        );

        tpConfiguracion.addTab("Generales", pCGenerales);

        pCBASCS.setPreferredSize(new java.awt.Dimension(755, 200));

        lBASCSServidor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSServidor.setText("Servidor");
        lBASCSServidor.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSPuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSPuerto.setText("Puerto");
        lBASCSPuerto.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSInstancia.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSInstancia.setText("Metodo");
        lBASCSInstancia.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSBD.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSBD.setText("Rubro");
        lBASCSBD.setPreferredSize(new java.awt.Dimension(80, 20));

        tVictoriaSServidor.setEditable(false);
        tVictoriaSServidor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaSServidor.setPreferredSize(new java.awt.Dimension(150, 20));

        tVictoriaPuerto.setEditable(false);
        tVictoriaPuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaPuerto.setPreferredSize(new java.awt.Dimension(80, 20));
        tVictoriaPuerto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tVictoriaPuertoActionPerformed(evt);
            }
        });

        tVictoriaMet.setEditable(false);
        tVictoriaMet.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaMet.setPreferredSize(new java.awt.Dimension(150, 20));

        tVictoriaRubro.setEditable(false);
        tVictoriaRubro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaRubro.setPreferredSize(new java.awt.Dimension(150, 20));

        jTabbedPane4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lImpalaUsuario6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario6.setText("Producto");
        lImpalaUsuario6.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario7.setText("Detalles");
        lImpalaUsuario7.setPreferredSize(new java.awt.Dimension(80, 20));

        tVictoriaProductos.setEditable(false);
        tVictoriaProductos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaProductos.setPreferredSize(new java.awt.Dimension(400, 20));
        tVictoriaProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tVictoriaProductosActionPerformed(evt);
            }
        });

        tVictoriaProductosDetalles.setEditable(false);
        tVictoriaProductosDetalles.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaProductosDetalles.setPreferredSize(new java.awt.Dimension(400, 20));
        tVictoriaProductosDetalles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tVictoriaProductosDetallesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tVictoriaProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tVictoriaProductosDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(762, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tVictoriaProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tVictoriaProductosDetalles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane4.addTab("Productos", jPanel4);

        lBASCSPuerto1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSPuerto1.setText("Marca");
        lBASCSPuerto1.setPreferredSize(new java.awt.Dimension(80, 20));

        tVictoriaMarca.setEditable(false);
        tVictoriaMarca.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaMarca.setPreferredSize(new java.awt.Dimension(80, 20));
        tVictoriaMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tVictoriaMarcaActionPerformed(evt);
            }
        });

        lBASCSPuerto2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSPuerto2.setText("Hilos");
        lBASCSPuerto2.setPreferredSize(new java.awt.Dimension(80, 20));

        tVictoriaHilos.setEditable(false);
        tVictoriaHilos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaHilos.setPreferredSize(new java.awt.Dimension(80, 20));
        tVictoriaHilos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tVictoriaHilosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pCBASCSLayout = new javax.swing.GroupLayout(pCBASCS);
        pCBASCS.setLayout(pCBASCSLayout);
        pCBASCSLayout.setHorizontalGroup(
            pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCBASCSLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pCBASCSLayout.createSequentialGroup()
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBASCSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tVictoriaMet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tVictoriaSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tVictoriaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tVictoriaRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lBASCSPuerto2, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSPuerto1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tVictoriaHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tVictoriaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(10, 10, 10))
            .addGroup(pCBASCSLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        pCBASCSLayout.setVerticalGroup(
            pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCBASCSLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tVictoriaSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tVictoriaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lBASCSPuerto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tVictoriaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tVictoriaMet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tVictoriaRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSPuerto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tVictoriaHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(406, Short.MAX_VALUE))
        );

        tpConfiguracion.addTab("Victoria", pCBASCS);

        pCImpala.setPreferredSize(new java.awt.Dimension(755, 200));

        lImpalaServidor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaServidor.setText("Servidor");
        lImpalaServidor.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaPuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaPuerto.setText("Puerto");
        lImpalaPuerto.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaMetodo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaMetodo.setText("Metodo");
        lImpalaMetodo.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaCliente.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaCliente.setText("Cliente");
        lImpalaCliente.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario.setText("Usuario");
        lImpalaUsuario.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaClave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaClave.setText("Clave");
        lImpalaClave.setPreferredSize(new java.awt.Dimension(80, 20));

        tImpalaServidor.setEditable(false);
        tImpalaServidor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaServidor.setPreferredSize(new java.awt.Dimension(150, 20));

        tImpalaPuerto.setEditable(false);
        tImpalaPuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaPuerto.setPreferredSize(new java.awt.Dimension(50, 20));
        tImpalaPuerto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaPuertoActionPerformed(evt);
            }
        });

        tImpalaMetodo.setEditable(false);
        tImpalaMetodo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaMetodo.setPreferredSize(new java.awt.Dimension(150, 20));

        tImpalaCliente.setEditable(false);
        tImpalaCliente.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaCliente.setPreferredSize(new java.awt.Dimension(150, 20));

        tImpalaUsuario.setEditable(false);
        tImpalaUsuario.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaUsuario.setPreferredSize(new java.awt.Dimension(150, 20));
        tImpalaUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaUsuarioActionPerformed(evt);
            }
        });

        tImpalaClave.setEditable(false);
        tImpalaClave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaClave.setPreferredSize(new java.awt.Dimension(150, 20));

        jSeparator1.setPreferredSize(new java.awt.Dimension(900, 5));

        jTabbedPane2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lImpalaUsuario1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario1.setText("Detalle");
        lImpalaUsuario1.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario2.setText("Maestro");
        lImpalaUsuario2.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaProductosImagenes.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaProductosImagenes.setText("Imagenes");
        lImpalaProductosImagenes.setPreferredSize(new java.awt.Dimension(80, 20));

        tImpalaRecursoProductoDetalle.setEditable(false);
        tImpalaRecursoProductoDetalle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaRecursoProductoDetalle.setPreferredSize(new java.awt.Dimension(400, 20));
        tImpalaRecursoProductoDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaRecursoProductoDetalleActionPerformed(evt);
            }
        });

        tImpalaRecursoProductoMaestro.setEditable(false);
        tImpalaRecursoProductoMaestro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaRecursoProductoMaestro.setPreferredSize(new java.awt.Dimension(400, 20));
        tImpalaRecursoProductoMaestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaRecursoProductoMaestroActionPerformed(evt);
            }
        });

        tImpalaRecursoProductoImagenes.setEditable(false);
        tImpalaRecursoProductoImagenes.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaRecursoProductoImagenes.setPreferredSize(new java.awt.Dimension(400, 20));
        tImpalaRecursoProductoImagenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaRecursoProductoImagenesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lImpalaProductosImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tImpalaRecursoProductoImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tImpalaRecursoProductoDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tImpalaRecursoProductoMaestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(400, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaUsuario1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaRecursoProductoDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaUsuario2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaRecursoProductoMaestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaProductosImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaRecursoProductoImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Recursos", jPanel1);

        lImpalaHilos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaHilos.setText("Hilos");
        lImpalaHilos.setPreferredSize(new java.awt.Dimension(80, 20));

        tImpalaHilos.setEditable(false);
        tImpalaHilos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaHilos.setPreferredSize(new java.awt.Dimension(50, 20));
        tImpalaHilos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaHilosActionPerformed(evt);
            }
        });

        jTabbedPane6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lImpalaUsuario10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario10.setText("Descuento");
        lImpalaUsuario10.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario11.setText("Recargo");
        lImpalaUsuario11.setPreferredSize(new java.awt.Dimension(80, 20));

        tImpalaOpcionesDescuento.setEditable(false);
        tImpalaOpcionesDescuento.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaOpcionesDescuento.setPreferredSize(new java.awt.Dimension(50, 20));
        tImpalaOpcionesDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaOpcionesDescuentoActionPerformed(evt);
            }
        });

        tImpalaOpcionesRecargo.setEditable(false);
        tImpalaOpcionesRecargo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tImpalaOpcionesRecargo.setPreferredSize(new java.awt.Dimension(50, 20));
        tImpalaOpcionesRecargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tImpalaOpcionesRecargoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tImpalaOpcionesDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tImpalaOpcionesRecargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(760, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaUsuario10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaOpcionesDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaUsuario11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaOpcionesRecargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jTabbedPane6.addTab("Opciones", jPanel2);

        javax.swing.GroupLayout pCImpalaLayout = new javax.swing.GroupLayout(pCImpala);
        pCImpala.setLayout(pCImpalaLayout);
        pCImpalaLayout.setHorizontalGroup(
            pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCImpalaLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pCImpalaLayout.createSequentialGroup()
                        .addGroup(pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pCImpalaLayout.createSequentialGroup()
                                .addComponent(lImpalaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pCImpalaLayout.createSequentialGroup()
                                .addComponent(lImpalaServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pCImpalaLayout.createSequentialGroup()
                                .addComponent(lImpalaMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50)
                        .addGroup(pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pCImpalaLayout.createSequentialGroup()
                                .addComponent(lImpalaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pCImpalaLayout.createSequentialGroup()
                                .addComponent(lImpalaClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pCImpalaLayout.createSequentialGroup()
                                .addComponent(lImpalaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(190, 190, 190)
                                .addComponent(lImpalaHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tImpalaHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 900, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pCImpalaLayout.setVerticalGroup(
            pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCImpalaLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCImpalaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tImpalaClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tImpalaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jTabbedPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        tpConfiguracion.addTab("Impala", pCImpala);

        pCJellyfish.setPreferredSize(new java.awt.Dimension(755, 200));

        lImpalaServidor1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaServidor1.setText("Servidor");
        lImpalaServidor1.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaPuerto1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaPuerto1.setText("Puerto");
        lImpalaPuerto1.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaMetodo1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaMetodo1.setText("Metodo");
        lImpalaMetodo1.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaCliente1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaCliente1.setText("Cliente");
        lImpalaCliente1.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario3.setText("Usuario");
        lImpalaUsuario3.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaClave1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaClave1.setText("Clave");
        lImpalaClave1.setPreferredSize(new java.awt.Dimension(80, 20));

        tJellyfishServidor.setEditable(false);
        tJellyfishServidor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishServidor.setPreferredSize(new java.awt.Dimension(150, 20));

        tJellyfishPuerto.setEditable(false);
        tJellyfishPuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishPuerto.setPreferredSize(new java.awt.Dimension(50, 20));
        tJellyfishPuerto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishPuertoActionPerformed(evt);
            }
        });

        tJellyfishMetodo.setEditable(false);
        tJellyfishMetodo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishMetodo.setPreferredSize(new java.awt.Dimension(150, 20));

        tJellyfishCliente.setEditable(false);
        tJellyfishCliente.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishCliente.setPreferredSize(new java.awt.Dimension(150, 20));

        tJellyfishUsuario.setEditable(false);
        tJellyfishUsuario.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishUsuario.setPreferredSize(new java.awt.Dimension(150, 20));
        tJellyfishUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishUsuarioActionPerformed(evt);
            }
        });

        tJellyfishClave.setEditable(false);
        tJellyfishClave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishClave.setPreferredSize(new java.awt.Dimension(150, 20));

        jSeparator3.setPreferredSize(new java.awt.Dimension(900, 5));

        jTabbedPane3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lImpalaUsuario4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario4.setText("Detalle");
        lImpalaUsuario4.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario5.setText("Maestro");
        lImpalaUsuario5.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaProductosImagenes1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaProductosImagenes1.setText("Imagenes");
        lImpalaProductosImagenes1.setPreferredSize(new java.awt.Dimension(80, 20));

        tJellyfishRecursoProductoDetalle.setEditable(false);
        tJellyfishRecursoProductoDetalle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishRecursoProductoDetalle.setPreferredSize(new java.awt.Dimension(400, 20));
        tJellyfishRecursoProductoDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishRecursoProductoDetalleActionPerformed(evt);
            }
        });

        tJellyfishRecursoProductoMaestro.setEditable(false);
        tJellyfishRecursoProductoMaestro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishRecursoProductoMaestro.setPreferredSize(new java.awt.Dimension(400, 20));
        tJellyfishRecursoProductoMaestro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishRecursoProductoMaestroActionPerformed(evt);
            }
        });

        tJellyfishRecursoProductoImagenes.setEditable(false);
        tJellyfishRecursoProductoImagenes.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishRecursoProductoImagenes.setPreferredSize(new java.awt.Dimension(400, 20));
        tJellyfishRecursoProductoImagenes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishRecursoProductoImagenesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lImpalaProductosImagenes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tJellyfishRecursoProductoImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tJellyfishRecursoProductoDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tJellyfishRecursoProductoMaestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(759, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tJellyfishRecursoProductoDetalle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tJellyfishRecursoProductoMaestro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tJellyfishRecursoProductoImagenes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaProductosImagenes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        jTabbedPane3.addTab("Recursos", jPanel3);

        lJellyfishHilos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lJellyfishHilos.setText("Hilos");
        lJellyfishHilos.setPreferredSize(new java.awt.Dimension(80, 20));

        tJellyfishHilos.setEditable(false);
        tJellyfishHilos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishHilos.setPreferredSize(new java.awt.Dimension(50, 20));
        tJellyfishHilos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishHilosActionPerformed(evt);
            }
        });

        jTabbedPane5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        lImpalaUsuario8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario8.setText("Descuento");
        lImpalaUsuario8.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaUsuario9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaUsuario9.setText("Recargo");
        lImpalaUsuario9.setPreferredSize(new java.awt.Dimension(80, 20));

        tJellyfishOpcionesDescuento.setEditable(false);
        tJellyfishOpcionesDescuento.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishOpcionesDescuento.setPreferredSize(new java.awt.Dimension(50, 20));
        tJellyfishOpcionesDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishOpcionesDescuentoActionPerformed(evt);
            }
        });

        tJellyfishOpcionesRecargo.setEditable(false);
        tJellyfishOpcionesRecargo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tJellyfishOpcionesRecargo.setPreferredSize(new java.awt.Dimension(50, 20));
        tJellyfishOpcionesRecargo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tJellyfishOpcionesRecargoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tJellyfishOpcionesDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(lImpalaUsuario9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tJellyfishOpcionesRecargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(1109, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tJellyfishOpcionesDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tJellyfishOpcionesRecargo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaUsuario9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(73, Short.MAX_VALUE))
        );

        jTabbedPane5.addTab("Opciones", jPanel5);

        javax.swing.GroupLayout pCJellyfishLayout = new javax.swing.GroupLayout(pCJellyfish);
        pCJellyfish.setLayout(pCJellyfishLayout);
        pCJellyfishLayout.setHorizontalGroup(
            pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCJellyfishLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                        .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pCJellyfishLayout.createSequentialGroup()
                                .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                                        .addComponent(lImpalaUsuario3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                                        .addComponent(lImpalaServidor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                                        .addComponent(lImpalaMetodo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(50, 50, 50)
                                .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                                        .addComponent(lImpalaCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                                        .addComponent(lImpalaClave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pCJellyfishLayout.createSequentialGroup()
                                        .addComponent(lImpalaPuerto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(190, 190, 190)
                                        .addComponent(lJellyfishHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tJellyfishHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        pCJellyfishLayout.setVerticalGroup(
            pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCJellyfishLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaServidor1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaPuerto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lJellyfishHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishHilos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaMetodo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishMetodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCJellyfishLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lImpalaUsuario3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaClave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tJellyfishClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jTabbedPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jTabbedPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(132, 132, 132))
        );

        tpConfiguracion.addTab("JellyFish", pCJellyfish);

        spCPrestashopDefault.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tbCPrestashopDefault.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbCPrestashopDefault.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Columna"
            }
        ));
        spCPrestashopDefault.setViewportView(tbCPrestashopDefault);

        spCPrestashopCargado.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N

        tbCPrestashopCargado.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbCPrestashopCargado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID", "Columna"
            }
        ));
        spCPrestashopCargado.setViewportView(tbCPrestashopCargado);

        lImpalaServidor2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaServidor2.setText("Default");
        lImpalaServidor2.setPreferredSize(new java.awt.Dimension(80, 20));

        lImpalaServidor3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lImpalaServidor3.setText("Cargado");
        lImpalaServidor3.setPreferredSize(new java.awt.Dimension(80, 20));

        javax.swing.GroupLayout pCPrestashopLayout = new javax.swing.GroupLayout(pCPrestashop);
        pCPrestashop.setLayout(pCPrestashopLayout);
        pCPrestashopLayout.setHorizontalGroup(
            pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCPrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator5)
                    .addComponent(spCPrestashopDefault, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(lImpalaServidor2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lImpalaServidor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spCPrestashopCargado, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                    .addComponent(jSeparator6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pCPrestashopLayout.setVerticalGroup(
            pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pCPrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lImpalaServidor2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lImpalaServidor3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pCPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(spCPrestashopDefault, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                    .addComponent(spCPrestashopCargado))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tpConfiguracion.addTab("Prestashop", pCPrestashop);

        javax.swing.GroupLayout pConfiguracionLayout = new javax.swing.GroupLayout(pConfiguracion);
        pConfiguracion.setLayout(pConfiguracionLayout);
        pConfiguracionLayout.setHorizontalGroup(
            pConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConfiguracionLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpConfiguracion)
                .addGap(0, 0, 0))
        );
        pConfiguracionLayout.setVerticalGroup(
            pConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConfiguracionLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 698, Short.MAX_VALUE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tpPrincipal.addTab("Configuracion", pConfiguracion);

        pDebug.setPreferredSize(new java.awt.Dimension(980, 730));

        taDebug.setColumns(20);
        taDebug.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        taDebug.setLineWrap(true);
        taDebug.setRows(5);
        spDebug.setViewportView(taDebug);

        javax.swing.GroupLayout pDebugLayout = new javax.swing.GroupLayout(pDebug);
        pDebug.setLayout(pDebugLayout);
        pDebugLayout.setHorizontalGroup(
            pDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDebugLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(spDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 1261, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        pDebugLayout.setVerticalGroup(
            pDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pDebugLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(spDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 695, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        tpPrincipal.addTab("Debug", pDebug);

        pVictoria.setPreferredSize(new java.awt.Dimension(980, 730));

        tpVictoria.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tpVictoria.setPreferredSize(new java.awt.Dimension(970, 720));

        pVictoriaDetalle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pVictoriaDetalle.setPreferredSize(new java.awt.Dimension(960, 710));

        lProductoCodigo1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo1.setText("Código");
        lProductoCodigo1.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDescripcionLarga1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDescripcionLarga1.setText("Más información:");
        lProductoDescripcionLarga1.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca1.setText("Marca:");
        lProductoMarca1.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoLimpiarV.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoLimpiarV.setText("Limpiar");
        bProductoLimpiarV.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoLimpiarV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoLimpiarVActionPerformed(evt);
            }
        });

        bProductoBuscarV.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoBuscarV.setText("Buscar");
        bProductoBuscarV.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoBuscarV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoBuscarVActionPerformed(evt);
            }
        });

        tProductoIDV.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoIDV.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoNombreV.setEditable(false);
        tProductoNombreV.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoNombreV.setPreferredSize(new java.awt.Dimension(400, 20));
        tProductoNombreV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoNombreVActionPerformed(evt);
            }
        });

        taProductoDescripcionV.setEditable(false);
        taProductoDescripcionV.setColumns(20);
        taProductoDescripcionV.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        taProductoDescripcionV.setLineWrap(true);
        taProductoDescripcionV.setRows(5);
        spProductoDescripcionLarga1.setViewportView(taProductoDescripcionV);

        tProductoMarcaVictoriaNombre.setEditable(false);
        tProductoMarcaVictoriaNombre.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarcaVictoriaNombre.setPreferredSize(new java.awt.Dimension(150, 20));

        spProductoDetallesTecnicos1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoDetallesTecnicos1.setPreferredSize(new java.awt.Dimension(400, 150));

        tbProductoCuotas.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoCuotas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Cuotas", "Precio Contado", "Precio Cuota", "Precio Crédito"
            }
        ));
        spProductoDetallesTecnicos1.setViewportView(tbProductoCuotas);

        tProductoStock1.setEditable(false);
        tProductoStock1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoStock1.setPreferredSize(new java.awt.Dimension(40, 20));

        lProductoExistencia7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia7.setText("Stock superior a:");
        lProductoExistencia7.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDetallesTecnicos3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDetallesTecnicos3.setText("Cuotas:");
        lProductoDetallesTecnicos3.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoRubroVictoriaCodigo.setEditable(false);
        tProductoRubroVictoriaCodigo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoRubroVictoriaCodigo.setPreferredSize(new java.awt.Dimension(150, 20));

        lProductoMarca3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca3.setText("Rubro:");
        lProductoMarca3.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoRubroVictoriaNombre.setEditable(false);
        tProductoRubroVictoriaNombre.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoRubroVictoriaNombre.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoMarcaVictoriaCodigo.setEditable(false);
        tProductoMarcaVictoriaCodigo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarcaVictoriaCodigo.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout pVictoriaDetalleLayout = new javax.swing.GroupLayout(pVictoriaDetalle);
        pVictoriaDetalle.setLayout(pVictoriaDetalleLayout);
        pVictoriaDetalleLayout.setHorizontalGroup(
            pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addComponent(lProductoCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(tProductoIDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tProductoNombreV, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(118, 118, 118)
                                .addComponent(bProductoLimpiarV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(bProductoBuscarV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sProductoSeparador4)))
                    .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                        .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDetallesTecnicos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(spProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spProductoDetallesTecnicos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoMarcaVictoriaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoMarcaVictoriaNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33)
                                .addComponent(lProductoExistencia7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tProductoStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addComponent(lProductoMarca3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoRubroVictoriaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoRubroVictoriaNombre, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(268, Short.MAX_VALUE))
        );
        pVictoriaDetalleLayout.setVerticalGroup(
            pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pVictoriaDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bProductoLimpiarV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoBuscarV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tProductoIDV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tProductoNombreV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lProductoCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64)
                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                        .addComponent(spProductoDetallesTecnicos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(53, 53, 53)
                        .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoExistencia7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lProductoMarca3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoRubroVictoriaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoRubroVictoriaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoMarcaVictoriaNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoMarcaVictoriaCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lProductoDetallesTecnicos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(224, 224, 224))
        );

        tpVictoria.addTab("Detalle", pVictoriaDetalle);

        pVictoriaMaestro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pVictoriaMaestro.setPreferredSize(new java.awt.Dimension(960, 710));

        sProductoSeparador5.setPreferredSize(new java.awt.Dimension(900, 10));

        bVictoriaLimpiar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bVictoriaLimpiar.setText("Limpiar");
        bVictoriaLimpiar.setPreferredSize(new java.awt.Dimension(80, 20));
        bVictoriaLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVictoriaLimpiarActionPerformed(evt);
            }
        });

        bVictoriaBuscar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bVictoriaBuscar.setText("Buscar");
        bVictoriaBuscar.setPreferredSize(new java.awt.Dimension(80, 20));
        bVictoriaBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVictoriaBuscarActionPerformed(evt);
            }
        });

        spMaestroProductos1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        spMaestroProductos1.setPreferredSize(new java.awt.Dimension(900, 480));

        tbVictoriaProductos.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        tbVictoriaProductos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spMaestroProductos1.setViewportView(tbVictoriaProductos);

        lMaestroCantidad1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad1.setText("Cantidad");
        lMaestroCantidad1.setPreferredSize(new java.awt.Dimension(80, 20));

        tVictoriaCantidad.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tVictoriaCantidad.setText("0");
        tVictoriaCantidad.setEnabled(false);
        tVictoriaCantidad.setPreferredSize(new java.awt.Dimension(100, 20));

        Cargar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        Cargar.setText("Cargar");
        Cargar.setPreferredSize(new java.awt.Dimension(80, 20));
        Cargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pVictoriaMaestroLayout = new javax.swing.GroupLayout(pVictoriaMaestro);
        pVictoriaMaestro.setLayout(pVictoriaMaestroLayout);
        pVictoriaMaestroLayout.setHorizontalGroup(
            pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pVictoriaMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sProductoSeparador5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pVictoriaMaestroLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tVictoriaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bVictoriaLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(Cargar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(bVictoriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spMaestroProductos1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1261, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        pVictoriaMaestroLayout.setVerticalGroup(
            pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaMaestroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tVictoriaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bVictoriaLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bVictoriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(Cargar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sProductoSeparador5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spMaestroProductos1, javax.swing.GroupLayout.DEFAULT_SIZE, 630, Short.MAX_VALUE))
        );

        tpVictoria.addTab("Productos", pVictoriaMaestro);

        tRubrosVictoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ));
        jScrollPane1.setViewportView(tRubrosVictoria);

        jLabel4.setText("MARCAS");

        btRubrosVTCargar.setText("CARGAR");
        btRubrosVTCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRubrosVTCargarActionPerformed(evt);
            }
        });

        tMarcasVictoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ));
        jScrollPane2.setViewportView(tMarcasVictoria);

        jLabel5.setText("RUBROS");

        btRubrosVTPost.setText("POST TEST R");
        btRubrosVTPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRubrosVTPostActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pVictoriaMyRLayout = new javax.swing.GroupLayout(pVictoriaMyR);
        pVictoriaMyR.setLayout(pVictoriaMyRLayout);
        pVictoriaMyRLayout.setHorizontalGroup(
            pVictoriaMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addGroup(pVictoriaMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btRubrosVTPost, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                    .addComponent(btRubrosVTCargar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
            .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(232, 232, 232))
            .addGroup(pVictoriaMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(868, Short.MAX_VALUE)))
        );
        pVictoriaMyRLayout.setVerticalGroup(
            pVictoriaMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                .addGroup(pVictoriaMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167)
                        .addComponent(btRubrosVTCargar)
                        .addGap(36, 36, 36)
                        .addComponent(btRubrosVTPost))
                    .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(228, Short.MAX_VALUE))
            .addGroup(pVictoriaMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pVictoriaMyRLayout.createSequentialGroup()
                    .addGap(97, 97, 97)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        tpVictoria.addTab("Marcas/Rubros", pVictoriaMyR);

        debugRubros.setColumns(20);
        debugRubros.setRows(5);
        jScrollPane3.setViewportView(debugRubros);

        debugRubro.setText("DEBUG RUBROS");
        debugRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugRubroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pVictoriaDebugLayout = new javax.swing.GroupLayout(pVictoriaDebug);
        pVictoriaDebug.setLayout(pVictoriaDebugLayout);
        pVictoriaDebugLayout.setHorizontalGroup(
            pVictoriaDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pVictoriaDebugLayout.createSequentialGroup()
                .addContainerGap(766, Short.MAX_VALUE)
                .addComponent(debugRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(364, 364, 364))
            .addGroup(pVictoriaDebugLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pVictoriaDebugLayout.setVerticalGroup(
            pVictoriaDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaDebugLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(debugRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tpVictoria.addTab("Debug", pVictoriaDebug);

        bVictoriaSincronizar.setText("Sincronizar");
        bVictoriaSincronizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVictoriaSincronizarActionPerformed(evt);
            }
        });

        taVictoriaSincronizar.setColumns(20);
        taVictoriaSincronizar.setRows(5);
        jScrollPane4.setViewportView(taVictoriaSincronizar);

        bVictoriaCuotas.setText("Insertar Cuotas");
        bVictoriaCuotas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bVictoriaCuotasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pVictoriaOperacionesLayout = new javax.swing.GroupLayout(pVictoriaOperaciones);
        pVictoriaOperaciones.setLayout(pVictoriaOperacionesLayout);
        pVictoriaOperacionesLayout.setHorizontalGroup(
            pVictoriaOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaOperacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pVictoriaOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4)
                    .addGroup(pVictoriaOperacionesLayout.createSequentialGroup()
                        .addComponent(bVictoriaSincronizar, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bVictoriaCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lVictoriaWorkerEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 834, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(lVictoriaEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        pVictoriaOperacionesLayout.setVerticalGroup(
            pVictoriaOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaOperacionesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pVictoriaOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pVictoriaOperacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bVictoriaSincronizar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bVictoriaCuotas))
                    .addComponent(lVictoriaWorkerEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lVictoriaEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 470, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(162, Short.MAX_VALUE))
        );

        tpVictoria.addTab("Operaciones", pVictoriaOperaciones);

        javax.swing.GroupLayout pVictoriaLayout = new javax.swing.GroupLayout(pVictoria);
        pVictoria.setLayout(pVictoriaLayout);
        pVictoriaLayout.setHorizontalGroup(
            pVictoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpVictoria, javax.swing.GroupLayout.DEFAULT_SIZE, 1271, Short.MAX_VALUE)
        );
        pVictoriaLayout.setVerticalGroup(
            pVictoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpVictoria, javax.swing.GroupLayout.DEFAULT_SIZE, 705, Short.MAX_VALUE)
        );

        tpPrincipal.addTab("Victoria", pVictoria);

        pWebsite.setPreferredSize(new java.awt.Dimension(980, 730));

        tpWebsite.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tpWebsite.setPreferredSize(new java.awt.Dimension(970, 720));

        pWebsiteMaestro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pWebsiteMaestro.setPreferredSize(new java.awt.Dimension(960, 710));

        sProductoSeparador8.setPreferredSize(new java.awt.Dimension(900, 10));

        bMaestroLimpiar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMaestroLimpiar2.setText("Limpiar");
        bMaestroLimpiar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bMaestroLimpiar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMaestroLimpiar2ActionPerformed(evt);
            }
        });

        bMaestroBuscar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMaestroBuscar2.setText("Buscar");
        bMaestroBuscar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bMaestroBuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMaestroBuscar2ActionPerformed(evt);
            }
        });

        spMaestroProductos2.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        spMaestroProductos2.setPreferredSize(new java.awt.Dimension(900, 480));

        tbProductosSC.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        tbProductosSC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spMaestroProductos2.setViewportView(tbProductosSC);

        lMaestroCantidad8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad8.setText("Cantidad");
        lMaestroCantidad8.setPreferredSize(new java.awt.Dimension(80, 20));

        tMaestroCantidad2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tMaestroCantidad2.setText("0");
        tMaestroCantidad2.setEnabled(false);
        tMaestroCantidad2.setPreferredSize(new java.awt.Dimension(100, 20));

        bBorrarWS.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bBorrarWS.setText("Borrar");
        bBorrarWS.setPreferredSize(new java.awt.Dimension(80, 20));
        bBorrarWS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBorrarWSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pWebsiteMaestroLayout = new javax.swing.GroupLayout(pWebsiteMaestro);
        pWebsiteMaestro.setLayout(pWebsiteMaestroLayout);
        pWebsiteMaestroLayout.setHorizontalGroup(
            pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sProductoSeparador8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pWebsiteMaestroLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 806, Short.MAX_VALUE)
                        .addComponent(bBorrarWS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bMaestroLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMaestroBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spMaestroProductos2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        pWebsiteMaestroLayout.setVerticalGroup(
            pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lMaestroCantidad8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bMaestroLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bMaestroBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bBorrarWS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spMaestroProductos2, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpWebsite.addTab("Productos", pWebsiteMaestro);

        pWebsiteDetalle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pWebsiteDetalle.setPreferredSize(new java.awt.Dimension(960, 710));

        lProductoCodigo2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo2.setText("Código");
        lProductoCodigo2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDescripcionLarga2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDescripcionLarga2.setText("Descripción:");
        lProductoDescripcionLarga2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca2.setText("Marca:");
        lProductoMarca2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia12.setText("Existencia:");
        lProductoExistencia12.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDetallesTecnicos4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDetallesTecnicos4.setText("Cuotas: ");
        lProductoDetallesTecnicos4.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoLimpiarSC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoLimpiarSC.setText("Limpiar");
        bProductoLimpiarSC.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoLimpiarSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoLimpiarSCActionPerformed(evt);
            }
        });

        bProductoBuscarSC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoBuscarSC.setText("Buscar");
        bProductoBuscarSC.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoBuscarSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoBuscarSCActionPerformed(evt);
            }
        });

        tProductoSC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoSC.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoNombre.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoNombre.setPreferredSize(new java.awt.Dimension(400, 20));
        tProductoNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoNombreActionPerformed(evt);
            }
        });

        taProductoDescripcionSC.setEditable(false);
        taProductoDescripcionSC.setColumns(20);
        taProductoDescripcionSC.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        taProductoDescripcionSC.setLineWrap(true);
        taProductoDescripcionSC.setRows(5);
        spProductoDescripcionLarga2.setViewportView(taProductoDescripcionSC);

        tProductoMarcaSCCodigo.setEditable(false);
        tProductoMarcaSCCodigo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarcaSCCodigo.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoSCMarca.setEditable(false);
        tProductoSCMarca.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoSCMarca.setPreferredSize(new java.awt.Dimension(150, 20));

        tStockSC.setEditable(false);
        tStockSC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tStockSC.setPreferredSize(new java.awt.Dimension(40, 20));

        tPoseeDescuentoSC.setEnabled(false);
        tPoseeDescuentoSC.setPreferredSize(new java.awt.Dimension(20, 20));

        spProductoDetallesTecnicos2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoDetallesTecnicos2.setPreferredSize(new java.awt.Dimension(400, 150));

        tbProductoCuotasSC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoCuotasSC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Numero", "Precio Crédito", "Precio Contado", "Precio Cuota"
            }
        ));
        spProductoDetallesTecnicos2.setViewportView(tbProductoCuotasSC);

        lProductoExistencia13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia13.setText("Stock:");
        lProductoExistencia13.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca4.setText("Código:");
        lProductoMarca4.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca5.setText("Rubro:");
        lProductoMarca5.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoSCRubro.setEditable(false);
        tProductoSCRubro.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoSCRubro.setPreferredSize(new java.awt.Dimension(150, 20));

        lProductoMarca6.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca6.setText("Código:");
        lProductoMarca6.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoRubroSCCodigo.setEditable(false);
        tProductoRubroSCCodigo.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoRubroSCCodigo.setPreferredSize(new java.awt.Dimension(150, 20));

        lProductoCodigo3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo3.setText("ID:");
        lProductoCodigo3.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoIDSC.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoIDSC.setPreferredSize(new java.awt.Dimension(100, 20));
        tProductoIDSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoIDSCActionPerformed(evt);
            }
        });

        lProductoCodigo4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo4.setText("Visible:");
        lProductoCodigo4.setPreferredSize(new java.awt.Dimension(80, 20));

        tVisibleSC.setPreferredSize(new java.awt.Dimension(20, 20));

        tHabilitadoSC.setPreferredSize(new java.awt.Dimension(20, 20));

        lProductoCodigo5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo5.setText("Habilitado");
        lProductoCodigo5.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoActualizar.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoActualizar.setText("Actualizar");
        bProductoActualizar.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoActualizarActionPerformed(evt);
            }
        });

        lProductoMarca7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca7.setText("Porcentaje Descuento:");
        lProductoMarca7.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca8.setText("Posee Descuento:");
        lProductoMarca8.setPreferredSize(new java.awt.Dimension(80, 20));

        tPorcentajeDescuento.setEditable(false);
        tPorcentajeDescuento.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPorcentajeDescuento.setPreferredSize(new java.awt.Dimension(40, 20));

        tProductoExistencia3.setEnabled(false);
        tProductoExistencia3.setPreferredSize(new java.awt.Dimension(20, 20));

        javax.swing.GroupLayout pWebsiteDetalleLayout = new javax.swing.GroupLayout(pWebsiteDetalle);
        pWebsiteDetalle.setLayout(pWebsiteDetalleLayout);
        pWebsiteDetalleLayout.setHorizontalGroup(
            pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addComponent(lProductoCodigo3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tProductoIDSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lProductoCodigo2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tProductoSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tProductoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(lProductoCodigo4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tVisibleSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(lProductoCodigo5, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tHabilitadoSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(70, 70, 70)
                        .addComponent(bProductoActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bProductoLimpiarSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bProductoBuscarSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sProductoSeparador7, javax.swing.GroupLayout.PREFERRED_SIZE, 1248, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addComponent(lProductoDetallesTecnicos4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(spProductoDetallesTecnicos2, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addComponent(lProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, 586, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lProductoMarca5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                    .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lProductoMarca6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                            .addComponent(lProductoMarca7, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(tPorcentajeDescuento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(112, 112, 112)
                                    .addComponent(lProductoExistencia13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                            .addGap(154, 154, 154)
                                            .addComponent(lProductoMarca4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(tStockSC, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                .addComponent(lProductoMarca2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tProductoSCMarca, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tProductoMarcaSCCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lProductoExistencia12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lProductoMarca8, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tPoseeDescuentoSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tProductoExistencia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tProductoSCRubro, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoRubroSCCodigo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pWebsiteDetalleLayout.setVerticalGroup(
            pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bProductoLimpiarSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoBuscarSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tProductoNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tProductoSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lProductoCodigo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lProductoCodigo3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tProductoIDSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lProductoCodigo4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tVisibleSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tHabilitadoSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lProductoCodigo5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sProductoSeparador7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lProductoDetallesTecnicos4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(spProductoDetallesTecnicos2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteDetalleLayout.createSequentialGroup()
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tProductoSCRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lProductoMarca4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoRubroSCCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(66, 66, 66))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteDetalleLayout.createSequentialGroup()
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoMarca2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoSCMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lProductoExistencia12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tProductoExistencia3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tProductoMarcaSCCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lProductoMarca6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lProductoMarca8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tPoseeDescuentoSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoMarca7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoExistencia13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tStockSC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(28, 28, 28))))
        );

        tpWebsite.addTab("Detalle", pWebsiteDetalle);

        tRubroSC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ));
        tRubrosSC.setViewportView(tRubroSC);

        jLabel6.setText("MARCAS");

        btRubrosSCCargar.setText("CARGAR");
        btRubrosSCCargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRubrosSCCargarActionPerformed(evt);
            }
        });

        tMarcaSC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ));
        tMarcasSC.setViewportView(tMarcaSC);

        jLabel7.setText("RUBROS");

        btRubrosSCPost.setText("POST TEST M");
        btRubrosSCPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRubrosSCPostActionPerformed(evt);
            }
        });

        btRubrosSCDelete.setText("DELETE");
        btRubrosSCDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btRubrosSCDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pMyRSCLayout = new javax.swing.GroupLayout(pMyRSC);
        pMyRSC.setLayout(pMyRSCLayout);
        pMyRSCLayout.setHorizontalGroup(
            pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMyRSCLayout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(234, 234, 234))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pMyRSCLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(tMarcasSC, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(btRubrosSCPost, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                        .addComponent(btRubrosSCDelete, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btRubrosSCCargar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(81, 81, 81)
                .addComponent(tRubrosSC, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(140, 140, 140))
        );
        pMyRSCLayout.setVerticalGroup(
            pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMyRSCLayout.createSequentialGroup()
                .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pMyRSCLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pMyRSCLayout.createSequentialGroup()
                                .addGap(116, 116, 116)
                                .addComponent(btRubrosSCCargar)
                                .addGap(31, 31, 31)
                                .addComponent(btRubrosSCPost)
                                .addGap(31, 31, 31)
                                .addComponent(btRubrosSCDelete))
                            .addGroup(pMyRSCLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tRubrosSC, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tMarcasSC, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(pMyRSCLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(196, Short.MAX_VALUE))
        );

        tpWebsite.addTab("Marcas/Rubros", pMyRSC);

        tProductoEstado.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEstado.setEnabled(false);
        tProductoEstado.setPreferredSize(new java.awt.Dimension(600, 20));
        tProductoEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoEstadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pWebsiteLayout = new javax.swing.GroupLayout(pWebsite);
        pWebsite.setLayout(pWebsiteLayout);
        pWebsiteLayout.setHorizontalGroup(
            pWebsiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteLayout.createSequentialGroup()
                .addGroup(pWebsiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pWebsiteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tpWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, 1259, Short.MAX_VALUE))
                    .addGroup(pWebsiteLayout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addComponent(tProductoEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pWebsiteLayout.setVerticalGroup(
            pWebsiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteLayout.createSequentialGroup()
                .addComponent(tpWebsite, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addComponent(tProductoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        tpPrincipal.addTab("Website", pWebsite);

        cbOrigen.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Impala", "Jellyfish", "Victoria", "Sara Comercial", " " }));
        cbOrigen.setPreferredSize(new java.awt.Dimension(60, 20));
        cbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOrigenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1271, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(cbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tpPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 732, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cbOrigen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tProductoEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoEstadoActionPerformed

    private void cbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrigenActionPerformed

    }//GEN-LAST:event_cbOrigenActionPerformed

    private void tJellyfishOpcionesRecargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishOpcionesRecargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishOpcionesRecargoActionPerformed

    private void tJellyfishOpcionesDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishOpcionesDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishOpcionesDescuentoActionPerformed

    private void tJellyfishHilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishHilosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishHilosActionPerformed

    private void tJellyfishRecursoProductoImagenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishRecursoProductoImagenesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishRecursoProductoImagenesActionPerformed

    private void tJellyfishRecursoProductoMaestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishRecursoProductoMaestroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishRecursoProductoMaestroActionPerformed

    private void tJellyfishRecursoProductoDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishRecursoProductoDetalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishRecursoProductoDetalleActionPerformed

    private void tJellyfishUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishUsuarioActionPerformed

    private void tJellyfishPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishPuertoActionPerformed

    private void tImpalaOpcionesRecargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaOpcionesRecargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaOpcionesRecargoActionPerformed

    private void tImpalaOpcionesDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaOpcionesDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaOpcionesDescuentoActionPerformed

    private void tImpalaHilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaHilosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaHilosActionPerformed

    private void tImpalaRecursoProductoImagenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaRecursoProductoImagenesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaRecursoProductoImagenesActionPerformed

    private void tImpalaRecursoProductoMaestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaRecursoProductoMaestroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaRecursoProductoMaestroActionPerformed

    private void tImpalaRecursoProductoDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaRecursoProductoDetalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaRecursoProductoDetalleActionPerformed

    private void tImpalaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaUsuarioActionPerformed

    private void tImpalaPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaPuertoActionPerformed

    private void tVictoriaPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaPuertoActionPerformed

    private void tGeneralesEnviosImporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGeneralesEnviosImporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGeneralesEnviosImporteActionPerformed

    private void bPrestashopAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopAbrirActionPerformed
        try {
            File archivo = new File("export/"+tPrestashopFileExport.getText());
            Desktop.getDesktop().open(archivo);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bPrestashopAbrirActionPerformed

    private void bPrestashopLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopLimpiarActionPerformed
        if(bPrestashopLimpiar.getText().equals("Detener")){
            if(!prestashopW.isCancelled()){
                prestashopW.cancelar(true);
            }
        }
        if(bPrestashopLimpiar.getText().equals("Limpiar")){
            limpiarPrestashop();
        }
    }//GEN-LAST:event_bPrestashopLimpiarActionPerformed

    private void bPrestashopProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopProcesarActionPerformed
        limpiarPrestashop();

        prestashopW = new PrestashopWorker(getPropiedades());
        prestashopW.estado = tPrestashopWorkerEstado;
        prestashopW.addPropertyChangeListener(this);
        prestashopW.iniciar();
        //psw.maestroW.addPropertyChangeListener(this); //just for debug
        prestashopW.execute();
    }//GEN-LAST:event_bPrestashopProcesarActionPerformed

    private void bMaestroBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroBuscarActionPerformed
buscarMaestro();
    }//GEN-LAST:event_bMaestroBuscarActionPerformed

    private void bMaestroLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroLimpiarActionPerformed
        if(bMaestroLimpiar.getText().equals("Detener")){
            if(!maestroW.isCancelled()){
                maestroW.cancel(true);
            }
        }else if(bMaestroLimpiar.getText().equals("Limpiar")){
            limpiarMaestro();
        }
    }//GEN-LAST:event_bMaestroLimpiarActionPerformed

    private void tProductoDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoDescripcionActionPerformed

    private void bProductoBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoBuscarActionPerformed
        productoBusqueda = new Producto(getPropiedades());
        productoBusqueda.setCodigo(tProductoID.getText());
        buscarProducto(productoBusqueda);
    }//GEN-LAST:event_bProductoBuscarActionPerformed

    private void bProductoLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiarActionPerformed
        if(bProductoLimpiar.getText().equals("Detener")){
            if(!detalleW.isCancelled()){
                detalleW.cancel(true);
            }
        }else if(bProductoLimpiar.getText().equals("Limpiar")){
            limpiarProducto(true);
        }
    }//GEN-LAST:event_bProductoLimpiarActionPerformed

    private void bPrestashopSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopSeleccionarActionPerformed
        String path = main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        appendMensaje("\n"+"Abriendo chooser en: "+path);
        fc = new JFileChooser(path);

        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            bPrestashopProcesar.setEnabled(true);

            fPrestashopImport = fc.getSelectedFile();
            tPrestashopPath.setText(fPrestashopImport.getAbsolutePath());
            appendMensaje("Archivo seleccionado: "+fPrestashopImport.getName());

            BufferedReader br = null;
            String line;

            Integer lineas =0;
            Integer columnas = 0;

            String[] encabezado;

            //"Product ID";Imagen;Nombre;Referencia;Categoría;"Precio (imp. excl.)";"Precio (imp. incl.)";Cantidad;Estado;Posición
            try {
                br = new BufferedReader(new FileReader(fPrestashopImport.getAbsolutePath()));
                if((line = br.readLine()) != null){
                    //leemos una linea sin hacer nada, el encabezado.
                    // aca se podria validad de entrada la cantidad de columnas
                    encabezado = line.split(";");
                    columnas = encabezado.length;
                }
                while ((line = br.readLine()) != null) {
                    lineas ++;
                    Producto producto = new Producto(getPropiedades());
                    producto.loadCSVPrestashop(line);
                    //System.out.println("");
                    //System.out.print("ID: "+producto.getId());
                    //System.out.print("\tNombre: "+producto.getDescripcion());
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                if (br != null) {
                    try {
                        br.close();
                        tPrestashopExportColumnas.setText(columnas.toString());
                        tPrestashopExportLineas.setText(lineas.toString());
                    } catch (IOException e) {
                        System.out.println("Error al cerrar archivo.");
                    }
                }
            }

        } else {
            appendMensaje("No se ha seleccionado ningun archivo.");
        }

    }//GEN-LAST:event_bPrestashopSeleccionarActionPerformed

    private void tVictoriaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaProductosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaProductosActionPerformed

    private void tVictoriaProductosDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaProductosDetallesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaProductosDetallesActionPerformed

    private void tVictoriaMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaMarcaActionPerformed

    private void tVictoriaHilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaHilosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaHilosActionPerformed

    private void debugRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugRubroActionPerformed
/*
buscarCuotas();
buscarProductoWebsite();
recorridoCuotas();
buscarProductosVictoria();
*/
//cuotasWSPOST();

    }//GEN-LAST:event_debugRubroActionPerformed

    private void btRubrosVTPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRubrosVTPostActionPerformed
        rubrosRecorrido();
    }//GEN-LAST:event_btRubrosVTPostActionPerformed

    private void btRubrosVTCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRubrosVTCargarActionPerformed
        isClicked = true;
        buscarRubrosVictoria();
        buscarMarcasVictoria();
    }//GEN-LAST:event_btRubrosVTCargarActionPerformed

    private void CargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarActionPerformed
  /*
        Integer productosOmitidos = 0;
        Boolean productoNuevo = false;
        // PUT - ACTUALIZA
        // POST - CREA
        try{
            //RECORRIDO VICTORIA
            for (ProductoVictoria podVictoria : productosW.get()) {
                productoNuevo = true;
                prendido = false;
                for (ProductoSC podSC : productosSC.get()) {
                    if(podVictoria.getCodigo().equals(podSC.getCodigo())){
                        productoNuevo = false;
                        if(!podVictoria.getDescripcion().equals(podSC.getDescripcion())){
            //                System.out.println("VT: "+podVictoria.getDescripcion()+"|"+"SC: "+podSC.getDescripcion());
                //               productosWSPUT(podSC.getId(), podVictoria);
                        }else{
                            productosOmitidos++;
                           
                        }
                    }
                    for (RubroSC rubSC : rubrosSC.get()) {
                        if(podVictoria.getRubro().equals(rubSC.getCodigo())){

                            if(rubrosSC.obtenerRubro(podVictoria.getRubro()) != null){
                                podVictoria.setRubro_id(rubrosSC.obtenerRubro(podVictoria.getRubro()).getId());
                            }else{
                                System.out.println("RUBRO ID: No se encuentra el RUBRO "+podVictoria.getRubro()+ "en el WS.");
                            }
                        }
                    }

                    for (MarcasSC marSC : marcasSC.get()) {
                        if(podVictoria.getMarca().equals(marSC.getCodigo())){
                            if(marcasSC.obtenerMarca(podVictoria.getMarca()) != null){
                                podVictoria.setMarca_id(marcasSC.obtenerMarca(podVictoria.getMarca()).getId());

                            }else{
                                System.out.println("MARCA ID: No se encuentra la MARCA "+podVictoria.getMarca()+ "en el WS.");
                            }
                        }
                    }

                }
          //  taVictoriaSincronizar.append("\nProductos a ser inser: " + podVictoria.getJSON());
                if(productoNuevo){
                    ProductosWSPOST(podVictoria);
              taVictoriaSincronizar.append("\nSe insertó: " + podVictoria.getJSON());
                }
            
            }
            taVictoriaSincronizar.append("\nSe omitieron " + productosOmitidos + " PRODUCTOS.");
            taVictoriaSincronizar.append("\nSe completo la sincronizacion de PRODUCTOS.");
        } catch (Exception e) {
            e.printStackTrace();
        }        
*/
    }//GEN-LAST:event_CargarActionPerformed

    private void bVictoriaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVictoriaBuscarActionPerformed
        //hilosWorker();
        buscarProductosWebsite();
        buscarMarcasVictoria();
        buscarRubrosVictoria();
        buscarRubrosSC();
        buscarMarcasSC();
        buscarCuotas();
        buscarProductosVictoria();
        
    }//GEN-LAST:event_bVictoriaBuscarActionPerformed

    private void bVictoriaLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVictoriaLimpiarActionPerformed
        limpiarProductosVictoria();
    }//GEN-LAST:event_bVictoriaLimpiarActionPerformed

    private void tProductoNombreVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoNombreVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoNombreVActionPerformed

    private void bProductoBuscarVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoBuscarVActionPerformed
        productoBusquedaV = new ProductoVictoria();
        productoBusquedaV.setCodigo(tProductoIDV.getText());
        buscarProductoVictoria(productoBusquedaV);        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoBuscarVActionPerformed

    private void bProductoLimpiarVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiarVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoLimpiarVActionPerformed

    private void bVictoriaSincronizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVictoriaSincronizarActionPerformed
        if(bVictoriaSincronizar.getText().equals("Sincronizar")){
        
            buscarRubrosVictoria();
            buscarMarcasVictoria();
            
            buscarRubrosSC();
            buscarMarcasSC();
            buscarProductosWebsite();
            
            
            while (!rubrosW.isDone() || !rubrosSC.isDone() || !marcasSC.isDone() || !marcasW.isDone()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
       
            sincronizarVictoria();
            
        }else{
            victoriaW.cancel(false);
        }
        
    }//GEN-LAST:event_bVictoriaSincronizarActionPerformed

    private void tProductoNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoNombreActionPerformed

    private void bProductoBuscarSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoBuscarSCActionPerformed
        productoBusquedaSC = new ProductoSC();
        productoBusquedaSC.setId(Integer.valueOf(tProductoIDSC.getText()));
        buscarProductoWebsite(productoBusquedaSC);         // TODO add your handling code here:
    }//GEN-LAST:event_bProductoBuscarSCActionPerformed

    private void bProductoLimpiarSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiarSCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoLimpiarSCActionPerformed

    private void btRubrosSCDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRubrosSCDeleteActionPerformed
        int row = tRubroSC.getSelectedRow();
        String selected = tRubroSC.getValueAt(row, 0).toString();

        if (row >= 0) {
            try{
                URL url = new URL("http://www.saracomercial.com/panel/api/loader/rubros/"+selected+"");
                //System.out.println(" url " + url.toString());
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("DELETE");
                http.setRequestProperty("Authorization", propSC.getProperty("clave"));

                switch(http.getResponseCode()){
                    case 200:
                    System.out.println("DELETE: Solicitud procesada correctamente.");
                    // FALTA DETERMINAR SI EFECTIVAMETNE FUE ELIMINADO O NO
                    break;
                    default:
                    break;
                }

                http.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            System.out.println("ELIMINAR. Error al eliminar, no se selecciono ninguna linea.");
        }

        buscarRubrosSC();

    }//GEN-LAST:event_btRubrosSCDeleteActionPerformed

    private void btRubrosSCPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRubrosSCPostActionPerformed
        marcasRecorrido();
    }//GEN-LAST:event_btRubrosSCPostActionPerformed

    private void btRubrosSCCargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btRubrosSCCargarActionPerformed
        buscarMarcasSC();
        buscarRubrosSC();
    }//GEN-LAST:event_btRubrosSCCargarActionPerformed

    private void bBorrarWSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBorrarWSActionPerformed
        int row = tbProductosSC.getSelectedRow();
        String selected = tbProductosSC.getValueAt(row, 0).toString();

        if (row >= 0) {
            try{
                URL url = new URL("http://www.saracomercial.com/panel/api/loader/productos/"+selected+"");
                //System.out.println(" url " + url.toString());
                HttpURLConnection http = (HttpURLConnection)url.openConnection();
                http.setRequestMethod("DELETE");
                http.setRequestProperty("Authorization", propSC.getProperty("clave"));

                switch(http.getResponseCode()){
                    case 200:
                    System.out.println("DELETE: Solicitud procesada correctamente.");
                    // FALTA DETERMINAR SI EFECTIVAMETNE FUE ELIMINADO O NO
                    break;
                    default:
                    break;
                }

                http.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else{
            System.out.println("ELIMINAR. Error al eliminar, no se selecciono ninguna linea.");
        }

        buscarProductosWebsite();           // TODO add your handling code here:
    }//GEN-LAST:event_bBorrarWSActionPerformed

    private void bMaestroBuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroBuscar2ActionPerformed
        buscarMarcasSC();
        buscarRubrosSC();        // TODO add your handling code here:
        buscarProductosWebsite();        // TODO add your handling code here:
    }//GEN-LAST:event_bMaestroBuscar2ActionPerformed

    private void bMaestroLimpiar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroLimpiar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bMaestroLimpiar2ActionPerformed

    private void tProductoIDSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoIDSCActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoIDSCActionPerformed

    private void bProductoActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoActualizarActionPerformed
actualizarProductoSC(productoBusquedaSC);     // TODO add your handling code here:
    }//GEN-LAST:event_bProductoActualizarActionPerformed

    private void bVictoriaCuotasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVictoriaCuotasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bVictoriaCuotasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        IntelliJTheme.install(etl.bascs.impala.main.class.getResourceAsStream("/json/Hiberbee.theme.json" ) );
        
        Font font = UIManager.getFont("TableHeader.font");
        font = font.deriveFont(10f);
        UIManager.put("TableHeader.font", font);
        /*
        try {
            
            javax.swing.UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        */

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cargar;
    private javax.swing.JButton bBorrarWS;
    private javax.swing.JButton bMaestroBuscar;
    private javax.swing.JButton bMaestroBuscar2;
    private javax.swing.JButton bMaestroLimpiar;
    private javax.swing.JButton bMaestroLimpiar2;
    private javax.swing.JButton bPrestashopAbrir;
    private javax.swing.JButton bPrestashopLimpiar;
    private javax.swing.JButton bPrestashopProcesar;
    private javax.swing.JButton bPrestashopSeleccionar;
    private javax.swing.JButton bProductoActualizar;
    private javax.swing.JButton bProductoBuscar;
    private javax.swing.JButton bProductoBuscarSC;
    private javax.swing.JButton bProductoBuscarV;
    private javax.swing.JButton bProductoLimpiar;
    private javax.swing.JButton bProductoLimpiarSC;
    private javax.swing.JButton bProductoLimpiarV;
    private javax.swing.JButton bVictoriaBuscar;
    private javax.swing.JButton bVictoriaCuotas;
    private javax.swing.JButton bVictoriaLimpiar;
    private javax.swing.JButton bVictoriaSincronizar;
    private javax.swing.JButton btRubrosSCCargar;
    private javax.swing.JButton btRubrosSCDelete;
    private javax.swing.JButton btRubrosSCPost;
    private javax.swing.JButton btRubrosVTCargar;
    private javax.swing.JButton btRubrosVTPost;
    private javax.swing.JComboBox<String> cbOrigen;
    private javax.swing.JButton debugRubro;
    private javax.swing.JTextArea debugRubros;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane4;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JLabel lBASCSBD;
    private javax.swing.JLabel lBASCSInstancia;
    private javax.swing.JLabel lBASCSInstancia1;
    private javax.swing.JLabel lBASCSInstancia2;
    private javax.swing.JLabel lBASCSInstancia3;
    private javax.swing.JLabel lBASCSPuerto;
    private javax.swing.JLabel lBASCSPuerto1;
    private javax.swing.JLabel lBASCSPuerto2;
    private javax.swing.JLabel lBASCSServidor;
    private javax.swing.JLabel lBASCSServidor2;
    private javax.swing.JLabel lBASCSUsuario1;
    private javax.swing.JLabel lImpalaClave;
    private javax.swing.JLabel lImpalaClave1;
    private javax.swing.JLabel lImpalaCliente;
    private javax.swing.JLabel lImpalaCliente1;
    private javax.swing.JLabel lImpalaHilos;
    private javax.swing.JLabel lImpalaMetodo;
    private javax.swing.JLabel lImpalaMetodo1;
    private javax.swing.JLabel lImpalaProductosImagenes;
    private javax.swing.JLabel lImpalaProductosImagenes1;
    private javax.swing.JLabel lImpalaPuerto;
    private javax.swing.JLabel lImpalaPuerto1;
    private javax.swing.JLabel lImpalaServidor;
    private javax.swing.JLabel lImpalaServidor1;
    private javax.swing.JLabel lImpalaServidor2;
    private javax.swing.JLabel lImpalaServidor3;
    private javax.swing.JLabel lImpalaUsuario;
    private javax.swing.JLabel lImpalaUsuario1;
    private javax.swing.JLabel lImpalaUsuario10;
    private javax.swing.JLabel lImpalaUsuario11;
    private javax.swing.JLabel lImpalaUsuario2;
    private javax.swing.JLabel lImpalaUsuario3;
    private javax.swing.JLabel lImpalaUsuario4;
    private javax.swing.JLabel lImpalaUsuario5;
    private javax.swing.JLabel lImpalaUsuario6;
    private javax.swing.JLabel lImpalaUsuario7;
    private javax.swing.JLabel lImpalaUsuario8;
    private javax.swing.JLabel lImpalaUsuario9;
    private javax.swing.JLabel lJellyfishHilos;
    private javax.swing.JLabel lMaestroCantidad;
    private javax.swing.JLabel lMaestroCantidad1;
    private javax.swing.JLabel lMaestroCantidad2;
    private javax.swing.JLabel lMaestroCantidad3;
    private javax.swing.JLabel lMaestroCantidad4;
    private javax.swing.JLabel lMaestroCantidad5;
    private javax.swing.JLabel lMaestroCantidad6;
    private javax.swing.JLabel lMaestroCantidad7;
    private javax.swing.JLabel lMaestroCantidad8;
    private javax.swing.JLabel lProductoCategoria;
    private javax.swing.JLabel lProductoCodigo;
    private javax.swing.JLabel lProductoCodigo1;
    private javax.swing.JLabel lProductoCodigo2;
    private javax.swing.JLabel lProductoCodigo3;
    private javax.swing.JLabel lProductoCodigo4;
    private javax.swing.JLabel lProductoCodigo5;
    private javax.swing.JLabel lProductoDescripcionLarga;
    private javax.swing.JLabel lProductoDescripcionLarga1;
    private javax.swing.JLabel lProductoDescripcionLarga2;
    private javax.swing.JLabel lProductoDetallesTecnicos;
    private javax.swing.JLabel lProductoDetallesTecnicos1;
    private javax.swing.JLabel lProductoDetallesTecnicos3;
    private javax.swing.JLabel lProductoDetallesTecnicos4;
    private javax.swing.JLabel lProductoDivision;
    private javax.swing.JLabel lProductoExistencia;
    private javax.swing.JLabel lProductoExistencia1;
    private javax.swing.JLabel lProductoExistencia12;
    private javax.swing.JLabel lProductoExistencia13;
    private javax.swing.JLabel lProductoExistencia2;
    private javax.swing.JLabel lProductoExistencia3;
    private javax.swing.JLabel lProductoExistencia4;
    private javax.swing.JLabel lProductoExistencia5;
    private javax.swing.JLabel lProductoExistencia7;
    private javax.swing.JLabel lProductoMarca;
    private javax.swing.JLabel lProductoMarca1;
    private javax.swing.JLabel lProductoMarca2;
    private javax.swing.JLabel lProductoMarca3;
    private javax.swing.JLabel lProductoMarca4;
    private javax.swing.JLabel lProductoMarca5;
    private javax.swing.JLabel lProductoMarca6;
    private javax.swing.JLabel lProductoMarca7;
    private javax.swing.JLabel lProductoMarca8;
    private javax.swing.JLabel lProductoPrecio;
    private javax.swing.JLabel lProductoPrecio1;
    private javax.swing.JLabel lProductoPrecio2;
    private javax.swing.JLabel lProductoPrecio3;
    private javax.swing.JLabel lVictoriaEstado;
    private javax.swing.JLabel lVictoriaWorkerEstado;
    private javax.swing.JPanel pCBASCS;
    private javax.swing.JPanel pCGenerales;
    private javax.swing.JPanel pCImpala;
    private javax.swing.JPanel pCJellyfish;
    private javax.swing.JPanel pCPrestashop;
    private javax.swing.JPanel pConfiguracion;
    private javax.swing.JPanel pConsulta;
    private javax.swing.JPanel pConsultaDetalle;
    private javax.swing.JPanel pConsultaMaestro;
    private javax.swing.JPanel pConsultaPrestashop;
    private javax.swing.JPanel pDebug;
    private javax.swing.JPanel pMyRSC;
    private javax.swing.JPanel pPrestashop;
    private javax.swing.JPanel pVictoria;
    private javax.swing.JPanel pVictoriaDebug;
    private javax.swing.JPanel pVictoriaDetalle;
    private javax.swing.JPanel pVictoriaMaestro;
    private javax.swing.JPanel pVictoriaMyR;
    private javax.swing.JPanel pVictoriaOperaciones;
    private javax.swing.JPanel pWebsite;
    private javax.swing.JPanel pWebsiteDetalle;
    private javax.swing.JPanel pWebsiteMaestro;
    private javax.swing.JSeparator sProductoSeparador1;
    private javax.swing.JSeparator sProductoSeparador2;
    private javax.swing.JSeparator sProductoSeparador3;
    private javax.swing.JSeparator sProductoSeparador4;
    private javax.swing.JSeparator sProductoSeparador5;
    private javax.swing.JSeparator sProductoSeparador7;
    private javax.swing.JSeparator sProductoSeparador8;
    private javax.swing.JScrollPane spCPrestashopCargado;
    private javax.swing.JScrollPane spCPrestashopDefault;
    private javax.swing.JScrollPane spDebug;
    private javax.swing.JScrollPane spMaestroProductos;
    private javax.swing.JScrollPane spMaestroProductos1;
    private javax.swing.JScrollPane spMaestroProductos2;
    private javax.swing.JScrollPane spPrestashop;
    private javax.swing.JScrollPane spProductoDescripcionLarga;
    private javax.swing.JScrollPane spProductoDescripcionLarga1;
    private javax.swing.JScrollPane spProductoDescripcionLarga2;
    private javax.swing.JScrollPane spProductoDetallesTecnicos;
    private javax.swing.JScrollPane spProductoDetallesTecnicos1;
    private javax.swing.JScrollPane spProductoDetallesTecnicos2;
    private javax.swing.JScrollPane spProductoImagenes;
    private javax.swing.JTextField tGeneralesEnviosDesde;
    private javax.swing.JTextField tGeneralesEnviosHasta;
    private javax.swing.JTextField tGeneralesEnviosImporte;
    private javax.swing.JCheckBox tGeneralesEnviosSumar;
    private javax.swing.JCheckBox tHabilitadoSC;
    private javax.swing.JPasswordField tImpalaClave;
    private javax.swing.JTextField tImpalaCliente;
    private javax.swing.JTextField tImpalaHilos;
    private javax.swing.JTextField tImpalaMetodo;
    private javax.swing.JTextField tImpalaOpcionesDescuento;
    private javax.swing.JTextField tImpalaOpcionesRecargo;
    private javax.swing.JTextField tImpalaPuerto;
    private javax.swing.JTextField tImpalaRecursoProductoDetalle;
    private javax.swing.JTextField tImpalaRecursoProductoImagenes;
    private javax.swing.JTextField tImpalaRecursoProductoMaestro;
    private javax.swing.JTextField tImpalaServidor;
    private javax.swing.JTextField tImpalaUsuario;
    private javax.swing.JPasswordField tJellyfishClave;
    private javax.swing.JTextField tJellyfishCliente;
    private javax.swing.JTextField tJellyfishHilos;
    private javax.swing.JTextField tJellyfishMetodo;
    private javax.swing.JTextField tJellyfishOpcionesDescuento;
    private javax.swing.JTextField tJellyfishOpcionesRecargo;
    private javax.swing.JTextField tJellyfishPuerto;
    private javax.swing.JTextField tJellyfishRecursoProductoDetalle;
    private javax.swing.JTextField tJellyfishRecursoProductoImagenes;
    private javax.swing.JTextField tJellyfishRecursoProductoMaestro;
    private javax.swing.JTextField tJellyfishServidor;
    private javax.swing.JTextField tJellyfishUsuario;
    private javax.swing.JTextField tMaestroCantidad;
    private javax.swing.JTextField tMaestroCantidad2;
    private javax.swing.JTable tMarcaSC;
    private javax.swing.JScrollPane tMarcasSC;
    private javax.swing.JTable tMarcasVictoria;
    private javax.swing.JTextField tPorcentajeDescuento;
    private javax.swing.JCheckBox tPoseeDescuentoSC;
    private javax.swing.JTextField tPrestashopExportColumnas;
    private javax.swing.JTextField tPrestashopExportLineas;
    private javax.swing.JTextField tPrestashopFileExport;
    private javax.swing.JTextField tPrestashopPath;
    private javax.swing.JLabel tPrestashopWorkerEstado;
    private javax.swing.JTextField tProductoCategoria;
    private javax.swing.JTextField tProductoDescripcion;
    private javax.swing.JTextField tProductoDivision;
    private javax.swing.JTextField tProductoEAN;
    private javax.swing.JTextField tProductoEnvioImporte;
    private javax.swing.JTextField tProductoEstado;
    private javax.swing.JCheckBox tProductoExistencia;
    private javax.swing.JCheckBox tProductoExistencia3;
    private javax.swing.JTextField tProductoFactorCosto;
    private javax.swing.JTextField tProductoFactorVenta;
    private javax.swing.JTextField tProductoID;
    private javax.swing.JTextField tProductoIDSC;
    private javax.swing.JTextField tProductoIDV;
    private javax.swing.JTextField tProductoMarca;
    private javax.swing.JTextField tProductoMarcaSCCodigo;
    private javax.swing.JTextField tProductoMarcaVictoriaCodigo;
    private javax.swing.JTextField tProductoMarcaVictoriaNombre;
    private javax.swing.JTextField tProductoMoneda;
    private javax.swing.JTextField tProductoNombre;
    private javax.swing.JTextField tProductoNombreV;
    private javax.swing.JTextField tProductoPrecioCosto;
    private javax.swing.JTextField tProductoPrecioLista;
    private javax.swing.JTextField tProductoPrecioVenta;
    private javax.swing.JTextField tProductoPrecioVentaFinal;
    private javax.swing.JTextField tProductoRubroSCCodigo;
    private javax.swing.JTextField tProductoRubroVictoriaCodigo;
    private javax.swing.JTextField tProductoRubroVictoriaNombre;
    private javax.swing.JTextField tProductoSC;
    private javax.swing.JTextField tProductoSCMarca;
    private javax.swing.JTextField tProductoSCRubro;
    private javax.swing.JTextField tProductoStock;
    private javax.swing.JTextField tProductoStock1;
    private javax.swing.JTable tRubroSC;
    private javax.swing.JScrollPane tRubrosSC;
    private javax.swing.JTable tRubrosVictoria;
    private javax.swing.JTextField tStockSC;
    private javax.swing.JTextField tVictoriaCantidad;
    private javax.swing.JTextField tVictoriaHilos;
    private javax.swing.JTextField tVictoriaMarca;
    private javax.swing.JTextField tVictoriaMet;
    private javax.swing.JTextField tVictoriaProductos;
    private javax.swing.JTextField tVictoriaProductosDetalles;
    private javax.swing.JTextField tVictoriaPuerto;
    private javax.swing.JTextField tVictoriaRubro;
    private javax.swing.JTextField tVictoriaSServidor;
    private javax.swing.JCheckBox tVisibleSC;
    private javax.swing.JTextArea taDebug;
    private javax.swing.JTextArea taProductoDescripcionLarga;
    private javax.swing.JTextArea taProductoDescripcionSC;
    private javax.swing.JTextArea taProductoDescripcionV;
    private javax.swing.JLabel taProductoImagen;
    private javax.swing.JTextArea taVictoriaSincronizar;
    private javax.swing.JTable tbCPrestashopCargado;
    private javax.swing.JTable tbCPrestashopDefault;
    private javax.swing.JTable tbMaestroProductos;
    private javax.swing.JTable tbPrestashop;
    private javax.swing.JTable tbProductoCuotas;
    private javax.swing.JTable tbProductoCuotasSC;
    private javax.swing.JTable tbProductoDetallesTecnicos;
    private javax.swing.JTable tbProductoImagenes;
    private javax.swing.JTable tbProductosSC;
    private javax.swing.JTable tbVictoriaProductos;
    private javax.swing.JTabbedPane tpConfiguracion;
    private javax.swing.JTabbedPane tpConsulta;
    private javax.swing.JTabbedPane tpPrincipal;
    private javax.swing.JTabbedPane tpVictoria;
    private javax.swing.JTabbedPane tpWebsite;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".")+1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".")+1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);
        
       System.out.println(clase+">> "+source+" > "+value);
        
        if("PrestashopWorker".equals(source)){
            if(value.equals("STARTED")){
                tPrestashopFileExport.setText("");
                bPrestashopLimpiar.setText("Detener");
                bPrestashopProcesar.setEnabled(false);
                bPrestashopAbrir.setEnabled(false);
                appendMensaje("Se inicio el proceso de Prestashop.");
            }else if(value.equals("DONE")){
                bPrestashopProcesar.setEnabled(true);
                bPrestashopLimpiar.setText("Limpiar");
                bPrestashopLimpiar.setEnabled(true);
                                
                try {
                    if(prestashopW.isDone()){
                        String ahora = new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
                        String archivo = "prestashop_" + cbOrigen.getSelectedItem().toString() + "_" + ahora + ".csv";
                        
                        if(prestashopW.isCancelled()){
                            appendMensaje("Se cancelo el proceso de Prestashop.");
                            bPrestashopLimpiar.setEnabled(true);
                            bPrestashopLimpiar.setText("Limpiar");
                        }else{
                            tPrestashopFileExport.setText(archivo);
                            generarArchivoPrestashop(tablaHeaderPrestashop, prestashopW.get(), archivo);
                            bPrestashopAbrir.setEnabled(true);
                            if(prestashopW.getHilosConError()>0){
                                tProductoEstado.setText("Listo pero con errores.");
                                
                                appendMensaje("Se finalizo el proceso de Prestashop con "+prestashopW.getHilosConError()+" errores. ("+prestashopW.getCodigosConError().substring(0, prestashopW.getCodigosConError().length()-2)+")");
                                
                                appendMensaje("Considere disminuir la cantidad de hilos en simultaneo.");
                            }else{
                                tProductoEstado.setText("Listo.");
                                appendMensaje("Se finalizo el proceso de Prestashop");
                            }
                        }
                    }else{
                        appendMensaje("Algun error no previsto");
                    }
                } catch (InterruptedException | ExecutionException ex) {
                    appendMensaje("Proceso finalizado con errores.");
                    Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                tProductoEstado.setText("Cargando prestashop "+value+"%");
            }
        }else if("MaestroWorker".equals(source)){
            if(value.equals("STARTED")){
                bMaestroBuscar.setEnabled(false);
                bMaestroLimpiar.setText("Detener");
                tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                bMaestroBuscar.setEnabled(true);
                bMaestroLimpiar.setText("Limpiar");
                tProductoEstado.setText("Cargando maestro..."); 
                
                appendMensaje("\nCONSULTA: "+maestroW.consulta.getCon().getURL()+"?"+maestroW.consulta.getParametros());
                try {
                    if(maestroW.isDone()){
                        if(maestroW.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            tMaestroCantidad.setText(maestroW.getCantidad().toString());
                            tablaContenidoMaestro = new Object[maestroW.getCantidad()][tablaHeaderMaestro.length];

                            int i = 0;
                            for (Producto producto : maestroW.get()) {
                                tablaContenidoMaestro[i][0] = producto; //Se utiliza para pasar despues a la consulta.
                                tablaContenidoMaestro[i][1] = i; //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoMaestro[i][2] = producto.getCodigo();
                                tablaContenidoMaestro[i][3] = producto.getCodigoCorto();
                                tablaContenidoMaestro[i][4] = producto.getCodigoEAN();
                                tablaContenidoMaestro[i][5] = producto.getStock();
                                tablaContenidoMaestro[i][6] = producto.getDescripcion();
                                tablaContenidoMaestro[i][7] = producto.getMarca();
                                tablaContenidoMaestro[i][8] = producto.getCategoria();
                                tablaContenidoMaestro[i][9] = producto.getDivision();
                                tablaContenidoMaestro[i][10] = formatInt.format(producto.getPrecioLista());
                                i++;
                            }
                            cargarTablaMaestro(tablaContenidoMaestro);
                            generarArchivoContenido(tablaHeaderMaestro, tablaContenidoMaestro, "maestro.csv");
                            tProductoEstado.setText(maestroW.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ maestroW.consulta.getDebugMessage()+ " | "+ maestroW.consulta.getJson().getJSONArray("data").getJSONObject(1)); 
                            appendMensaje("Se obtuvieron "+maestroW.getCantidad()+" registros.");
                        }
                    }else{
                        System.out.println("Proceso no terminado: "+maestroW.consulta.getDebugMessage());
                    }
                } catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+maestroW.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
            }else{
                tProductoEstado.setText("Cargando maestro "+value+"%");
            }
        }else if("DetalleWorker".equals(source)){
            
            if(value.equals("STARTED")){
                bProductoBuscar.setEnabled(false);
                bProductoLimpiar.setText("Detener");
                tProductoEstado.setText("Buscando producto...");
            }else if(value.equals("DONE")){
                bProductoBuscar.setEnabled(true);
                bProductoLimpiar.setText("Limpiar");
                tProductoEstado.setText("Cargando producto...");
                
                appendMensaje("\nCONSULTA: "+detalleW.consulta.getCon().getURL()+"?"+detalleW.consulta.getParametros());
                try {
                    if(detalleW.isDone()){
                        if(detalleW.isCancelled()){
                            tProductoEstado.setText("Busqueda cancelada.");
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            productoBusqueda = detalleW.get();
                            cargarProducto(productoBusqueda); 
                            tProductoEstado.setText(detalleW.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+detalleW.consulta.getDebugMessage()+" | "+detalleW.consulta.getJson().toString());
                        }
                    }else{
                        System.out.println("Error desconocido: "+detalleW.consulta.getDebugMessage());
                    }
                } catch (InterruptedException | ExecutionException ex){
                    System.out.println("Error desconocido: "+detalleW.consulta.getDebugMessage());
                }
            }else{
                tProductoEstado.setText("Cargando producto "+value+"%");
            }
        }else if("VictoriaWorker".equals(source)){
            if(value.equals("STARTED")){
                taVictoriaSincronizar.setText("");
                bVictoriaSincronizar.setText("Detener");
                taVictoriaSincronizar.append("\nSe inicio el proceso de VICTORIA WORKER.");
            }else if(value.equals("DONE")){
                bVictoriaSincronizar.setText("Sincronizar");
                                
                
                if(victoriaW.isDone()){

                    if(victoriaW.isCancelled()){
                        taVictoriaSincronizar.append("\nSe cancelo el proceso de VICTORIA WORKER.");
                        lVictoriaEstado.setText("Cancelado");

                    }else{
                        //productosFinalizados = new ProductoVictoria[victoriaW.productosFinalizados.length];
                        
                        try {
                            productosFinalizados = victoriaW.get();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        if(productosFinalizados != null){
                            
                           
                            if(victoriaW.getHilosConError()>0){
                                lVictoriaEstado.setText("Finalizado con errores.");

                                taVictoriaSincronizar.append("\nSe finalizo el proceso de VICTORIA WORKER con "+victoriaW.getHilosConError()+" errores. ("+victoriaW.getCodigosConError().substring(0, victoriaW.getCodigosConError().length()-2)+")");
                                taVictoriaSincronizar.append("\nConsidere disminuir la cantidad de hilos en simultaneo.");
                            }else{
                                lVictoriaEstado.setText("Listo.");
                                taVictoriaSincronizar.append("\nSe finalizo el proceso de VICTORIA WORKER.");
                            

                            
                            // CONSULTAR WEB SERVICE ETC.
                             
                            }
                            productosRecorrido();
                            rubrosRecorrido();
                            marcasRecorrido();
                          
                        
                        }else{
                            taVictoriaSincronizar.append("\nNo se pudo obtener los productos finalizados VICTORIA WORKER.");
                            lVictoriaEstado.setText("Error");
                        }
                        
                    }
                }else{
                    taVictoriaSincronizar.append("\nAlgun error no previsto");
                }
                
            }else{
                lVictoriaEstado.setText("Cargando "+value+"%");
            }
        }else if("ProductosVictoriaWorker".equals(source)){
            if(value.equals("STARTED")){
                bVictoriaBuscar.setEnabled(false);
                bVictoriaLimpiar.setText("Detener");
                tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                bVictoriaBuscar.setEnabled(true);
                bVictoriaLimpiar.setText("Limpiar");
                tProductoEstado.setText("Cargando maestro...");
                
                appendMensaje("\nCONSULTA: "+productosW.consulta.getCon().getURL());
                try {
                    if(productosW.isDone()){
                        if(productosW.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            tVictoriaCantidad.setText(productosW.getCantidad().toString());
                            tablaContenidoProductos = new Object[productosW.getCantidad()][tablaHeaderProductos.length];

                            int i = 0;
                            for (ProductoVictoria producto : productosW.get()) {
                                tablaContenidoProductos[i][0] = producto; //Se utiliza para pasar despues a la consulta.
                                tablaContenidoProductos[i][1] = i; //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductos[i][2] = producto.getCodigo();
                                tablaContenidoProductos[i][3] = producto.getNombre();
                                tablaContenidoProductos[i][4] = producto.getDescripcion();
                                tablaContenidoProductos[i][5] = producto.getMarcaVictoria().getCodigo();
                                tablaContenidoProductos[i][6] = producto.getRubroVictoria().getCodigo();
                                tablaContenidoProductos[i][7] = producto.getPrecio_contado();
                                tablaContenidoProductos[i][8] = Integer.valueOf(propVictoria.getProperty("stock"));
                                
                     
                                i++;
                            }
                            cargarTablaProductosVictoria(tablaContenidoProductos);
                            tProductoEstado.setText(productosW.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ productosW.consulta.getDebugMessage()+ " | "+ productosW.consulta.getJson().getJSONArray("items").getJSONObject(1)); 
                            appendMensaje("Se obtuvieron "+productosW.getCantidad()+" registros.");
                        }
                    }else{
                        System.out.println("Proceso no terminado: "+productosW.consulta.getDebugMessage());
                    }
                } catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+productosW.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
            }
        }else if("RubrosVictoriaWorker".equals(source)){
            if(value.equals("STARTED")){
                bVictoriaBuscar.setEnabled(false);
                bVictoriaLimpiar.setText("Detener");
                tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                bVictoriaBuscar.setEnabled(true);
                bVictoriaLimpiar.setText("Limpiar");
                tProductoEstado.setText("Cargando maestro...");
               appendMensaje("\nCONSULTA: "+rubrosW.consulta.getCon().getURL());
                try {
                    
                    if(rubrosW.isDone()){
                        if(rubrosW.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            if(rubrosW != null){
                                tVictoriaCantidad.setText(rubrosW.getCantidad().toString());
                                tablaContenidoRubros = new Object[rubrosW.getCantidad()][tablaHeaderRubros.length];

                                int i = 0;
                                for (RubroVictoria rubros : rubrosW.get()) {

                                    tablaContenidoRubros[i][0] = rubros; 
                                    tablaContenidoRubros[i][1] = i; 
                                    tablaContenidoRubros[i][2] = rubros.getCodigo();
                                    tablaContenidoRubros[i][3] = rubros.getNombre();
                                    tablaContenidoRubros[i][4] = rubros.getParent_codigo();

                                  i++;
                                }   
                                cargarTablaRubrosVictoria(tablaContenidoRubros);
                                tProductoEstado.setText(rubrosW.consulta.getErrorMessage());
                                appendMensaje("RESPUESTA: "+ rubrosW.consulta.getDebugMessage()+ " | "+ rubrosW.consulta.getJson().getJSONArray("items").getJSONObject(1)); 
                                appendMensaje("Se obtuvieron "+rubrosW.getCantidad()+" registros.");
                            }else{
                                appendMensaje("Se obtuvieron 0 registros. Verifique el funcionamiento del WS de Victoria.");
                            }
                            
                        
                        
                        }
                        }} catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+rubrosW.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
            }
        }else if("MarcasVictoriaWorker".equals(source)){
            if(value.equals("STARTED")){
                bVictoriaBuscar.setEnabled(false);
                bVictoriaLimpiar.setText("Detener");
                tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                bVictoriaBuscar.setEnabled(true);
                bVictoriaLimpiar.setText("Limpiar");
                tProductoEstado.setText("Cargando maestro...");
                
                appendMensaje("\nCONSULTA: "+marcasW.consultaV.getCon().getURL());
              
                try {
                    if(marcasW.isDone()){
                        if(marcasW.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            tVictoriaCantidad.setText(marcasW.getCantidad().toString());
                            tablaContenidoMarcas = new Object[marcasW.getCantidad()][tablaHeaderMarcas.length];

                            int i = 0;
                            for (MarcaVictoria marcas : marcasW.get()) {
                                tablaContenidoMarcas[i][0] = marcas; //Se utiliza para pasar despues a la consulta.
                                tablaContenidoMarcas[i][1] = i; //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoMarcas[i][2] = marcas.getCodigo();
                                tablaContenidoMarcas[i][3] = marcas.getNombre();
                                i++;
                            }  
                            
                            cargarTablaMarcas(tablaContenidoMarcas);
                            tProductoEstado.setText(marcasW.consultaV.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ marcasW.consultaV.getDebugMessage()+ " | "+ marcasW.consultaV.getJson().getJSONArray("items").getJSONObject(1)); 
                            appendMensaje("Se obtuvieron "+marcasW.getCantidad()+" registros.");
                            }
                    }else{
                        System.out.println("Proceso no terminado: "+maestroW.consulta.getDebugMessage());
                    }
                } catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+maestroW.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
            }
        }else if("ProductoVictoriaWorker".equals(source)){
         
            if(value.equals("STARTED")){
                bProductoBuscarV.setEnabled(false);
                bProductoLimpiarV.setText("Detener");
                tProductoEstado.setText("Buscando producto...");
            }else if(value.equals("DONE")){
                bProductoBuscarV.setEnabled(true);
                bProductoLimpiarV.setText("Limpiar");
                tProductoEstado.setText("Cargando producto...");
                
                appendMensaje("\nCONSULTA: "+productoW.consulta.getCon().getURL());
                
                if(productoW.isDone()){
                    if(productoW.isCancelled()){
                        
                        tProductoEstado.setText("Busqueda cancelada.");
                        System.out.println("Proceso de busqueda cancelado.");
                        
                    }else{
                        try {
                          
                            productoBusquedaV = productoW.get();
                           
                            
                            cargarProductosdeVictoria(productoBusquedaV);
                            tProductoEstado.setText(productoW.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+productoW.consulta.getDebugMessage()+" | "+productoW.consulta.getJson().toString());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }else{
                    System.out.println("Error desconocido: "+productoW.consulta.getDebugMessage());
                }
            }else{
                tProductoEstado.setText("Cargando producto "+value+"%");
            }
            tProductoEstado.setText("Cargando maestro "+value+"%");
        }else if("MarcasWorkerSC".equals(source)){
            if(value.equals("STARTED")){
                 tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                 tProductoEstado.setText("Cargando maestro...");
                
                appendMensaje("\nCONSULTA: "+marcasSC.consulta.getCon().getURL());
              
                try {
                    if(marcasSC.isDone()){
                        if(marcasSC.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            tVictoriaCantidad.setText(marcasSC.getCantidad().toString()); //WORKER SARAA
                            tablaContenidoMarcasSC = new Object[marcasSC.getCantidad()][tablaHeaderMarcasSC.length];

                            int i = 0;
                            for (MarcaSC marcas : marcasSC.get()) {
                                tablaContenidoMarcasSC[i][0] = marcas; //Se utiliza para pasar despues a la consulta.
                                //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoMarcasSC[i][1] = marcas.getId(); //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoMarcasSC[i][2] = marcas.getCodigo();
                                tablaContenidoMarcasSC[i][3] = marcas.getNombre();
                                i++;
                            }  
                            
                            cargarTablaMarcasSC(tablaContenidoMarcasSC);
                            tProductoEstado.setText(marcasSC.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ marcasSC.consulta.getDebugMessage()+ " | "+ marcasSC.consulta.getJason()); 
                            appendMensaje("Se obtuvieron "+marcasSC.getCantidad()+" registros.");
                            }
                    }else{
                        System.out.println("Proceso no terminado: "+marcasSC.consulta.getDebugMessage());
                    }
                } catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+marcasSC.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
            } 
        }else if("RubrosWorkerSC".equals(source)){
            if(value.equals("STARTED")){
                 tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                 tProductoEstado.setText("Cargando maestro...");
                
                appendMensaje("\nCONSULTA: "+rubrosSC.consulta.getCon().getURL());
              
                try {
                    if(rubrosSC.isDone()){
                        if(rubrosSC.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            tVictoriaCantidad.setText(rubrosSC.getCantidad().toString()); //WORKER SARAA
                            tablaContenidoRubrosSC = new Object[rubrosSC.getCantidad()][tablaHeaderRubrosSC.length];

                            int i = 0;
                            for (RubroSC rubros : rubrosSC.get()) {
                                tablaContenidoRubrosSC[i][0] = rubros; //Se utiliza para pasar despues a la consulta.
                                //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoRubrosSC[i][1] = rubros.getId(); //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoRubrosSC[i][2] = rubros.getCodigo();
                                tablaContenidoRubrosSC[i][3] = rubros.getNombre();
                                tablaContenidoRubrosSC[i][4] = rubros.getParent_id();
                                i++;
                            }  
                            
                            cargarTablaRubrosSC(tablaContenidoRubrosSC);
                            tProductoEstado.setText(rubrosSC.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ rubrosSC.consulta.getDebugMessage()+ " | "+ rubrosSC.consulta.getJason()); 
                            appendMensaje("Se obtuvieron "+rubrosSC.getCantidad()+" registros.");
                            }
                    }else{
                        System.out.println("Proceso no terminado: "+rubrosSC.consulta.getDebugMessage());
                    }
                } catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+rubrosSC.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
            }
        }else if("ProductosWorkerSC".equals(source)){
            if(value.equals("STARTED")){
                 tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                System.out.println("e n t r o ______");
                 tProductoEstado.setText("Cargando maestro...");
                appendMensaje("\nCONSULTA: "+productosSC.consulta.getCon().getURL());
                try { 
                    if(productosSC.isDone()){
                        if(productosSC.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                            tVictoriaCantidad.setText(productosSC.getCantidad().toString()); //WORKER SARAA
                            tablaContenidoProductosSC = new Object[productosSC.getCantidad()][tablaHeaderProductosSC.length];
                            codigosSC = new ArrayList<>();
                            
                            int i = 0;
                            
                            for (ProductoSC productos : productosSC.get()) {
                                tablaContenidoProductosSC[i][0] = productos; //Se utiliza para pasar despues a la consulta.
                                //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductosSC[i][1] = productos.getId(); //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductosSC[i][2] = productos.getCodigo(); //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductosSC[i][3] = productos.getNombre();
                                tablaContenidoProductosSC[i][4] = productos.getDescripcion();
                                tablaContenidoProductosSC[i][5] = productos.getRubroSC().getNombre();
                                tablaContenidoProductosSC[i][6] = productos.getMarcaSC().getNombre();
                                tablaContenidoProductosSC[i][7] = productos.getPrecio();
                                tablaContenidoProductosSC[i][8] = productos.getStock();
                                codigosSC.add(productos.getCodigo());
                                  
                                  i++;
                              }
                             buscarCuotasSC(codigosSC); //SE DEBE MANDAR UNA LISTA, YA QUE EL GET FUNCIONA COMO CODIGO: ["100","102"]
                            //AL MANDAR UN STRING[] EL RESULTADO SERA POR CADA CODIGO, UN ARRAY, CODIGO:["100"], CODIGO["102"]. SOBRECARGA EL SERVIDOR
                            //AL BUSCAR TODOS LOS PRODUCTOS DE LA WEBSITE, SE VA A IR CARGANDO LOS CODIGOS PARA LAS CUOTAS
                           
                            cargarTablaProductoSC(tablaContenidoProductosSC);
                            
                            tProductoEstado.setText(productosSC.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ productosSC.consulta.getDebugMessage()+ " | "+ productosSC.consulta.getJason()); 
                            appendMensaje("Se obtuvieron "+productosSC.getCantidad()+" registros.");
                            }
                        
                    }else{
                        System.out.println("Proceso no terminado: "+productosSC.consulta.getDebugMessage());
                    }
                   
                } catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+productosSC.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
             }
                 
      } /*else if("ProductoDetalleWorkerSC".equals(source)){
         
            if(value.equals("STARTED")){
                bProductoBuscarSC.setEnabled(false);
                bProductoLimpiarSC.setText("Detener");
                tProductoEstado.setText("Buscando producto...");
            }else if(value.equals("DONE")){
                bProductoBuscarSC.setEnabled(true);
                bProductoLimpiarV.setText("Limpiar");
                tProductoEstado.setText("Cargando producto...");
                
                appendMensaje("\nCONSULTA: "+productoSC.consulta.getCon().getURL());
                
                if(productoSC.isDone()){
                    if(productoSC.isCancelled()){
                        
                        tProductoEstado.setText("Busqueda cancelada.");
                        System.out.println("Proceso de busqueda cancelado.");
                        
                    }else{
                        try {
                            productoBusquedaSC = productoSC.get();
                            
                            cargarProductosSC(productoBusquedaSC);
                            tProductoEstado.setText(productoSC.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+productoSC.consulta.getDebugMessage()+" | "+productoW.consulta.getJson().toString());
                        } catch (InterruptedException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ExecutionException ex) {
                            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }else{
                    System.out.println("Error desconocido: "+productoW.consulta.getDebugMessage());
                }
            }else{
                tProductoEstado.setText("Cargando producto "+value+"%");
            }
                tProductoEstado.setText("Cargando maestro "+value+"%");
            }*/
    } 
        else if("ProductoDetalleWorkerSC".equals(source)){
         
            if(value.equals("STARTED")){
                bProductoBuscarV.setEnabled(false);
                bProductoLimpiarV.setText("Detener");
                tProductoEstado.setText("Buscando producto...");
            }else if(value.equals("DONE")){
                bProductoBuscarV.setEnabled(true);
                bProductoLimpiarV.setText("Limpiar");
                tProductoEstado.setText("Cargando producto...");
                
                appendMensaje("\nCONSULTA: "+productoSC.consulta.getCon().getURL());
                
                if(productoSC.isDone()){
                    if(productoSC.isCancelled()){            
                        tProductoEstado.setText("Busqueda cancelada.");
                        System.out.println("Proceso de busqueda cancelado.");
                        
                    }else{
                        productoBusquedaSC = productoSC.productoSC;
                        
                        cargarProductosSC(productoBusquedaSC);
                        String arrnum[] ={productoBusquedaSC.getCodigo()};
                            buscarCuotaSC(arrnum);
    
                        
                        
                        tProductoEstado.setText(productoSC.consulta.getErrorMessage());
                        appendMensaje("RESPUESTA: "+productoSC.consulta.getDebugMessage()+" | "+productoSC.consulta.getJason().toString());
                    }
                }else{
                    System.out.println("Error desconocido: "+productoSC.consulta.getDebugMessage());
                }
            }else{
                tProductoEstado.setText("Cargando producto "+value+"%");
            }
                tProductoEstado.setText("Cargando maestro "+value+"%");
    }
    }
    public Integer extraeEntero(String cadena){
        System.out.println("ORIG: "+cadena);
        String numeros = "0";
        
        for(int i = 0; i< cadena.length(); i ++){
            if(".".equals(cadena.charAt(i))){
                break;
            }else{
                if(Character.isDigit(cadena.charAt(i))){
                    numeros = ""+numeros+cadena.charAt(i);
                }
            }
        }
        System.out.println("FORM: "+numeros);
        return Integer.valueOf(numeros);
    }    
}

    
    

