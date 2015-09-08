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
public class LEDDimValue {
    
    private int dim=0;

    public int getDim() {
        return dim;
    }

    public void setDim(int dim) {
        this.dim = dim;
    }
    
    public String toString() {
        return Integer.toString(dim);
    }
}
