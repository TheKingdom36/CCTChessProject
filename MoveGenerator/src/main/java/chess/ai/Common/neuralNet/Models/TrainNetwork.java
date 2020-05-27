package chess.ai.Common.neuralNet.Models;

import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Training.TrainingData;

//Done on network Updater
public class TrainNetwork {

    public TrainNetwork(){

    }



    //Not yet implemented
    public static NetworkWeights Train(TrainingData trainingData, INeuralNetwork neuralNetwork){

        neuralNetwork.GetInputLayer().CalculateErrors();


        NetworkWeights newNetworkWeights = neuralNetwork.UpdateWeights();


        return newNetworkWeights;

    }
}
