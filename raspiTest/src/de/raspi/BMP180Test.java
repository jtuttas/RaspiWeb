/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jörg
 */
public class BMP180Test {

    static BMP180 bmp180;


    

    public static void main(String[] args) {
        try {
            bmp180=BMP180.getInstance(1,0x77);
            bmp180.readSensorData();
            System.out.println ("Temperatur:"+bmp180.getTemperature()+" C");
            System.out.println ("Luftdruck:"+bmp180.getPressure()+" Pa");
        } catch (IOException ex) {
            Logger.getLogger(BMP180Test.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }

}
