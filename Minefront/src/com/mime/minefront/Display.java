package com.mime.minefront;

// Game testing
import com.mime.minefront.graphics.Screen;
import com.mime.minefront.gui.Launcher;
import com.mime.minefront.input.Controller;
import com.mime.minefront.input.InputHandler;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.BufferStrategy;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;

public class Display extends Canvas implements Runnable {

    public static int width = 800;
    public static int height = 600;

    public static final String TITLE = "Minefront pre-alpha 0.04";

    private Thread thread;
    private Screen screen;
    private Game game;
    private boolean running = false;
    private BufferedImage img;
    private int[] pixels;
    private InputHandler input;
    private int newX = 0;
    private int oldX = 0;
    private int fps;
    public static int mouseSpeed;
    public static int selection = -1;

    public Display() {
        Dimension size = new Dimension(getGameWidth(), getGameHeight());
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        screen = new Screen(getGameWidth(), getGameHeight());
        game = new Game();
        img = new BufferedImage(getGameWidth(), getGameHeight(), BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        input = new InputHandler();
        addKeyListener(input);
        addFocusListener(input);
        addMouseListener(input);
        addMouseMotionListener(input);
    }

    public static int getGameWidth() {
        if (selection == 0) {
            width = 640;
        }
        if (selection == 1 || selection == -1) {
            width = 800;
        }
        if (selection == 2) {
            width = 1024;
        }
        
        return width;
    }

    public static int getGameHeight() {
        if (selection == 0) {
            height = 480;
        }
        if (selection == 1 || selection == -1) {
            height = 600;
        }
        if (selection == 2) {
            height = 768;
        }
        
        return height;
    }

    public synchronized void start() {
        if (running) {
            return;
        }
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) {
            return;
        }
        running = false;
        try {
            thread.join();
        } catch (InterruptedException ex) {
            System.out.println("Unable to join thread!");
            System.exit(0);
        }
    }

    @Override
    public void run() {
        int frames = 0;
        double unprocessedSeconds = 0;
        long previousTime = System.nanoTime();
        double secondsPerTick = 1 / 60.0;
        int tickCount = 0;
        boolean ticked = false;
        requestFocus();
        while (running) {
            long currentTime = System.nanoTime();
            long passedTime = currentTime - previousTime;
            previousTime = currentTime;
            unprocessedSeconds += passedTime / 1000000000.0;

            while (unprocessedSeconds > secondsPerTick) {
                tick();
                unprocessedSeconds -= secondsPerTick;
                ticked = true;
                tickCount++;
                if (tickCount % 60 == 0) {
                    previousTime += 1000;
                    fps = frames;
                    frames = 0;
                }
            }
            if (ticked) {
                render();
                frames++;
            }
            render();
            frames++;

            newX = InputHandler.MouseX;
            if (newX > oldX) {
                Controller.turnRight = true; // Detects right turn
            }
            if (newX < oldX) {
                Controller.turnLeft = true; // Detects left turn
            }
            if (newX == oldX) {
                Controller.turnLeft = false; // Prevents continuous movement
                Controller.turnRight = false;
            }
            mouseSpeed = Math.abs(newX - oldX);
            oldX = newX;
        }
    }

    /* Update game events */
    private void tick() {
        game.tick(input.key);
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.render(game);

        System.arraycopy(screen.pixels, 0, pixels, 0, getGameWidth() * getGameHeight());

        Graphics g = bs.getDrawGraphics();
        g.drawImage(img, 0, 0, getGameWidth(), getGameHeight(), null);
        g.setFont(new Font("Verdana", 0, 50));
        g.setColor(Color.cyan);
        g.drawString("fps: " + fps, 20, 50);
        g.dispose();
        bs.show();
    }

    public static void main(String[] args) {
        new Launcher(0);
    }

}
