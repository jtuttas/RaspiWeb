/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.Pin;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jörg
 */
public class LEDControl implements Runnable{

    // create gpio controller
    private GpioController gpio;
    // provision gpio pin #01 as an output pin and turn on
    private GpioPinDigitalOutput opin;
    private long onTime=0;
    private Thread dimmer;
    private boolean running=false;
    private static LEDControl instance;
    private int dimValue;
    private ArrayList<LEDValueChanged> listeners = new ArrayList<>();
    
    private LEDControl() {
        this(RaspiPin.GPIO_01, PinState.HIGH);
    }
    
    private LEDControl(Pin pin, PinState state) {
        // TODO ändern zum Testen
        gpio = GpioFactory.getInstance();
        opin = gpio.provisionDigitalOutputPin(pin, state);
        //---------------------------------
        if (state==PinState.HIGH) this.dim(100);
        else this.dim(0);
        dimmer = new Thread(this);
        running=true;
        dimmer.start();
    }

    public static LEDControl getInstance(Pin pin, PinState state) {
        if (instance==null) {
            instance=new LEDControl(pin, state);
        }
        return instance;
    }
    
    public void turnOn(boolean b) {
        if (b) {
            this.dim(100);
        } else {
            this.dim(0);

        }
    }

    public void shutDown() {
        running = false;
        gpio.shutdown();
    }

    public void dim(int i) {
        dimValue=i;
        onTime=20*i/100;
        System.out.println ("Dimmer set to "+i+"% onTime is "+onTime+" ms");
        for (LEDValueChanged l:listeners) {
            l.valueChanged(i);
        }
    }

    

    @Override
    public void run() {

        while(running) {
            if (onTime != 0) {
                if (opin!=null) opin.high();
                try {
                    Thread.sleep(onTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LEDControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if (opin!=null) opin.low();
            try {
                Thread.sleep(20-onTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(LEDControl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }
    
    public int getDimValue() {
        return dimValue;
    }

    public void addListener(LEDValueChanged aThis) {
        listeners.add(aThis);
    }

    public void removeListener(LEDValueChanged aThis) {
        listeners.remove(aThis);
    }
}
