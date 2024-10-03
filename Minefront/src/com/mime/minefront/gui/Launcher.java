package com.mime.minefront.gui;

import com.mime.minefront.Configuration;
import com.mime.minefront.RunGame;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Launcher extends JFrame {

    protected JPanel window = new JPanel();
    private JButton play, options, help, quit;
    private Rectangle rplay, roptions, rhelp, rquit;

    protected int width = 240;
    protected int height = 320;
    protected int buttonWidth = 80;
    protected int buttonHeight = 40;
    Configuration config = new Configuration();

    public Launcher(int id) {

        setTitle("Minefront Launcher");
        setSize(new Dimension(width, height));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        getContentPane().add(window);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        window.setLayout(null);

        if (id == 0) {
            drawButtons();
        }

    }

    private void drawButtons() {

        play = new JButton("Play!");
        rplay = new Rectangle((width / 2) - (buttonWidth / 2), 30, buttonWidth, buttonHeight);
        play.setBounds(rplay);
        window.add(play);

        options = new JButton("Options");
        roptions = new Rectangle((width / 2) - (buttonWidth / 2), 80, buttonWidth, buttonHeight);
        options.setBounds(roptions);
        window.add(options);

        help = new JButton("Help");
        rhelp = new Rectangle((width / 2) - (buttonWidth / 2), 130, buttonWidth, buttonHeight);
        help.setBounds(rhelp);
        window.add(help);

        quit = new JButton("Quit");
        rquit = new Rectangle((width / 2) - (buttonWidth / 2), 180, buttonWidth, buttonHeight);
        quit.setBounds(rquit);
        window.add(quit);

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                config.loadConfiguration("res/settings/config.xml");
                dispose(); // Destroys the launcher window
                new RunGame();
            }
        });
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                dispose();
                new Options();
            }
        });
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

    }

}
