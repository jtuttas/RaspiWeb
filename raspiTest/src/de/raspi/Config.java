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
public class Config {
    // Raspbery PI Config
    public static final String SensorAdr="/sys/bus/w1/devices/28-000006369255/w1_slave";
    public static final String gpio = "/sys/class/gpio/gpio";
    public static final String export = "/sys/class/gpio/export";
}
