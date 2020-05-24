package chess.ai.Common.ChessBoard.MoveCheckers;



import chess.ai.Common.ChessBoard.Models.BoardSquare;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Moves.Move;

import java.util.List;

public abstract class MoveChecker {
    Piece piece;

    public MoveChecker(Piece piece){
        this.piece = piece;
    }

   public abstract List<Move> checkAllAvailableMoves(Position piecePos, BoardSquare[][] board) ;
}
