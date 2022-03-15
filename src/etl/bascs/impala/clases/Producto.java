/*
 * @author junjuis
 */
package etl.bascs.impala.clases;

import etl.bascs.impala.main;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
Descripción de campos
errorCode: integer – código de resultado, 0 es ÉXITO
errorText: string – descripción del resultado
data: object – objeto contenedor de los datos
    title: string – descripción general del producto
    brand: string – nombre de la marca del producto
    product_code: string – código largo del producto
    price: string – precio del producto
    currency: string – moneda
    existence: string – indica existencia del producto
    description: string – detalles del producto
    category: string – nombre de la categoría del producto
    division: string – nombre de la división del producto
    technical_details: array – arreglo con los detalles técnicos del producto
        atributo: string – nombre del atributo
        valor: string – valor del atributo
    images: array – arreglo con las imágenes del producto
        id: string – identificador de la imagen
        url: string – nombre+extensión de la imagen del producto (concatenar al path de imágenes)
*/


public class Producto {
    private Properties propiedades;
    
    private Integer id;
    private String codigo;
    private String codigoCorto;
    private String codigoEAN;
    private String descripcion;
    private String descripcionLarga;
    private String descripcionTecnica;
    private String marca;
    private String categoria;
    private String division;
    private Double precioLista;
    private Double precioCosto;
    private Double precioVenta;
    private Double precioVentaFinal;
    private Double precioDescuento;
    private Double factorCosto;
    private Double factorVenta;
    private Double enviosImporte;
    private Double enviosDesde;
    private Double enviosHasta;
    private Boolean enviosSumar;
    private String moneda;
    private Boolean existencia;
    private Integer stock;
    private Boolean oferta;
    private ProductoDetalleTecnico[] detallesTecnicos;
    private ProductoImagenes[] imagenes;
    
    public Boolean cargado;
    
    private Double dAncho;
    private Double dAlto;
    private Double dLargo;
    private Double dPeso;
    private Double dPesoMayor;
    private Boolean pesoRecalculado;

