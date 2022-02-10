/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;

import org.json.JSONArray;
import org.json.JSONObject;


public class ProductosVictoria {

    private String codigo;
    private String descripcion;
    private String rubro;
    private String marca;
    private JSONArray cuotas;

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

    public String getRubro() {
        return rubro;
    }

    public void setRubro(String rubro) {
        this.rubro = rubro;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public JSONArray getCuotas() {
        return cuotas;
    }

    public void setCuotas(JSONArray cuotas) {
        this.cuotas = cuotas;
    }
    
    
}
