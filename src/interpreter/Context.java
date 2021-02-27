package interpreter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 *
 * @author Invitado
 */
public class Context {
    private String contenido;
    private JsonObject json;
    
    public Context(String contenido) {
        this.contenido = contenido;
    }

    public Context(JsonObject json) {
        this.json = json;
    }
    
    public String getContenido() {
        return contenido;
    }
    
    public JsonObject getJson() {
        return json;
    }
}