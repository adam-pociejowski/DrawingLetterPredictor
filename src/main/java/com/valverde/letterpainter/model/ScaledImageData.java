package com.valverde.letterpainter.model;

import lombok.Data;

import java.util.List;

@Data
public class ScaledImageData {

    private double originalDrawingWidth;

    private double originalDrawingHeight;

    private List<Point> points;
}
