/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;


/**
 *
 * @author Jörg
 */
public class Controller implements DS18B20ValueChangedListener{
    LED red,green,blue;
    DS18B20 sensor;
    double lastValue;
    
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
        try {
            if (Math.abs(temp-lastValue)>=0.5) {
                postRequest(temp);
                lastValue=temp;
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
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

    private void postRequest(double temp) throws MalformedURLException {
        
        try {
            String url = "http://maker.ifttt.com/trigger/TemperatureChanged/with/key/W4JUGEGMsLuY9wdYZg80Y?value1="+Double.toString(temp);
            
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            
            // optional default is GET
            con.setRequestMethod("GET");
                      
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);
            
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            //print result
            System.out.println(response.toString());
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
