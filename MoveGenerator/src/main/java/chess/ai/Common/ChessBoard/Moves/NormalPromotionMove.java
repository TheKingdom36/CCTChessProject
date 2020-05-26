package chess.ai.Common.ChessBoard.Moves;


import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.PieceFactory;
import lombok.Getter;
import lombok.Setter;

public class NormalPromotionMove extends NormalMove {

    @Getter @Setter protected Piece promotedTo;

    {
        try {
            promotedTo = PieceFactory.getPiece(Type.Queen,this.piece.getColor());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public NormalPromotionMove(Position to, Position from, Piece piece) {
        super(to, from, piece);
    }


    public String toString(){
        return "From x:"+from.getX()+ " y:"+from.getY()+"To x:"+to.getX()+ " y:"+to.getY() +" " +piece.getType().name() + " NormalPromotionMove ";
    }

    @Override
    public int compareTo(Move otherMove) {

        if(super.compareTo(otherMove)==1){
              if(promotedTo.compareTo(((NormalPromotionMove)otherMove).getPromotedTo()) == 1){
                        return 1;
              }
        }
        return 0;
    }

    @Override
    public Move Copy() {
        NormalPromotionMove newMove =   new NormalPromotionMove(this.to,this.from, PieceFactory.getPiece(piece.getType(),piece.getColor()));
        newMove.setPromotedTo(PieceFactory.getPiece(promotedTo.getType(),promotedTo.getColor()));
        return newMove;
    }

}
