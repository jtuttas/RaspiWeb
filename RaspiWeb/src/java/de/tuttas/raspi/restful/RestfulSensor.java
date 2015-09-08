/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi.restful;


import de.raspi.Config;
import de.raspi.DS18B20;
import de.raspi.DS18B20Value;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author Jörg
 */
@Path("temperature")
public class RestfulSensor {
    private DS18B20 sensor = new DS18B20(Config.SensorAdr);
    
     @GET
     public DS18B20Value getTempValue() {
        try {
            DS18B20Value v = sensor.getTemperature();
            return v;
        } catch (IOException ex) {
            Logger.getLogger(RestfulSensor.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
     }
}
