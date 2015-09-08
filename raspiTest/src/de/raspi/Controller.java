/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jörg
 */
public class Controller implements DS18B20ValueChangedListener{
    LED red,green,blue;
    DS18B20 sensor;
    
    public Controller() throws FileNotFoundException {
        red=LED.getInstance(23);
        green=LED.getInstance(24);
        blue=LED.getInstance(25);
        sensor = new DS18B20(Config.SensorAdr);
        sensor.addListener(this);
        sensor.start();

    }

    @Override
    public void valueChanged(double temp) {
        System.out.println ("value changed:"+temp);
                
        if (temp<Config.LEVEL0) {
            System.out.println ("set Green");
            green.dim(100);
            red.dim(0);
            blue.dim(0);
        }        
        else if (temp<Config.LEVEL1) {
            System.out.println ("set yellow");
            green.dim(20);
            red.dim(100);
            blue.dim(0);            
        }
        else {
            System.out.println ("set red");
            green.dim(0);
            red.dim(100);
            blue.dim(0);            
        }
    }
    
    public static void main(String[] args) {
        try {
            Controller c = new Controller();
            Scanner sc = new Scanner(System.in);
            String s = sc.nextLine();
            while (s.compareTo("e")!=0) {
                System.out.println ("red="+c.red.toString()+" green="+c.green.toString()+" yellow="+c.blue.toString());
                s = sc.nextLine();
            }
            System.out.println("shutdown");
            c.green.shutDown();
            c.red.shutDown();
            c.blue.shutDown();
            c.sensor.removeListener(c);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
    }
}
