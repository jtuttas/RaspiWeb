/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.tuttas.raspi.restful;

/**
 *
 * @author Jörg
 */
public class StepperValue {
    
    private int step;
    private String dir;

    public StepperValue() {
    }

    public StepperValue(int step, String dir) {
        this.step = step;
        this.dir = dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getDir() {
        return dir;
    }

    public int getStep() {
        return step;
    }
        
    
}
