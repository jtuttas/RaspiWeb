/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import de.raspi.BMP180;
import de.raspi.BMP180Value;
import de.raspi.BMP180ValueChangeListener;
import de.raspi.DS18B20;
import de.raspi.DS18B20ValueChangedListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TempWSEndpoint implements DS18B20ValueChangedListener {

    Session session;
    DS18B20 sensor;

    
    
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        try {
            sensor = new DS18B20("/sys/bus/w1/devices/28-000006369255/w1_slave");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TempWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        sensor.addListener(this);
        System.out.println("TempWSEndPoint " + session.getId() + " has opened a connection");
        send();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        this.session=null;
        sensor.removeListener(this);
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("message=" + message);
        return null;
    }

    public void send() {
        try {
            session.getBasicRemote().sendText(sensor.toJson(true));
        } catch (IOException ex) {
            Logger.getLogger(TempWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void valueChanged(double temp) {
        send();
    }
}
