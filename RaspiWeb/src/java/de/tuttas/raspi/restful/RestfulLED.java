/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi.restful;

import de.raspi.LED;
import de.raspi.LEDDimValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 *
 * @author Jörg
 */
@Path("led")
public class RestfulLED {
    
    
     LED ledControl = LED.getInstance(18);    
     @GET
     public LEDDimValue getDimValue() {
         return ledControl.getDimValue();
     }
     
     @POST
     public LEDDimValue setDimValue(LEDDimValue d) {
         ledControl.dim(d.getDim());
         return ledControl.getDimValue();
     }
}
