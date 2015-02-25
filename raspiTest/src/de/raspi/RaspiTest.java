/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jörg
 */
public class RaspiTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
       
        
        LEDControl ledControl = LEDControl.getInstance(RaspiPin.GPIO_01, PinState.HIGH);
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");
        
        
        // provision gpio pin #01 as an output pin and turn on
        System.out.println("--> GPIO state should be: ON");
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RaspiTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // turn off gpio pin #01
        ledControl.turnOn(false);
        System.out.println("--> GPIO state should be: OFF");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RaspiTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        ledControl.dim(20);
        System.out.println("--> GPIO state should be: 20%");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(RaspiTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        ledControl.shutDown();

    }
    
}
