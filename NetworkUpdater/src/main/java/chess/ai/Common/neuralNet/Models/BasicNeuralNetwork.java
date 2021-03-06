package chess.ai.Common.neuralNet.Models;


import chess.ai.Common.Networked.BatchOfEvaluatedBoards;
import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Layers.*;
import chess.ai.Common.neuralNet.Output.AllPieceMoveOptions;
import chess.ai.Common.neuralNet.Util.ChessInputConverter;

import java.util.ArrayList;
import java.util.List;


public class BasicNeuralNetwork implements INeuralNetwork {

    InputLayer inputLayer;
    FullyConnectedLayer fcLayer1;

    BatchNormLayer batchNormLayer1;
    BatchNormLayer batchNormLayer2;
    BatchNormLayer batchNormLayer3;

    ConvLayer convLayer1;
    ConvLayer convLayer2;

    ReLULayer ReLULayer ;
    ReLULayer ReLULayer2 ;
    ReLULayer ReLULayer3;

    OutputLayer outputLayer;

    public BasicNeuralNetwork(){

    }

    public BasicNeuralNetwork(NetworkWeights networkWeights){
        this.Configuration(networkWeights);
    }

    @Override
    public NNOutput EvaluateBoard(BoardState boardState){
        NNOutput nnOutput = new NNOutput();
        Layer.setBatchSize(1);


        Plane[][] inputplanes = new Plane[1][1];
        inputplanes[0]= ChessInputConverter.ConvertChessBoardToInput(boardState);
        inputLayer = new InputLayer(inputplanes);

        inputLayer.setNextLayer(convLayer1);

        convLayer1.setPreviousLayer(inputLayer);

        outputLayer.CalculateOutputplanes();
        Plane[][] policy= outputLayer.getPolicy();

        double[] probs = new double[policy[0][0].getWidth()];
        for(int i=0;i<policy[0][0].getWidth();i++){
            probs[i] = policy[0][0].getValues()[i][0];
        }

        nnOutput.setProbabilities(probs);
        nnOutput.setWin_score(outputLayer.getValues()[0][0].getValues()[0][0]);

        return nnOutput;
    }




    @Override
    public BatchOfEvaluatedBoards EvaluateBatchOfBoards(List<BoardState> boardStates) {
        List<NNOutput> nnOutputList = new ArrayList<>();

        Layer.setBatchSize(boardStates.size());

        Plane[][] inputplanes = new Plane[boardStates.size()][21];

        for(int i=0; i< boardStates.size();i++){

            inputplanes[i]= ChessInputConverter.ConvertChessBoardToInput(boardStates.get(i));
        }

        inputLayer = new InputLayer(inputplanes);

        inputLayer.setNextLayer(convLayer1);

        convLayer1.setPreviousLayer(inputLayer);

        outputLayer.CalculateOutputplanes();

        Plane[][] policys= outputLayer.getPolicy();
        Plane[][] values = outputLayer.getValues();

        double[] tempProbs;
        NNOutput tempNNOutput;
        for(int batchElement=0;batchElement<boardStates.size();batchElement++) {

            tempProbs = new double[policys[batchElement][0].getWidth()];
            for (int i = 0; i < policys[batchElement][0].getWidth(); i++) {
                tempProbs[i] = policys[batchElement][0].getValues()[i][0];
            }
            tempNNOutput = new NNOutput();
            tempNNOutput.setProbabilities(tempProbs);
            tempNNOutput.setWin_score(values[batchElement][0].getValues()[0][0]);

            nnOutputList.add(tempNNOutput);
        }

        BatchOfEvaluatedBoards batchOfEvaluatedBoards = new BatchOfEvaluatedBoards(nnOutputList, this);

        return batchOfEvaluatedBoards;
    }

    @Override
    public void Configuration() {

    }


