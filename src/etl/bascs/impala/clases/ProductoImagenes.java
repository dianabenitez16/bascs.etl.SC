/*
 * @author junjuis
 */
package etl.bascs.impala.clases;

/**
    images: array – arreglo con las imágenes del producto
        id: string – identificador de la imagen
        url: string – nombre+extensión de la imagen del producto (concatenar al path de imágenes)
 */
public class ProductoImagenes {
    private Integer id;
    private String url;

    public ProductoImagenes(Integer id, String url) {
        this.id = id;
        this.url = url;
    }
    
    public ProductoImagenes(String id, String url) {
        try{
            this.id = Integer.valueOf(id);
            this.url = url;
        }catch (NumberFormatException ex){
            this.id = 10;
            this.url = id;
        }
        
    }

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
}
