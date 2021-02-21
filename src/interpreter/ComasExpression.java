package interpreter;

/**
 *
 * @author Invitado
 */
public class ComasExpression implements IExpression{

    @Override
    public String interpret(Context context) {
        String in=context.getContenido();
        String out="";
        
        String[] elementos=in.split("\\.");
        
        for (String elemento : elementos) {
            out+=elemento+",";
        }
        out=out.substring(0, out.length()-1);
        
        return out;
    }
    
}
