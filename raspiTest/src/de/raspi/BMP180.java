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
public class BMP180 {

    private static BMP180 instance;
    private I2CBus bus;
    private I2CDevice bmp180;
    private static short AC1;
    private static short AC2;
    private static short AC3;
    private static int AC4;
    private static int AC5;
    private static int AC6;
    private static short B1;
    private static short B2;
    private static short MB;
    private static short MC;
    private static short MD;
//Variable common between temperature & pressure calculations
    private static long B5;
    
    // Sensor Data
    private float temperature=0;
    private long pressure=0;
    private BMP180Value bmp180Value;

    private BMP180(int i2cbus, int adress) {
        try {
            bus = I2CFactory.getInstance(i2cbus);
            //System.out.println("Connected to bus OK!!!");
            bmp180 = bus.getDevice(adress);
            //System.out.println("Connected to device OK!!!");
            gettingCallibration();
        } catch (IOException ex) {
            Logger.getLogger(BMP180.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BMP180 getInstance(int i2cbus, int adr) {
        if (instance == null) {
            instance = new BMP180(i2cbus, adr);
        }
        return instance;
    }

    public long getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }
    
    public BMP180Value getValue() {
        return bmp180Value;
    }

    
    
    
    private void gettingCallibration() throws IOException {
        byte[] bytes = new byte[22];

        //read all callibration data into byte array
        int readTotal = bmp180.read(0xAA, bytes, 0, 22);
        if (readTotal != 22) {
            System.out.println("Error bytes read: "
                    + readTotal);
        }

        DataInputStream bmp180CaliIn = new DataInputStream(new ByteArrayInputStream(bytes));

        // Read each of the pairs of data as signed short
        AC1 = bmp180CaliIn.readShort();
        AC2 = bmp180CaliIn.readShort();
        AC3 = bmp180CaliIn.readShort();

        // Unsigned short Values
        AC4 = bmp180CaliIn.readUnsignedShort();
        AC5 = bmp180CaliIn.readUnsignedShort();
        AC6 = bmp180CaliIn.readUnsignedShort();

        //Signed sort values
        B1 = bmp180CaliIn.readShort();
        B2 = bmp180CaliIn.readShort();
        MB = bmp180CaliIn.readShort();
        MC = bmp180CaliIn.readShort();
        MD = bmp180CaliIn.readShort();

        /*
         System.out.println("Callibration: " + AC1 + ":" + AC2
         + ":" + AC3 + ":" + AC4 + ":" + AC5 + ":" + AC6
         + ":" + B1 + ":" + B2 + ":" + MB + ":" + MC + ":" + MD);
         */
    }

    public void readSensorData() throws IOException {
        byte[] bytesTemp = new byte[2];
        byte[] bytesPress = new byte[3];
        try {
            // Write 0x2E into 0xF4 and wait 5 ms (see documentation Page 15)
            bmp180.write((byte) 0xf4, (byte) 0x2e);
            Thread.sleep(500);
            // Read two bytes from Adress 0xF6
            int readTotal = bmp180.read(0xF6, bytesTemp, 0, 2);
            if (readTotal < 2) {
                System.out.format("Error: %n bytes read/n", readTotal);                
            }
            DataInputStream bmp180In = new DataInputStream(new ByteArrayInputStream(bytesTemp));
            int UT = bmp180In.readUnsignedShort();
            //System.out.println("READ UT=" + UT);
            
            // read pressure Values
            // Write 0x34 into 0xF4 and wait 5 ms (see documentation Page 15)
            bmp180.write((byte) 0xf4, (byte) 0x34);
            Thread.sleep(500);
            // Read three bytes from Adress 0xF6
            int readTotalPress = bmp180.read(0xF6, bytesPress, 0, 3);
            if (readTotalPress < 3) {
                System.out.format("Error: %n bytes read/n", readTotalPress);
                
            }
            bmp180In = new DataInputStream(new ByteArrayInputStream(bytesPress));
            byte msb = bmp180In.readByte();
            byte lsb = bmp180In.readByte();
            byte xlsb = bmp180In.readByte();
            //System.out.println("Read msb:" + msb + " lsb:" + lsb + " xlsb:" + xlsb);
            int UP = ((msb << 8) & 0xFF00) + lsb;
            //System.out.println("READ UP=" + UP);
            
            //calculate temperature
            
            temperature=calcTemp(UT);
            
            //System.out.println("Temperature: " + temperature + "C");
            
            pressure = calcPressture(UP);
            bmp180Value = new BMP180Value(temperature, pressure);
            //System.out.println("Pressure: " + pressure + "Pa");
        } catch (InterruptedException ex) {
            Logger.getLogger(BMP180.class.getName()).log(Level.SEVERE, null, ex);
        }

        
    }
    
    private float calcTemp(int UT) {
        long X1 = ((UT - AC6) * AC5) / 32768;
        long X2 = (MC << 11) / (X1 + MD);
        B5 = X1 + X2;
        return (((B5 + 8) >> 4) / 10);
    }
    
    

    private long calcPressture(int UP) {
                //calculate pressure
                /*
         UP=23843;
         AC1=408;
         AC2=-72;
         AC3=-14383;
         AC4=32741;
         AC5=32757;
         AC6=23153;
         B1=6190;
         B2=5;
         MB=-32768;
         MC=-8711;
         MD=2868;
         B5=2399;
         */
        long B6 = B5 - 4000;
        //System.out.println ("B6="+B6);
        long X1 = (B2 * (B6 * B6 / (1L << 12))) / (1L << 11);
        //System.out.println ("X1="+X1);
        long X2 = AC2 * B6 / (1L << 11);
        //System.out.println ("X2="+X2);
        long X3 = X1 + X2;
        //System.out.println ("X3="+X3);
        long B3 = (((AC1 * 4 + X3) << 0) + 2) / 4;
        //System.out.println ("B3="+B3);
        X1 = AC3 * B6 / (1L << 13);
        //System.out.println ("X1="+X1);                
        X2 = (B1 * (B6 * B6 / 4096)) / 65536;
        //System.out.println ("X2="+X2);
        X3 = ((X1 + X2) + 2) / 4;
        //System.out.println ("X3="+X3);              
        long B4 = AC4 * Math.abs(X3 + 32768) / (1L << 15);
        //System.out.println ("B4="+B4);              
        long B7 = Math.abs(UP - B3) * (50000 >> 0);
        //System.out.println ("B7="+B7);

        long p;
        if (B7 < 0x80000000) {
            p = (B7 * 2) / B4;
        } else {
            p = (B7 / B4) * 2;
        }
                //System.out.println ("p="+p);

        X1 = (p / (1L << 8)) * (p / (1L << 8));
                //System.out.println ("X1="+X1);

        X1 = (X1 * 3038) / (1L << 16);
                //System.out.println ("X1="+X1);

        X2 = (-7357 * p) / (1L << 16);
               // System.out.println ("X2="+X2);

        return (p + (X1 + X2 + 3791) / (1L << 4));
    }

}
