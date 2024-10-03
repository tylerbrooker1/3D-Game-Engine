package com.mime.minefront;

import java.applet.Applet;
import java.awt.BorderLayout;

public class MinefrontApplet extends Applet {
    
    private static final long serialVersionUID = 1L;
    
    private Display display = new Display();
    
    @Override
    public void init() { 
        setLayout(new BorderLayout());
        add(display);
    }
    
    @Override
    public void start() {
        display.start();
    }
    
    @Override
    public void stop() {
        display.stop();
    }
    
}
