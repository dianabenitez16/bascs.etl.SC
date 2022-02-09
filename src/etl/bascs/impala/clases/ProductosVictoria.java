/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etl.bascs.impala.clases;


public class ProductosVictoria {

	private String codigo;
	private int cuotas;
	private String descripcion;
	private String descripcionlarga;
	private int precio_contado;
	private int precio_cuota;
	private int precio_credito;
	
	
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
	public int getCuotas() {
		return cuotas;
	}
	public void setCuotas(int cuotas) {
		this.cuotas = cuotas;
	}
	public String getDescripcionlarga() {
		return descripcionlarga;
	}
	public void setDescripcionlarga(String descripcionlarga) {
		this.descripcionlarga = descripcionlarga;
	}
	public int getPrecio_contado() {
		return precio_contado;
	}
	public void setPrecio_contado(int precio_contado) {
		this.precio_contado = precio_contado;
	}
	public int getPrecio_cuota() {
		return precio_cuota;
	}
	public void setPrecio_cuota(int precio_cuota) {
		this.precio_cuota = precio_cuota;
	}
	public int getPrecio_credito() {
		return precio_credito;
	}
	public void setPrecio_credito(int precio_credito) {
		this.precio_credito = precio_credito;
	}
	

}
