/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.config;

import etl.bascs.impala.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author Juan Bogado
 */
public class Propiedades {
    
    private main main;
    private final String[] tablaHeaderPrestashopDefault = {"ID","Nombre","Precio impuestos incluidos","ID regla de impuestos","En oferta (0/1)","Valor del descuento","Porcentaje de descuento","Descuento desde (aaaa-mm-dd)","Descuento hasta (aaaa-mm-dd)","Referencia nº","N° de referencia proveedor","Marca","EAN13","UPC","Ecotasa","Anchura","Altura","Profundidad","Peso","Cantidad","Cantidad mínima","Nivel de stock bajo","Envíame un mensaje de correo electrónico cuando la cantidad esté por debajo de este nivel","Visible en","Coste adicional del envío","Unidad para el precio unitario","Resumen","Descripción","Etiquetas (x,y,z...)","Meta-título","Meta keywords","Meta descripción","URL reescrita","Disponible para pedidos (0 = No, 1 = Si)","Fecha de disponibilidad del producto","Fecha de creación del producto","URL's de las imágenes (x,y,z...)","Textos alternativos de imagen (x,y,z...)","Elimine las imágenes existentes (0 = No, 1 = Si)","Característica (Nombre:Valor:Posición:Personalizado)","Estado","Destacado"};

    public Propiedades(main main) {
        this.main = main;
        iniciarPropiedades();
    }
    
    private void iniciarPropiedades(){
        main.propGenerales = new Properties();
        main.propGenerales.setProperty("config","generales");
        cargarPropiedades(main.propGenerales,"config/generales.conf");
        main.appendMensaje("Se cargo archivo de configuraciones Generales");
        main.appendMensaje(main.propGenerales.toString());
        main.appendMensaje("");
        
        main.propVictoria = new Properties();
        main.propVictoria.setProperty("config","victoria");
        cargarPropiedades(main.propVictoria,"config/victoria.conf");
        main.appendMensaje("Se cargo archivo de configuraciones Victoria");
        main.appendMensaje(main.propVictoria.toString());
        main.appendMensaje("");
        
        main.propSC = new Properties();
        main.propSC.setProperty("config","SaraComercial");
        cargarPropiedades(main.propSC,"config/SaraComercial.conf");
        main.appendMensaje("Se cargo archivo de configuraciones de Sara Comercial");
        main.appendMensaje(main.propSC.toString());
        main.appendMensaje("");
        
        
        main.propImpala = new Properties();
        main.propImpala.setProperty("config","impala");
        cargarPropiedades(main.propImpala,"config/impala.conf");
        main.appendMensaje("Se cargo archivo de configuraciones Impala");
        main.appendMensaje(main.propImpala.toString());
        main.appendMensaje("");
        
        main.propJellyfish = new Properties();
        main.propJellyfish.setProperty("config","jellyfish");
        cargarPropiedades(main.propJellyfish,"config/jellyfish.conf");
        main.appendMensaje("Se cargo archivo de configuraciones Jellyfish");
        main.appendMensaje(main.propJellyfish.toString());
        main.appendMensaje("");
                
        main.tablaHeaderPrestashop = cargarLista("config/prestashop.conf");
        main.appendMensaje("Se cargo archivo de configuraciones Prestashop");
        main.appendMensaje("");
    }
    
    private void cargarPropiedades(Properties propiedades, String ruta){
        try {
            FileInputStream archivo = new FileInputStream(ruta);
            propiedades.load(archivo);
            archivo.close();
        } catch (FileNotFoundException ex) {
            main.appendMensaje("No existe archivo de configuracion.");
            generarPropiedades(propiedades, ruta);
        } catch (IOException ex) {
            main.appendMensaje("Error al acceder al archivo de configuracion.");
            generarPropiedades(propiedades, ruta);
        } catch (NullPointerException ex) {
            main.appendMensaje("Archivo de configuracion vacio.");
            generarPropiedades(propiedades, ruta);
        } catch (Exception ex) {
            main.appendMensaje("Error desconocido al cargar propiedades.");
        }
    }
    
