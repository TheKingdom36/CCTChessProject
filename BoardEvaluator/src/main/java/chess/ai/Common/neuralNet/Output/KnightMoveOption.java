package chess.ai.Common.neuralNet.Output;


import chess.ai.Common.ChessBoard.Models.Position;

public class KnightMoveOption extends MoveOption {
    public KnightMoveOption(Position position, Position direction) {
        super(position,direction);
    }

    @Override
    public String toString() {
        return "KnightMoveOption{" +
                "piecePos=" + piecePos +
                ", direction=" + direction +
                '}';
    }
}
