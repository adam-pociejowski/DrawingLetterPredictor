package com.valverde.letterpainter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Point {
    private int x;
    private int y;
    private double darkness;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
}
