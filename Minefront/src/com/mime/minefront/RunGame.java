package com.mime.minefront;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;

public class RunGame {
    
    public RunGame() {
        
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blank = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0,0), "blank");
        Display game = new Display();
        JFrame frame = new JFrame();
        frame.add(game);
        frame.setResizable(false);
        frame.setSize(Display.getGameWidth(), Display.getGameHeight());
        //frame.getContentPane().setCursor(blank);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(Display.TITLE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.start();

    }

}
