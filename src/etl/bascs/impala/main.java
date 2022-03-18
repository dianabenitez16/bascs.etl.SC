/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala;

import com.formdev.flatlaf.IntelliJTheme;
import etl.bascs.impala.clases.MarcasVictoria;
import etl.bascs.victoria.clases.MarcasWorker;
import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.ProductosVictoria;
import bascs.website.clases.RubrosSC;
import etl.bascs.impala.clases.RubrosVictoria;
import etl.bascs.impala.clases.Scalr;
import etl.bascs.impala.config.Propiedades;
import etl.bascs.impala.worker.DetalleWorker;
import etl.bascs.impala.worker.MaestroWorker;
import bascs.website.clases.MarcasWorkerSC;
import bascs.website.clases.ProductoSC;
import bascs.website.clases.ProductoWorkerSC;
import bascs.website.clases.RubrosWorkerSC;
import etl.bascs.impala.clases.MarcasSC;
import etl.bascs.impala.worker.PrestashopWorker;
import etl.bascs.victoria.clases.ProductoWorkerDetalle;
import etl.bascs.victoria.clases.ProductosWorker;
import etl.bascs.victoria.clases.RubrosWorker;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
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
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import static org.apache.http.HttpHeaders.USER_AGENT;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author junju
 */
public class main extends javax.swing.JFrame implements java.beans.PropertyChangeListener {
    public static DecimalFormat formatInt = new DecimalFormat("#,##0");
    public static DecimalFormat formatDec = new DecimalFormat("#,##0.##");
    
    public Propiedades propiedades;
    public Properties propGenerales = new Properties();
    public Properties propVictoria = new Properties();
    public Properties propSC = new Properties();
    public Properties propImpala = new Properties();
    public Properties propJellyfish = new Properties();
    
    public Boolean isClicked = false;
    
    
    //PARA MAÑANA ARREGLAR LA PARTE DE TABLAS, EN EL WORKER, APARENTEMENTE DESPUES DE ESO YA FUNCIOONA
    
    public RubrosWorker rubrosW;
    public ProductosWorker productosW;
    public MarcasWorker marcasW;
    
    public MarcasWorkerSC marcasSC;
    public RubrosWorkerSC rubrosSC;
    public ProductoWorkerSC productosSC;
    
    public PrestashopWorker prestashopW;
    public MaestroWorker maestroW;
    public DetalleWorker detalleW;
    public ProductoWorkerDetalle productoW;
    
    public RubrosVictoria rubVt;
    public RubrosSC rubScl;
    
    public JFileChooser fc;
    public File fPrestashopImport;
    
    public Integer contadorVolumetrico;
    ////---CARGA DE TABLAS-----
    public String[] tablaHeaderMaestro = new String[] {"X", "ID","Codigo", "Alternativo", "EAN", "Stock", "Descripcion", "Marca", "Categoria", "Division" , "Precio"};
    public Integer[] tablaWithMaestro = new Integer[] {5,90,30,80,20,500,80,150,150,50};
    public Object[][] tablaContenidoMaestro;
    
    public String[] tablaHeaderProductos = new String[] {"X","ID","Codigo", "Nombre", "Descripción", "Rubro", "Marca"};
    public Integer[] tablaWithProductos = new Integer[] {5,90,150,500,150,150,150};
    public Object[][] tablaContenidoProductos;
    
    public String[] tablaHeaderProductosSC = new String[] {"X","ID","Codigo", "Nombre", "Descripción", "Rubro", "Marca"};
    public Integer[] tablaWithProductosSC = new Integer[] {5,90,150,500,150,150,150};
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
   
    public String[] tablaHeaderRubrosSC = new String[] {"X","ID","Codigo", "Nombre"};
    public Integer[] tablaRubrosSC = new Integer[] {30,30,30,30};
    public Object[][] tablaContenidoRubrosSC;
  
    public String[] tablaHeaderPrestashop;
    
