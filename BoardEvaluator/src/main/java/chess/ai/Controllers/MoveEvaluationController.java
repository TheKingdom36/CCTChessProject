package chess.ai.Controllers;


import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.Networked.BatchOfEvaluatedBoards;
import chess.ai.Common.Redis.RedisObjectStorer;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import chess.ai.Services.BoardEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Class used to define Restful endpoints for registration
@RestController
@Validated
public class MoveEvaluationController  {
   // @Autowired
   // RedisMessageSubscriberService redisMessageSubscriber;

    @Autowired
    BoardEvaluateService boardEvaluateService;


    @PostMapping("evaluateBoard")
    public NNOutput EvaluateBoard(@RequestBody BoardState boardState) {
       boardState.getBoard().getBoardSquares()[0][0].getPiece().getType();
        return boardEvaluateService.EvaluateBoard(boardState);
    }

    @PostMapping("evaluateBatchOfBoards")
    public BatchOfEvaluatedBoards EvaluateBatchOfBoards(@RequestBody ArrayList<BoardState> boardStates) {
        return boardEvaluateService.EvaluateBatchOfBoards(boardStates);
    }


}
