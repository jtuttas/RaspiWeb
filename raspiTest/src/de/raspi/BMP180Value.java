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
public class BMP180Value {
    
    private Timestamp timestamp ;
    private float temperature;
    private long pressure;
    
    public BMP180Value(float temperature, long pressure) {
        timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
        this.temperature=temperature;
        this.pressure=pressure;
    }

    public BMP180Value(Timestamp time,float temp,long p) {
        this.timestamp=time;
        this.temperature=temp;
        this.pressure=p;
    }

    public boolean isLike(BMP180Value obj) {
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
        String out= "<measure timestamp=\""+timestamp+"\">\n";
        out+="<temperature>"+temperature+"</temperature>\n";
        out+="<pressure>"+pressure+"</pressure>\n";
        out+="</measure>\n";
        return out;
    }
    
    public String toJson(boolean last) {
        String out = "{";
        out += "\"temperature\" : "+temperature+",\n";
        out += "\"pressure\" : "+pressure+",\n";
        out += "\"timestamp\" : \""+timestamp.toString()+"\"}\n";
        if (!last) out+=",";
        return out;
    }
    
    public String toCsv() {
        String out = "\""+timestamp.toString()+"\";"+temperature+";"+pressure+"\n";
        return out;
    }
    
    
}
