package chess.ai.Common.neuralNet.Output;


import chess.ai.Common.ChessBoard.Models.Position;

public class QueenMoveOption extends MoveOption {
    int distanceFromPiecePos;

    public int getDistanceFromPiecePos() {
        return distanceFromPiecePos;
    }

    public QueenMoveOption(Position position, Position direction, int distanceFromPiecePos) {
        super(position,direction);
        this.distanceFromPiecePos = distanceFromPiecePos;
    }

    @Override
    public String toString() {
        return "QueenMoveOption{" +
                "distanceFromPiecePos=" + distanceFromPiecePos +
                ", piecePos=" + piecePos +
                ", direction=" + direction +
                '}';
    }
}
