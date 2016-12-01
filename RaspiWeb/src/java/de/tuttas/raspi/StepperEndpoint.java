/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi;

import de.raspi.Config;
import de.raspi.DS18B20;
import de.raspi.LED;
import de.raspi.Stepper;
import de.raspi.StepperPosition;
import de.raspi.StepperPositionChangedListener;
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
@ServerEndpoint("/stepper")
public class StepperEndpoint implements StepperPositionChangedListener{
    
     Session session;
    private Stepper stepper;
    
       
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
         try {
             stepper=Stepper.getInstance();
         } catch (Exception ex) {
             Logger.getLogger(StepperEndpoint.class.getName()).log(Level.SEVERE, null, ex);
             Stepper.getInstance(LED.getInstance(12),LED.getInstance(16),LED.getInstance(13),LED.getInstance(21));
         }
        stepper.addListener(this);
        this.positionChanged(stepper.getPostion());
        System.out.println(session.getId() + " has opened a connection");
    }

    @OnClose
     public void onClose(Session session) {
         if (stepper!=null) {
             stepper.removeListener(this);
         }
     }
     
    @Override
    public void positionChanged(StepperPosition sp) {
            System.out.println("Stepper Value Changed:"+sp.getPosition());
         try {
             session.getBasicRemote().sendText(sp.toJSON());
         } catch (IOException ex) {
             Logger.getLogger(StepperEndpoint.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    @Override
    public void moving(boolean b) {        
         try {
             session.getBasicRemote().sendText("{\"moving\":"+b+"}");
         } catch (IOException ex) {
             Logger.getLogger(StepperEndpoint.class.getName()).log(Level.SEVERE, null, ex);
         }
    }
    
}
