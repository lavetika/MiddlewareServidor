/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.json.JSONObject;

/**
 *
 * @author Invitado
 */
public interface FramerJson{
    public byte[] nextMsgJson(InputStream in) throws IOException;
}
