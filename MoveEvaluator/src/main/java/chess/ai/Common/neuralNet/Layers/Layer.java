package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.Map;

import java.util.Arrays;

public abstract class Layer {
    protected Layer nextLayer;
    protected Layer previousLayer;

    // a double array of Maps first papameter is the batchnumber second parameter is the map number
    protected Map[][] outputMaps;
    protected static int batchSize;

    //batchSize, error plane, error plane width , error plane height
    protected double[][][][] errors;

    public static int getBatchSize() {
        return batchSize;
    }
    public static void setBatchSize(int batchSize) {
        Layer.batchSize = batchSize;
    }

    public double[][][][] getErrors() {
        return errors;
    }
    public abstract Kernel[] getKernels();


    public Layer getNextLayer(){
        return nextLayer;
    }
    public void setNextLayer(Layer layer){
        this.nextLayer = layer;
    }
    Layer getPreviousLayer(){
        return previousLayer;
    }
    public void setPreviousLayer(Layer layer){
        previousLayer = layer;
    }

    public abstract void CalculateOutputMaps();
    public abstract void CalculateErrors();

    public Map[][] getOutputMaps(){
        return outputMaps;
    }
    public void setOutputMaps(Map[][] maps){
        this.outputMaps = maps;
    }

    public double[][] extendMatrix(double[][] matrix,int extendByWidth,int extendByHeight){

        int m = matrix.length;
        int n = matrix[0].length;


        final double[][] extendMatrix = new double[m + 2 * (extendByWidth)][n + 2 * (extendByHeight)];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                extendMatrix[i + extendByWidth - 1][j + extendByHeight - 1] = matrix[i][j];
        }

        return extendMatrix;
    }


    @Override
    public String toString() {
        return "Layer{" +
                ", nextLayer=" + nextLayer +
                ", previousLayer=" + previousLayer +
                ", outputMaps=" + Arrays.toString(outputMaps) +
                ", errors=" + Arrays.toString(errors) +
                '}';
    }
}
