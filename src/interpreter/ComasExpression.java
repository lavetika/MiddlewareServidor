package interpreter;

/**
 *
 * @author Invitado
 */
public class ComasExpression implements IExpression {

    @Override
    public String interpret(Context context) {
        String out = "";

        switch (context.getTipoContexto()) {
            case PUNTOS:
                String in = context.getContenido();

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
