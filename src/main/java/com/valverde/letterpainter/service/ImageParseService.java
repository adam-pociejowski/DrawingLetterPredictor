package com.valverde.letterpainter.service;

import com.valverde.letterpainter.model.Point;
import com.valverde.letterpainter.model.ScaledImageData;
import com.valverde.letterpainter.util.FileUtils;
import com.valverde.letterpainter.util.ImageUtils;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageParseService {

    public ScaledImageData getScaledImageData(final String letterRelativePath) throws Exception {
        final BufferedImage image = FileUtils.getBufferedImage(LETTERS_PATH+"/"+letterRelativePath);
        return getScaledImageData(image);
    }

    private ScaledImageData getScaledImageData(final BufferedImage image) throws Exception {
        ImageUtils.changeFromAlphaToBlackColor(image);
        final ScaledImageData imageData = new ScaledImageData();
        final Point northWestPoint = ImageUtils.findNorthWestPoint(image);
        final Point southEastPoint = ImageUtils.findSouthEastPoint(image);
        final BufferedImage scaledImage = ImageUtils.trimAndScaleImage(image, northWestPoint, southEastPoint);
        imageData.setPoints(getPoints(scaledImage));
        imageData.setOriginalDrawingHeight(southEastPoint.getY() - northWestPoint.getY());
        imageData.setOriginalDrawingWidth(southEastPoint.getX() - northWestPoint.getX());
        return imageData;
    }

    public List<Point> getDrawingPointsFromPath(final String letterRelativePath) throws Exception {
        final BufferedImage image = FileUtils.getBufferedImage(LETTERS_PATH+"/"+letterRelativePath);
        return getPoints(image);
    }

    public ScaledImageData getDrawingScaledImageDataFile(final File file) throws Exception {
        final BufferedImage image = ImageIO.read(file);
        return getScaledImageData(image);
    }

    public List<Point> getDrawingPointsFromFile(final File file) throws Exception {
        final BufferedImage image = ImageIO.read(file);
        return getPoints(image);
    }

    private List<Point> getPoints(final BufferedImage image) throws Exception {
        final List<Point> points = new ArrayList<>();
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                final Color color = new Color(image.getRGB(i, j), true);
                if (ImageUtils.isPointDrawn(color))
                    points.add(new Point(i, j, ImageUtils.getPixelDarkness(color)));
            }
        }
        return points;
    }

    private String LETTERS_PATH = "src/main/resources/letters";
}