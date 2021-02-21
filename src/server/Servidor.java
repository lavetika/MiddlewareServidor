package server;

import interpreter.ComasExpression;
import interpreter.Context;
import interpreter.IExpression;
import interpreter.PuntosExpression;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author Invitado
 */
public class Servidor {

    private final ServerSocket socketAlumno;
    private final ServerSocket socketKardex;
    private final IExpression interpreterKardex;
    private final IExpression interpreterAlumno;

    public Servidor(ServerSocket socketAlumno, ServerSocket socketKardex) {
        this.socketAlumno = socketAlumno;
        this.socketKardex = socketKardex;
        this.interpreterKardex = new PuntosExpression();
        this.interpreterAlumno = new ComasExpression();
        escuchar();
    }

    protected void escuchar() {
        Context context;
        byte[] bytes;
        try {
            Socket clienteKardex = this.socketKardex.accept();
            OutputStream outKardex=clienteKardex.getOutputStream();
            InputStream inKardex=clienteKardex.getInputStream();
            while (true) {
                Socket clienteAlumno = this.socketAlumno.accept();
                //PARA EL SISTEMA ALUMNO
                InputStream inAlumno = new BufferedInputStream(clienteAlumno.getInputStream());

                bytes = new byte[esperarDatos(inAlumno)];
                inAlumno.read(bytes);

                String recibidoAlumno = deserializar(bytes);
                System.out.println("Recibido del Sistema Alumno");
                System.out.println("Sistema Alumno envía: " + recibidoAlumno);
                System.out.println("----");

                context = new Context(recibidoAlumno);
                
                //PARA EL SISTEMA KARDEX
                String paraKardex = interpreterKardex.interpret(context);
                
                outKardex.write(serializar(paraKardex));
                System.out.println("Se ha enviado " + paraKardex + " al Sistema Kardex");
                System.out.println("----");
                outKardex.flush();

                bytes = new byte[esperarDatos(inKardex)];
                inKardex.read(bytes);
                String recibidoKardex = deserializar(bytes);
                System.out.println("Recibido del Sistema Kardex");
                System.out.println("Sistema Kardex envía: " + recibidoKardex);
                System.out.println("----");

                context = new Context(recibidoKardex);
                String paraAlumno = interpreterAlumno.interpret(context);
                OutputStream outAlumno = clienteAlumno.getOutputStream();
                outAlumno.write(serializar(paraAlumno));
                System.out.println("Se ha enviado " + paraAlumno + " al Sistema Alumno");
                System.out.println("----");
                
                outAlumno.flush();
                outAlumno.close();
                inAlumno.close();
                clienteAlumno.close();
            }

        } catch (Exception e) {
            System.out.println("Falla algo: " + e.getMessage());
        }
    }

    private byte[] serializar(String cadena) throws IOException {
        return cadena.getBytes();
    }

    private String deserializar(byte[] datos) throws IOException, ClassNotFoundException {
        return new String(datos, StandardCharsets.UTF_8);
    }

    private int esperarDatos(InputStream in) throws IOException {

        int tam;
        while ((tam = in.available()) == 0) {
            if (tam > 0) {
                break;
            }
        }
        return tam;
    }
}
