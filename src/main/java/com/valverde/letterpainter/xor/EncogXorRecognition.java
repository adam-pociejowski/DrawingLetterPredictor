package com.valverde.letterpainter.xor;

import org.encog.ml.CalculateScore;
import org.encog.ml.data.MLData;
import org.encog.ml.data.MLDataSet;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.ml.data.basic.BasicMLDataSet;
import org.encog.ml.ea.train.EvolutionaryAlgorithm;
import org.encog.neural.neat.NEATNetwork;
import org.encog.neural.neat.NEATPopulation;
import org.encog.neural.neat.NEATUtil;
import org.encog.neural.networks.training.TrainingSetScore;
import org.encog.util.simple.EncogUtility;

class EncogXorRecognition {

    boolean isXor(final double value, final double value2) {
        double[] array = {value, value2};
        MLData data = new BasicMLData(array);
        final MLData output = network.compute(data);
        return output.getData(0) > THRESHOLD;
    }

    void train() {
        MLDataSet trainingSet = new BasicMLDataSet(XOR_INPUT, XOR_IDEAL);
        NEATPopulation pop = new NEATPopulation(2,1,1000);
        pop.setInitialConnectionDensity(1.0);
        pop.reset();

        CalculateScore score = new TrainingSetScore(trainingSet);
        final EvolutionaryAlgorithm train = NEATUtil.constructNEATTrainer(pop, score);

        do {
            train.iteration();
//            System.out.println("Epoch #" + train.getIteration() + " Error:" + train.getError()+ ", Species:" + pop.getSpecies().size());
        } while(train.getError() > 0.01);
        network = (NEATNetwork)train.getCODEC().decode(train.getBestGenome());
        System.out.println("Neural Network Results:");
        EncogUtility.evaluate(network, trainingSet);
    }

    private static NEATNetwork network;

    private static final double THRESHOLD = 0.5;

    private static double XOR_INPUT[][] = { { 0.0, 0.0 }, { 1.0, 0.0 },
            { 0.0, 1.0 }, { 1.0, 1.0 } };

    private static double XOR_IDEAL[][] = { { 0.0 }, { 1.0 }, { 1.0 }, { 0.0 } };
}