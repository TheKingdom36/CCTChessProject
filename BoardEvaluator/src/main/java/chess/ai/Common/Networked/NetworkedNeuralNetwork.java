package chess.ai.Common.Networked;

import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Layers.Layer;
import chess.ai.Common.neuralNet.Models.BasicNeuralNetwork;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import chess.ai.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("network")
public class NetworkedNeuralNetwork implements INeuralNetwork {

    BasicNeuralNetwork basicNeuralNetwork;

    @Override
    public NNOutput EvaluateBoard(BoardState boardState) {
        return (NNOutput) PostRequestService.PostBoardState(Configuration.prop.getProperty("BoardEvaluateEndpoint"),boardState);

    }

    @Override
    public BatchOfEvaluatedBoards EvaluateBatchOfBoards(List<BoardState> boardStates) {
        BatchOfEvaluatedBoards evaluatedBoards = (BatchOfEvaluatedBoards) PostRequestService.PostBoardStates(Configuration.prop.getProperty("BatchOfBoardEvaluateEndpoint"),boardStates);
        basicNeuralNetwork = evaluatedBoards.getBasicNeuralNetwork();
        return evaluatedBoards;
    }

    @Override
    public void Configuration() {

    }

    @Override
    public Layer GetInputLayer() {
        return basicNeuralNetwork.GetInputLayer();
    }

    @Override
    public NetworkWeights UpdateWeights() {
        return basicNeuralNetwork.UpdateWeights();
    }

    @Override
    public NetworkWeights getNetworkWeights() {
        return  basicNeuralNetwork.getNetworkWeights();
    }
}
