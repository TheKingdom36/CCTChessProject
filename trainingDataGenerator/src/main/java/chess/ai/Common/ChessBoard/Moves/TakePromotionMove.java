package chess.ai.Common.ChessBoard.Moves;

import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.PieceFactory;
import lombok.Getter;
import lombok.Setter;

public class TakePromotionMove extends TakeMove {

     @Getter @Setter
     protected Piece promotedTo = PieceFactory.getPiece(Type.Queen,this.piece.getColor());

    public TakePromotionMove(Position toPos, Position fromPos, Piece piece, Piece pieceTaken) {
        super(toPos, fromPos, piece, pieceTaken);
    }




    @Override
    public int compareTo(Move otherMove) {

        if(super.compareTo(otherMove)==1){
            if(promotedTo.compareTo(((TakePromotionMove)otherMove).getPromotedTo()) == 1){
                return 1;
            }

        }
        return 0;
    }

    @Override
    public Move Copy() {
        TakePromotionMove newMove =   new TakePromotionMove(this.to,this.from, PieceFactory.getPiece(piece.getType(),piece.getColor()), PieceFactory.getPiece(piece.getType(),piece.getColor()));
        newMove.setPromotedTo(PieceFactory.getPiece(promotedTo.getType(),promotedTo.getColor()));
        return newMove;
    }

    public String toString(){
        return "From x:"+from.getX()+ " y:"+from.getY()+"To x:"+to.getX()+ " y:"+to.getY() +" piece: " +piece.getType().name() + " taken: "+this.pieceTaken.getType().name() + " TakePromotionMove";
    }
}
