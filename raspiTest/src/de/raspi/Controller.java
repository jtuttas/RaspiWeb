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
    LED red,green,yellow;
    DS18B20 sensor;
    
    public Controller() {
        red=LED.getInstance(23, false);
        green=LED.getInstance(24, false);
        yellow=LED.getInstance(25, false);
        try {
            sensor = new DS18B20(Config.SensorAdr);
            sensor.addListener(this);
            sensor.start();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void valueChanged(double temp) {
        if (temp<Config.LEVEL0) {
            green.dim(100);
            red.dim(0);
            yellow.dim(0);
        }        
        else if (temp<Config.LEVEL1) {
            green.dim(0);
            red.dim(0);
            yellow.dim(100);            
        }
        else {
            green.dim(0);
            red.dim(100);
            yellow.dim(0);            
        }
    }
    
    public static void main(String[] args) {
        Controller c = new Controller();
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        while (s.compareTo("e")!=0) {
            System.out.println ("red="+c.red.toString()+" green="+c.green.toString()+" yellow="+c.yellow.toString());
            s = sc.nextLine();
        }
        System.out.println("shutdown");
        c.green.shutDown();
        c.red.shutDown();
        c.yellow.shutDown();
        c.sensor.removeListener(c);
        System.exit(0);
    }
}
