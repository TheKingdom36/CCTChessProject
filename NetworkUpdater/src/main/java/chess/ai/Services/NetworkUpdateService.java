package chess.ai.Services;


import chess.ai.Common.montoCarlo.TrainingSample;
import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Models.*;
import chess.ai.Common.neuralNet.Training.TrainingData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NetworkUpdateService {

    @Autowired
    RedisService redisService;

    @Autowired
    @Qualifier("network")
    INeuralNetwork neuralNetwork;

    public void processSamples(List<TrainingSample> trainingSamples){


        List<BoardState> boardStates = new ArrayList<>();
        for(TrainingSample sample : trainingSamples){
            boardStates.add(new BoardState( new BoardState(sample.getBoard(),sample.getPlayerColor())));
        }

        List<NNOutput> batchOfNNOutputs = neuralNetwork.EvaluateBatchOfBoards(boardStates).getNnOutputs();


        TrainingData trainingData = new TrainingData(trainingSamples,batchOfNNOutputs);

        TrainNetwork.Train(trainingData,neuralNetwork);

        redisService.PublishMessage(neuralNetwork.getNetworkWeights());

        redisService.StoreInRedis("NetworkWeights",neuralNetwork.getNetworkWeights());
    }
}
