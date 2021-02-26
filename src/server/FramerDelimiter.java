/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Invitado
 */
public interface FramerDelimiter {

    public void frameMsgDelimiter(byte[] message, OutputStream out) throws IOException;

    public byte[] nextMsgDelimiter(InputStream in) throws IOException;
}
