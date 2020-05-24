package chess.ai.Common.neuralNet.ActivationFunctions;


public class Softmax {


    public static double[] calculate(double[] values){
        double[] exponentValues = new double[values.length];
        double[] outputValues = new double[values.length];
        int exponentSum=0;
        for(int i=0;i<values.length;i++){
            exponentValues[i] = Math.exp(values[i]);
            exponentSum += exponentValues[i];
        }

        for(int i=0;i<outputValues.length;i++){
            outputValues[i] = exponentValues[i]/exponentSum;
        }

        return outputValues;
    }
}
