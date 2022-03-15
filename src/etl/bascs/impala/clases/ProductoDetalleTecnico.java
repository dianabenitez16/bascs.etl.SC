/*
 * @author junjuis
 */
package etl.bascs.impala.clases;

/**
 technical_details: array – arreglo con los detalles técnicos del producto
    atributo: string – nombre del atributo
    valor: string – valor del atributo
 */
public class ProductoDetalleTecnico {
    private String atributo;
    private String valor;

    public ProductoDetalleTecnico(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
    }

    public String getAtributo() {
        return atributo;
    }

    public void setAtributo(String atributo) {
        this.atributo = atributo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }
    
    
}
