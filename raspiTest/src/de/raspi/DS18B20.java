/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jörg
 */
public class DS18B20 implements Runnable {

 private Timestamp timestamp ;
 private String fileName;
    private BufferedReader reader;
    private Thread runner;
    private boolean running;
     // Listener
    private ArrayList<DS18B20ValueChangedListener> listeners = new ArrayList<>();

    public DS18B20(String file) throws FileNotFoundException {
        fileName = file;
        
        runner = new Thread(this);
    }

    public void addListener(DS18B20ValueChangedListener l) {
        listeners.add(l);
    }
    
    public void removeListener(DS18B20ValueChangedListener l) {
        listeners.remove(l);
    }

    public void start() {
        running = true;
        runner.start();
    }

    public void stop() {
        running = false;
    }

    public double getTemperature() throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
        String zeile = reader.readLine();
        double out = 0.0;
        while (zeile != null) {
            if (zeile.indexOf("t=") != -1) {
                out = Double.parseDouble(zeile.substring(zeile.indexOf("t=") + 2));
                out = out / 1000;
            }
            zeile = reader.readLine();

        }
        reader.close();
        return out;

    }

    public void close() throws IOException {
        reader.close();
    }

    public static void main(String[] args) {
        try {
            DS18B20 ds = new DS18B20(Config.SensorAdr);
            System.out.println("Temperatur=" + ds.getTemperature());
            ds.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DS18B20.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DS18B20.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        double oldTemp = 0.0;
        double aktualTemp = 0.0;
        while (running) {
            try {
                aktualTemp = this.getTemperature();
                System.out.println("Read actual=" + aktualTemp);

                if (aktualTemp != oldTemp) {
                    for (DS18B20ValueChangedListener l:listeners) {
                        l.valueChanged(aktualTemp);
                    }
                    oldTemp = aktualTemp;
                }
               
            } catch (IOException ex) {
                System.err.println("IOException:"+ex.getMessage());
                Logger.getLogger(DS18B20.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DS18B20.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

    @Override
    public String toString() {
        timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
     try {
         return "Timestame: "+timestamp.toString()+"\nTemperature: "+this.getTemperature()+" C";
     } catch (IOException ex) {
         Logger.getLogger(DS18B20.class.getName()).log(Level.SEVERE, null, ex);
          return "Timestame: "+timestamp.toString()+"\nTemperature: "+-1.0+" C";
     }
    }
    public String toHtml() {
        timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
     try {
         return "<div class=\"measurement\"><div class=\"timestamp\">Timestame: "+timestamp.toString()+"</div><div class=\"temperature\">Temperature: "+this.getTemperature()+" C</div></div>";
     } catch (IOException ex) {
 return "<div class=\"measurement\"><div class=\"timestamp\">Timestame: "+timestamp.toString()+"</div><div class=\"temperature\">Temperature: "+-1.0+" C</div></div>";     }
    }
    
    public String toXml() {
        timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
        String out= "<measure timestamp=\""+timestamp.getTime()+"\" datetime=\""+timestamp.toString()+"\" >\n";
     try {
         out+="<temperature>"+this.getTemperature()+"</temperature>\n";
     } catch (IOException ex) {
         out+="<temperature>"+-1.0+"</temperature>\n";
     }
        out+="</measure>\n";
        return out;
    }
    
    public String toJson(boolean last) {
    timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
        String out = "{";
     try {
         out += "\"temperature\" : "+this.getTemperature()+",";
     } catch (IOException ex) {
          out += "\"temperature\" : "+-1.0+",";
     }
            out += "\"level0\" : "+Config.LEVEL0+",";
            out += "\"level1\" : "+Config.LEVEL1+",";
        out += "\"datetime\" : \""+timestamp.toString()+"\",";
        out += "\"timestamp\" : "+timestamp.getTime()+"}";
        if (!last) out+=",";
        return out;
    }
    
    public String toCsv() {
        timestamp = new Timestamp(GregorianCalendar.getInstance().getTime().getTime());
        String out;
     try {
         out = timestamp.getTime()+";\""+timestamp.toString()+"\";"+getTemperature()+";"+-1.0+"\n";
     } catch (IOException ex) {
         out = timestamp.getTime()+";\""+timestamp.toString()+"\";"+-1.0+";"+-1.0+"\n";
     }
        return out;
    }

    public SensorValue getValue() {
     try {
         return new SensorValue((float) this.getTemperature(), -1);
     } catch (IOException ex) {
          return new SensorValue((float) -1.0, -1);
     }
     }
    
    

}
