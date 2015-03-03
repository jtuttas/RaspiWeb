/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import de.raspi.BMP180;
import de.raspi.BMP180Value;
import de.raspi.BMP180ValueChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Init;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Jörg
 */
@ServerEndpoint("/temppoint")
public class TempWSEndpoint implements BMP180ValueChangeListener {

    Session session;
    BMP180 bmp180 = BMP180.getInstance(1, 0x77);
    BMP180Value lastValue;

    
    
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        bmp180.addListener(this);
        System.out.println("TempWSEndPoint " + session.getId() + " has opened a connection");
        if (lastValue != null) {
            send();
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        this.session=null;
        bmp180.removeListener(this);
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("message=" + message);
        return null;
    }

    public void send() {
        try {
            if (session != null) {
                session.getBasicRemote().sendText(lastValue.toJson(true));
                //System.out.println("Send " + lastValue.toJson(true));
            }
        } catch (IOException ex) {
            Logger.getLogger(TempWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void valueChanged(BMP180Value v) {
        lastValue = v;
        send();
    }
}
