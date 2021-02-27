package interpreter;

import com.google.gson.JsonObject;

/**
 *
 * @author Invitado
 */
public class JsonExpression implements IExpressionJson{

  @Override
    public String interpret(ContextJson context) {
        JsonObject in=context.getJson();
        String out="";
//        in.get("maestro").toString();
//        in.get("materia").toString();
//        in.get("alumno").toString();
//        in.get("calificacion").toString();
        
        out=in.get("maestro").toString()+"."+ in.get("materia").toString()+"."+
                in.get("alumno").toString()+"."+in.get("calificacion").toString();
        
        return out;
    }
    
}
