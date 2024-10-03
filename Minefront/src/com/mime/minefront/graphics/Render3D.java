package com.mime.minefront.graphics;

import com.mime.minefront.Game;
import com.mime.minefront.input.Controller;
import com.mime.minefront.level.Block;
import com.mime.minefront.level.Level;

public class Render3D extends Render {

    public double[] zBuffer;
    public double[] zBufferWall;
    private double renderDistance = 10000; // Larger number = longer distance
    private double forward, right, sine, cosine, up, walking;

    public Render3D(int width, int height) {
        super(width, height);
        zBuffer = new double[width * height];
        zBufferWall = new double[width];
    }

    public void floor(Game game) {
        
        for (int x = 0; x < width; x++) {
            zBufferWall[x] = 0;
        }

        double floorPosition = 8;
        double ceilingPosition = 40;

        forward = game.controls.z;
        right = game.controls.x;
        up = game.controls.y;
        walking = 0;

        double rotation = game.controls.rotation; //Math.sin(game.time % 1000.0 / 80);
        sine = Math.sin(rotation);
        cosine = Math.cos(rotation);

        for (int y = 0; y < height; y++) {

            double ceiling = (y - height / 2.0) / height;
            double z = (floorPosition + up) / ceiling;

            if (Controller.walk) {
                z = (floorPosition + up + walking) / ceiling;
            }
            if (Controller.crouchWalk && Controller.walk) {
                walking = Math.sin(game.time / 12.0) * 0.4;
            }
            if (Controller.runWalk && Controller.walk) {
                walking = Math.sin(game.time / 4.0) * 0.6;
            }

            if (ceiling < 0) {
                z = (ceilingPosition - up) / -ceiling;
                if (Controller.walk) {
                    walking = (Math.sin(game.time / 6.0) * 0.5);
                    z = (ceilingPosition - up - walking) / -ceiling;
                }
            }

            for (int x = 0; x < width; x++) {
                double depth = (x - width / 2.0) / height;
                depth *= z;
                double xx = depth * cosine + z * sine + right;
                double yy = z * cosine - depth * sine + forward;
                int xPix = (int) (xx + right);
                int yPix = (int) (yy + forward);
                zBuffer[x + y * width] = z;
                pixels[x + y * width] = Texture.floor.pixels[(xPix & 7) + (yPix & 7) * 16]; // Floor texture
            }
        }

        Level level = game.level;
        int size = 15;
        for (int xBlock = -size; xBlock <= size; xBlock++) {
            for (int zBlock = -size; zBlock <= size; zBlock++) {
                Block block = level.create(xBlock, zBlock);
                Block east = level.create(xBlock + 1, zBlock);
                Block south = level.create(xBlock, zBlock + 1);

                if (block.solid) {
                    if (!east.solid) {
                        renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0);
                    }
                    if (!south.solid) {
                        renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0);
                    }
                } else {
                    if (east.solid) {
                        renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0);
                    }
                    if (south.solid) {
                        renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0);
                    }
                }
            }
        }
        for (int xBlock = -size; xBlock <= size; xBlock++) {
            for (int zBlock = -size; zBlock <= size; zBlock++) {
                Block block = level.create(xBlock, zBlock);
                Block east = level.create(xBlock + 1, zBlock);
                Block south = level.create(xBlock, zBlock + 1);

                if (block.solid) {
                    if (!east.solid) {
                        renderWall(xBlock + 1, xBlock + 1, zBlock, zBlock + 1, 0.5);
                    }
                    if (!south.solid) {
                        renderWall(xBlock + 1, xBlock, zBlock + 1, zBlock + 1, 0.5);
                    }
                } else {
                    if (east.solid) {
                        renderWall(xBlock + 1, xBlock + 1, zBlock + 1, zBlock, 0.5);
                    }
                    if (south.solid) {
                        renderWall(xBlock, xBlock + 1, zBlock + 1, zBlock + 1, 0.5);
                    }
                }
            }
        }

    }

    public void renderWall(double xLeft, double xRight, double zDistanceLeft, double zDistanceRight, double yHeight) {
        double xcLeft = ((xLeft / 2) - (right * 0.0625)) * 2;
        double zcLeft = ((zDistanceLeft / 2) - (forward * 0.0625)) * 2;

        double rotLeftSideX = xcLeft * cosine - zcLeft * sine;
        double yCornerTL = ((-yHeight) - (-up * 0.0625 + (walking * -0.0625))) * 2;
        double yCornerBL = ((+0.5 - yHeight) - (-up * 0.0625 + (walking * -0.0625))) * 2;
        double rotLeftSideZ = zcLeft * cosine + xcLeft * sine;

        double xcRight = (xRight / 2 - (right * 0.0625)) * 2;
        double zcRight = (zDistanceRight / 2 - (forward * 0.0625)) * 2;

        double rotRightSideX = xcRight * cosine - zcRight * sine;
        double yCornerTR = ((-yHeight) - (-up * 0.0625 + (walking * -0.0625))) * 2;
        double yCornerBR = ((+0.5 - yHeight) - (-up * 0.0625 + (walking * -0.0625))) * 2;
        double rotRightSideZ = zcRight * cosine + xcRight * sine;

        double texture30 = 0;
        double texture40 = 8;
        double clip = 0.5;

        if (rotLeftSideZ < clip && rotRightSideZ < clip) {
            return;
        }

        // Fixes clipping
        if (rotLeftSideZ < clip) {
            double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
            rotLeftSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotLeftSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            texture30 = texture30 + (texture40 - texture30) * clip0;
        }

        if (rotRightSideZ < clip) {
            double clip0 = (clip - rotLeftSideZ) / (rotRightSideZ - rotLeftSideZ);
            rotRightSideZ = rotLeftSideZ + (rotRightSideZ - rotLeftSideZ) * clip0;
            rotRightSideX = rotLeftSideX + (rotRightSideX - rotLeftSideX) * clip0;
            texture40 = texture30 + (texture40 - texture30) * clip0;
        }

        double xPixelLeft = (rotLeftSideX / rotLeftSideZ * height + width / 2);
        double xPixelRight = (rotRightSideX / rotRightSideZ * height + width / 2);

        if (xPixelLeft >= xPixelRight) {
            return;
        }

        int xPixelLeftInt = (int) (xPixelLeft);
        int xPixelRightInt = (int) (xPixelRight);

        if (xPixelLeftInt < 0) {
            xPixelLeftInt = 0;
        }
        if (xPixelRightInt > width) {
            xPixelRightInt = width;
        }

        double yPixelLeftTop = (yCornerTL / rotLeftSideZ * height + height / 2.0);
        double yPixelLeftBottom = (yCornerBL / rotLeftSideZ * height + height / 2.0);
        double yPixelRightTop = (yCornerTR / rotRightSideZ * height + height / 2.0);
        double yPixelRightBottom = (yCornerBR / rotRightSideZ * height + height / 2.0);

        double texture1 = 1 / rotLeftSideZ;
        double texture2 = 1 / rotRightSideZ;
        double texture3 = texture30 / rotLeftSideZ;
        double texture4 = texture40 / rotRightSideZ - texture3;

        for (int x = xPixelLeftInt; x < xPixelRightInt; x++) {
            double pixelRotation = (x - xPixelLeft) / (xPixelRight - xPixelLeft);
            double zWall = (texture1 + (texture2 - texture1) * pixelRotation);
            
            if (zBufferWall[x] > zWall) {
                continue;
            }
            zBufferWall[x] = zWall;
            
            double yPixelTop = yPixelLeftTop + (yPixelRightTop - yPixelLeftTop) * pixelRotation;
            double yPixelBottom = yPixelLeftBottom + (yPixelRightBottom - yPixelLeftBottom) * pixelRotation;

            int xTexture = (int) ((texture3 + texture4 * pixelRotation) / zWall);

            int yPixelTopInt = (int) (yPixelTop);
            int yPixelBottomInt = (int) (yPixelBottom);

            if (yPixelTopInt < 0) {
                yPixelTopInt = 0;
            }
            if (yPixelBottomInt > height) {
                yPixelBottomInt = height;
            }

            for (int y = yPixelTopInt; y < yPixelBottomInt; y++) {
                double pixelRotationY = (y - yPixelTop) / (yPixelBottom - yPixelTop);
                int yTexture = (int) (8 * pixelRotationY);
                pixels[x + y * width] = Texture.floor.pixels[((xTexture & 7) + 8) + (yTexture & 7) * 16]; // Wall texture
                //pixels[x + y * width] = xTexture * 100 + yTexture * 100 * 200;
                zBuffer[x + y * width] = 0;
            }
        }
    }

    public void renderDistanceLimiter() {
        for (int i = 0; i < width * height; i++) {
            int colour = pixels[i];
            int brightness = (int) (renderDistance / zBuffer[i]);

            if (brightness < 0) {
                brightness = 0;
            }
            if (brightness > 255) {
                brightness = 255;
            }

            int r = (colour >> 16) & 0xff;
            int g = (colour >> 8) & 0xff;
            int b = (colour) & 0xff;

            r = r * brightness / 255;
            g = g * brightness / 255;
            b = b * brightness / 255;

            pixels[i] = r << 16 | g << 8 | b;
        }
    }

}
