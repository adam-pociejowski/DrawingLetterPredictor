package com.valverde.letterpainter.xor;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class EncogXorRecognitionTest {

    @BeforeClass
    public static void setup() {
        encogXorRecognition = new EncogXorRecognition();
        encogXorRecognition.train();
    }

    @Test
    public void shouldReturnTrueWhenIsXor() {
        boolean xor = encogXorRecognition.isXor(1.0, 0.0);
        assertTrue(xor);
    }

    @Test
    public void shouldReturnFalseWhenBothValuesAreOne() {
        boolean xor = encogXorRecognition.isXor(1.0, 1.0);
        assertFalse(xor);
    }

    @Test
    public void shouldReturnFalseWhenBothValuesAreZero() {
        boolean xor = encogXorRecognition.isXor(0.0, 0.0);
        assertFalse(xor);
    }

    @Test
    public void shouldReturnFalseWhenBothValuesAreCloserToOne() {
        boolean xor = encogXorRecognition.isXor(0.7, 0.7);
        assertFalse(xor);
    }

    private static EncogXorRecognition encogXorRecognition;
}