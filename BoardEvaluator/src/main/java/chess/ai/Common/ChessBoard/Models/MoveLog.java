package chess.ai.Common.ChessBoard.Models;



import chess.ai.Common.ChessBoard.Moves.Move;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public class MoveLog {
    @Getter
    private List<Move> pastMoves;

    public MoveLog(){
        pastMoves = new ArrayList<>();
    }



    public void AddMove(Move move){
        pastMoves.add(move);
    }

    public MoveLog Copy(){
        MoveLog moveLog = new MoveLog();

        for (Move m:pastMoves) {
            moveLog.AddMove(m.Copy());
        }

        moveLog.getPastMoves();
        return moveLog;
    }

}
