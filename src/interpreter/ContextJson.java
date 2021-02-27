
package interpreter;

import com.google.gson.JsonObject;

/**
 *
 * @author Diana Jiménez
 */
public class ContextJson {
    private JsonObject json;
    
    public ContextJson(JsonObject json) {
        this.json = json;
    }
    public JsonObject getJson() {
        return json;
    }
    
}


