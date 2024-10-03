package com.mime.minefront.gui;

import com.mime.minefront.Display;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class Options extends Launcher {

    public JButton OK;
    public Rectangle rOK, rresolution;
    private Choice resolution = new Choice();

    public Options() {
        super(1);
        setTitle("Options");
        setSize(new Dimension(width + 100, height + 100));
        setLocationRelativeTo(null);

        drawButtons();
    }

    private void drawButtons() {
        OK = new JButton("OK");
        rOK = new Rectangle(width, height, buttonWidth, buttonHeight);
        OK.setBounds(rOK);
        window.add(OK);
        
        rresolution = new Rectangle(50, 80, 80, 25);
        resolution.setBounds(rresolution);
        resolution.add("640, 480");
        resolution.add("800, 600");
        resolution.add("1024, 768");
        resolution.select(1);
        window.add(resolution);
        

        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selection = resolution.getSelectedIndex();
                int w = 0;
                int h = 0;
                switch (selection) {
                    case 0:
                        w = 640;
                        h = 480;
                        break;
                    case 1:
                    case -1:
                        w = 800;
                        h = 600;
                        break;
                    default:
                        w = 1024;
                        h = 768;
                        break;
                }
                config.saveConfiguration("width", w);
                config.saveConfiguration("height", h);
                new Launcher(0);
                dispose();
            }
        });
    }

}
