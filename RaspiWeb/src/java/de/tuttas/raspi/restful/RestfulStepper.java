/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi.restful;

import de.raspi.LED;
import de.raspi.LEDDimValue;
import de.raspi.Stepper;
import de.raspi.StepperMovingException;
import de.raspi.StepperPosition;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Jörg
 */
@Path("stepper")
public class RestfulStepper {
     
     Stepper stepperControl = Stepper.getInstance(LED.getInstance(12),LED.getInstance(16),LED.getInstance(13),LED.getInstance(21));   
    
     @POST
     @Consumes(MediaType.APPLICATION_JSON)
     public StepperPosition setStepperValue(StepperValue d) {
         if (d.getDir().equals("left")) {
             try {
                 stepperControl.left(d.getStep(), 20);
             } catch (StepperMovingException ex) {
                 Logger.getLogger(RestfulStepper.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         else if (d.getDir().equals("right")) {
             try {             
                 stepperControl.right(d.getStep(), 20);
             } catch (StepperMovingException ex) {
                 Logger.getLogger(RestfulStepper.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
         return stepperControl.getPostion();
     }
     
     @GET
     public StepperPosition getStepperPosition() {
         return stepperControl.getPostion();
     }
     
     @DELETE
     public void resetStepperPosition() {
         try {
             stepperControl.reset();
         } catch (StepperMovingException ex) {
             Logger.getLogger(RestfulStepper.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
}
