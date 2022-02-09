/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.json;

/**
 *
 * @author User
 */
public class Website {

  private Integer id;
  private String codigo_interno_ws;
  private String nombre;
  private Integer precio;
  private Integer precio_oferta;
  private String descripcion;
  private Object marca;
  private Object rubro;

    public Website(Integer id, String codigo_interno_ws, String nombre, Object marca, Object rubro) {
        this.id = id;
        this.codigo_interno_ws = codigo_interno_ws;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.rubro = rubro;
    }

 
  
  
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo_interno_ws() {
        return codigo_interno_ws;
    }

    public void setCodigo_interno_ws(String codigo_interno_ws) {
        this.codigo_interno_ws = codigo_interno_ws;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getPrecio() {
        return precio;
    }

    public void setPrecio(Integer precio) {
        this.precio = precio;
    }

    public Integer getPrecio_oferta() {
        return precio_oferta;
    }

    public void setPrecio_oferta(Integer precio_oferta) {
        this.precio_oferta = precio_oferta;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Object getMarca() {
        return marca;
    }

    public void setMarca(Object marca) {
        this.marca = marca;
    }

    public Object getRubro() {
        return rubro;
    }

    public void setRubro(Object rubro) {
        this.rubro = rubro;
    }

  
  

    public static void main(String[] args) {
      }   
}
