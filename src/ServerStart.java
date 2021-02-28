
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.ServidorAlumno;
/**
 *
 * @author Invitado
 */
public class ServerStart {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
           ServidorAlumno sAlumno = new ServidorAlumno(new ServerSocket(9000), new ServerSocket(9001));
           new Thread(sAlumno).run();
        } catch (Exception ex) {
            Logger.getLogger(ServerStart.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
