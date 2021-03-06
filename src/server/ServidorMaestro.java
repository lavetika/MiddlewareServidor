package server;

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
public class ServidorMaestro implements FramerJson, FramerDelimiter, Runnable {

    private static final byte DELIMITADOR = '~';
    private final ServerSocket socketMaestro;
    private final Socket clienteKardex;
    private final IExpression interpreterKardex;

    public ServidorMaestro(ServerSocket socketMaestro, Socket clienteKardex) {
        this.socketMaestro = socketMaestro;
        this.clienteKardex = clienteKardex;
        this.interpreterKardex = new PuntosExpression();
    }

    protected void escuchar() {
        System.out.println("Se está escuchando al sistema maestro.");
        System.out.println("----");
        Context context;
        try {
            OutputStream outKardex = clienteKardex.getOutputStream();
            while (true) {
                Socket clienteMaestro = this.socketMaestro.accept();
                System.out.println("Se ha conectado el Sistema Maestro");
                //PARA EL SISTEMA MAESTRO
                InputStream inMaestro = new BufferedInputStream(clienteMaestro.getInputStream());

                String recibidoMaestro = deserializar(nextMsgJson(inMaestro));

                context = new Context(recibidoMaestro, TipoContexto.JSON);

                System.out.println("Recibido del Sistema Maestro");
                System.out.println("Sistema Maestro envía: " + recibidoMaestro);
                System.out.println("----");

                //PARA EL SISTEMA KARDEX
                String paraKardex = "maestro." + interpreterKardex.interpret(context);

                frameMsgDelimiter(serializar(paraKardex), outKardex);
                System.out.println("Se ha enviado " + paraKardex + " al Sistema Kardex");
                System.out.println("----");

                inMaestro.close();
                clienteMaestro.close();
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
    public byte[] nextMsgJson(InputStream in) throws IOException {
        byte[] bytes = new byte[esperarDatos(in)];
        in.read(bytes);
        return bytes;
    }

    @Override
    public void run() {
        escuchar();
    }
}
