package interpreter;

/**
 *
 * @author Invitado
 */
public class ComasExpression implements IExpression {

    @Override
    public String interpret(Context context) {
        String out = "";
        String in = context.getContenido();

        switch (context.getTipoContexto()) {
            case PUNTOS:
                String[] elementos = in.split("\\.");

                for (String elemento : elementos) {
                    out += elemento + ",";
                }
                out = out.substring(0, out.length() - 1);
                break;
        }
        return out;
    }

}
