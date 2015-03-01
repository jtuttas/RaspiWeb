/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.raspi;

import com.mysql.jdbc.Connection;
import static de.raspi.BMP180Test.bmp180;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.System.exit;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Jörg
 */
public class DBlogger {

    Connection connection = null;


    public DBlogger(String serverIP, String dbName, String user, String password) {
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = (Connection) DriverManager.getConnection("jdbc:mysql://" + serverIP + ":3306/" + dbName, user, password);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return connection;
    }
    
    public void log(BMP180Value bmp180Value) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            if (bmp180Value!=null) stmt.execute("insert into bmp180 (timestamp,temp,pressure) Values (now(),"+bmp180Value.getTemperature()+","+bmp180Value.getPressure()+")");
            else stmt.execute("insert into bmp180 (timestamp,temp,pressure) Values (now(),-1,0)");
        } catch (SQLException ex) {
            Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        limit(10);

    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String[] args) {
        if (args[0]==null) {
            System.out.println ("No path to config.json added!");
        }
        else {
            
            System.out.println ("read config.json: "+args[0]);
            try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(args[0]));
                JSONObject jsonObject = (JSONObject) obj;
                System.out.println ("Conntect to MYSQL Server:"+jsonObject.get("dbserver"));
                DBlogger dbl = new DBlogger(jsonObject.get("dbserver").toString(), jsonObject.get("dbname").toString(), jsonObject.get("dbuser").toString(), jsonObject.get("dbpassword").toString());
                bmp180=BMP180.getInstance(1,0x77);
                bmp180.readSensorData();
                System.out.println ("Temperatur:"+bmp180.getTemperature()+" C");
                System.out.println ("Luftdruck:"+bmp180.getPressure()+" Pa");
                dbl.log(bmp180.getValue());
                dbl.close();
            } catch (IOException ex) {
                Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println ("Kann Konfigurationsdatei nicht finden!");
                exit(1);
            } catch (ParseException ex) {
                Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println ("Kann Konfigurationsdatei hat ein flasches Format!");
                exit(1);
            }
        }  
    }

    private void limit(int max) {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) as num from bmp180");
            rs.next();
            System.out.println ("Found "+rs.getString("num")+" rows!");
            int num = Integer.parseInt(rs.getString("num"));
            if (num>max) {
                stmt = connection.createStatement();
                stmt.execute("DELETE FROM bmp180 ORDER BY id LIMIT "+(num-max));	
            }
                  
        } catch (SQLException ex) {
            Logger.getLogger(DBlogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