    private void generarPropiedades(Properties propiedades, String ruta){
        switch (propiedades.getProperty("config")) {
            case "generales":
                propiedades.setProperty("envios_sumar", "TRUE");
                propiedades.setProperty("envios_importe", "15000");
                propiedades.setProperty("envios_desde", "0");
                propiedades.setProperty("envios_hasta", "499999");
                break;
            case "bascs":
                propiedades.setProperty("servidor", "192.168.25.5");
                propiedades.setProperty("puerto", "1433");
                propiedades.setProperty("instancia", "");
                propiedades.setProperty("db", "SARA_COMERCIAL");
                propiedades.setProperty("usuario", "ws");
                propiedades.setProperty("clave", "ws");
                break;
            case "victoria":
                propiedades.setProperty("servidor", "192.168.192.60");
                propiedades.setProperty("puerto", "8080");
                propiedades.setProperty("metodoGET", "GET");
                propiedades.setProperty("rubros", "/WS/webapi/victoria/rubros");
                propiedades.setProperty("marcas", "/WS/webapi/victoria/marcas");
                propiedades.setProperty("productos", "/WS/webapi/victoria/productos");
                propiedades.setProperty("marcas", "/WS/webapi/victoria/marcas");
                propiedades.setProperty("detalle", "/WS/webapi/victoria/productos/");
                propiedades.setProperty("cuotas", "/WS/webapi/victoria/cuotas/");
                propiedades.setProperty("hilos", "200");
                propiedades.setProperty("stock", "10");
                break;
            case "SaraComercial":
                propiedades.setProperty("servidor", "portal.saracomercial.com");
                propiedades.setProperty("metodoGET", "GET");
                propiedades.setProperty("metodoPOST", "POST");
                propiedades.setProperty("rubros", "/api/loader/rubros");
                propiedades.setProperty("marcas", "/api/loader/marcas");
                propiedades.setProperty("productos", "/api/loader/productos");
                propiedades.setProperty("detalle", "/api/loader/productos/");
                propiedades.setProperty("clave", "Bearer 3|GJSYrWbgAtXlJ7COevCZgC9ecCFhCNAtmCujZ8RE");
                propiedades.setProperty("pages", "1,2,3");
                break;
            case "prestashop":
                propiedades.setProperty("servidor", "192.168.25.5");
                propiedades.setProperty("puerto", "1433");
                propiedades.setProperty("instancia", "");
                propiedades.setProperty("db", "SARA_COMERCIAL");
                propiedades.setProperty("usuario", "ws");
                propiedades.setProperty("clave", "ws");
                break;
            case "impala":
                propiedades.setProperty("servidor", "181.40.23.235");
                propiedades.setProperty("puerto", "8099");
                propiedades.setProperty("metodo", "POST");  //Tradicional%
                propiedades.setProperty("cliente", "Ang0ras4");
                propiedades.setProperty("usuario", "warthog");
                propiedades.setProperty("clave", "joez.mapa");
                propiedades.setProperty("detalle", "/impala/public/products/info");
                propiedades.setProperty("maestro", "/impala/public/products/master");
                propiedades.setProperty("imagenes", "/files/imagenes/productos/");
                propiedades.setProperty("hilos", "10");
                propiedades.setProperty("descuento", "0.873");
                propiedades.setProperty("recargo", "1.16");
                break;
            case "jellyfish":
                propiedades.setProperty("servidor", "181.40.23.235");
                propiedades.setProperty("puerto", "8199");
                propiedades.setProperty("metodo", "POST");  //Tradicional%
                propiedades.setProperty("cliente", "Boh1s5b5");
                propiedades.setProperty("usuario", "shr1mp15");
                propiedades.setProperty("clave", "dp5p0bwj5vt");
                propiedades.setProperty("detalle", "/jellyfish/public/products/info");
                propiedades.setProperty("maestro", "/jellyfish/public/products/master");
                propiedades.setProperty("imagenes", "/files/imagenes/productos/");
                propiedades.setProperty("hilos", "10");
                propiedades.setProperty("descuento", "0.873");
                propiedades.setProperty("recargo", "1.16");
                break;
            default:
                break;
        }
        
        File carpeta = new File("config/");
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                main.appendMensaje("Se crea directorio de configuraciones.");
            } else {
                main.appendMensaje("Error al crear carpeta de configuraciones.");
            }
        }
        
         
        try {
            FileOutputStream archivo;
            archivo = new FileOutputStream(ruta);
            propiedades.store(archivo, null);
            archivo.close();
            main.appendMensaje("Se crea archivo de configuraciones: "+propiedades.getProperty("config"));
        } catch (FileNotFoundException ex) {
            main.appendMensaje("Error al crear archivo de configuracion.");
        } catch (IOException ex) {
            main.appendMensaje("Error al crear archivo de configuracion.");
        }
            
    }
    
    private String[] cargarLista(String ruta){
        String[] lista = null;
        try {
            Boolean configValido = true;
            String configInvalida = "";
            FileReader fileReader = new FileReader(ruta);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<>();
            String line = null;
            //VALIDAR LINEA CON LINEAS POR DEFECTO
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
                if(Arrays.asList(tablaHeaderPrestashopDefault).indexOf(line) < 0){
                    configValido = false;
                    configInvalida += (configInvalida.isEmpty()?line:", "+line);
                }
            }
            
            if(!configValido){
                main.appendMensaje("Linea de prestashop incorrecta: "+configInvalida);
            }
            bufferedReader.close();
            lista = lines.toArray(new String[lines.size()]);
        } catch (FileNotFoundException ex) {
            main.appendMensaje("No existe archivo de lista.");
            generarLista("prestashop", ruta);
        } catch (IOException ex) {
            main.appendMensaje("Error al acceder al archivo de lista.");
            generarLista("prestashop", ruta);
        } catch (NullPointerException ex) {
            main.appendMensaje("Archivo de lista vacio.");
            generarLista("prestashop", ruta);
        } catch (Exception ex) {
            main.appendMensaje("Error desconocido al cargar lista.");
        }
        return lista;
    }
    
    private void generarLista(String lista, String ruta){
        String[] contenido;
        switch (lista) {
            case "prestashop":
                contenido = tablaHeaderPrestashopDefault;
                break;
            default:
                contenido = new String[]{""};
                break;
        }
        
        File carpeta = new File("config/");
        if (!carpeta.exists()) {
            if (carpeta.mkdirs()) {
                main.appendMensaje("Se crea directorio de configuraciones.");
            } else {
                main.appendMensaje("Error al crear carpeta de configuraciones.");
            }
        }
        
         
        try {
            FileWriter writer = new FileWriter(ruta); 
            for (String string : contenido) {
                writer.write(string+"\n");
            }
            writer.flush();
            main.appendMensaje("Se crea archivo de lista: "+lista);
        } catch (FileNotFoundException ex) {
            main.appendMensaje("Error al crear archivo de configuracion.");
        } catch (IOException ex) {
            main.appendMensaje("Error al crear archivo de configuracion.");
        }
    }

    public String[] getTablaHeaderPrestashopDefault() {
        return tablaHeaderPrestashopDefault;
    }
    
    
}