    public Producto productoBusqueda;
    public ProductosVictoria productoBusquedaV;
            
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
    public void buscarProductoVictoria (ProductosVictoria productoV){
        if(!tProductoIDV.getText().isEmpty()){
            productoW = new ProductoWorkerDetalle(productoV, getPropiedades());
            productoW.addPropertyChangeListener(this);
            productoW.execute();
        }else{
            JOptionPane.showMessageDialog(null, "Ingrese un codigo de producto válido.");
        }
    }
    
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
    public void cargarProductosdeVictoria(ProductosVictoria producto){
        if(producto.cargado){
            tProductoIDV.setText(producto.getCodigo());
            tProductoNombreV.setText(producto.getNombre());
            taProductoDescripcionV.setText(producto.getDescripcion());
            tProductoMarcaV.setText(producto.getMarca());
            tProductoRubroV.setText(producto.getRubro());
            
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
  
    public void cargarTablaCuotasV(Object[][] contenido){
       tbProductoCuotas.setModel(new javax.swing.table.DefaultTableModel(contenido,new String [] {"CUOTA", "PRECIO CUOTA", "PRECIO CREDITO", "PRECIO CONTADO"}));
        tbProductoCuotas.getColumnModel().getColumn(0).setPreferredWidth(130);
        tbProductoCuotas.getColumnModel().getColumn(1).setPreferredWidth(130);
        tbProductoCuotas.getColumnModel().getColumn(2).setPreferredWidth(130);
        tbProductoCuotas.getColumnModel().getColumn(3).setPreferredWidth(130);
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
    
    public void buscarRubros(){
        rubrosW = new RubrosWorker(getPropiedades());
        rubrosW.addPropertyChangeListener(this);
        rubrosW.execute();
    }
    public void buscarRubrosSC(){
        rubrosSC = new RubrosWorkerSC(getPropiedades());
        rubrosSC.addPropertyChangeListener(this);
        rubrosSC.execute();
    }
    public void buscarMarcasSC(){
        marcasSC = new MarcasWorkerSC(getPropiedades());
        marcasSC.addPropertyChangeListener(this);
        marcasSC.execute();
    }
    public void buscarMarcas(){
        limpiarMaestro();
        marcasW = new MarcasWorker(getPropiedades());
        marcasW.addPropertyChangeListener(this);
        marcasW.execute();
    }
    public void buscarMaestro(){
        limpiarMaestro();
        maestroW = new MaestroWorker(getPropiedades());
        maestroW.addPropertyChangeListener(this);
        maestroW.execute();
    }
    public void buscarProductoVictoria(){
        limpiarMaestro();
        productosW = new ProductosWorker(getPropiedades());
        productosW.addPropertyChangeListener(this);
        productosW.execute();
    }
    public void buscarProductoWebsite(){
        limpiarMaestro();
        productosSC = new ProductoWorkerSC(getPropiedades());
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
     public void cargarTablaProductos(Object[][] contenidos){
        tbVictoriaProductos.setModel(new javax.swing.table.DefaultTableModel(contenidos,tablaHeaderProductos));
        tbVictoriaProductos.getColumnModel().removeColumn(tbVictoriaProductos.getColumnModel().getColumn(0));
        for (int i = 0; i < tbVictoriaProductos.getColumnCount(); i++) {
            tbVictoriaProductos.getColumnModel().getColumn(i).setPreferredWidth(tablaWithProductos[i]);
        }
    }
    public void cargarTablaRubros(Object[][] contenidos){
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
        if(isClicked){
            return propVictoria;
        }
    switch(cbOrigen.getSelectedIndex()){
            case 0:
                return propImpala;
            case 1:
                return propJellyfish;
            case 2:
                return propVictoria;
            case 3:
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
                    System.out.println("EL VALOR DEL DETALLE ES " + tablaContenidoMaestro[(int) table.getValueAt(row, 0)][2].toString());
                    
                    buscarProducto((Producto) tablaContenidoMaestro[(int) table.getValueAt(row, 0)][0]);
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
                    buscarProductoVictoria((ProductosVictoria) tablaContenidoProductos[(int) table.getValueAt(row, 0)][0]);
                } 
            }
        });
        tbVictoriaProductos.setDefaultEditor(Object.class, null);
        tbVictoriaProductos.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
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
        lProductoCategoria1 = new javax.swing.JLabel();
        bProductoLimpiarV = new javax.swing.JButton();
        bProductoBuscarV = new javax.swing.JButton();
        tProductoIDV = new javax.swing.JTextField();
        sProductoSeparador4 = new javax.swing.JSeparator();
        tProductoNombreV = new javax.swing.JTextField();
        spProductoDescripcionLarga1 = new javax.swing.JScrollPane();
        taProductoDescripcionV = new javax.swing.JTextArea();
        tProductoMarcaV = new javax.swing.JTextField();
        spProductoDetallesTecnicos1 = new javax.swing.JScrollPane();
        tbProductoCuotas = new javax.swing.JTable();
        tProductoStock1 = new javax.swing.JTextField();
        lProductoExistencia7 = new javax.swing.JLabel();
        lProductoDetallesTecnicos3 = new javax.swing.JLabel();
        tProductoPrecioVentaFinal1 = new javax.swing.JTextField();
        lProductoPrecio7 = new javax.swing.JLabel();
        tProductoRubroV = new javax.swing.JTextField();
        lProductoMarca3 = new javax.swing.JLabel();
        pVictoriaMaestro = new javax.swing.JPanel();
        sProductoSeparador5 = new javax.swing.JSeparator();
        bVictoriaLimpiar = new javax.swing.JButton();
        bVictoriaBuscar = new javax.swing.JButton();
        spMaestroProductos1 = new javax.swing.JScrollPane();
        tbVictoriaProductos = new javax.swing.JTable();
        lMaestroCantidad1 = new javax.swing.JLabel();
        tVictoriaCantidad = new javax.swing.JTextField();
        pConsultaPrestashop1 = new javax.swing.JPanel();
        bPrestashopProcesar1 = new javax.swing.JButton();
        bPrestashopLimpiar1 = new javax.swing.JButton();
        sProductoSeparador6 = new javax.swing.JSeparator();
        spPrestashop1 = new javax.swing.JScrollPane();
        tbPrestashop1 = new javax.swing.JTable();
        bPrestashopAbrir1 = new javax.swing.JButton();
        tPrestashopFileExport1 = new javax.swing.JTextField();
        lMaestroCantidad12 = new javax.swing.JLabel();
        lMaestroCantidad13 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        tPrestashopWorkerEstado1 = new javax.swing.JLabel();
        pMyR = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tRubrosVictoria = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        bRubros = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tMarcasVictoria = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        pDebugRYM = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        debugRubros = new javax.swing.JTextArea();
        debugRubro = new javax.swing.JButton();
        pWebsite = new javax.swing.JPanel();
        tpWebsite = new javax.swing.JTabbedPane();
        pWebsiteDetalle = new javax.swing.JPanel();
        lProductoCodigo2 = new javax.swing.JLabel();
        lProductoDescripcionLarga2 = new javax.swing.JLabel();
        lProductoMarca2 = new javax.swing.JLabel();
        lProductoCategoria2 = new javax.swing.JLabel();
        lProductoDivision2 = new javax.swing.JLabel();
        lProductoPrecio8 = new javax.swing.JLabel();
        lProductoExistencia12 = new javax.swing.JLabel();
        lProductoDetallesTecnicos4 = new javax.swing.JLabel();
        bProductoLimpiar2 = new javax.swing.JButton();
        bProductoBuscar2 = new javax.swing.JButton();
        tProductoID2 = new javax.swing.JTextField();
        sProductoSeparador7 = new javax.swing.JSeparator();
        tProductoDescripcion2 = new javax.swing.JTextField();
        spProductoDescripcionLarga2 = new javax.swing.JScrollPane();
        taProductoDescripcionLarga2 = new javax.swing.JTextArea();
        tProductoMarca2 = new javax.swing.JTextField();
        tProductoCategoria2 = new javax.swing.JTextField();
        tProductoDivision2 = new javax.swing.JTextField();
        tProductoPrecioLista2 = new javax.swing.JTextField();
        tProductoMoneda2 = new javax.swing.JTextField();
        tProductoExistencia2 = new javax.swing.JCheckBox();
        spProductoDetallesTecnicos2 = new javax.swing.JScrollPane();
        tbProductoDetallesTecnicos2 = new javax.swing.JTable();
        spProductoImagenes2 = new javax.swing.JScrollPane();
        tbProductoImagenes2 = new javax.swing.JTable();
        taProductoImagen2 = new javax.swing.JLabel();
        lProductoPrecio9 = new javax.swing.JLabel();
        tProductoPrecioVenta2 = new javax.swing.JTextField();
        tProductoStock2 = new javax.swing.JTextField();
        lProductoExistencia13 = new javax.swing.JLabel();
        lProductoPrecio10 = new javax.swing.JLabel();
        tProductoPrecioCosto2 = new javax.swing.JTextField();
        lProductoExistencia14 = new javax.swing.JLabel();
        tProductoFactorVenta2 = new javax.swing.JTextField();
        lProductoExistencia15 = new javax.swing.JLabel();
        lProductoExistencia16 = new javax.swing.JLabel();
        tProductoFactorCosto2 = new javax.swing.JTextField();
        tProductoEAN2 = new javax.swing.JTextField();
        lProductoDetallesTecnicos5 = new javax.swing.JLabel();
        tProductoPrecioVentaFinal2 = new javax.swing.JTextField();
        lProductoPrecio11 = new javax.swing.JLabel();
        lProductoExistencia17 = new javax.swing.JLabel();
        tProductoEnvioImporte2 = new javax.swing.JTextField();
        pWebsiteMaestro = new javax.swing.JPanel();
        sProductoSeparador8 = new javax.swing.JSeparator();
        bMaestroLimpiar2 = new javax.swing.JButton();
        bMaestroBuscar2 = new javax.swing.JButton();
        spMaestroProductos2 = new javax.swing.JScrollPane();
        tbProductosSC = new javax.swing.JTable();
        lMaestroCantidad8 = new javax.swing.JLabel();
        tMaestroCantidad2 = new javax.swing.JTextField();
        pWebsitePrestashop = new javax.swing.JPanel();
        bPrestashopProcesar2 = new javax.swing.JButton();
        bPrestashopLimpiar2 = new javax.swing.JButton();
        sProductoSeparador9 = new javax.swing.JSeparator();
        spPrestashop2 = new javax.swing.JScrollPane();
        tbPrestashop2 = new javax.swing.JTable();
        bPrestashopAbrir2 = new javax.swing.JButton();
        tPrestashopFileExport2 = new javax.swing.JTextField();
        lMaestroCantidad14 = new javax.swing.JLabel();
        lMaestroCantidad15 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        tPrestashopWorkerEstado2 = new javax.swing.JLabel();
        pMyRSC = new javax.swing.JPanel();
        tRubrosSC = new javax.swing.JScrollPane();
        tRubroSC = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        bRubrosSC = new javax.swing.JButton();
        tMarcasSC = new javax.swing.JScrollPane();
        tMarcaSC = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
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
                .addContainerGap(455, Short.MAX_VALUE))
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
                    .addComponent(sProductoSeparador2, javax.swing.GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
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
                    .addContainerGap(277, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                    .addContainerGap(287, Short.MAX_VALUE)
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
                .addComponent(spPrestashop, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                    .addGap(331, 331, 331)
                    .addComponent(lMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(172, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                    .addContainerGap(182, Short.MAX_VALUE)
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
                .addComponent(tpConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
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
                .addContainerGap(660, Short.MAX_VALUE))
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
                .addContainerGap(404, Short.MAX_VALUE))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pCBASCSLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(lBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pCBASCSLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tVictoriaRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pCBASCSLayout.createSequentialGroup()
                                .addComponent(tVictoriaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)
                                .addComponent(lBASCSPuerto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tVictoriaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 327, Short.MAX_VALUE))
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
                    .addComponent(tVictoriaSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tVictoriaPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lBASCSPuerto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tVictoriaMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tVictoriaRubro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(tVictoriaMet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(279, Short.MAX_VALUE))
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
                .addContainerGap(490, Short.MAX_VALUE))
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
                .addContainerGap(840, Short.MAX_VALUE))
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
                .addComponent(tpConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
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
                .addComponent(spDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        pDebugLayout.setVerticalGroup(
            pDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pDebugLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(spDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE)
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

        lProductoCategoria1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCategoria1.setText("Categoria");
        lProductoCategoria1.setPreferredSize(new java.awt.Dimension(80, 20));

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

        tProductoMarcaV.setEditable(false);
        tProductoMarcaV.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarcaV.setPreferredSize(new java.awt.Dimension(150, 20));

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

        tProductoPrecioVentaFinal1.setEditable(false);
        tProductoPrecioVentaFinal1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioVentaFinal1.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoPrecio7.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio7.setText("Precio final");
        lProductoPrecio7.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoRubroV.setEditable(false);
        tProductoRubroV.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoRubroV.setPreferredSize(new java.awt.Dimension(150, 20));

        lProductoMarca3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca3.setText("Rubro:");
        lProductoMarca3.setPreferredSize(new java.awt.Dimension(80, 20));

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
                            .addComponent(lProductoCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDetallesTecnicos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(tProductoMarcaV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(55, 55, 55)
                                .addComponent(lProductoExistencia7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tProductoStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(63, 63, 63)
                                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoPrecio7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tProductoPrecioVentaFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoMarca3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(tProductoRubroV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addGap(126, 126, 126)
                                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(spProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spProductoDetallesTecnicos1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                        .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addGap(53, 53, 53)
                                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(tProductoMarcaV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lProductoExistencia7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tProductoStock1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lProductoMarca3, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(tProductoRubroV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(lProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pVictoriaDetalleLayout.createSequentialGroup()
                                .addGap(202, 202, 202)
                                .addComponent(lProductoCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(lProductoDetallesTecnicos3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pVictoriaDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lProductoPrecio7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoPrecioVentaFinal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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

        javax.swing.GroupLayout pVictoriaMaestroLayout = new javax.swing.GroupLayout(pVictoriaMaestro);
        pVictoriaMaestro.setLayout(pVictoriaMaestroLayout);
        pVictoriaMaestroLayout.setHorizontalGroup(
            pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pVictoriaMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sProductoSeparador5, javax.swing.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE)
                    .addGroup(pVictoriaMaestroLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tVictoriaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bVictoriaLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bVictoriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spMaestroProductos1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        pVictoriaMaestroLayout.setVerticalGroup(
            pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tVictoriaCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pVictoriaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bVictoriaLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bVictoriaBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spMaestroProductos1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpVictoria.addTab("Productos", pVictoriaMaestro);

        pConsultaPrestashop1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pConsultaPrestashop1.setPreferredSize(new java.awt.Dimension(960, 710));

        bPrestashopProcesar1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopProcesar1.setText("Procesar");
        bPrestashopProcesar1.setPreferredSize(new java.awt.Dimension(80, 20));
        bPrestashopProcesar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopProcesar1ActionPerformed(evt);
            }
        });

        bPrestashopLimpiar1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopLimpiar1.setText("Limpiar");
        bPrestashopLimpiar1.setPreferredSize(new java.awt.Dimension(80, 20));
        bPrestashopLimpiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopLimpiar1ActionPerformed(evt);
            }
        });

        spPrestashop1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spPrestashop1.setPreferredSize(new java.awt.Dimension(900, 480));

        tbPrestashop1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbPrestashop1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spPrestashop1.setViewportView(tbPrestashop1);

        bPrestashopAbrir1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopAbrir1.setText("Abrir archivo");
        bPrestashopAbrir1.setEnabled(false);
        bPrestashopAbrir1.setPreferredSize(new java.awt.Dimension(100, 20));
        bPrestashopAbrir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopAbrir1ActionPerformed(evt);
            }
        });

        tPrestashopFileExport1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPrestashopFileExport1.setEnabled(false);
        tPrestashopFileExport1.setPreferredSize(new java.awt.Dimension(300, 20));

        lMaestroCantidad12.setText("Cantidad");
        lMaestroCantidad12.setPreferredSize(new java.awt.Dimension(100, 25));

        lMaestroCantidad13.setText("Cantidad");
        lMaestroCantidad13.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText("Ruta");
        jLabel2.setPreferredSize(new java.awt.Dimension(80, 20));

        tPrestashopWorkerEstado1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPrestashopWorkerEstado1.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout pConsultaPrestashop1Layout = new javax.swing.GroupLayout(pConsultaPrestashop1);
        pConsultaPrestashop1.setLayout(pConsultaPrestashop1Layout);
        pConsultaPrestashop1Layout.setHorizontalGroup(
            pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaPrestashop1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spPrestashop1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pConsultaPrestashop1Layout.createSequentialGroup()
                        .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashop1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(tPrestashopFileExport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tPrestashopWorkerEstado1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                                .addComponent(bPrestashopAbrir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(bPrestashopLimpiar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(bPrestashopProcesar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sProductoSeparador6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(5, 5, 5))))
            .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pConsultaPrestashop1Layout.createSequentialGroup()
                    .addGap(628, 628, 628)
                    .addComponent(lMaestroCantidad12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(281, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashop1Layout.createSequentialGroup()
                    .addContainerGap(291, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(618, 618, 618)))
        );
        pConsultaPrestashop1Layout.setVerticalGroup(
            pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaPrestashop1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopFileExport1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopWorkerEstado1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bPrestashopAbrir1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bPrestashopLimpiar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bPrestashopProcesar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spPrestashop1, javax.swing.GroupLayout.DEFAULT_SIZE, 581, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pConsultaPrestashop1Layout.createSequentialGroup()
                    .addGap(331, 331, 331)
                    .addComponent(lMaestroCantidad12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(277, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashop1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashop1Layout.createSequentialGroup()
                    .addContainerGap(287, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(321, 321, 321)))
        );

        tpVictoria.addTab("Prestashop", pConsultaPrestashop1);

        tRubrosVictoria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ));
        jScrollPane1.setViewportView(tRubrosVictoria);

        jLabel4.setText("MARCAS");

        bRubros.setText("CARGAR");
        bRubros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRubrosActionPerformed(evt);
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

        jButton1.setText("POST TEST");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pMyRLayout = new javax.swing.GroupLayout(pMyR);
        pMyR.setLayout(pMyRLayout);
        pMyRLayout.setHorizontalGroup(
            pMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMyRLayout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addGroup(pMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                    .addComponent(bRubros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
            .addGroup(pMyRLayout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(232, 232, 232))
            .addGroup(pMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pMyRLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(606, Short.MAX_VALUE)))
        );
        pMyRLayout.setVerticalGroup(
            pMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMyRLayout.createSequentialGroup()
                .addGroup(pMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pMyRLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167)
                        .addComponent(bRubros)
                        .addGap(36, 36, 36)
                        .addComponent(jButton1))
                    .addGroup(pMyRLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(183, Short.MAX_VALUE))
            .addGroup(pMyRLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pMyRLayout.createSequentialGroup()
                    .addGap(97, 97, 97)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(182, Short.MAX_VALUE)))
        );

        tpVictoria.addTab("Marcas/Rubros", pMyR);

        debugRubros.setColumns(20);
        debugRubros.setRows(5);
        jScrollPane3.setViewportView(debugRubros);

        debugRubro.setText("DEBUG RUBROS");
        debugRubro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugRubroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pDebugRYMLayout = new javax.swing.GroupLayout(pDebugRYM);
        pDebugRYM.setLayout(pDebugRYMLayout);
        pDebugRYMLayout.setHorizontalGroup(
            pDebugRYMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDebugRYMLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 849, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 160, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pDebugRYMLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(debugRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(364, 364, 364))
        );
        pDebugRYMLayout.setVerticalGroup(
            pDebugRYMLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pDebugRYMLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 464, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(debugRubro, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 100, Short.MAX_VALUE))
        );

        tpVictoria.addTab("Debug", pDebugRYM);

        javax.swing.GroupLayout pVictoriaLayout = new javax.swing.GroupLayout(pVictoria);
        pVictoria.setLayout(pVictoriaLayout);
        pVictoriaLayout.setHorizontalGroup(
            pVictoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpVictoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pVictoriaLayout.setVerticalGroup(
            pVictoriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pVictoriaLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpVictoria, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpPrincipal.addTab("Victoria", pVictoria);

        pWebsite.setPreferredSize(new java.awt.Dimension(980, 730));

        tpWebsite.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tpWebsite.setPreferredSize(new java.awt.Dimension(970, 720));

        pWebsiteDetalle.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pWebsiteDetalle.setPreferredSize(new java.awt.Dimension(960, 710));

        lProductoCodigo2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo2.setText("Código");
        lProductoCodigo2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDescripcionLarga2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDescripcionLarga2.setText("Más información");
        lProductoDescripcionLarga2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca2.setText("Marca");
        lProductoMarca2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoCategoria2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCategoria2.setText("Categoria");
        lProductoCategoria2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDivision2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDivision2.setText("División");
        lProductoDivision2.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoPrecio8.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio8.setText("Precio de lista");
        lProductoPrecio8.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia12.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia12.setText("Existencia");
        lProductoExistencia12.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDetallesTecnicos4.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDetallesTecnicos4.setText("Detalles Técnicos");
        lProductoDetallesTecnicos4.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoLimpiar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoLimpiar2.setText("Limpiar");
        bProductoLimpiar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoLimpiar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoLimpiar2ActionPerformed(evt);
            }
        });

        bProductoBuscar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoBuscar2.setText("Buscar");
        bProductoBuscar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoBuscar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoBuscar2ActionPerformed(evt);
            }
        });

        tProductoID2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoID2.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoDescripcion2.setEditable(false);
        tProductoDescripcion2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoDescripcion2.setPreferredSize(new java.awt.Dimension(400, 20));
        tProductoDescripcion2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoDescripcion2ActionPerformed(evt);
            }
        });

        taProductoDescripcionLarga2.setEditable(false);
        taProductoDescripcionLarga2.setColumns(20);
        taProductoDescripcionLarga2.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        taProductoDescripcionLarga2.setLineWrap(true);
        taProductoDescripcionLarga2.setRows(5);
        spProductoDescripcionLarga2.setViewportView(taProductoDescripcionLarga2);

        tProductoMarca2.setEditable(false);
        tProductoMarca2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarca2.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoCategoria2.setEditable(false);
        tProductoCategoria2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoCategoria2.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoDivision2.setEditable(false);
        tProductoDivision2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoDivision2.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoPrecioLista2.setEditable(false);
        tProductoPrecioLista2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioLista2.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoMoneda2.setEditable(false);
        tProductoMoneda2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMoneda2.setPreferredSize(new java.awt.Dimension(40, 20));

        tProductoExistencia2.setEnabled(false);
        tProductoExistencia2.setPreferredSize(new java.awt.Dimension(20, 20));

        spProductoDetallesTecnicos2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoDetallesTecnicos2.setPreferredSize(new java.awt.Dimension(400, 150));

        tbProductoDetallesTecnicos2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoDetallesTecnicos2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Atributo", "Valor"
            }
        ));
        spProductoDetallesTecnicos2.setViewportView(tbProductoDetallesTecnicos2);

        spProductoImagenes2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoImagenes2.setPreferredSize(new java.awt.Dimension(400, 100));

        tbProductoImagenes2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoImagenes2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "URL"
            }
        ));
        spProductoImagenes2.setViewportView(tbProductoImagenes2);

        taProductoImagen2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        taProductoImagen2.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        taProductoImagen2.setPreferredSize(new java.awt.Dimension(400, 400));

        lProductoPrecio9.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio9.setText("Precio de venta");
        lProductoPrecio9.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoPrecioVenta2.setEditable(false);
        tProductoPrecioVenta2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioVenta2.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoStock2.setEditable(false);
        tProductoStock2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoStock2.setPreferredSize(new java.awt.Dimension(40, 20));

        lProductoExistencia13.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia13.setText("Stock superior a");
        lProductoExistencia13.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoPrecio10.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio10.setText("Precio de costo");
        lProductoPrecio10.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoPrecioCosto2.setEditable(false);
        tProductoPrecioCosto2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioCosto2.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoExistencia14.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia14.setText("Moneda");
        lProductoExistencia14.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoFactorVenta2.setEditable(false);
        tProductoFactorVenta2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoFactorVenta2.setPreferredSize(new java.awt.Dimension(40, 20));

        lProductoExistencia15.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia15.setText("Recargo venta");
        lProductoExistencia15.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia16.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia16.setText("Descuento costo");
        lProductoExistencia16.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoFactorCosto2.setEditable(false);
        tProductoFactorCosto2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoFactorCosto2.setPreferredSize(new java.awt.Dimension(40, 20));

        tProductoEAN2.setEditable(false);
        tProductoEAN2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEAN2.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoDetallesTecnicos5.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDetallesTecnicos5.setText("Detalles Técnicos");
        lProductoDetallesTecnicos5.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoPrecioVentaFinal2.setEditable(false);
        tProductoPrecioVentaFinal2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoPrecioVentaFinal2.setPreferredSize(new java.awt.Dimension(100, 20));

        lProductoPrecio11.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoPrecio11.setText("Precio final");
        lProductoPrecio11.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoExistencia17.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoExistencia17.setText("Recargo envío");
        lProductoExistencia17.setPreferredSize(new java.awt.Dimension(80, 20));

        tProductoEnvioImporte2.setEditable(false);
        tProductoEnvioImporte2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEnvioImporte2.setPreferredSize(new java.awt.Dimension(40, 20));

        javax.swing.GroupLayout pWebsiteDetalleLayout = new javax.swing.GroupLayout(pWebsiteDetalle);
        pWebsiteDetalle.setLayout(pWebsiteDetalleLayout);
        pWebsiteDetalleLayout.setHorizontalGroup(
            pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDetallesTecnicos4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDetallesTecnicos5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoCategoria2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDivision2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tProductoDivision2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoCategoria2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoMarca2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(55, 55, 55)
                                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                                .addComponent(lProductoExistencia14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteDetalleLayout.createSequentialGroup()
                                                .addComponent(lProductoExistencia13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteDetalleLayout.createSequentialGroup()
                                                .addComponent(lProductoExistencia12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)))
                                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(tProductoExistencia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoStock2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(tProductoMoneda2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(spProductoImagenes2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spProductoDetallesTecnicos2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(5, 5, 5)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(taProductoImagen2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                .addComponent(lProductoPrecio11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoPrecioVentaFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lProductoPrecio8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lProductoPrecio10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lProductoPrecio9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tProductoPrecioLista2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tProductoPrecioCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tProductoPrecioVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoExistencia16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tProductoFactorCosto2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoExistencia15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tProductoFactorVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                        .addComponent(lProductoExistencia17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(tProductoEnvioImporte2, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addComponent(lProductoCodigo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tProductoID2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tProductoEAN2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tProductoDescripcion2, javax.swing.GroupLayout.PREFERRED_SIZE, 505, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bProductoLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(bProductoBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sProductoSeparador7))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pWebsiteDetalleLayout.setVerticalGroup(
            pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteDetalleLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bProductoLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tProductoDescripcion2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoEAN2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoID2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lProductoCodigo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoDescripcionLarga2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addComponent(lProductoDetallesTecnicos4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(135, 135, 135)
                        .addComponent(lProductoDetallesTecnicos5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                                .addGap(125, 125, 125)
                                .addComponent(spProductoDetallesTecnicos2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(spProductoImagenes2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(taProductoImagen2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lProductoPrecio8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoPrecioLista2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoFactorCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tProductoFactorVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoPrecioCosto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoPrecio10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(5, 5, 5)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lProductoDivision2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoDivision2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tProductoMoneda2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lProductoExistencia14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tProductoEnvioImporte2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoPrecioVenta2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoPrecio9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGap(387, 387, 387)
                        .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tProductoExistencia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoExistencia12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoMarca2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(tProductoStock2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(lProductoExistencia13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(tProductoCategoria2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pWebsiteDetalleLayout.createSequentialGroup()
                        .addGap(412, 412, 412)
                        .addComponent(lProductoCategoria2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addGroup(pWebsiteDetalleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lProductoPrecio11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoPrecioVentaFinal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        tpWebsite.addTab("Detalle", pWebsiteDetalle);

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

        javax.swing.GroupLayout pWebsiteMaestroLayout = new javax.swing.GroupLayout(pWebsiteMaestro);
        pWebsiteMaestro.setLayout(pWebsiteMaestroLayout);
        pWebsiteMaestroLayout.setHorizontalGroup(
            pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsiteMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsiteMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sProductoSeparador8, javax.swing.GroupLayout.DEFAULT_SIZE, 999, Short.MAX_VALUE)
                    .addGroup(pWebsiteMaestroLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addComponent(bMaestroBuscar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(spMaestroProductos2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpWebsite.addTab("Productos", pWebsiteMaestro);

        pWebsitePrestashop.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pWebsitePrestashop.setPreferredSize(new java.awt.Dimension(960, 710));

        bPrestashopProcesar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopProcesar2.setText("Procesar");
        bPrestashopProcesar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bPrestashopProcesar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopProcesar2ActionPerformed(evt);
            }
        });

        bPrestashopLimpiar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopLimpiar2.setText("Limpiar");
        bPrestashopLimpiar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bPrestashopLimpiar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopLimpiar2ActionPerformed(evt);
            }
        });

        spPrestashop2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spPrestashop2.setPreferredSize(new java.awt.Dimension(900, 480));

        tbPrestashop2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbPrestashop2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spPrestashop2.setViewportView(tbPrestashop2);

        bPrestashopAbrir2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bPrestashopAbrir2.setText("Abrir archivo");
        bPrestashopAbrir2.setEnabled(false);
        bPrestashopAbrir2.setPreferredSize(new java.awt.Dimension(100, 20));
        bPrestashopAbrir2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bPrestashopAbrir2ActionPerformed(evt);
            }
        });

        tPrestashopFileExport2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPrestashopFileExport2.setEnabled(false);
        tPrestashopFileExport2.setPreferredSize(new java.awt.Dimension(300, 20));

        lMaestroCantidad14.setText("Cantidad");
        lMaestroCantidad14.setPreferredSize(new java.awt.Dimension(100, 25));

        lMaestroCantidad15.setText("Cantidad");
        lMaestroCantidad15.setPreferredSize(new java.awt.Dimension(100, 25));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel3.setText("Ruta");
        jLabel3.setPreferredSize(new java.awt.Dimension(80, 20));

        tPrestashopWorkerEstado2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tPrestashopWorkerEstado2.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout pWebsitePrestashopLayout = new javax.swing.GroupLayout(pWebsitePrestashop);
        pWebsitePrestashop.setLayout(pWebsitePrestashopLayout);
        pWebsitePrestashopLayout.setHorizontalGroup(
            pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsitePrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(spPrestashop2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pWebsitePrestashopLayout.createSequentialGroup()
                        .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsitePrestashopLayout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(tPrestashopFileExport2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(tPrestashopWorkerEstado2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                                .addComponent(bPrestashopAbrir2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(bPrestashopLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(5, 5, 5)
                                .addComponent(bPrestashopProcesar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sProductoSeparador9, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(5, 5, 5))))
            .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pWebsitePrestashopLayout.createSequentialGroup()
                    .addGap(628, 628, 628)
                    .addComponent(lMaestroCantidad14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(281, Short.MAX_VALUE)))
            .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsitePrestashopLayout.createSequentialGroup()
                    .addContainerGap(291, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(618, 618, 618)))
        );
        pWebsitePrestashopLayout.setVerticalGroup(
            pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsitePrestashopLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopFileExport2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tPrestashopWorkerEstado2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bPrestashopAbrir2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bPrestashopLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bPrestashopProcesar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(spPrestashop2, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pWebsitePrestashopLayout.createSequentialGroup()
                    .addGap(331, 331, 331)
                    .addComponent(lMaestroCantidad14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(172, Short.MAX_VALUE)))
            .addGroup(pWebsitePrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pWebsitePrestashopLayout.createSequentialGroup()
                    .addContainerGap(182, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(321, 321, 321)))
        );

        tpWebsite.addTab("Prestashop", pWebsitePrestashop);

        tRubroSC.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nombre"
            }
        ));
        tRubrosSC.setViewportView(tRubroSC);

        jLabel6.setText("MARCAS");

        bRubrosSC.setText("CARGAR");
        bRubrosSC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRubrosSCActionPerformed(evt);
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

        javax.swing.GroupLayout pMyRSCLayout = new javax.swing.GroupLayout(pMyRSC);
        pMyRSC.setLayout(pMyRSCLayout);
        pMyRSCLayout.setHorizontalGroup(
            pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMyRSCLayout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addComponent(bRubrosSC, javax.swing.GroupLayout.DEFAULT_SIZE, 133, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tRubrosSC, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(86, 86, 86))
            .addGroup(pMyRSCLayout.createSequentialGroup()
                .addGap(155, 155, 155)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(232, 232, 232))
            .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pMyRSCLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addComponent(tMarcasSC, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(606, Short.MAX_VALUE)))
        );
        pMyRSCLayout.setVerticalGroup(
            pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pMyRSCLayout.createSequentialGroup()
                .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pMyRSCLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(167, 167, 167)
                        .addComponent(bRubrosSC))
                    .addGroup(pMyRSCLayout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(tRubrosSC, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(78, Short.MAX_VALUE))
            .addGroup(pMyRSCLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pMyRSCLayout.createSequentialGroup()
                    .addGap(97, 97, 97)
                    .addComponent(tMarcasSC, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(77, Short.MAX_VALUE)))
        );

        tpWebsite.addTab("Marcas/Rubros", pMyRSC);

        javax.swing.GroupLayout pWebsiteLayout = new javax.swing.GroupLayout(pWebsite);
        pWebsite.setLayout(pWebsiteLayout);
        pWebsiteLayout.setHorizontalGroup(
            pWebsiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        pWebsiteLayout.setVerticalGroup(
            pWebsiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pWebsiteLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, 555, Short.MAX_VALUE)
                .addContainerGap())
        );

        tpPrincipal.addTab("Website", pWebsite);

        tProductoEstado.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEstado.setEnabled(false);
        tProductoEstado.setPreferredSize(new java.awt.Dimension(600, 20));
        tProductoEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoEstadoActionPerformed(evt);
            }
        });

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
                    .addComponent(tpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1009, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tProductoEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tpPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 588, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tProductoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOrigen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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

    private void bProductoLimpiarVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiarVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoLimpiarVActionPerformed

    private void bProductoBuscarVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoBuscarVActionPerformed
        productoBusquedaV = new ProductosVictoria(getPropiedades());
        productoBusquedaV.setCodigo(tProductoIDV.getText());
        buscarProductoVictoria(productoBusquedaV);        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoBuscarVActionPerformed

    private void tProductoNombreVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoNombreVActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoNombreVActionPerformed

    private void bVictoriaLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVictoriaLimpiarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bVictoriaLimpiarActionPerformed

    private void bVictoriaBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bVictoriaBuscarActionPerformed
buscarProductoVictoria();        // TODO add your handling code here:
    }//GEN-LAST:event_bVictoriaBuscarActionPerformed

    private void bPrestashopProcesar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopProcesar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bPrestashopProcesar1ActionPerformed

    private void bPrestashopLimpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopLimpiar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bPrestashopLimpiar1ActionPerformed

    private void bPrestashopAbrir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopAbrir1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bPrestashopAbrir1ActionPerformed

    private void bProductoLimpiar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoLimpiar2ActionPerformed

    private void bProductoBuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoBuscar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoBuscar2ActionPerformed

    private void tProductoDescripcion2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoDescripcion2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoDescripcion2ActionPerformed

    private void bMaestroLimpiar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroLimpiar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bMaestroLimpiar2ActionPerformed

    private void bMaestroBuscar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroBuscar2ActionPerformed
buscarProductoWebsite();        // TODO add your handling code here:
    }//GEN-LAST:event_bMaestroBuscar2ActionPerformed

    private void bPrestashopProcesar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopProcesar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bPrestashopProcesar2ActionPerformed

    private void bPrestashopLimpiar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopLimpiar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bPrestashopLimpiar2ActionPerformed

    private void bPrestashopAbrir2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopAbrir2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bPrestashopAbrir2ActionPerformed

    private void bRubrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRubrosActionPerformed
isClicked = true;
buscarRubros();
buscarMarcas();
    }//GEN-LAST:event_bRubrosActionPerformed

    private void tVictoriaProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaProductosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaProductosActionPerformed

    private void tVictoriaProductosDetallesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaProductosDetallesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaProductosDetallesActionPerformed

    private void debugRubroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugRubroActionPerformed
    try{
        String url = "http://www.saracomercial.com/panel/api/loader/rubros";

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
   
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader( "Accept", "application/json");
        post.setHeader( "Content-Type", "application/json");
        post.setHeader("Authorization", "Bearer 4|fRCGP9hboE5eiZPOrCu0bnpEug2IlGfIv05L7uYK");
        post.setHeader("Method", "GET");
        
          List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        RubrosVictoria rubros = new RubrosVictoria();
        urlParameters.add(new BasicNameValuePair("codigo_interno_ws", "0"));
        urlParameters.add(new BasicNameValuePair("nombre", "No Posee"));
            System.out.println("NOMBRE: " +rubros.getCodigo());
           
 

        
          post.setEntity(new UrlEncodedFormEntity(urlParameters));
 
        HttpResponse response = client.execute(post);
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + post.getEntity());
        System.out.println("Response Code : " +
                                    response.getStatusLine().getStatusCode());
 
        BufferedReader rd = new BufferedReader(
                        new InputStreamReader(response.getEntity().getContent()));
 
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
       System.out.println(result.toString());
                          
                 }catch (Exception ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
                         } 
    }//GEN-LAST:event_debugRubroActionPerformed

    private void tVictoriaMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tVictoriaMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tVictoriaMarcaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
isClicked = true;  
boolean result = false;
Integer i = 0;

   HttpClient hc = new DefaultHttpClient();
   String message;
  
                HttpPost p = new HttpPost("http://www.saracomercial.com/panel/api/loader/rubros");
                 JSONObject object = new JSONObject();
                
              try {
                  for (RubrosVictoria rub : rubrosW.rubrosV) {
            object.put("codigo_interno_ws", rub.getCodigo());
            object.put("nombre", rub.getNombre());
            object.put("parent_id", rub.getParent_id());
               
                message = object.toString();
                JSONObject json = new JSONObject(message); // Convert text to object
                System.out.println(json.toString(8));

                p.setEntity(new StringEntity(message));
                p.setHeader("Content-type", "application/json");
                p.setHeader("Accept", "application/json");
                p.setHeader("Connection", "keep-alive");
                p.setHeader("Authorization", "Bearer 4|fRCGP9hboE5eiZPOrCu0bnpEug2IlGfIv05L7uYK");
                            
                HttpResponse resp = hc.execute(p);
                  resp.getEntity().consumeContent();
                      if (resp != null) {
                        System.out.println("RESP " + resp.toString());
                        if (resp.getStatusLine().getStatusCode() == 204)
                            result = true;
                    }  
                      
              }            
         } catch (Exception e) {
                    e.printStackTrace();
         }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void bRubrosSCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRubrosSCActionPerformed
buscarMarcasSC();
buscarRubrosSC();
    }//GEN-LAST:event_bRubrosSCActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        IntelliJTheme.install( etl.bascs.impala.main.class.getResourceAsStream("/json/Hiberbee.theme.json" ) );
        
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
    private javax.swing.JButton bMaestroBuscar;
    private javax.swing.JButton bMaestroBuscar2;
    private javax.swing.JButton bMaestroLimpiar;
    private javax.swing.JButton bMaestroLimpiar2;
    private javax.swing.JButton bPrestashopAbrir;
    private javax.swing.JButton bPrestashopAbrir1;
    private javax.swing.JButton bPrestashopAbrir2;
    private javax.swing.JButton bPrestashopLimpiar;
    private javax.swing.JButton bPrestashopLimpiar1;
    private javax.swing.JButton bPrestashopLimpiar2;
    private javax.swing.JButton bPrestashopProcesar;
    private javax.swing.JButton bPrestashopProcesar1;
    private javax.swing.JButton bPrestashopProcesar2;
    private javax.swing.JButton bPrestashopSeleccionar;
    private javax.swing.JButton bProductoBuscar;
    private javax.swing.JButton bProductoBuscar2;
    private javax.swing.JButton bProductoBuscarV;
    private javax.swing.JButton bProductoLimpiar;
    private javax.swing.JButton bProductoLimpiar2;
    private javax.swing.JButton bProductoLimpiarV;
    private javax.swing.JButton bRubros;
    private javax.swing.JButton bRubrosSC;
    private javax.swing.JButton bVictoriaBuscar;
    private javax.swing.JButton bVictoriaLimpiar;
    private javax.swing.JComboBox<String> cbOrigen;
    private javax.swing.JButton debugRubro;
    private javax.swing.JTextArea debugRubros;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
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
    private javax.swing.JLabel lMaestroCantidad12;
    private javax.swing.JLabel lMaestroCantidad13;
    private javax.swing.JLabel lMaestroCantidad14;
    private javax.swing.JLabel lMaestroCantidad15;
    private javax.swing.JLabel lMaestroCantidad2;
    private javax.swing.JLabel lMaestroCantidad3;
    private javax.swing.JLabel lMaestroCantidad4;
    private javax.swing.JLabel lMaestroCantidad5;
    private javax.swing.JLabel lMaestroCantidad6;
    private javax.swing.JLabel lMaestroCantidad7;
    private javax.swing.JLabel lMaestroCantidad8;
    private javax.swing.JLabel lProductoCategoria;
    private javax.swing.JLabel lProductoCategoria1;
    private javax.swing.JLabel lProductoCategoria2;
    private javax.swing.JLabel lProductoCodigo;
    private javax.swing.JLabel lProductoCodigo1;
    private javax.swing.JLabel lProductoCodigo2;
    private javax.swing.JLabel lProductoDescripcionLarga;
    private javax.swing.JLabel lProductoDescripcionLarga1;
    private javax.swing.JLabel lProductoDescripcionLarga2;
    private javax.swing.JLabel lProductoDetallesTecnicos;
    private javax.swing.JLabel lProductoDetallesTecnicos1;
    private javax.swing.JLabel lProductoDetallesTecnicos3;
    private javax.swing.JLabel lProductoDetallesTecnicos4;
    private javax.swing.JLabel lProductoDetallesTecnicos5;
    private javax.swing.JLabel lProductoDivision;
    private javax.swing.JLabel lProductoDivision2;
    private javax.swing.JLabel lProductoExistencia;
    private javax.swing.JLabel lProductoExistencia1;
    private javax.swing.JLabel lProductoExistencia12;
    private javax.swing.JLabel lProductoExistencia13;
    private javax.swing.JLabel lProductoExistencia14;
    private javax.swing.JLabel lProductoExistencia15;
    private javax.swing.JLabel lProductoExistencia16;
    private javax.swing.JLabel lProductoExistencia17;
    private javax.swing.JLabel lProductoExistencia2;
    private javax.swing.JLabel lProductoExistencia3;
    private javax.swing.JLabel lProductoExistencia4;
    private javax.swing.JLabel lProductoExistencia5;
    private javax.swing.JLabel lProductoExistencia7;
    private javax.swing.JLabel lProductoMarca;
    private javax.swing.JLabel lProductoMarca1;
    private javax.swing.JLabel lProductoMarca2;
    private javax.swing.JLabel lProductoMarca3;
    private javax.swing.JLabel lProductoPrecio;
    private javax.swing.JLabel lProductoPrecio1;
    private javax.swing.JLabel lProductoPrecio10;
    private javax.swing.JLabel lProductoPrecio11;
    private javax.swing.JLabel lProductoPrecio2;
    private javax.swing.JLabel lProductoPrecio3;
    private javax.swing.JLabel lProductoPrecio7;
    private javax.swing.JLabel lProductoPrecio8;
    private javax.swing.JLabel lProductoPrecio9;
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
    private javax.swing.JPanel pConsultaPrestashop1;
    private javax.swing.JPanel pDebug;
    private javax.swing.JPanel pDebugRYM;
    private javax.swing.JPanel pMyR;
    private javax.swing.JPanel pMyRSC;
    private javax.swing.JPanel pPrestashop;
    private javax.swing.JPanel pVictoria;
    private javax.swing.JPanel pVictoriaDetalle;
    private javax.swing.JPanel pVictoriaMaestro;
    private javax.swing.JPanel pWebsite;
    private javax.swing.JPanel pWebsiteDetalle;
    private javax.swing.JPanel pWebsiteMaestro;
    private javax.swing.JPanel pWebsitePrestashop;
    private javax.swing.JSeparator sProductoSeparador1;
    private javax.swing.JSeparator sProductoSeparador2;
    private javax.swing.JSeparator sProductoSeparador3;
    private javax.swing.JSeparator sProductoSeparador4;
    private javax.swing.JSeparator sProductoSeparador5;
    private javax.swing.JSeparator sProductoSeparador6;
    private javax.swing.JSeparator sProductoSeparador7;
    private javax.swing.JSeparator sProductoSeparador8;
    private javax.swing.JSeparator sProductoSeparador9;
    private javax.swing.JScrollPane spCPrestashopCargado;
    private javax.swing.JScrollPane spCPrestashopDefault;
    private javax.swing.JScrollPane spDebug;
    private javax.swing.JScrollPane spMaestroProductos;
    private javax.swing.JScrollPane spMaestroProductos1;
    private javax.swing.JScrollPane spMaestroProductos2;
    private javax.swing.JScrollPane spPrestashop;
    private javax.swing.JScrollPane spPrestashop1;
    private javax.swing.JScrollPane spPrestashop2;
    private javax.swing.JScrollPane spProductoDescripcionLarga;
    private javax.swing.JScrollPane spProductoDescripcionLarga1;
    private javax.swing.JScrollPane spProductoDescripcionLarga2;
    private javax.swing.JScrollPane spProductoDetallesTecnicos;
    private javax.swing.JScrollPane spProductoDetallesTecnicos1;
    private javax.swing.JScrollPane spProductoDetallesTecnicos2;
    private javax.swing.JScrollPane spProductoImagenes;
    private javax.swing.JScrollPane spProductoImagenes2;
    private javax.swing.JTextField tGeneralesEnviosDesde;
    private javax.swing.JTextField tGeneralesEnviosHasta;
    private javax.swing.JTextField tGeneralesEnviosImporte;
    private javax.swing.JCheckBox tGeneralesEnviosSumar;
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
    private javax.swing.JTextField tPrestashopExportColumnas;
    private javax.swing.JTextField tPrestashopExportLineas;
    private javax.swing.JTextField tPrestashopFileExport;
    private javax.swing.JTextField tPrestashopFileExport1;
    private javax.swing.JTextField tPrestashopFileExport2;
    private javax.swing.JTextField tPrestashopPath;
    private javax.swing.JLabel tPrestashopWorkerEstado;
    private javax.swing.JLabel tPrestashopWorkerEstado1;
    private javax.swing.JLabel tPrestashopWorkerEstado2;
    private javax.swing.JTextField tProductoCategoria;
    private javax.swing.JTextField tProductoCategoria2;
    private javax.swing.JTextField tProductoDescripcion;
    private javax.swing.JTextField tProductoDescripcion2;
    private javax.swing.JTextField tProductoDivision;
    private javax.swing.JTextField tProductoDivision2;
    private javax.swing.JTextField tProductoEAN;
    private javax.swing.JTextField tProductoEAN2;
    private javax.swing.JTextField tProductoEnvioImporte;
    private javax.swing.JTextField tProductoEnvioImporte2;
    private javax.swing.JTextField tProductoEstado;
    private javax.swing.JCheckBox tProductoExistencia;
    private javax.swing.JCheckBox tProductoExistencia2;
    private javax.swing.JTextField tProductoFactorCosto;
    private javax.swing.JTextField tProductoFactorCosto2;
    private javax.swing.JTextField tProductoFactorVenta;
    private javax.swing.JTextField tProductoFactorVenta2;
    private javax.swing.JTextField tProductoID;
    private javax.swing.JTextField tProductoID2;
    private javax.swing.JTextField tProductoIDV;
    private javax.swing.JTextField tProductoMarca;
    private javax.swing.JTextField tProductoMarca2;
    private javax.swing.JTextField tProductoMarcaV;
    private javax.swing.JTextField tProductoMoneda;
    private javax.swing.JTextField tProductoMoneda2;
    private javax.swing.JTextField tProductoNombreV;
    private javax.swing.JTextField tProductoPrecioCosto;
    private javax.swing.JTextField tProductoPrecioCosto2;
    private javax.swing.JTextField tProductoPrecioLista;
    private javax.swing.JTextField tProductoPrecioLista2;
    private javax.swing.JTextField tProductoPrecioVenta;
    private javax.swing.JTextField tProductoPrecioVenta2;
    private javax.swing.JTextField tProductoPrecioVentaFinal;
    private javax.swing.JTextField tProductoPrecioVentaFinal1;
    private javax.swing.JTextField tProductoPrecioVentaFinal2;
    private javax.swing.JTextField tProductoRubroV;
    private javax.swing.JTextField tProductoStock;
    private javax.swing.JTextField tProductoStock1;
    private javax.swing.JTextField tProductoStock2;
    private javax.swing.JTable tRubroSC;
    private javax.swing.JScrollPane tRubrosSC;
    private javax.swing.JTable tRubrosVictoria;
    private javax.swing.JTextField tVictoriaCantidad;
    private javax.swing.JTextField tVictoriaMarca;
    private javax.swing.JTextField tVictoriaMet;
    private javax.swing.JTextField tVictoriaProductos;
    private javax.swing.JTextField tVictoriaProductosDetalles;
    private javax.swing.JTextField tVictoriaPuerto;
    private javax.swing.JTextField tVictoriaRubro;
    private javax.swing.JTextField tVictoriaSServidor;
    private javax.swing.JTextArea taDebug;
    private javax.swing.JTextArea taProductoDescripcionLarga;
    private javax.swing.JTextArea taProductoDescripcionLarga2;
    private javax.swing.JTextArea taProductoDescripcionV;
    private javax.swing.JLabel taProductoImagen;
    private javax.swing.JLabel taProductoImagen2;
    private javax.swing.JTable tbCPrestashopCargado;
    private javax.swing.JTable tbCPrestashopDefault;
    private javax.swing.JTable tbMaestroProductos;
    private javax.swing.JTable tbPrestashop;
    private javax.swing.JTable tbPrestashop1;
    private javax.swing.JTable tbPrestashop2;
    private javax.swing.JTable tbProductoCuotas;
    private javax.swing.JTable tbProductoDetallesTecnicos;
    private javax.swing.JTable tbProductoDetallesTecnicos2;
    private javax.swing.JTable tbProductoImagenes;
    private javax.swing.JTable tbProductoImagenes2;
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
        }else if("ProductosWorker".equals(source)){
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
                            for (ProductosVictoria producto : productosW.get()) {
                                tablaContenidoProductos[i][0] = producto; //Se utiliza para pasar despues a la consulta.
                                tablaContenidoProductos[i][1] = i; //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductos[i][2] = producto.getCodigo();
                                tablaContenidoProductos[i][3] = producto.getNombre();
                                tablaContenidoProductos[i][4] = producto.getDescripcion();
                                tablaContenidoProductos[i][5] = producto.getMarca();
                                tablaContenidoProductos[i][6] = producto.getRubro();
//                                tablaContenidoProductos[i][8] = 0;
                                i++;
                            }
                            cargarTablaProductos(tablaContenidoProductos);
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
            }}else if("RubrosWorker".equals(source)){
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
                            tVictoriaCantidad.setText(rubrosW.getCantidad().toString());
                            tablaContenidoRubros = new Object[rubrosW.getCantidad()][tablaHeaderRubros.length];

                            int i = 0;
                            for (RubrosVictoria rubros : rubrosW.get()) {
                             
                                tablaContenidoRubros[i][0] = rubros; 
                                tablaContenidoRubros[i][1] = i; 
                                tablaContenidoRubros[i][2] = rubros.getCodigo();
                                tablaContenidoRubros[i][3] = rubros.getNombre();
                                tablaContenidoRubros[i][4] = rubros.getParent_id();
                                
                              i++;
                            }   
                            cargarTablaRubros(tablaContenidoRubros);
                            tProductoEstado.setText(rubrosW.consulta.getErrorMessage());
                            appendMensaje("RESPUESTA: "+ rubrosW.consulta.getDebugMessage()+ " | "+ rubrosW.consulta.getJson().getJSONArray("items").getJSONObject(1)); 
                            appendMensaje("Se obtuvieron "+rubrosW.getCantidad()+" registros.");
                        
                        
                            }
                        }} catch (InterruptedException | ExecutionException | JSONException ex){
                    System.out.println("Error desconocido: "+rubrosW.consulta.getDebugMessage());
                    System.err.println(ex.getMessage());
                }
                }
            }else if("MarcasWorker".equals(source)){
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
                            for (MarcasVictoria marcas : marcasW.get()) {
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
            }
        else if("ProductoWorkerDetalle".equals(source)){
         
            if(value.equals("STARTED")){
                bProductoBuscarV.setEnabled(false);
                bProductoLimpiarV.setText("Detener");
                tProductoEstado.setText("Buscando producto...");
            }else if(value.equals("DONE")){
                bProductoBuscarV.setEnabled(true);
                bProductoLimpiarV.setText("Limpiar");
                tProductoEstado.setText("Cargando producto...");
                
                appendMensaje("\nCONSULTA: "+productoW.consulta.getCon().getURL());
                System.out.println("ENTRO EN DETALLES ");
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
                            for (MarcasSC marcas : marcasSC.get()) {
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
            } }else if("RubrosWorkerSC".equals(source)){
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
                            for (RubrosSC rubros : rubrosSC.get()) {
                                tablaContenidoRubrosSC[i][0] = rubros; //Se utiliza para pasar despues a la consulta.
                                //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoRubrosSC[i][1] = rubros.getId(); //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoRubrosSC[i][2] = rubros.getCodigo();
                                tablaContenidoRubrosSC[i][3] = rubros.getNombre();
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
            }else if("ProductoWorkerSC".equals(source)){
            if(value.equals("STARTED")){
                 tProductoEstado.setText("Descargando maestro...");
            }else if(value.equals("DONE")){
                 tProductoEstado.setText("Cargando maestro...");
                
                appendMensaje("\nCONSULTA: "+productosSC.consulta.getCon().getURL());
              
                try { 
                    if(productosSC.isDone()){
                    if(productosSC.isCancelled()){
                            System.out.println("Proceso de busqueda cancelado.");
                        }else{
                         
                    
                          tVictoriaCantidad.setText(productosSC.getCantidad().toString()); //WORKER SARAA
                            tablaContenidoProductosSC = new Object[productosSC.getCantidad()][tablaHeaderProductosSC.length];

                            int i = 0;
                            for (ProductoSC productos : productosSC.get()) {
                                tablaContenidoProductosSC[i][0] = productos; //Se utiliza para pasar despues a la consulta.
                                tablaContenidoProductosSC[i][1] = i; //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductosSC[i][2] = productos.getId(); //Se utiliza para asociar desde el Modelo al array de contenidos.
                                tablaContenidoProductosSC[i][3] = productos.getCodigo();
                                tablaContenidoProductosSC[i][4] = productos.getMarca();
                                tablaContenidoProductosSC[i][5] = productos.getRubro();
                                 i++;
                            
                            
                             }  
                            
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
            }
            }
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
