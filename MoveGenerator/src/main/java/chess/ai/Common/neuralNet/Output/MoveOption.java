package chess.ai.Common.neuralNet.Output;


import chess.ai.Common.ChessBoard.Models.Position;

public class MoveOption {
     Position piecePos;
     Position direction;

    public Position getPiecePos() {
        return piecePos;
    }

    public Position getDirection() {
        return direction;
    }

    public MoveOption(Position piecePos, Position direction){
         this.direction = direction;

         this.piecePos = piecePos;
     }

    @Override
    public String toString() {
        return "MoveOption{" +
                "piecePos=" + piecePos +
                ", direction=" + direction +
                '}';
    }
}
