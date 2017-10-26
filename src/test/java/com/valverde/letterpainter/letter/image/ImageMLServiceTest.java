package com.valverde.letterpainter.letter.image;

import com.valverde.letterpainter.enums.Letter;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import static org.junit.Assert.*;

public class ImageMLServiceTest {


    @Before
    public void setup() {
        imageMLService = new ImageMLService();
    }

    @Test
    public void trainTest() throws Exception {
        imageMLService.train();
        final Letter predictedLetter = imageMLService.predict(new File("src/main/resources/letters/Q/Q_3.png"));
        assertEquals(Letter.Q, predictedLetter);
    }

    private ImageMLService imageMLService;
}