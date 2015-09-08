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
public class DS18B20Value {

    private double temperature;

    public DS18B20Value(double temperature) {
        this.temperature = temperature;
    }

    public DS18B20Value() {
    }

    
    
    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    
    
    
    
}
