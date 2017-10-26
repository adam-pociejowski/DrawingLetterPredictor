package com.valverde.letterpainter.service;

import com.valverde.letterpainter.model.Point;
import com.valverde.letterpainter.model.ScaledImageData;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class ImageParseServiceTest {

    @Before
    public void setup() {
        imageParseService = new ImageParseService();
    }

    @Test
    public void filledImageTest() throws Exception {
        List<Point> points = imageParseService.getDrawingPointsFromPath("A/A_0");
        assertFalse(points.isEmpty());
    }

    @Test
    public void emptyImageTest() throws Exception {
        List<Point> points = imageParseService.getDrawingPointsFromPath("empty/empty_0");
        assertTrue(points.isEmpty());
    }

    @Test
    public void getScaledImageDataTest() throws Exception {
        ScaledImageData scaledImageData = imageParseService.getScaledImageData("A/A_0.png");
    }

    private ImageParseService imageParseService;
}