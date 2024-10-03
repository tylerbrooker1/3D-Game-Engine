
package com.mime.minefront.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Texture {

    public static Render floor = loadBitmap("/textures/floor.png");
    
    public static Render loadBitmap(String filename) {
        
        try {
            BufferedImage image = ImageIO.read(Texture.class.getResource(filename));
            int width = image.getWidth();
            int height = image.getHeight();
            Render result = new Render(width, height);
            image.getRGB(0, 0, width, height, result.pixels, 0, width);
            return result;
        } catch (IOException e) {
            System.out.println("Image not found!");
            throw new RuntimeException(e);
        }
    }
}
