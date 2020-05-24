package chess.ai.Common.neuralNet.Interfaces;


import chess.ai.Common.Networked.BatchOfEvaluatedBoards;
import chess.ai.Common.neuralNet.Layers.Layer;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface INeuralNetwork {
    NNOutput EvaluateBoard(BoardState boardState);

    BatchOfEvaluatedBoards EvaluateBatchOfBoards(List<BoardState> boardStates);

    void Configuration();

    Layer GetInputLayer();

    NetworkWeights UpdateWeights();

    NetworkWeights getNetworkWeights();
}
