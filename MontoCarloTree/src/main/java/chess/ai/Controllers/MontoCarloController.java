package chess.ai.Controllers;

import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.montoCarlo.MontoCarloTrainingOutput;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Services.MontoCarloTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MontoCarloController {

    @Autowired
    MontoCarloTreeService treeService;


    @PostMapping("determineNextBestMove")
    public Move EvaluateBoard(@RequestBody Map<String,Object> parameterMap){
        return treeService.DetermineNextBestMove((BoardState) parameterMap.get("boardState"),(long)parameterMap.get("searchTime"));
    }

    @PostMapping("genTrainingSample")
    public MontoCarloTrainingOutput GenerateTrainingSample(@RequestBody Map<String,Object> parameterMap){
        return treeService.GenerateTrainingSample((BoardState) parameterMap.get("boardState"),(long)parameterMap.get("searchTime"));
    }



}
