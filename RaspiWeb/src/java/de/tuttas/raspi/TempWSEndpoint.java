/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import de.raspi.Config;
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
            sensor = new DS18B20(Config.SensorAdr);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TempWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        sensor.addListener(this);
        sensor.start();
        System.out.println("TempWSEndPoint " + session.getId() + " has opened a connection");
        send();
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        this.session=null;
        sensor.removeListener(this);
        sensor.stop();
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
        System.out.println("TempWSEndpoint: value Changed"+temp);
        send();
    }
}
