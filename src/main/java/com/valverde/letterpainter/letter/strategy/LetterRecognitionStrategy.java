package com.valverde.letterpainter.letter.strategy;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.letter.LetterClassificationRecognition;
import java.io.File;

public interface LetterRecognitionStrategy {

    Letter predict(final File file) throws Exception;

    void train() throws Exception;

    String LETTERS_PATH = "src/main/resources/letters";
    int imageWidth = 20;
    int imageHeight = 20;
    LetterClassificationRecognition network = new LetterClassificationRecognition();
}