package com.valverde.letterpainter.util;

import com.valverde.letterpainter.model.Point;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageUtils {

    public static double getPixelDarkness(final Color color) {
        int rgbSum = color.getRed() + color.getGreen() + color.getBlue();
        return (255.0 - (rgbSum / 3.0)) / 255.0;
    }

    public static BufferedImage trimAndScaleImage(final BufferedImage image, final Point northWestPoint, final Point southEastPoint) {
        int height = southEastPoint.getY() - northWestPoint.getY();
        int width = southEastPoint.getX() - northWestPoint.getX();
        final BufferedImage trimImage = image.getSubimage(northWestPoint.getX(), northWestPoint.getY(), width, height);
        return createResizedCopy(trimImage, image.getWidth(), image.getHeight(), true);
    }

    public static Point findNorthWestPoint(final BufferedImage image) {
        int topPixel = image.getHeight();
        int mostLeftPixel = image.getWidth();

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final Color color = new Color(image.getRGB(i, j), true);
                if (isPointDrawn(color)) {
                    if (j < topPixel) {
                        topPixel = j;
                    }
                    if (i < mostLeftPixel) {
                        mostLeftPixel = i;
                    }
                }
            }
        }
        return new Point(mostLeftPixel, topPixel);
    }

    public static Point findSouthEastPoint(final BufferedImage image) {
        int bottomPixel = 0;
        int mostRightPixel = 0;

        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final Color color = new Color(image.getRGB(i, j), true);
                if (isPointDrawn(color)) {
                    if (j > bottomPixel) {
                        bottomPixel = j;
                    }
                    if (i > mostRightPixel) {
                        mostRightPixel = i;
                    }
                }
            }
        }
        return new Point(mostRightPixel, bottomPixel);
    }

    public static void changeFromAlphaToBlackColor(final BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final Color color = new Color(image.getRGB(i, j), true);
                int rgb = 255 - color.getAlpha();
                int newColor = new Color(rgb, rgb, rgb).getRGB();
                image.setRGB(i, j, newColor);
            }
        }
//        printFilledPoints(image);
    }

    private static void printFilledPoints(final BufferedImage image) {
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final Color color = new Color(image.getRGB(i, j), true);
                if (isPointDrawn(color)) {
                    System.out.println("("+i+","+j+")"+
                            " | alpha: "+color.getAlpha()+
                            " | red: "+color.getRed()+
                            " | green: "+color.getGreen()+
                            " | blue: "+color.getBlue());
                }
            }
        }
    }

    public static boolean isPointDrawn(final Color color) {
        return (color.getRed() <= ALPHA_THRESHOLD &&
                        color.getGreen() <= ALPHA_THRESHOLD &&
                        color.getBlue() <= ALPHA_THRESHOLD);
    }

    private static BufferedImage createResizedCopy(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    private static int ALPHA_THRESHOLD = 100;
}