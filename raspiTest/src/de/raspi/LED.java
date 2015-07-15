/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jörg
 */
public class LED implements Runnable {

    private long onTime = 0;
    private Thread dimmer;
    private boolean running = false;
    private static LED[] instances = new LED[28];
    private int dimValue;
    private ArrayList<LEDValueChanged> listeners = new ArrayList<>();
    private int pin;
    
    private LED(int pin) {
        this.pin=pin;
        init(pin);
        dimmer = new Thread(this);
        running = true;
        dimmer.start();
    }

    public static LED getInstance(int pin, boolean state) {
        if (instances[pin] == null) {
            instances[pin] = new LED(pin);
        }
        instances[pin].turnOn(state);
        return instances[pin];
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
        this.setOn(false);
    }

    public void dim(int i) {
        dimValue = i;
        onTime = 20 * i / 100;
        System.out.println("Dimmer set to " + i + "% onTime is " + onTime + " ms");
        for (LEDValueChanged l : listeners) {
            l.valueChanged(i);
        }
    }

    private void setOn(boolean b) {
        int p;
        if (b) p=1;
        else p=0;
        PrintWriter writer = null;
        try {
            //echo "0" > /sys/class/gpio/gpio23/value
            writer = new PrintWriter(Config.gpio + pin + "/value", "UTF-8");
            writer.print(p);
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void run() {
        while (running) {
            if (onTime != 0) {
                setOn(true);
                try {
                    Thread.sleep(onTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LEDControl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            setOn(false);
            try {
                Thread.sleep(20 - onTime);
            } catch (InterruptedException ex) {
                Logger.getLogger(LEDControl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public String toString() {
        return "LED PIN="+pin+" dim Value="+this.getDimValue();
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

    private void init(int pin) {
        PrintWriter writer = null;
        try {
            // echo "23" > /sys/class/gpio/export
            writer = new PrintWriter(Config.export, "UTF-8");
            writer.print(Integer.toString(pin));
            writer.close();

            // echo "out" > /sys/class/gpio/gpio23/direction
            writer = new PrintWriter(Config.gpio + pin + "/direction", "UTF-8");
            writer.print("out");
            writer.close();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }
    
    public static void main(String[] args) {
        try {
            LED l = LED.getInstance(18, true);
            
            System.out.println("LED shoud be on");
            Thread.sleep(10000);
            l.dim(0);
            System.out.println("LED shoud be off");
            Thread.sleep(5000);
            l.dim(50);
            System.out.println("LED shoud be 50%");
            Thread.sleep(5000);
            System.out.println("LED shoud be on");
            Thread.sleep(10000);
            l.shutDown();
        } catch (InterruptedException ex) {
            Logger.getLogger(LED.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
