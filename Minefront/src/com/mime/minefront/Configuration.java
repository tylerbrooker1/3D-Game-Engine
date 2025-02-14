package com.mime.minefront;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class Configuration {
    
    Properties properties = new Properties();
    
    public void saveConfiguration(String key, int value) {
        String path = "res/settings/config.xml";
        try {
            File file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            OutputStream write = new FileOutputStream(path);
            properties.setProperty(key, Integer.toString(value));
            properties.storeToXML(write, "Resolution");
            write.close();
        } catch (IOException e) {
        }
    }
    
    public void loadConfiguration(String path) {
        try {
            InputStream read = new FileInputStream(path);
            properties.loadFromXML(read);
            String width = properties.getProperty("width");
            String height = properties.getProperty("height");
            System.out.println("Width: " + width + "Height: " + height);
            setResolution(Integer.parseInt(width), Integer.parseInt(height));
            read.close();
        } catch (FileNotFoundException e) {
            saveConfiguration("width", 800);
            saveConfiguration("height", 600);
            loadConfiguration(path);
        } catch (IOException f) {
            
        }
    }
    
    public void setResolution(int width, int height) {
        if (width == 640 && height == 480) {
            Display.selection = 0;
        }
        if (width == 800 && height == 600) {
            Display.selection = 1;
        }
        if (width == 1024 && height == 768) {
            Display.selection = 2;
        }
    }
    
}
