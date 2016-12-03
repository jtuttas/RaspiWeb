/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

/**
 *
 * @author Jörg
 */
public class StepperMovingException extends Exception{
    private Stepper stepper;

    public StepperMovingException(Stepper stepper,String msg) {
        super(msg);
        this.stepper = stepper;
    }

    public Stepper getStepper() {
        return stepper;
    }
    
    
    
    
    
}
