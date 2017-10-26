package com.valverde.letterpainter.letter.strategy;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.letter.PointsHelper;
import com.valverde.letterpainter.model.ImagePointsPart;
import com.valverde.letterpainter.model.Point;
import com.valverde.letterpainter.service.ImageParseService;
import com.valverde.letterpainter.util.FileUtils;
import lombok.extern.apachecommons.CommonsLog;

import java.io.File;
import java.util.Arrays;
import java.util.List;

@CommonsLog
public class SimpleLetterPartsRecognitionStrategy implements LetterRecognitionStrategy {

    public Letter predict(final File file) throws Exception {
        final List<Point> testingPoints = imageParseService.getDrawingPointsFromFile(file);
        final List<ImagePointsPart> parts = pointsHelper.convertToParts(testingPoints, false);
        return network.predict(convertToStringArray(parts));
    }

    public void train() throws Exception {
        final List<Letter> letters = Arrays.asList(Letter.values());
        final File testDataFile = new File(DATA_FILE_PREFIX + (horizontalParts * verticalParts) + ".csv");
        for (Letter letter : letters) {
            final List<String> fileNames = FileUtils.getFileNames(letter);
            for (String fileName : fileNames) {
                final List<Point> points = imageParseService.getDrawingPointsFromPath(letter+"/"+fileName);
                List<ImagePointsPart> parts = pointsHelper.convertToParts(points, false);
                String row = FileUtils.createCsvRow(parts, letter);
                org.apache.commons.io.FileUtils.writeStringToFile(testDataFile, row, true);
            }
        }
        network.train(testDataFile, horizontalParts * verticalParts);
    }

    private String[] convertToStringArray(final List<ImagePointsPart> parts) {
        final String[] values = new String[parts.size()];
        int i = 0;
        for (ImagePointsPart part : parts)
            values[i++] = Double.toString(part.getSumValues());
        return values;
    }

    public SimpleLetterPartsRecognitionStrategy(final int horizontalParts, final int verticalParts) {
        this.horizontalParts = horizontalParts;
        this.verticalParts = verticalParts;
        this.pointsHelper = new PointsHelper(imageWidth, imageHeight, horizontalParts, verticalParts);
        this.imageParseService = new ImageParseService();
    }

    private final int horizontalParts;

    private final int verticalParts;

    private final String DATA_FILE_PREFIX = "letterPartsRecognition_";

    private final PointsHelper pointsHelper;

    private final ImageParseService imageParseService;
}