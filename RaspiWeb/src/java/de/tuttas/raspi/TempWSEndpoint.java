/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

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
public class TempWSEndpoint implements Runnable{

     private Session s;
     Thread runner = new Thread(this);
     private double temp=40;
     private double offset=0.5;
    
     @OnOpen
    public void onOpen(Session session){
        s=session;
        System.out.println(session.getId() + " has opened a connection"); 
         send("10.0");
         runner.start();
    }
 
     @OnClose
    public void onClose(Session session){
        System.out.println("Session " +session.getId()+" has ended");
        s=null;
    }
    
    @OnMessage
    public String onMessage(String message) {
        System.out.println ("message="+message);
        return null;
    }

    public  void send(String msg) {
        try {
            if (s!=null) {
              s.getBasicRemote().sendText(msg);
              //System.out.println ("Nachricht ("+msg+")  gesendet an "+s.getId());
            }
        } catch (IOException ex) {
            Logger.getLogger(TempWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        while (s!=null) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(TempWSEndpoint.class.getName()).log(Level.SEVERE, null, ex);
            }
            send(Double.toString(temp));
            temp+=offset;
            if (temp>50) offset=-offset;
            if (temp<10) offset=-offset;
        }
    }
}
