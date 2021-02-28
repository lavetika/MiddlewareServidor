package server;

import interpreter.ComasExpression;
import interpreter.Context;
import interpreter.TipoContexto;
import interpreter.IExpression;
import interpreter.PuntosExpression;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
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
public class ServidorAlumno implements FramerLength, FramerDelimiter, Runnable {

    private static final byte DELIMITADOR = '~';
    private static final int LONGITUD_ENTRADA = 25;
    private static final int LONGITUD_SALIDA = 15;
    private final ServerSocket socketAlumno;
    private final ServerSocket socketKardex;
    private Socket clienteKardex;
    private final IExpression interpreterKardex;
    private final IExpression interpreterAlumno;

    public ServidorAlumno(ServerSocket socketAlumno, ServerSocket socketKardex) {
        this.socketAlumno = socketAlumno;
        this.socketKardex = socketKardex;
        this.interpreterKardex = new PuntosExpression();
        this.interpreterAlumno = new ComasExpression();
    }

    protected void escuchar() {
        System.out.println("El servidor esta en linea.");
        System.out.println("----");
        Context context;
        byte[] bytes;
        try {
            clienteKardex = this.socketKardex.accept();
            ServidorMaestro sMaestro=new ServidorMaestro(new ServerSocket(9002), clienteKardex);
            new Thread(sMaestro).start();
            System.out.println("Se ha conectado el SistemaKardex");
            System.out.println("----");
            OutputStream outKardex = clienteKardex.getOutputStream();
            InputStream inKardex = clienteKardex.getInputStream();
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

                context = new Context(recibidoAlumno, TipoContexto.COMAS);

                //PARA EL SISTEMA KARDEX
                String paraKardex = interpreterKardex.interpret(context);

                //   outKardex.write(serializar(paraKardex));
                frameMsgDelimiter(serializar(paraKardex), outKardex);
                System.out.println("Se ha enviado " + paraKardex + " al Sistema Kardex");
                System.out.println("----");
                outKardex.flush();

                bytes = new byte[esperarDatos(inKardex)];
                inKardex.read(bytes);
                String recibidoKardex = deserializar(bytes);
                System.out.println("Recibido del Sistema Kardex");
                System.out.println("Sistema Kardex envía: " + recibidoKardex);
                System.out.println("----");

                context = new Context(recibidoKardex, TipoContexto.PUNTOS);
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

    public Socket getClienteKardex() {
        return clienteKardex;
    }

    @Override
    public void frameMsgDelimiter(byte[] mensaje, OutputStream out) throws IOException {
        for (byte b : mensaje) {
            if (b == DELIMITADOR) {
                throw new IOException("El mensaje contiene el delimitador.");
            }
        }
        out.write(mensaje);
        out.write(DELIMITADOR);
        out.flush();
    }

    @Override
    public byte[] nextMsgDelimiter(InputStream in) throws IOException {
        ByteArrayOutputStream msgBuffer = new ByteArrayOutputStream();
        int sigByte;

        while ((sigByte = in.read()) != DELIMITADOR) {
            if (sigByte == -1) {
                if (msgBuffer.size() == 0) {
                    return null;
                } else {
                    throw new IOException("Mensaje sin delimitador.");
                }
            }
            msgBuffer.write(sigByte);
        }
        return msgBuffer.toByteArray();
    }

    @Override
    public void frameMsgLength(byte[] mensaje, OutputStream out) throws IOException {
        if (mensaje.length != LONGITUD_SALIDA) {
            throw new IOException("El tamaño del mensaje no es de la longitud establecida: " + LONGITUD_SALIDA);
        }

        out.write(mensaje);
        out.flush();
    }

    @Override
    public byte[] nextMsgLength(InputStream in) throws IOException {
        byte[] salida = new byte[LONGITUD_ENTRADA];
        in.read(salida);
        return salida;
    }

    @Override
    public void run() {
        escuchar();
    }
}
