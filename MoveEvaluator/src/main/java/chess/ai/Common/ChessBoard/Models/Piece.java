package chess.ai.Common.ChessBoard.Models;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.MoveCheckers.MoveChecker;
import chess.ai.Common.ChessBoard.Moves.Move;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class Piece implements Comparable<Piece>{

    @Getter @Setter private Color color;
    @Setter private MoveChecker mvChecker;
    @Getter @Setter private Type type;

    public Piece(){
    }


    public List<Move> GetAvailableMoves(Position piecePos, BoardSquare[][] board)  {
        return mvChecker.checkAllAvailableMoves(piecePos,board);
    }

    @Override
    public int compareTo(Piece otherPiece) {
        if(color == otherPiece.getColor() && type == otherPiece.getType()){
            return 1;
        }

        return 0;
    }
}
