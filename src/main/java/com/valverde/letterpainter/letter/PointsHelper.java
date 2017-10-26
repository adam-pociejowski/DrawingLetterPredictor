package com.valverde.letterpainter.letter;

import com.valverde.letterpainter.model.ImagePointsPart;
import com.valverde.letterpainter.model.Point;
import java.util.ArrayList;
import java.util.List;

public class PointsHelper {

    public List<ImagePointsPart> convertToParts(final List<Point> points, final boolean withDarkness) {
        final List<ImagePointsPart> parts = new ArrayList<>();
        for (int i = 0; i < horizontalParts; i++) {
            for (int j = 0; j < verticalParts; j++) {
                int startX = getPartWidth() * i;
                int startY = getPartHeight() * j;
                double amount;
                if (withDarkness) {
                    amount = getAmountOfPointsInPartWithDarkness(startX, startY, points);
                } else {
                    amount = getAmountOfPointsInPart(startX, startY, points);
                }
                parts.add(new ImagePointsPart(amount, i, j));
            }
        }
        return parts;
    }

    private double getAmountOfPointsInPartWithDarkness(final int startX, final int startY, final List<Point> points) {
        double sum = 0;
        for (Point point : points) {
            if (isPointInPart(point, startX, startY))
                sum += point.getDarkness();
        }
        return sum;
    }

    private double getAmountOfPointsInPart(final int startX, final int startY, final List<Point> points) {
        int amount = 0;
        for (Point point : points) {
            if (isPointInPart(point, startX, startY))
                amount++;
        }
        return amount;
    }

    private boolean isPointInPart(final Point p, final int startX, final int startY) {
        return (p.getX() >= startX && p.getX() <= startX + getPartWidth()) &&
                (p.getY() >= startY && p.getY() <= startY + getPartHeight());
    }

    private int getPartWidth() {
        return width / horizontalParts;
    }

    private int getPartHeight() {
        return height / verticalParts;
    }

    public PointsHelper(final int width, final int height,
                        final int horizontalParts, final int verticalParts) {
        this.width = width;
        this.height = height;
        this.horizontalParts = horizontalParts;
        this.verticalParts = verticalParts;
    }

    private int width;

    private int height;

    private int horizontalParts;

    private int verticalParts;
}