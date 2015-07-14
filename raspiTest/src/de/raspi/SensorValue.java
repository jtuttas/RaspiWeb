/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.GregorianCalendar;

/**
 *
 * @author Jörg
 */
public class SensorValue {
    
    private Timestamp timestamp ;
    private float temperature;
    private long pressure;
    
    public SensorValue(float temperature, long pressure) {
        timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
        this.temperature=temperature;
        this.pressure=pressure;
    }

    public SensorValue(Timestamp time,float temp,long p) {
        this.timestamp=time;
        this.temperature=temp;
        this.pressure=p;
    }

    public boolean isLike(SensorValue obj) {
        if (obj==null) return false;
        if (obj.getTemperature()==temperature && obj.getPressure()==pressure) return true;
        
        return false;
    }
    
    
    
    

    public long getPressure() {
        return pressure;
    }

    public float getTemperature() {
        return temperature;
    }
    
    
    
    @Override
    public String toString() {
        return "Timestame: "+timestamp.toString()+"\nTemperature: "+temperature+" C\nPressure: "+pressure+" Pa\n";
    }
    public String toHtml() {
        return "<div class=\"measurement\"><div class=\"timestamp\">Timestame: "+timestamp.toString()+"</div><div class=\"temperature\">Temperature: "+temperature+" C</div><div class=\"pressure\">Pressure: "+pressure+" Pa</div></div>";
    }
    
    public String toXml() {
        String out= "<measure timestamp=\""+timestamp.getTime()+"\" datetime=\""+timestamp.toString()+"\" >\n";
        out+="<temperature>"+temperature+"</temperature>\n";
        out+="<pressure>"+pressure+"</pressure>\n";
        out+="</measure>\n";
        return out;
    }
    
    public String toJson(boolean last) {
        String out = "{";
        out += "\"temperature\" : "+temperature+",";
        out += "\"pressure\" : "+pressure+",";
        out += "\"datetime\" : \""+timestamp.toString()+"\",";
        out += "\"timestamp\" : "+timestamp.getTime()+"}";
        if (!last) out+=",";
        return out;
    }
    
    public String toCsv() {
        String out = timestamp.getTime()+";\""+timestamp.toString()+"\";"+temperature+";"+pressure+"\n";
        return out;
    }
    
    
}
