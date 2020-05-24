package chess.ai.Common.Networked;

import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.montoCarlo.MontoCarloTrainingOutput;
import chess.ai.Common.montoCarlo.interfaces.IMontoCarloTree;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Configuration;

import java.util.HashMap;
import java.util.Map;

public class NetworkedMontoCarloTree implements IMontoCarloTree {
    @Override
    public Move findNextMove(BoardState boardState, long searchTime) {
        String url = "http://"+ Configuration.prop.get("DetermineNextBestMoveEndpoint");
        Map<String,Object> map = new HashMap<>();

        map.put("boardState",boardState);
        map.put("searchTime",searchTime);

        Move move = (Move)PostRequestService.HttpPost(url,map);
        return move;
    }

    @Override
    public MontoCarloTrainingOutput findNextMoveTraining(BoardState boardState, long searchTime) {
        String url = "http://"+ Configuration.prop.get("GenerateTrainingSampleEndpoint");
        Map<String,Object> map = new HashMap<>();

        map.put("boardState",boardState);
        map.put("searchTime",searchTime);

        MontoCarloTrainingOutput montoCarloTrainingOutput = (MontoCarloTrainingOutput)PostRequestService.HttpPost(url,map);
        return montoCarloTrainingOutput;
    }
}
