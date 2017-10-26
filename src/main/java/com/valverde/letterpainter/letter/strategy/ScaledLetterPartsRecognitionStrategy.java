package com.valverde.letterpainter.letter.strategy;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.letter.PointsHelper;
import com.valverde.letterpainter.model.ImagePointsPart;
import com.valverde.letterpainter.model.ScaledImageData;
import com.valverde.letterpainter.service.ImageParseService;
import com.valverde.letterpainter.util.FileUtils;
import lombok.extern.apachecommons.CommonsLog;
import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

@CommonsLog
public class ScaledLetterPartsRecognitionStrategy implements LetterRecognitionStrategy {

    public Letter predict(final File file) throws Exception {
        final ScaledImageData imageData = imageParseService.getDrawingScaledImageDataFile(file);
        final List<ImagePointsPart> parts = pointsHelper.convertToParts(imageData.getPoints(), true);
        return network.predict(convertToStringArray(parts, imageData));
    }

    public void train() throws Exception {
        final List<Letter> letters = Arrays.asList(Letter.values());
        final File testDataFile = new File(DATA_FILE_PREFIX + (horizontalParts * verticalParts) + ".csv");
        for (Letter letter : letters) {
            final List<String> fileNames = FileUtils.getFileNames(letter);
            for (String fileName : fileNames) {
                final ScaledImageData imageData = imageParseService.getScaledImageData(letter + "/" + fileName);
                final List<ImagePointsPart> parts = pointsHelper.convertToParts(imageData.getPoints(), true);
                final String row = createCsvRow(parts, letter, imageData);
                org.apache.commons.io.FileUtils.writeStringToFile(testDataFile, row, true);
            }
        }
        network.train(testDataFile, (horizontalParts * verticalParts) + 2);
    }

    private String createCsvRow(final List<ImagePointsPart> parts, final Letter letter, final ScaledImageData imageData) {
        String row = "";
        for (ImagePointsPart part : parts)
            row += decimalFormat.format(part.getSumValues()) + ",";
        row += imageData.getOriginalDrawingWidth() + ",";
        row += imageData.getOriginalDrawingHeight() + ",";
        row += letter +"\n";
        return row;
    }

    private String[] convertToStringArray(final List<ImagePointsPart> parts, final ScaledImageData imageData) {
        final String[] values = new String[parts.size() + 2];
        int i = 0;
        for (ImagePointsPart part : parts)
            values[i++] = decimalFormat.format(part.getSumValues());
        values[i++] = Double.toString(imageData.getOriginalDrawingWidth());
        values[i] = Double.toString(imageData.getOriginalDrawingHeight());
        return values;
    }

    public ScaledLetterPartsRecognitionStrategy(final int horizontalParts, final int verticalParts) {
        this.horizontalParts = horizontalParts;
        this.verticalParts = verticalParts;
        this.pointsHelper = new PointsHelper(imageWidth, imageHeight, horizontalParts, verticalParts);
        this.imageParseService = new ImageParseService();
    }

    private final DecimalFormat decimalFormat = new DecimalFormat("#.####");

    private final int horizontalParts;

    private final int verticalParts;

    private final String DATA_FILE_PREFIX = "scaledLetterPartsRecognition_";

    private final PointsHelper pointsHelper;

    private final ImageParseService imageParseService;
}