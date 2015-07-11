/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import de.raspi.DS180;
import de.raspi.DS180ValueChanged;
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
@ServerEndpoint("/ds180")
public class DS180WSEndpoint implements DS180ValueChanged{

    Session session;
    private DS180 ds180; 
    
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        try {
            // TODO anpassen
            //ds180 = new DS180("c:\\\\Temp\\pi.txt");
            ds180 = new DS180("/sys/bus/w1/devices/28-000006369255/w1_slave");
            ds180.setListener(this);
            System.out.println("ds180 initialisiert");
            ds180.start();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DS180WSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(session.getId() + " has opened a connection");
    }

    @OnClose
     public void onClose(Session session) {
         if (ds180!=null) {
             ds180.stop();
             try {
                 ds180.close();
             } catch (IOException ex) {
                 Logger.getLogger(DS180WSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
     }
     
    @Override
    public void valueChanged(double temp) {
        try {
            System.out.println("Value Changed:"+temp);
            session.getBasicRemote().sendText(Double.toString(temp));
        } catch (IOException ex) {
            Logger.getLogger(LedWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