    public void Configuration(NetworkWeights networkWeights) {


        fcLayer1 = new FullyConnectedLayer(20);
        fcLayer1.setKernels(networkWeights.getFCKernels());


        batchNormLayer1 = new BatchNormLayer();
        batchNormLayer1.setBeta(networkWeights.batchNorm1BetaValues);
        batchNormLayer1.setGamma(networkWeights.batchNorm1GammaValues);

        batchNormLayer2 = new BatchNormLayer();
        batchNormLayer2.setBeta(networkWeights.batchNorm2BetaValues);
        batchNormLayer2.setGamma(networkWeights.batchNorm2GammaValues);

        batchNormLayer3 = new BatchNormLayer();
        batchNormLayer3.setBeta(networkWeights.batchNorm3BetaValues);
        batchNormLayer3.setGamma(networkWeights.batchNorm3GammaValues);

        //number of input planes theres 21 input planes
        convLayer1 = new ConvLayer(10,1,3,3,21,1);
        convLayer1.setKernels(networkWeights.getConv1Kernels());

        convLayer2 = new ConvLayer(10,1,3,3,10,1);
        convLayer2.setKernels(networkWeights.getConv2Kernels());

        ReLULayer = new ReLULayer();
        ReLULayer2 = new ReLULayer();
        ReLULayer3 = new ReLULayer();

        outputLayer = new OutputLayer(20, AllPieceMoveOptions.getMoveOptions().size());
        outputLayer.setKernels(networkWeights.getOutputKernels());
        outputLayer.setPolicyKernels(networkWeights.getPolicyHeadKernels());
        outputLayer.setValueKernels(networkWeights.getValueHeadKernels());

        convLayer1.setNextLayer(batchNormLayer1);

        batchNormLayer1.setPreviousLayer(convLayer1);
        batchNormLayer1.setNextLayer(ReLULayer);

        ReLULayer.setPreviousLayer(batchNormLayer1);
        ReLULayer.setNextLayer(convLayer2);

        convLayer2.setPreviousLayer(ReLULayer);
        convLayer2.setNextLayer(batchNormLayer2);

        batchNormLayer2.setPreviousLayer(convLayer2);
        batchNormLayer2.setNextLayer(ReLULayer2);

        ReLULayer2.setPreviousLayer(batchNormLayer2);
        ReLULayer2.setNextLayer(fcLayer1);

        fcLayer1.setPreviousLayer(ReLULayer2);
        fcLayer1.setNextLayer(batchNormLayer3);

        batchNormLayer3.setPreviousLayer(fcLayer1);
        batchNormLayer3.setNextLayer(ReLULayer3);

        ReLULayer3.setPreviousLayer(batchNormLayer3);
        ReLULayer3.setNextLayer(outputLayer);

        outputLayer.setPreviousLayer(ReLULayer3);


    }


    @Override
    public Layer GetInputLayer() {
        return inputLayer;
    }

    public synchronized void AssignNewWeights(NetworkWeights networkWeights){
        convLayer1.setKernels(networkWeights.getConv1Kernels());
        convLayer2.setKernels(networkWeights.getConv2Kernels());
        outputLayer.setKernels(networkWeights.getOutputKernels());
        fcLayer1.setKernels(networkWeights.getFCKernels());
        batchNormLayer1.setBeta(networkWeights.batchNorm1BetaValues);
        batchNormLayer2.setBeta(networkWeights.batchNorm2BetaValues);
        batchNormLayer3.setBeta(networkWeights.batchNorm3BetaValues);
        batchNormLayer1.setGamma(networkWeights.batchNorm1GammaValues);
        batchNormLayer2.setGamma(networkWeights.batchNorm2GammaValues);
        batchNormLayer3.setGamma(networkWeights.batchNorm3GammaValues);

    }

    @Override
    public NetworkWeights UpdateWeights() {
        convLayer1.UpdateWeights();
        convLayer2.UpdateWeights();
        outputLayer.UpdateWeights();
        fcLayer1.UpdateWeights();
        batchNormLayer1.UpdateWeights();
        batchNormLayer2.UpdateWeights();
        batchNormLayer3.UpdateWeights();

        return getNetworkWeights();
    }

    @Override
    public NetworkWeights getNetworkWeights() {
        NetworkWeights networkWeights = new NetworkWeights();

        networkWeights.setConv1Kernels(convLayer1.getKernels());
        networkWeights.setConv2Kernels(convLayer2.getKernels());
        networkWeights.setFCKernels(fcLayer1.getKernels());
        networkWeights.setOutputKernels(outputLayer.getKernels());
        networkWeights.setBatchNorm1GammaValues(batchNormLayer1.getGamma());
        networkWeights.setBatchNorm2GammaValues(batchNormLayer2.getGamma());
        networkWeights.setBatchNorm3GammaValues(batchNormLayer3.getGamma());
        networkWeights.setBatchNorm1BetaValues(batchNormLayer1.getBeta());
        networkWeights.setBatchNorm2BetaValues(batchNormLayer2.getBeta());
        networkWeights.setBatchNorm3BetaValues(batchNormLayer3.getBeta());

        return networkWeights;
    }

    private Kernel[] initializeKernels(int numOfKernels, int kernalWidth, int kernalHeight, int kernalDepth){

            Kernel[] kernels = new Kernel[numOfKernels];

            for(int i=0;i<numOfKernels;i++){
                kernels[i] = new Kernel(kernalWidth,kernalHeight,kernalDepth);
                kernels[i].InitializeRandomValues();
            }

            return kernels;
        }

}

