package chess.ai.Common.neuralNet.Output;


import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Position;

public class PawnPromotionMoveOption extends MoveOption {
    Type promotedToPieceType;

    public Type getPromotedToPieceType() {
        return promotedToPieceType;
    }

    public PawnPromotionMoveOption(Position piecePos, Position direction, Type promotedToPieceType) {
        super(piecePos, direction);
        this.promotedToPieceType = promotedToPieceType;
    }

}
