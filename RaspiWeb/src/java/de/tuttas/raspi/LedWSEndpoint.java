/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import de.raspi.LEDControl;
import de.raspi.LEDValueChanged;
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
@ServerEndpoint("/ledpoint")
public class LedWSEndpoint  implements LEDValueChanged{

    Session session;
    LEDControl ledControl = LEDControl.getInstance(RaspiPin.GPIO_01, PinState.LOW);
    int oldDimValue = 0;

    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        ledControl.addListener(this);
        System.out.println(session.getId() + " has opened a connection");
        this.sendDimValue();
    }

    @OnClose
    public void onClose(Session session) {
        ledControl.removeListener(this);
        System.out.println("Session " + session.getId() + " has ended");
        session=null;
    }

    @OnMessage
    public String onMessage(String message) {
        System.out.println("Dim Websocket receive:" + message);
        ledControl.dim(Integer.parseInt(message));
        return null;
    }

    private void sendDimValue() {
        try {
            session.getBasicRemote().sendText(Integer.toString(ledControl.getDimValue()));
        } catch (IOException ex) {
            Logger.getLogger(LedWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void valueChanged(int v) {
        this.sendDimValue();
    }

   
}
