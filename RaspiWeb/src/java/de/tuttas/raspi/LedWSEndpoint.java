/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.raspi.LEDControl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import sun.awt.windows.ThemeReader;

/**
 *
 * @author Jörg
 */
@ServerEndpoint("/ledpoint")
public class LedWSEndpoint implements Runnable {

    ArrayList<Session> sessions = new ArrayList<>();
    Thread runner = new Thread(this);
    Boolean running = false;
    LEDControl ledControl = LEDControl.getInstance(RaspiPin.GPIO_01, PinState.LOW);
    int oldDimValue = 0;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        if (!running) {
            running = true;
            runner.start();
        }
        System.out.println(session.getId() + " has opened a connection");
        this.sendDimValue(session);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Session " + session.getId() + " has ended");
        sessions.remove(session);
        if (sessions.size() == 0) {
            running = false;
        }
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("Dim Websocket receive:" + message);
        ledControl.dim(Integer.parseInt(message));
        return null;
    }

    @Override
    public void run() {
        //TODO auto generated Method
        while (running) {
             
            if (oldDimValue != ledControl.getDimValue()) {
               // System.out.println("Dim Value changed from " + oldDimValue + " to " + ledControl.getDimValue());
                oldDimValue = ledControl.getDimValue();

                for (Session s : sessions) {
                    sendDimValue(s);
                }
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
    }

    private void sendDimValue(Session s) {
        try {
            s.getBasicRemote().sendText(Integer.toString(ledControl.getDimValue()));
        } catch (IOException ex) {
            Logger.getLogger(LedWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
