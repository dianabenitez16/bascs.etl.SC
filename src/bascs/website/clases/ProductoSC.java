package bascs.website.clases;

import bascs.website.clases.CuotasSC;
import etl.bascs.impala.clases.MarcaSC;
import etl.bascs.impala.main;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author User
 */
public class ProductoSC {

    private Integer id;
    private String codigo;
    private String descripcion;
    private String nombre;
    private String marca;
    private Integer precio;
    private Integer stock;
    private Integer visible;
    private Integer habilitado;
    public Boolean cargado;

    private String rubro;
    private List<CuotasSC> cuotas;
    private MarcaSC marcaSC;
    private RubroSC rubroSC;
    private MarcaSC[] marcasSC;
    private RubroSC[] rubrosSC;

    public ProductoSC(Properties propiedades) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.marca = marca;
        this.rubro = rubro;
        this.cuotas = cuotas;

    }

    public ProductoSC() {
    }

    public void loadJSONConsulta(JSONObject productoJ) {
        try {
            //         setCodigo((getId() == null ? productoJ.optInt("id"):getId()));
            setId((getId() == null ? productoJ.optInt("id") : getId()));
            setCodigo((getCodigo() == null ? productoJ.optString("codigo_interno_ws") : getCodigo()));
            setNombre((getNombre() == null ? productoJ.optString("nombre") : getNombre()));
            setDescripcion((getDescripcion() == null ? productoJ.optString("descripcion") : getDescripcion()));
            setPrecio((getPrecio() == null ? productoJ.optInt("precio") : getPrecio()));
            setStock((getStock() == null ? productoJ.optInt("stock") : getStock()));
            RubroSC rubSC = new RubroSC();
            rubSC.setId(productoJ.optInt("rubro_id"));
            setRubroSC(rubSC);
            MarcaSC marSC = new MarcaSC();
            marSC.setId(productoJ.optInt("marca_id"));
            setMarcaSC(marSC);
            setVisible((getVisible() == null ? productoJ.optInt("visible") : getVisible()));
            setHabilitado((getHabilitado() == null ? productoJ.optInt("habilitado") : getHabilitado()));

            cargado = true;

        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void loadJSONMaestro(JSONObject productoJ) {
        try {
            setId(productoJ.optInt("id"));
            setCodigo(productoJ.optString("codigo_interno_ws"));
            setNombre(productoJ.optString("nombre"));
            setDescripcion(productoJ.optString("descripcion"));
            setStock(productoJ.optInt("stock"));
            setPrecio(productoJ.optInt("precio"));
            setMarcaSC(new MarcaSC(productoJ.getJSONObject("marca")));
            setRubroSC(new RubroSC(productoJ.getJSONObject("rubro")));
            setVisible(productoJ.optInt("visible"));
            setHabilitado(productoJ.optInt("habilitado"));

            cargado = true;
        } catch (JSONException e) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    public List<CuotasSC> getCuotas() {
        return cuotas;
    }

    public void setCuotas(List<CuotasSC> cuotas) {
        this.cuotas = cuotas;
    }

    public MarcaSC obtenerMarca(String codigo) {
        for (MarcaSC marcasSC1 : marcasSC) {
            if (marcasSC1.getCodigo().equals(codigo)) {

                return marcasSC1;
            }
        }
        return null;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public MarcaSC getMarcaSC() {
        return marcaSC;
    }

    public void setMarcaSC(MarcaSC marcaSC) {
        this.marcaSC = marcaSC;
    }

    public RubroSC getRubroSC() {
        return rubroSC;
    }

    public void setRubroSC(RubroSC rubroSC) {
        this.rubroSC = rubroSC;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Integer getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Integer habilitado) {
        this.habilitado = habilitado;
    }

    public JSONObject getJSON() {
        JSONObject object;
        object = new JSONObject();

        object.put("codigo_interno_ws", getCodigo());
        object.put("nombre", getNombre());
        //    object.put("descripcion", getDescripcion());
        object.put("marca_id", getMarcaSC().getId());
        object.put("rubro_id", getRubroSC().getId());
        object.put("precio", getPrecio());
        object.put("stock", getStock());
        object.put("habilitado", getHabilitado());
        object.put("visible", getVisible());

        // SE CREA UN JSON OBJECT PARA ACTUALIZAR LOS PRODUCTOS QUE HAN SIDO BUSCADOS DESDE EL PANEL 'DETALLES' DE LA WEBSITE
        return object;
    }

}
