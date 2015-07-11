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
public interface DS18B20ValueChangedListener {
 
    public void valueChanged(double temp);
}
