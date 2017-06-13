package com.oppsis.app.hftracker.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {  
    private static Properties prop = new Properties();
    private static PropertyUtil instance = new PropertyUtil();
    
    private PropertyUtil(){
        InputStream in = this.getClass().getResourceAsStream("/config.properties");
        try {
            prop.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public static PropertyUtil getInstance() {
        return instance;
    }

    public String getProperty(String key){
        return (String) prop.get(key);
    }  
    
    public static void main(String[] args){
    	System.out.println(PropertyUtil.getInstance().getProperty("iourls"));
    	//Date d = DateUtil.getDate("2014-07-16 09:18:08");
    	//System.out.println(d.getTime());
    }
}
