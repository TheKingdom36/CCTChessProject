package chess.ai.Services;

import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.montoCarlo.MontoCarloTrainingOutput;
import chess.ai.Common.montoCarlo.interfaces.IMontoCarloTree;
import chess.ai.Common.neuralNet.Models.BoardState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class MontoCarloTreeService {

    @Autowired
    @Qualifier("local")
    IMontoCarloTree tree;

    private long maxSearchTime = 2000;
    private long minSearchTime = 200;

    public Move DetermineNextBestMove(BoardState boardState, long searchTime){

        if(searchTime>maxSearchTime){
            searchTime = maxSearchTime;
        }else if(searchTime<minSearchTime){
            searchTime = minSearchTime;
        }

        return tree.findNextMove( boardState,searchTime);
    }


    public MontoCarloTrainingOutput GenerateTrainingSample(BoardState boardState, long searchTime){

        if(searchTime>maxSearchTime){
            searchTime = maxSearchTime;
        }else if(searchTime<minSearchTime){
            searchTime = minSearchTime;
        }


        return tree.findNextMoveTraining(boardState,searchTime);
    }

}
