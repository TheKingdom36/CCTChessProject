package chess.ai.Common.ChessBoard.Moves;


import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.PieceFactory;

public class NormalMove extends Move {
    public NormalMove(Position to , Position from, Piece piece){
        super(to,from,piece);
    }

    @Override
    public String toString() {
        return "NormalMove{" +
                "piece=" + piece +
                ", to=" + to.toString() +
                ", from=" + from.toString() +
                '}';
    }

    @Override
    public Move Copy() {
        return  new NormalMove(this.to,this.from, PieceFactory.getPiece(piece.getType(),piece.getColor()));
    }


}
