/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

import bascs.website.clases.RubroSC;
import etl.bascs.impala.main;
import etl.bascs.victoria.clases.CuotasVictoriaWorker;
import etl.bascs.victoria.clases.ProductoVictoriaWorker;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author User
 */
public class ProductoVictoria {

    private String codigo;
    private String codigoAlternativo;
    private String descripcion;
    private String nombre;
    private String marca;
    private Integer precio_contado;
    private Integer marca_id;
    private Integer stock;
    private Integer producto_id;
    public Boolean cargado = false;

    public List<ProductoCuotasVictoria> cuotas;

    private RubroVictoria rubroVictoria;
    private RubroSC rubroSC;

    private MarcaVictoria marcaVictoria;
    private MarcaSC marcaSC;
    private Boolean bandera = false;
    public ProductoVictoria() {
    }

    public void loadJSONDetalle(JSONObject productoJ) {
        try {
            ProductoCuotasVictoria cuota;

            setCodigo((getCodigo() == null ? productoJ.optString("codigo_interno_ws") : getCodigo()));
            setCodigoAlternativo((getCodigoAlternativo() == null ? productoJ.optString("codigoAlternativo") : getCodigoAlternativo()));
            setNombre((getNombre() == null ? productoJ.optString("nombre") : getNombre()));
            setDescripcion((getDescripcion() == null ? productoJ.optString("descripcion") : getDescripcion()));
            setMarcaVictoria(new MarcaVictoria(productoJ.getJSONObject("marca")));
            setRubroVictoria(new RubroVictoria(productoJ.getJSONObject("rubro")));

            setPrecio_contado((getPrecio_contado() == null ? productoJ.optInt("precio") : getPrecio_contado()));

            cuotas = new ArrayList<>();
            if (productoJ.has("cuotas")) {
                bandera = true;
                System.out.println("BANDERAita " + bandera);
                JSONArray cuotaJ = productoJ.optJSONArray("cuotas");
                
                for (int i = 0; i < cuotaJ.length(); i++) {
                    cuota = new ProductoCuotasVictoria(
                            cuotaJ.optJSONObject(i).getInt("numero"),
                            cuotaJ.optJSONObject(i).getInt("precio_cuota"),
                            cuotaJ.optJSONObject(i).getInt("precio_contado"),
                            cuotaJ.optJSONObject(i).getInt("precio_credito"),
                            cuotaJ.optJSONObject(i).getInt("posee_descuento"),
                            cuotaJ.optJSONObject(i).getInt("porcentaje_descuento"));
                    cuotas.add(cuota);
                    //System.out.println("CUOTAS " + cuotas[i]);
                }

            }

            cargado = true;
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loadJSONMaestro(JSONObject productoJ) {
        try {
            setCodigo(productoJ.optString("codigo_interno_ws"));
            setCodigoAlternativo(productoJ.optString("codigoAlternativo"));
            //System.out.println("CODIGO DEL MAESTRO "  + productoJ.optString("codigo_interno_ws"));
            setNombre(productoJ.optString("nombre"));
            setDescripcion(productoJ.optString("descripcion"));
            RubroVictoria rubVictoria = new RubroVictoria();
            rubVictoria.setCodigo(productoJ.optString("rubro"));
            setRubroVictoria(rubVictoria);
            MarcaVictoria marVictoria = new MarcaVictoria();
            marVictoria.setCodigo(productoJ.optString("marca"));
            setMarcaVictoria(marVictoria);
            setPrecio_contado(productoJ.optInt("precio"));
            /*
            RubroSC rubroSC = new RubroSC();
            rubroSC.setId(productoJ.optInt("rubro"));
            setRubroSC(rubroSC);
             */
            cargado = true;
            //       System.out.println(" " + cargado);
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public Integer getProducto_id() {
        return producto_id;
    }

    public void setProducto_id(Integer producto_id) {
        this.producto_id = producto_id;
    }

    public RubroVictoria getRubroVictoria() {
        return rubroVictoria;
    }

    public void setRubroVictoria(RubroVictoria rubroVictoria) {
        this.rubroVictoria = rubroVictoria;
    }

    public RubroSC getRubroSC() {
        return rubroSC;
    }

    public void setRubroSC(RubroSC rubroSC) {
        this.rubroSC = rubroSC;
    }

    public MarcaSC getMarcaSC() {
        return marcaSC;
    }

    public void setMarcaSC(MarcaSC marcaSC) {
        this.marcaSC = marcaSC;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = decodeUFT(codigo);
    }

    public MarcaVictoria getMarcaVictoria() {
        return marcaVictoria;
    }

    public void setMarcaVictoria(MarcaVictoria marcaVictoria) {
        this.marcaVictoria = marcaVictoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = decodeUFT(descripcion);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = decodeUFT(nombre);
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public List<ProductoCuotasVictoria> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<ProductoCuotasVictoria> cuotas) {
        this.cuotas = cuotas;
    }

    

    public Integer getPrecio_contado() {
        return precio_contado;
    }

    public void setPrecio_contado(Integer precio_contado) {
        this.precio_contado = precio_contado;
    }

    public Integer getMarca_id() {
        return marca_id;
    }

    public void setMarca_id(Integer marca_id) {
        this.marca_id = marca_id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCodigoAlternativo() {
        return codigoAlternativo;
    }

    public void setCodigoAlternativo(String codigoAlternativo) {
        this.codigoAlternativo = codigoAlternativo;
    }

    public String decodeUFT(String rawString) {
        if (rawString == null || rawString.isEmpty()) {
            return "";
        }

        String stringLegible = rawString;
        try {
            stringLegible = new String(rawString.getBytes("UTF-8"));

        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ProductoVictoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stringLegible;
    }

    public JSONObject getJSON() {
        JSONObject object;
        object = new JSONObject();

        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
        //    object.put("descripcion", getDescripcion());
        object.put("marca_id", getMarcaSC().getId());
//        System.out.println("MARCASC " + getMarcaSC().getId() + "RUBROSC " + getRubroSC().getId());
        object.put("rubro_id", getRubroSC().getId());
        object.put("precio", getPrecio_contado());
//        object.put("stock", "10");

        return object;
    }

}
