package com.valverde.letterpainter.service;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.letter.image.ImageMLService;
import com.valverde.letterpainter.letter.strategy.ScaledLetterPartsRecognitionStrategy;
import com.valverde.letterpainter.letter.strategy.SimpleLetterPartsRecognitionStrategy;
import com.valverde.letterpainter.letter.strategy.LetterRecognition;
import com.valverde.letterpainter.letter.strategy.LetterRecognitionStrategy;
import com.valverde.letterpainter.util.FileUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import static com.valverde.letterpainter.letter.strategy.LetterRecognition.SCALED_PARTS_RECOGNITION;

@Service
@CommonsLog
public class LetterRecognitionService {

    public Letter predictLetter(final MultipartFile multipartFile) throws Exception {
        final File file = FileUtils.convertToFile(multipartFile);
        return strategy.predict(file);
    }

    public void trainModel() throws Exception {
        final LetterRecognition strategyType = SCALED_PARTS_RECOGNITION;
        strategy = getStrategy(strategyType);
        strategy.train();
        log.info("Model trained with strategy "+ strategyType);
    }

    private LetterRecognitionStrategy getStrategy(final LetterRecognition type) {
        switch (type) {
            case SIMPLE_PARTS_RECOGNITION:
                return new SimpleLetterPartsRecognitionStrategy(5, 5);
            case SCALED_PARTS_RECOGNITION:
                return new ScaledLetterPartsRecognitionStrategy(5, 5);
        }
        throw new RuntimeException("No strategy found");
    }

    private ImageMLService imageMLService;

    private LetterRecognitionStrategy strategy;
}