package interpreter;

/**
 *
 * @author Invitado
 */
public class Context {
    private String contenido;
    private TipoContexto tipoContexto;

    public Context(String contenido, TipoContexto tipoContexto) {
        this.contenido = contenido;
        this.tipoContexto = tipoContexto;
    }
   
    
    public String getContenido() {
        return contenido;
    }

    public TipoContexto getTipoContexto() {
        return tipoContexto;
    }
}