    public Producto(Properties propiedades) {
        this.id = null;
        this.codigo = null;
        this.codigoCorto = null;
        this.codigoEAN = null;
        this.descripcion = null;
        this.descripcionLarga = null;
        this.descripcionTecnica = null;
        this.marca = null;
        this.categoria = null;
        this.division = null;
        this.precioLista = null;
        this.moneda = null;
        this.existencia = null;
        this.stock = null;
        this.detallesTecnicos = null;
        this.imagenes = null;
        this.oferta = false;
        this.cargado = false;
        this.pesoRecalculado = false;
        
        loadPropiedades(propiedades);
    }
    
    
    public void loadJSONConsulta(JSONObject productoJ){
        try {
            setCodigo((getCodigo() == null ? productoJ.optString("product_code"):getCodigo()));
            setCodigoCorto((getCodigoCorto() == null ? productoJ.optString("short_code") : getCodigoCorto()));
            // Se rediseña este tipo de linea, para considerar que previamente se le haya asignado un valor, desdel el maestro por ejemplo
            //setCodigoEAN((getCodigoEAN() == null ? productoJ.optString("ean") : getCodigoEAN()));
            setCodigoEAN((getCodigoEAN() == null ? productoJ.optString("ean") : (getCodigoEAN().isEmpty() ? productoJ.optString("ean") : getCodigoEAN())));
            setDescripcion((getDescripcion() == null ? productoJ.optString("title") : getDescripcion()));
            // Se rediseña este tipo de linea, para considerar que previamente se le haya asignado un valor, desdel el maestro por ejemplo
            setDescripcionLarga((getDescripcionLarga() == null ? productoJ.optString("description") : (getDescripcionLarga().isEmpty() ? productoJ.optString("description") : getDescripcionLarga())));
            setMarca((getMarca() == null ? productoJ.optString("brand") : getMarca()));
            setCategoria((getCategoria() == null ? productoJ.optString("category") : getCategoria()));
            setDivision((getDivision() == null ? productoJ.optString("division") : getDivision()));
            // Se rediseña este tipo de linea, para considerar que previamente se le haya asignado un valor, desdel el maestro por ejemplo            
            //setPrecioLista((getPrecioLista() == null ? productoJ.optInt("price") : getPrecioLista())); 
            setPrecioLista((getPrecioLista() == null ? productoJ.optInt("price") : (getPrecioLista() == 0 ? productoJ.optInt("price") : getPrecioLista())));
            setMoneda((getMoneda() == null ? productoJ.optString("currency") : getMoneda()));
            setExistencia((getExistencia() == null ? productoJ.optBoolean("existence") : getExistencia()));
            // Se rediseña este tipo de linea, para considerar que previamente se le haya asignado un valor, desdel el maestro por ejemplo            
            //setStock((getStock() == null ? productoJ.optInt("stock") : getStock()));
            setStock((getStock() == null ? productoJ.optInt("stock") : (getStock() == 0 ? productoJ.optInt("stock") : getStock())));
            setPrecioCosto();
            setPrecioVenta();
            setPrecioFinal();
            
            detallesTecnicos = new ProductoDetalleTecnico[0];
            if(productoJ.has("technical_details")){
                JSONArray technicalDetails = productoJ.optJSONArray("technical_details");
                detallesTecnicos = new ProductoDetalleTecnico[technicalDetails.length()];
                for (int i = 0; i < detallesTecnicos.length; i++){
                    detallesTecnicos[i] = new ProductoDetalleTecnico(technicalDetails.optJSONObject(i).getString("atributo"), technicalDetails.optJSONObject(i).getString("valor"));
                }
            }
            procesarDetallesTecnicos();
            
            if(productoJ.has("images")){
                JSONArray images = productoJ.optJSONArray("images");
                imagenes = new ProductoImagenes[images.length()];
                for (int i = 0; i < imagenes.length; i++){
                    imagenes[i] = new ProductoImagenes(images.getJSONObject(i).optString("id"), images.getJSONObject(i).optString("url"));
                }
            }
            
            cargado = true;
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    public void loadJSONMaestro(JSONObject productoJ){
        try {
            setCodigo(productoJ.optString("product_code"));
            setCodigoCorto(productoJ.optString("short_code"));
            setCodigoEAN(productoJ.optString("ean"));
            setDescripcion(productoJ.optString("title"));
            setMarca(productoJ.optString("brand"));
            setCategoria(productoJ.optString("category"));
            setDivision(productoJ.optString("division"));
            setPrecioLista(productoJ.optDouble("price"));
            setExistencia(productoJ.optBoolean("existence"));
            String stock = productoJ.optString("stock");
            if(stock.isEmpty()){
                stock = "0";
            }else{
                stock = productoJ.optString("stock");
            }
            setStock(Integer.valueOf(stock));
            setPrecioCosto();
            setPrecioVenta();
            setPrecioFinal();
            
            cargado = true;
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    public void loadCSVPrestashop(String linea){
        String cvsSplitBy = ";";
        String[] productoLinea = linea.split(cvsSplitBy);
        //"0.Product ID";1.Imagen;2.Nombre;3.Referencia;4.Categoría;"5.Precio (imp. excl.)";"6.Precio (imp. incl.)";7.Cantidad;8.Estado;9.Posición

        try {
            Boolean existe = Integer.valueOf(productoLinea[7]) > 0;
            setId(Integer.valueOf(productoLinea[0]));
            setCodigo(productoLinea[3]);
            setDescripcion(productoLinea[2]);
            setCategoria(productoLinea[4]);
            setPrecioVenta(Double.valueOf(productoLinea[6]));
            setExistencia(existe);
            
            cargado = true;
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
        
    }
    
    
    
    private void loadPropiedades(Properties prop){
        this.propiedades = prop;
        enviosSumar = Boolean.valueOf(propiedades.getProperty("envios_sumar"));
        enviosImporte = Double.valueOf(propiedades.getProperty("envios_importe"));
        enviosDesde = Double.valueOf(propiedades.getProperty("envios_desde"));
        enviosHasta = Double.valueOf(propiedades.getProperty("envios_hasta"));
        
        factorCosto = Double.parseDouble(propiedades.getProperty("descuento"));
        factorVenta = Double.parseDouble(propiedades.getProperty("recargo"));

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo.replace("/", "");
    }
    
    
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if(descripcion == null || descripcion.isEmpty()) {
            this.descripcion = "";
        }else{
            this.descripcion = decodeUFT(descripcion);
        }
        
    }

    public String getDescripcionLarga() {
        return descripcionLarga;
    }
    
    public String getDescripcionLargaFormateada() {
        return descripcionLarga.replaceAll("(\\t|\\r?\\n)+", ", ");
    }

    public void setDescripcionLarga(String descripcionLarga) {
        if(descripcionLarga == null || descripcionLarga.isEmpty()) {
            this.descripcionLarga = "";
        }else{
            if(descripcionLarga.length() > 799){
                descripcionLarga = descripcionLarga.substring(0, 799);
            }
            this.descripcionLarga = decodeUFT(descripcionLarga);
        }
        //System.out.println(getCodigo()+": "+ descripcionLarga +" | "+this.descripcionLarga);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = decodeUFT(marca);
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = decodeUFT(categoria);
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = decodeUFT(division);
    }

    public Double getPrecioLista() {
        return precioLista;
    }
    
    public void setPrecioLista(Double precio) {
        this.precioLista = precio;
    }

    public void setPrecioCosto() {
        if(getPrecioLista() != null){
            this.precioCosto = getPrecioLista() * getFactorCosto();
        }else{
            this.precioCosto = 0.0;
        }
    }

    public void setPrecioVenta() {
        if(getPrecioCosto() != null){
            this.precioVenta = getPrecioCosto() * getFactorVenta();
        }else{
            this.precioVenta = 0.0;
        }
    }
    
    public void setPrecioVenta(Double precio) {
        if(precio != null){
            this.precioVenta = precio;
        }else{
            this.precioVenta = 0.0;
        }
    }
    
    private void setPrecioFinal(){
        if(enviosSumar){
            if(precioVenta >= enviosDesde && precioVenta <= enviosHasta){
                precioVentaFinal = precioVenta + enviosImporte;
            }else{
                precioVentaFinal = precioVenta;
                enviosSumar = false;
            }
        }else{
            precioVentaFinal = precioVenta;
        }
    }
    
    public Integer getPrecioVenta() {
        return Math.round(precioVenta.intValue()/1000)*1000;
    }
    
    public Double getPrecioCosto() {
        return precioCosto;
    }

    public Double getFactorCosto() {
        return factorCosto;
    }

    
    public Double getFactorVenta() {
        if(getDivision().equals("GASTRONOMIA Y EQUIPOS COMERCIALES")){
            factorVenta = 1.20;
        }
        // LADISLAO SOLICITA ESTA PARTICULARIDAD 17/06/2021
        if(getMarca().equals("IMUSA")){
            factorVenta = 1.30;
        }
        return factorVenta;
    }


    public Double getPrecioDescuento() {
        return precioDescuento;
    }

    public void setPrecioDescuento(Double precioDescuento) {
        this.precioDescuento = precioDescuento;
    }
    
    public Double getPorcentajeDescuento() {
        return precioVenta / precioDescuento;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public Boolean getExistencia() {
        return existencia;
    }

    public void setExistencia(Boolean existencia) {
        this.existencia = existencia;
    }

    public ProductoDetalleTecnico[] getDetallesTecnicos() {
        return detallesTecnicos;
    }
    
    public void procesarDetallesTecnicos(){
        dAncho = 0.0;
        dAlto = 0.0;
        dLargo = 0.0;
        dPeso = 0.0;
        dPesoMayor = 0.0;
        descripcionTecnica = "";
        
        for(ProductoDetalleTecnico pdt : getDetallesTecnicos()){
            if(!descripcionTecnica.equals("")){descripcionTecnica+="&";}
            descripcionTecnica += primeraLetraMayuscula(pdt.getAtributo())+":"+pdt.getValor().trim();
            switch(pdt.getAtributo()){
                case "ANCHO":
                    dAncho = convertirDouble(pdt.getValor());
                    break;
                case "ALTO":
                    dAlto = convertirDouble(pdt.getValor());
                    break;
                case "LARGO":
                    dLargo = convertirDouble(pdt.getValor());
                    break;
                case "PESO":
                    dPeso = convertirDouble(pdt.getValor());
                    dPesoMayor = getPesoMayor(dAncho,dAlto,dLargo,dPeso);
                    if(dPesoMayor > dPeso){
                        pesoRecalculado = true;
                    }
                    break;
                default:
                    //System.out.println("Detalle técnico no detectado: "+pdt.getAtributo());
                    break;
            }
        }
    }
    
    public String getDetalleTecnicoFormateado(String detalle){
        String resultado = "";
        switch(detalle){
            case "ANCHO":
                resultado = String.format("%.2f", convertirDouble(dAncho.toString()));
                break;
            case "ALTO":
                resultado = String.format("%.2f", convertirDouble(dAncho.toString()));
                break;
            case "LARGO":
                resultado = String.format("%.2f", convertirDouble(dAncho.toString()));
                break;
            case "PESO":
                resultado = String.format("%f", dPeso);
                //210323 - Se cambia decimales por enteros para el peso
                break;
            case "PESOMAYOR":
                resultado = String.format("%f", dPesoMayor);
                //210323 - Se cambia decimales por enteros para el peso
                break;
            default:
                break;
        }
        return resultado;
    }

    public String getDescripcionTecnica() {
        return descripcionTecnica;
    }
    
    

    public Boolean getPesoRecalculado() {
        return pesoRecalculado;
    }

    public void setPesoRecalculado(Boolean pesoRecalculado) {
        this.pesoRecalculado = pesoRecalculado;
    }

    
    
    public void setDetallesTecnicos(ProductoDetalleTecnico[] detallesTecnicos) {
        this.detallesTecnicos = detallesTecnicos;
    }

    public ProductoImagenes[] getImagenes() {
        return imagenes;
    }
    
    public String getImagenesURL(String pathImagenes){
        String urls = "";
        String[] extensiones = {"jpg","jpeg","png"};
        if(getImagenes() != null) {
            if(getImagenes().length > 0){
                for(ProductoImagenes img : getImagenes()){
                    if(!urls.equals("")){
                        urls += "&";
                    }
                    for (String extension : extensiones) {
                        if(img.getUrl().toLowerCase().contains(extension) && !img.getUrl().toLowerCase().contains("\\")){
                            urls += pathImagenes+img.getUrl();
                        }
                    }
                }
            }
        }
        return urls;
    }
    
    public String getCaracteristicas(){
        // SUMAR GET FICHA TECNICOA
        return "Marca:"+getMarca()+"&Producto:"+getCategoria()+"&Segmento:"+getDivision()+"&Vendedor:Sara Comercial"; //"Caracteristicas",
    }

    public void setImagenes(ProductoImagenes[] imagenes) {
        this.imagenes = imagenes;
    }

    public String getCodigoCorto() {
        return codigoCorto;
    }

    public void setCodigoCorto(String codigoCorto) {
        this.codigoCorto = codigoCorto;
    }

    public String getCodigoEAN() {
        if(codigoEAN != null) {
            if(!codigoEAN.isEmpty()){
                codigoEAN = codigoEAN.replace("-", "");
                if(isNumeric(codigoEAN)){
                    if(codigoEAN.length() > 10 && codigoEAN.length() < 14){
                        return codigoEAN;
                    }
                    System.out.println("Codigo EAN inválido. "+getCodigo()+": "+codigoEAN);
                }
                
            }
        }
        return "";
    }

    public void setCodigoEAN(String codigoEAN) {
        this.codigoEAN = codigoEAN;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Boolean getCargado() {
        return cargado;
    }

    public void setCargado(Boolean cargado) {
        this.cargado = cargado;
    }

    public Integer getOferta() {
        return (oferta?1:0);
    }

    public void setOferta(Boolean oferta) {
        this.oferta = oferta;
    }

    public Integer getPrecioVentaFinal() {
        return Math.round(precioVentaFinal.intValue()/1000)*1000;
    }

    public void setPrecioVentaFinal(Double precioVentaFinal) {
        this.precioVentaFinal = precioVentaFinal;
    }

    public Double getEnviosImporte() {
        return enviosImporte;
    }

    public void setEnviosImporte(Double enviosImporte) {
        this.enviosImporte = enviosImporte;
    }

    public Double getEnviosDesde() {
        return enviosDesde;
    }

    public void setEnviosDesde(Double enviosDesde) {
        this.enviosDesde = enviosDesde;
    }

    public Double getEnviosHasta() {
        return enviosHasta;
    }

    public void setEnviosHasta(Double enviosHasta) {
        this.enviosHasta = enviosHasta;
    }

    public Boolean getEnviosSumar() {
        return enviosSumar;
    }

    public void setEnviosSumar(Boolean enviosSumar) {
        this.enviosSumar = enviosSumar;
    }
    
    
    
    
    /************************************/
    
    public String getDescripcionFormateada() {
        if(descripcion == null || descripcion.isEmpty()) {
            return descripcion;
        }
                       
        String descripcionMinus = descripcion.toLowerCase();
        String marcaMinus = marca.toLowerCase();
        String descripcionFLU;
        
        if(descripcionMinus.contains(marcaMinus)){
            descripcionMinus = descripcionMinus.replace(marcaMinus, primeraLetraMayuscula(marca.toLowerCase()));
        }

        descripcionFLU = escapeEspecial(primeraLetraMayuscula(descripcionMinus));
        
        return descripcionFLU;
    }
    
    public String decodeUFT(String rawString){
        if(rawString == null || rawString.isEmpty()) {
            return "";
        }
        
        String stringLegible = rawString;
        try {
            stringLegible = new String(rawString.getBytes("ISO-8859-1"), "UTF-8");
                       
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Producto.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringLegible;
    }
    
    /*
    Backspace is replaced with \b
    Newline is replaced with \n
    Tab is replaced with \t
    Carriage return is replaced with \r
    Form feed is replaced with \f
    Double quote is replaced with \"
    Backslash is replaced with \\
    */
    
    public String escapeEspecial(String texto){
        String escapado;
        escapado = texto.replace("\b", "");
        escapado = escapado.replace("\n", "");
        escapado = escapado.replace("\t", "");
        escapado = escapado.replace("\r", "");
        escapado = escapado.replace("\f", "");
        escapado = escapado.replace("\"", "");
        escapado = escapado.replace("\\", "");
        
        return escapado;
    }
    
    //Primera Letra Mayuscula
    public String primeraLetraMayuscula(String texto){ 
        texto = texto.trim();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }
       
    public static Double getPesoMayor(Double ancho, Double alto, Double largo, Double peso){
        Double dPesoVolumetrico = ancho * alto * largo / 4000;
        
        if(dPesoVolumetrico > peso){
            return dPesoVolumetrico;
        }else{
            return peso;
        }
    }
    
    public static Double convertirDouble(String cadena){
        
        String numeros = "0";
        char decimal;
        
        if(cadena.contains(".")){
            decimal = '.';
        }else{
            decimal = ',';
        }
        for(int i = 0; i< cadena.length(); i ++){
            if(cadena.charAt(i) == decimal){
                numeros = ""+numeros+".";
            }else{
                if(Character.isDigit(cadena.charAt(i))){
                    numeros = ""+numeros+cadena.charAt(i);
                }
            }
        }
        //System.out.println("STRING: "+cadena + "\t\t"+"DOUBLE: "+numeros);
        
        if(cadena.contains("CM")){
            return Double.valueOf(numeros);
        }else if(cadena.contains("GR")){
            return Double.valueOf(numeros)/1000;
        }else{
            //System.out.println("Verificar detalle tecnico: "+cadena + "\t\t"+"DOUBLE: "+numeros);
            return Double.valueOf(numeros);
        }
    }
    
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
