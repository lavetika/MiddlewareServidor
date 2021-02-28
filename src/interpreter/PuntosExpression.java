package interpreter;

import org.json.JSONObject;

/**
 *
 * @author Invitado
 */
public class PuntosExpression implements IExpression {

    @Override
    public String interpret(Context context) {
        String out = "";
        String in = context.getContenido();

        switch (context.getTipoContexto()) {
            case COMAS:

                String[] elementos = in.split(",");

                for (String elemento : elementos) {
                    out += elemento + ".";
                }

                out = out.substring(0, out.length() - 1);
                break;
            case JSON:
                JSONObject json = new JSONObject(in);
                
                out = json.getString("maestro") + "." + json.getString("materia")+ "."
                        + json.getString("alumno") + "." + json.getString("calificacion");
                break;
        }

        return out;
    }
}