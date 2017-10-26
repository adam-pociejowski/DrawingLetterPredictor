package com.valverde.letterpainter.letter;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.util.AppUtils;
import org.apache.commons.io.FileUtils;
import org.encog.ConsoleStatusReportable;
import org.encog.ml.MLRegression;
import org.encog.ml.data.MLData;
import org.encog.ml.data.versatile.NormalizationHelper;
import org.encog.ml.data.versatile.VersatileMLDataSet;
import org.encog.ml.data.versatile.columns.ColumnDefinition;
import org.encog.ml.data.versatile.columns.ColumnType;
import org.encog.ml.data.versatile.sources.CSVDataSource;
import org.encog.ml.data.versatile.sources.VersatileDataSource;
import org.encog.ml.factory.MLMethodFactory;
import org.encog.ml.model.EncogModel;
import org.encog.util.csv.CSVFormat;
import org.encog.util.simple.EncogUtility;
import java.io.File;
import java.util.List;

import static org.encog.ml.data.versatile.columns.ColumnType.continuous;

public class LetterClassificationRecognition {

    public Letter predict(final String[] values) {
        if (AppUtils.isNull(network))
            throw new RuntimeException("Network is not trained yet");
        final NormalizationHelper helper = data.getNormHelper();
        final MLData input = helper.allocateInputVector();
        helper.normalizeInputVector(values, input.getData(),false);
        final MLData output = network.compute(input);
        final String predicted = helper.denormalizeOutputVectorToString(output)[0];
        return Letter.valueOf(predicted);
    }

    public void train(final File file, final int valuesAmount) throws Exception {
        final VersatileDataSource source = new CSVDataSource(file, false, CSVFormat.DECIMAL_POINT);
        data = new VersatileMLDataSet(source);
        for (int i = 0; i < valuesAmount; i++) {
            data.defineSourceColumn("part"+i, i, continuous);
        }
        final ColumnDefinition outputColumn = data.defineSourceColumn("letters", valuesAmount, ColumnType.nominal);
        data.analyze();
        data.defineSingleOutputOthersInput(outputColumn);
        final EncogModel model = new EncogModel(data);
        model.selectMethod(data, MLMethodFactory.TYPE_FEEDFORWARD);
        model.setReport(new ConsoleStatusReportable());
        data.normalize();

        model.holdBackValidation(0.3, true, 1001);
        model.selectTrainingType(data);
        network = (MLRegression)model.crossvalidate(7, true);
        System.out.println( "Training error: " + EncogUtility.calculateRegressionError(network, model.getTrainingDataset()));
        System.out.println( "Validation error: " + EncogUtility.calculateRegressionError(network, model.getValidationDataset()));
   }

    private void testModel(final File file, final VersatileMLDataSet data) throws Exception {
        final NormalizationHelper helper = data.getNormHelper();
        System.out.println(helper.toString());
        System.out.println("Final model: " + network);
        final MLData input = helper.allocateInputVector();
        int correctPredicted = 0;
        final List<String> lines = FileUtils.readLines(file, "UTF-8");
        for (String row : lines) {
            final String[] rowValues = row.split(",");
            final String correct = rowValues[rowValues.length - 1];
            final String[] values = cutArray(rowValues, rowValues.length - 1);
            helper.normalizeInputVector(values, input.getData(),false);
            final MLData output = network.compute(input);
            final String predicted = helper.denormalizeOutputVectorToString(output)[0];

            if (predicted.equals(correct))
                correctPredicted++;
        }
        System.out.println("Correctly predicted: "+correctPredicted+"/"+lines.size());
    }

    private String[] cutArray(final String[] strings, final int size) {
        String[] values = new String[size];
        System.arraycopy(strings, 0, values, 0, size);
        return values;
    }

    private VersatileMLDataSet data;

    private MLRegression network;
}