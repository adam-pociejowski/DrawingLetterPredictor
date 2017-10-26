package com.valverde.letterpainter.letter.image;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.service.ImageParseService;
import com.valverde.letterpainter.util.AppUtils;
import com.valverde.letterpainter.util.FileUtils;
import com.valverde.letterpainter.util.ImageUtils;
import lombok.extern.apachecommons.CommonsLog;
import org.encog.ml.data.MLData;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.train.strategy.ResetStrategy;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.training.propagation.resilient.ResilientPropagation;
import org.encog.platformspecific.j2se.data.image.ImageMLData;
import org.encog.platformspecific.j2se.data.image.ImageMLDataSet;
import org.encog.util.downsample.RGBDownsample;
import org.encog.util.downsample.SimpleIntensityDownsample;
import org.encog.util.simple.EncogUtility;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CommonsLog
public class ImageMLService {

    public Letter predict(final File file) throws Exception {
        final BufferedImage image = ImageIO.read(file);
        ImageUtils.changeFromAlphaToBlackColor(image);
        final ImageMLData imageData = new ImageMLData(image);
        imageData.downsample(downsample, false, imageHeight, imageWidth, 1, -1);
        int winner = network.winner(imageData);
        return AppUtils.getLetterByNumber(winner);
    }

    public void train() throws Exception {
        downsample = new RGBDownsample();

        final ImageMLDataSet dataSet = new ImageMLDataSet(downsample, false, 1, -1);
        appendTrainingImagesToTrainingSet(dataSet);
        network = EncogUtility.simpleFeedForward(dataSet.getInputSize(), hidden1, hidden2, dataSet.getIdealSize(), true);

        final ResilientPropagation propagation = new ResilientPropagation(network, dataSet);
        propagation.addStrategy(new ResetStrategy(strategyError, strategyCycles));
        EncogUtility.trainConsole(propagation, network, dataSet, minutes);
        log.info("Network trained successfully");
    }

    private void appendTrainingImagesToTrainingSet(final ImageMLDataSet trainingSet) throws Exception {
//        final List<Letter> letters = Arrays.asList(Letter.values());
        final List<Letter> letters = Arrays.asList(Letter.A, Letter.Z);
        for (Letter letter : letters) {
            final List<String> fileNames = FileUtils.getFileNames(letter);
            for (String fileName : fileNames) {
                final BufferedImage image = FileUtils.getBufferedImage(LETTERS_PATH+"/"+letter+"/"+fileName);
                ImageUtils.changeFromAlphaToBlackColor(image);
                final ImageMLData imageData = new ImageMLData(image);
                final MLData ideal = generateIdealOutput(letters.size(), letter.getNumber());
                trainingSet.add(imageData, ideal);
            }
        }
        trainingSet.downsample(imageHeight, imageWidth);
    }

    private MLData generateIdealOutput(final int lettersAmount, final int actualLetter) {
        final MLData ideal = new BasicMLData(lettersAmount);
        for (int i = 0; i < lettersAmount; i++) {
            if (i == actualLetter) {
                ideal.add(i, 1);
            } else {
                ideal.add(i, -1);
            }
        }
        return ideal;
    }

    private RGBDownsample downsample;

    private BasicNetwork network;

    private final int hidden1 = 100;

    private final int hidden2 = 0;

    private final double strategyError = 0.25;

    private final int minutes = 1;

    private final int strategyCycles = 50;

    private final String LETTERS_PATH = "src/main/resources/letters";

    private final int imageWidth = 20;

    private final int imageHeight = 20;
}
