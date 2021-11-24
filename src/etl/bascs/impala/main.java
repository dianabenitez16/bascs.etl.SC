/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala;

import com.formdev.flatlaf.IntelliJTheme;
import etl.bascs.impala.clases.Producto;
import etl.bascs.impala.clases.Scalr;
import etl.bascs.impala.config.Propiedades;
import etl.bascs.impala.json.ConsultaHttp;
import etl.bascs.impala.worker.DetalleWorker;
import etl.bascs.impala.worker.MaestroWorker;
import etl.bascs.impala.worker.PrestashopWorker;
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
import java.util.Iterator;
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
import org.json.JSONArray;
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
    public Properties propBASCS = new Properties();
    public Properties propImpala = new Properties();
    public Properties propJellyfish = new Properties();
    
    public PrestashopWorker prestashopW;
    public MaestroWorker maestroW;
    public DetalleWorker detalleW;
    
    public JFileChooser fc;
    public File fPrestashopImport;
    
    public Integer contadorVolumetrico;
    
    public String[] tablaHeaderMaestro = new String[] {"X", "ID","Codigo", "Alternativo", "EAN", "Stock", "Descripcion", "Marca", "Categoria", "Division" , "Precio"};
    public Integer[] tablaWithMaestro = new Integer[] {5,90,30,80,20,500,80,150,150,50};
    public Object[][] tablaContenidoMaestro;
    
    public String[] tablaHeaderPrestashop;
    
    public Producto productoBusqueda;
            
    /* CONSTRUCTOR */        
    /**********************************************************************************************************/
    public main() {
        initComponents();
        setLocationRelativeTo(null);
        iniciarPropiedades();
        iniciarListeners();
                
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
    
    /* MAESTRO */
    
    public void limpiarMaestro(){
        tMaestroCantidad.setText("0");
        cargarTablaMaestro(new Object [][] {});
    }
    
    public void buscarMaestro(){
        limpiarMaestro();
        maestroW = new MaestroWorker(getPropiedades());
        maestroW.addPropertyChangeListener(this);
        maestroW.execute();
    }
    
    public void cargarTablaMaestro(Object[][] contenido){
        tbMaestroProductos.setModel(new javax.swing.table.DefaultTableModel(contenido,tablaHeaderMaestro));
        tbMaestroProductos.getColumnModel().removeColumn(tbMaestroProductos.getColumnModel().getColumn(0));
        for (int i = 0; i < tbMaestroProductos.getColumnCount(); i++) {
            tbMaestroProductos.getColumnModel().getColumn(i).setPreferredWidth(tablaWithMaestro[i]);
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
                
        tBASCSServidor.setText(propBASCS.getProperty("servidor"));
        tBASCSPuerto.setText(propBASCS.getProperty("puerto"));
        tBASCSInstancia.setText(propBASCS.getProperty("instancia"));
        tBASCSBD.setText(propBASCS.getProperty("db"));
        tBASCSUsuario.setText(propBASCS.getProperty("usuario"));
        tBASCSClave.setText(propBASCS.getProperty("clave"));
        
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
        switch(cbOrigen.getSelectedIndex()){
            case 0:
                return propImpala;
            case 1:
                return propJellyfish;
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
                    
                    buscarProducto((Producto) tablaContenidoMaestro[(int) table.getValueAt(row, 0)][0]);
                }
            }
        });
        tbMaestroProductos.setDefaultEditor(Object.class, null);
        tbMaestroProductos.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        
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
        tProductoEstado = new javax.swing.JTextField();
        cbOrigen = new javax.swing.JComboBox<>();
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
        lBASCSUsuario = new javax.swing.JLabel();
        lBASCSClave = new javax.swing.JLabel();
        tBASCSServidor = new javax.swing.JTextField();
        tBASCSPuerto = new javax.swing.JTextField();
        tBASCSInstancia = new javax.swing.JTextField();
        tBASCSBD = new javax.swing.JTextField();
        tBASCSUsuario = new javax.swing.JTextField();
        tBASCSClave = new javax.swing.JPasswordField();
        jSeparator2 = new javax.swing.JSeparator();
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
        pWebsite = new javax.swing.JPanel();
        tpWebsite = new javax.swing.JTabbedPane();
        pConsultaProductos = new javax.swing.JPanel();
        sProductoSeparador5 = new javax.swing.JSeparator();
        bMaestroLimpiar1 = new javax.swing.JButton();
        bMaestroBuscar1 = new javax.swing.JButton();
        spMaestroProductos1 = new javax.swing.JScrollPane();
        tbMaestroProductos1 = new javax.swing.JTable();
        lMaestroCantidad1 = new javax.swing.JLabel();
        tMaestroCantidad1 = new javax.swing.JTextField();
        pConsultaProducto = new javax.swing.JPanel();
        lProductoCodigo1 = new javax.swing.JLabel();
        lProductoDescripcionLarga1 = new javax.swing.JLabel();
        lProductoMarca1 = new javax.swing.JLabel();
        lProductoCategoria1 = new javax.swing.JLabel();
        bProductoLimpiar1 = new javax.swing.JButton();
        bProductoBuscar1 = new javax.swing.JButton();
        tProductoID1 = new javax.swing.JTextField();
        sProductoSeparador4 = new javax.swing.JSeparator();
        tProductoDescripcion1 = new javax.swing.JTextField();
        spProductoDescripcionLarga1 = new javax.swing.JScrollPane();
        taProductoDescripcionLarga1 = new javax.swing.JTextArea();
        tProductoMarca1 = new javax.swing.JTextField();
        tProductoCategoria1 = new javax.swing.JTextField();
        spProductoImagenes1 = new javax.swing.JScrollPane();
        tbProductoImagenes1 = new javax.swing.JTable();
        taProductoImagen1 = new javax.swing.JLabel();
        tProductoEAN1 = new javax.swing.JTextField();
        lCuotas = new javax.swing.JLabel();
        bProductoLimpiar2 = new javax.swing.JButton();

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
                .addContainerGap(694, Short.MAX_VALUE))
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

        tProductoEstado.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEstado.setEnabled(false);
        tProductoEstado.setPreferredSize(new java.awt.Dimension(600, 20));
        tProductoEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoEstadoActionPerformed(evt);
            }
        });

        cbOrigen.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        cbOrigen.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Impala", "Jellyfish" }));
        cbOrigen.setPreferredSize(new java.awt.Dimension(60, 20));
        cbOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbOrigenActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pConsultaMaestroLayout = new javax.swing.GroupLayout(pConsultaMaestro);
        pConsultaMaestro.setLayout(pConsultaMaestroLayout);
        pConsultaMaestroLayout.setHorizontalGroup(
            pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaMaestroLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sProductoSeparador2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1005, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaMaestroLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tMaestroCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bMaestroLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMaestroBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spMaestroProductos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pConsultaMaestroLayout.createSequentialGroup()
                        .addComponent(tProductoEstado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(spMaestroProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(pConsultaMaestroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tProductoEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbOrigen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(130, 130, 130))
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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
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
                    .addContainerGap(282, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                    .addContainerGap(292, Short.MAX_VALUE)
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
                .addComponent(spPrestashop, javax.swing.GroupLayout.DEFAULT_SIZE, 696, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pConsultaPrestashopLayout.createSequentialGroup()
                    .addGap(331, 331, 331)
                    .addComponent(lMaestroCantidad2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(397, Short.MAX_VALUE)))
            .addGroup(pConsultaPrestashopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaPrestashopLayout.createSequentialGroup()
                    .addContainerGap(407, Short.MAX_VALUE)
                    .addComponent(lMaestroCantidad7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(321, 321, 321)))
        );

        tpConsulta.addTab("Prestashop", pConsultaPrestashop);

        javax.swing.GroupLayout pConsultaLayout = new javax.swing.GroupLayout(pConsulta);
        pConsulta.setLayout(pConsultaLayout);
        pConsultaLayout.setHorizontalGroup(
            pConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tpConsulta, javax.swing.GroupLayout.DEFAULT_SIZE, 1015, Short.MAX_VALUE)
                .addContainerGap())
        );
        pConsultaLayout.setVerticalGroup(
            pConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaLayout.createSequentialGroup()
                .addComponent(tpConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 780, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
                .addContainerGap(685, Short.MAX_VALUE))
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
                .addContainerGap(632, Short.MAX_VALUE))
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
        lBASCSInstancia.setText("Instancia");
        lBASCSInstancia.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSBD.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSBD.setText("Base de datos");
        lBASCSBD.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSUsuario.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSUsuario.setText("Usuario");
        lBASCSUsuario.setPreferredSize(new java.awt.Dimension(80, 20));

        lBASCSClave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lBASCSClave.setText("Clave");
        lBASCSClave.setPreferredSize(new java.awt.Dimension(80, 20));

        tBASCSServidor.setEditable(false);
        tBASCSServidor.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tBASCSServidor.setPreferredSize(new java.awt.Dimension(150, 20));

        tBASCSPuerto.setEditable(false);
        tBASCSPuerto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tBASCSPuerto.setPreferredSize(new java.awt.Dimension(80, 20));
        tBASCSPuerto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBASCSPuertoActionPerformed(evt);
            }
        });

        tBASCSInstancia.setEditable(false);
        tBASCSInstancia.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tBASCSInstancia.setPreferredSize(new java.awt.Dimension(150, 20));

        tBASCSBD.setEditable(false);
        tBASCSBD.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tBASCSBD.setPreferredSize(new java.awt.Dimension(150, 20));

        tBASCSUsuario.setEditable(false);
        tBASCSUsuario.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tBASCSUsuario.setPreferredSize(new java.awt.Dimension(150, 20));
        tBASCSUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tBASCSUsuarioActionPerformed(evt);
            }
        });

        tBASCSClave.setEditable(false);
        tBASCSClave.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tBASCSClave.setPreferredSize(new java.awt.Dimension(150, 20));

        javax.swing.GroupLayout pCBASCSLayout = new javax.swing.GroupLayout(pCBASCS);
        pCBASCS.setLayout(pCBASCSLayout);
        pCBASCSLayout.setHorizontalGroup(
            pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCBASCSLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pCBASCSLayout.createSequentialGroup()
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pCBASCSLayout.createSequentialGroup()
                                .addComponent(lBASCSUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tBASCSUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pCBASCSLayout.createSequentialGroup()
                                .addComponent(lBASCSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tBASCSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pCBASCSLayout.createSequentialGroup()
                                .addComponent(lBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(50, 50, 50)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tBASCSBD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tBASCSClave, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 485, Short.MAX_VALUE))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(10, 10, 10))
        );
        pCBASCSLayout.setVerticalGroup(
            pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pCBASCSLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBASCSServidor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBASCSPuerto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBASCSInstancia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBASCSBD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addGroup(pCBASCSLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lBASCSUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBASCSUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lBASCSClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tBASCSClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tpConfiguracion.addTab("BasCS", pCBASCS);

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
                .addContainerGap(515, Short.MAX_VALUE))
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
                .addContainerGap(865, Short.MAX_VALUE))
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
                .addComponent(tpConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, 783, Short.MAX_VALUE)
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
                .addComponent(spDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 1025, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );
        pDebugLayout.setVerticalGroup(
            pDebugLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pDebugLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(spDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 787, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        tpPrincipal.addTab("Debug", pDebug);

        pWebsite.setPreferredSize(new java.awt.Dimension(980, 730));

        tpWebsite.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tpWebsite.setPreferredSize(new java.awt.Dimension(970, 720));

        pConsultaProductos.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pConsultaProductos.setPreferredSize(new java.awt.Dimension(960, 710));

        sProductoSeparador5.setPreferredSize(new java.awt.Dimension(900, 10));

        bMaestroLimpiar1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMaestroLimpiar1.setText("Limpiar");
        bMaestroLimpiar1.setPreferredSize(new java.awt.Dimension(80, 20));
        bMaestroLimpiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMaestroLimpiar1ActionPerformed(evt);
            }
        });

        bMaestroBuscar1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bMaestroBuscar1.setText("Buscar");
        bMaestroBuscar1.setPreferredSize(new java.awt.Dimension(80, 20));
        bMaestroBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bMaestroBuscar1ActionPerformed(evt);
            }
        });

        spMaestroProductos1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        spMaestroProductos1.setPreferredSize(new java.awt.Dimension(900, 480));

        tbMaestroProductos1.setFont(new java.awt.Font("Tahoma", 0, 8)); // NOI18N
        tbMaestroProductos1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            tablaHeaderMaestro
        ));
        spMaestroProductos1.setViewportView(tbMaestroProductos1);

        lMaestroCantidad1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lMaestroCantidad1.setText("Cantidad");
        lMaestroCantidad1.setPreferredSize(new java.awt.Dimension(80, 20));

        tMaestroCantidad1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tMaestroCantidad1.setText("0");
        tMaestroCantidad1.setEnabled(false);
        tMaestroCantidad1.setPreferredSize(new java.awt.Dimension(100, 20));

        javax.swing.GroupLayout pConsultaProductosLayout = new javax.swing.GroupLayout(pConsultaProductos);
        pConsultaProductos.setLayout(pConsultaProductosLayout);
        pConsultaProductosLayout.setHorizontalGroup(
            pConsultaProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaProductosLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sProductoSeparador5, javax.swing.GroupLayout.DEFAULT_SIZE, 1020, Short.MAX_VALUE)
                    .addGroup(pConsultaProductosLayout.createSequentialGroup()
                        .addComponent(lMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(bMaestroLimpiar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bMaestroBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(spMaestroProductos1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(5, 5, 5))
        );
        pConsultaProductosLayout.setVerticalGroup(
            pConsultaProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaProductosLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tMaestroCantidad1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pConsultaProductosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bMaestroLimpiar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bMaestroBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(spMaestroProductos1, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(93, 93, 93))
        );

        tpWebsite.addTab("Productos", pConsultaProductos);

        pConsultaProducto.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        pConsultaProducto.setPreferredSize(new java.awt.Dimension(960, 710));

        lProductoCodigo1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCodigo1.setText("Código");
        lProductoCodigo1.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoDescripcionLarga1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoDescripcionLarga1.setText("Descripción");
        lProductoDescripcionLarga1.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoMarca1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoMarca1.setText("Rubro");
        lProductoMarca1.setPreferredSize(new java.awt.Dimension(80, 20));

        lProductoCategoria1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lProductoCategoria1.setText("Marca");
        lProductoCategoria1.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoLimpiar1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoLimpiar1.setText("Limpiar");
        bProductoLimpiar1.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoLimpiar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoLimpiar1ActionPerformed(evt);
            }
        });

        bProductoBuscar1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoBuscar1.setText("Buscar");
        bProductoBuscar1.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoBuscar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoBuscar1ActionPerformed(evt);
            }
        });

        tProductoID1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoID1.setPreferredSize(new java.awt.Dimension(100, 20));

        tProductoDescripcion1.setEditable(false);
        tProductoDescripcion1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoDescripcion1.setPreferredSize(new java.awt.Dimension(400, 20));
        tProductoDescripcion1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tProductoDescripcion1ActionPerformed(evt);
            }
        });

        taProductoDescripcionLarga1.setEditable(false);
        taProductoDescripcionLarga1.setColumns(20);
        taProductoDescripcionLarga1.setFont(new java.awt.Font("Monospaced", 0, 10)); // NOI18N
        taProductoDescripcionLarga1.setLineWrap(true);
        taProductoDescripcionLarga1.setRows(5);
        spProductoDescripcionLarga1.setViewportView(taProductoDescripcionLarga1);

        tProductoMarca1.setEditable(false);
        tProductoMarca1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoMarca1.setPreferredSize(new java.awt.Dimension(150, 20));

        tProductoCategoria1.setEditable(false);
        tProductoCategoria1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoCategoria1.setPreferredSize(new java.awt.Dimension(150, 20));

        spProductoImagenes1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        spProductoImagenes1.setPreferredSize(new java.awt.Dimension(400, 100));

        tbProductoImagenes1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tbProductoImagenes1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Número", "Importe", "Descuento", "Porcentaje Descuento"
            }
        ));
        spProductoImagenes1.setViewportView(tbProductoImagenes1);

        taProductoImagen1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        taProductoImagen1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        taProductoImagen1.setPreferredSize(new java.awt.Dimension(400, 400));

        tProductoEAN1.setEditable(false);
        tProductoEAN1.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tProductoEAN1.setPreferredSize(new java.awt.Dimension(100, 20));

        lCuotas.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        lCuotas.setText("Cuotas");
        lCuotas.setPreferredSize(new java.awt.Dimension(80, 20));

        bProductoLimpiar2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        bProductoLimpiar2.setText("Actualizar");
        bProductoLimpiar2.setPreferredSize(new java.awt.Dimension(80, 20));
        bProductoLimpiar2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProductoLimpiar2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pConsultaProductoLayout = new javax.swing.GroupLayout(pConsultaProducto);
        pConsultaProducto.setLayout(pConsultaProductoLayout);
        pConsultaProductoLayout.setHorizontalGroup(
            pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pConsultaProductoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pConsultaProductoLayout.createSequentialGroup()
                        .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaProductoLayout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lProductoCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tProductoCategoria1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(87, 87, 87)
                                .addComponent(lProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(pConsultaProductoLayout.createSequentialGroup()
                                .addComponent(lProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(spProductoImagenes1, javax.swing.GroupLayout.PREFERRED_SIZE, 502, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(spProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(taProductoImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pConsultaProductoLayout.createSequentialGroup()
                        .addComponent(lProductoCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(tProductoID1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tProductoEAN1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tProductoDescripcion1, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(bProductoLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bProductoLimpiar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(bProductoBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(sProductoSeparador4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pConsultaProductoLayout.setVerticalGroup(
            pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pConsultaProductoLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(bProductoLimpiar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoBuscar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(bProductoLimpiar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tProductoDescripcion1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoEAN1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tProductoID1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lProductoCodigo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(5, 5, 5)
                .addComponent(sProductoSeparador4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pConsultaProductoLayout.createSequentialGroup()
                        .addComponent(taProductoImagen1, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(263, 263, 263))
                    .addGroup(pConsultaProductoLayout.createSequentialGroup()
                        .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(spProductoDescripcionLarga1, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(37, 37, 37)
                        .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(spProductoImagenes1, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lCuotas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38)
                        .addGroup(pConsultaProductoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tProductoCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoCategoria1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tProductoMarca1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(125, 125, 125))))
        );

        tpWebsite.addTab("Producto", pConsultaProducto);

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
                .addComponent(tpWebsite, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addGap(73, 73, 73))
        );

        tpPrincipal.addTab("Website", pWebsite);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(tpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1040, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tProductoEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoEstadoActionPerformed

    private void tImpalaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaUsuarioActionPerformed

    private void tImpalaPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaPuertoActionPerformed

    private void tBASCSUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBASCSUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tBASCSUsuarioActionPerformed

    private void tBASCSPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tBASCSPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tBASCSPuertoActionPerformed

    private void tImpalaRecursoProductoDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaRecursoProductoDetalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaRecursoProductoDetalleActionPerformed

    private void tImpalaRecursoProductoMaestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaRecursoProductoMaestroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaRecursoProductoMaestroActionPerformed

    private void tImpalaRecursoProductoImagenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaRecursoProductoImagenesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaRecursoProductoImagenesActionPerformed

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

    private void bPrestashopProcesarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopProcesarActionPerformed
        limpiarPrestashop();
        
        prestashopW = new PrestashopWorker(getPropiedades());
        prestashopW.estado = tPrestashopWorkerEstado;
        prestashopW.addPropertyChangeListener(this);
        prestashopW.iniciar();
        //psw.maestroW.addPropertyChangeListener(this); //just for debug
        prestashopW.execute();        
    }//GEN-LAST:event_bPrestashopProcesarActionPerformed

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

    private void tJellyfishPuertoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishPuertoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishPuertoActionPerformed

    private void tJellyfishUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishUsuarioActionPerformed

    private void tJellyfishRecursoProductoDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishRecursoProductoDetalleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishRecursoProductoDetalleActionPerformed

    private void tJellyfishRecursoProductoMaestroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishRecursoProductoMaestroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishRecursoProductoMaestroActionPerformed

    private void tJellyfishRecursoProductoImagenesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishRecursoProductoImagenesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishRecursoProductoImagenesActionPerformed

    private void tImpalaHilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaHilosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaHilosActionPerformed

    private void tJellyfishHilosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishHilosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishHilosActionPerformed

    private void bPrestashopAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bPrestashopAbrirActionPerformed
        try {
            File archivo = new File("export/"+tPrestashopFileExport.getText());
            Desktop.getDesktop().open(archivo);
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_bPrestashopAbrirActionPerformed

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

    private void tProductoDescripcionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoDescripcionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoDescripcionActionPerformed

    private void tGeneralesEnviosImporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tGeneralesEnviosImporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tGeneralesEnviosImporteActionPerformed

    private void tJellyfishOpcionesDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishOpcionesDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishOpcionesDescuentoActionPerformed

    private void tJellyfishOpcionesRecargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tJellyfishOpcionesRecargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tJellyfishOpcionesRecargoActionPerformed

    private void tImpalaOpcionesDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaOpcionesDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaOpcionesDescuentoActionPerformed

    private void tImpalaOpcionesRecargoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tImpalaOpcionesRecargoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tImpalaOpcionesRecargoActionPerformed

    private void bProductoLimpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoLimpiar1ActionPerformed

    private void bProductoBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoBuscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoBuscar1ActionPerformed

    private void tProductoDescripcion1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tProductoDescripcion1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tProductoDescripcion1ActionPerformed

    private void bMaestroLimpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroLimpiar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bMaestroLimpiar1ActionPerformed

    private void bMaestroBuscar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bMaestroBuscar1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bMaestroBuscar1ActionPerformed

    private void bProductoLimpiar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProductoLimpiar2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bProductoLimpiar2ActionPerformed

    private void cbOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbOrigenActionPerformed

    }//GEN-LAST:event_cbOrigenActionPerformed

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
    private javax.swing.JButton bMaestroBuscar1;
    private javax.swing.JButton bMaestroLimpiar;
    private javax.swing.JButton bMaestroLimpiar1;
    private javax.swing.JButton bPrestashopAbrir;
    private javax.swing.JButton bPrestashopLimpiar;
    private javax.swing.JButton bPrestashopProcesar;
    private javax.swing.JButton bPrestashopSeleccionar;
    private javax.swing.JButton bProductoBuscar;
    private javax.swing.JButton bProductoBuscar1;
    private javax.swing.JButton bProductoLimpiar;
    private javax.swing.JButton bProductoLimpiar1;
    private javax.swing.JButton bProductoLimpiar2;
    private javax.swing.JComboBox<String> cbOrigen;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JTabbedPane jTabbedPane5;
    private javax.swing.JTabbedPane jTabbedPane6;
    private javax.swing.JLabel lBASCSBD;
    private javax.swing.JLabel lBASCSClave;
    private javax.swing.JLabel lBASCSInstancia;
    private javax.swing.JLabel lBASCSInstancia1;
    private javax.swing.JLabel lBASCSInstancia2;
    private javax.swing.JLabel lBASCSInstancia3;
    private javax.swing.JLabel lBASCSPuerto;
    private javax.swing.JLabel lBASCSServidor;
    private javax.swing.JLabel lBASCSServidor2;
    private javax.swing.JLabel lBASCSUsuario;
    private javax.swing.JLabel lBASCSUsuario1;
    private javax.swing.JLabel lCuotas;
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
    private javax.swing.JLabel lProductoCategoria;
    private javax.swing.JLabel lProductoCategoria1;
    private javax.swing.JLabel lProductoCodigo;
    private javax.swing.JLabel lProductoCodigo1;
    private javax.swing.JLabel lProductoDescripcionLarga;
    private javax.swing.JLabel lProductoDescripcionLarga1;
    private javax.swing.JLabel lProductoDetallesTecnicos;
    private javax.swing.JLabel lProductoDetallesTecnicos1;
    private javax.swing.JLabel lProductoDivision;
    private javax.swing.JLabel lProductoExistencia;
    private javax.swing.JLabel lProductoExistencia1;
    private javax.swing.JLabel lProductoExistencia2;
    private javax.swing.JLabel lProductoExistencia3;
    private javax.swing.JLabel lProductoExistencia4;
    private javax.swing.JLabel lProductoExistencia5;
    private javax.swing.JLabel lProductoMarca;
    private javax.swing.JLabel lProductoMarca1;
    private javax.swing.JLabel lProductoPrecio;
    private javax.swing.JLabel lProductoPrecio1;
    private javax.swing.JLabel lProductoPrecio2;
    private javax.swing.JLabel lProductoPrecio3;
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
    private javax.swing.JPanel pConsultaProducto;
    private javax.swing.JPanel pConsultaProductos;
    private javax.swing.JPanel pDebug;
    private javax.swing.JPanel pPrestashop;
    private javax.swing.JPanel pWebsite;
    private javax.swing.JSeparator sProductoSeparador1;
    private javax.swing.JSeparator sProductoSeparador2;
    private javax.swing.JSeparator sProductoSeparador3;
    private javax.swing.JSeparator sProductoSeparador4;
    private javax.swing.JSeparator sProductoSeparador5;
    private javax.swing.JScrollPane spCPrestashopCargado;
    private javax.swing.JScrollPane spCPrestashopDefault;
    private javax.swing.JScrollPane spDebug;
    private javax.swing.JScrollPane spMaestroProductos;
    private javax.swing.JScrollPane spMaestroProductos1;
    private javax.swing.JScrollPane spPrestashop;
    private javax.swing.JScrollPane spProductoDescripcionLarga;
    private javax.swing.JScrollPane spProductoDescripcionLarga1;
    private javax.swing.JScrollPane spProductoDetallesTecnicos;
    private javax.swing.JScrollPane spProductoImagenes;
    private javax.swing.JScrollPane spProductoImagenes1;
    private javax.swing.JTextField tBASCSBD;
    private javax.swing.JPasswordField tBASCSClave;
    private javax.swing.JTextField tBASCSInstancia;
    private javax.swing.JTextField tBASCSPuerto;
    private javax.swing.JTextField tBASCSServidor;
    private javax.swing.JTextField tBASCSUsuario;
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
    private javax.swing.JTextField tMaestroCantidad1;
    private javax.swing.JTextField tPrestashopExportColumnas;
    private javax.swing.JTextField tPrestashopExportLineas;
    private javax.swing.JTextField tPrestashopFileExport;
    private javax.swing.JTextField tPrestashopPath;
    private javax.swing.JLabel tPrestashopWorkerEstado;
    private javax.swing.JTextField tProductoCategoria;
    private javax.swing.JTextField tProductoCategoria1;
    private javax.swing.JTextField tProductoDescripcion;
    private javax.swing.JTextField tProductoDescripcion1;
    private javax.swing.JTextField tProductoDivision;
    private javax.swing.JTextField tProductoEAN;
    private javax.swing.JTextField tProductoEAN1;
    private javax.swing.JTextField tProductoEnvioImporte;
    private javax.swing.JTextField tProductoEstado;
    private javax.swing.JCheckBox tProductoExistencia;
    private javax.swing.JTextField tProductoFactorCosto;
    private javax.swing.JTextField tProductoFactorVenta;
    private javax.swing.JTextField tProductoID;
    private javax.swing.JTextField tProductoID1;
    private javax.swing.JTextField tProductoMarca;
    private javax.swing.JTextField tProductoMarca1;
    private javax.swing.JTextField tProductoMoneda;
    private javax.swing.JTextField tProductoPrecioCosto;
    private javax.swing.JTextField tProductoPrecioLista;
    private javax.swing.JTextField tProductoPrecioVenta;
    private javax.swing.JTextField tProductoPrecioVentaFinal;
    private javax.swing.JTextField tProductoStock;
    private javax.swing.JTextArea taDebug;
    private javax.swing.JTextArea taProductoDescripcionLarga;
    private javax.swing.JTextArea taProductoDescripcionLarga1;
    private javax.swing.JLabel taProductoImagen;
    private javax.swing.JLabel taProductoImagen1;
    private javax.swing.JTable tbCPrestashopCargado;
    private javax.swing.JTable tbCPrestashopDefault;
    private javax.swing.JTable tbMaestroProductos;
    private javax.swing.JTable tbMaestroProductos1;
    private javax.swing.JTable tbPrestashop;
    private javax.swing.JTable tbProductoDetallesTecnicos;
    private javax.swing.JTable tbProductoImagenes;
    private javax.swing.JTable tbProductoImagenes1;
    private javax.swing.JTabbedPane tpConfiguracion;
    private javax.swing.JTabbedPane tpConsulta;
    private javax.swing.JTabbedPane tpPrincipal;
    private javax.swing.JTabbedPane tpWebsite;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String clase = getClass().getName().substring(getClass().getName().lastIndexOf(".")+1, getClass().getName().length()).toUpperCase();
        String source = evt.getSource().toString().substring(evt.getSource().toString().lastIndexOf(".")+1, evt.getSource().toString().indexOf("@"));
        String value = evt.getNewValue().toString();
        evt.setPropagationId(clase);
        
        //System.out.println(clase+">> "+source+" > "+value);
        
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
