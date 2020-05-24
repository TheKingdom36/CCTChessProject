package chess.ai.Services;

import chess.ai.Common.Networked.BatchOfEvaluatedBoards;
import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class BoardEvaluateService {

    @Autowired
    @Qualifier("basic")
    INeuralNetwork neuralNetwork;


    public NNOutput EvaluateBoard(@RequestBody BoardState boardState) {
        return neuralNetwork.EvaluateBoard(boardState);
    }

    public BatchOfEvaluatedBoards EvaluateBatchOfBoards(@RequestBody List<BoardState> boardStates) {
        return neuralNetwork.EvaluateBatchOfBoards(boardStates);
    }
}
