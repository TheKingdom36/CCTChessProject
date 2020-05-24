package chess.ai.Common.ChessBoard.Moves;


import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.PieceFactory;
import lombok.Getter;
import lombok.Setter;

public class TakeMove extends Move {


    @Getter @Setter
    Piece pieceTaken;



    public TakeMove(Position toPos, Position fromPos, Piece piece, Piece pieceTaken){
        super(toPos,fromPos,piece);
        this.pieceTaken = pieceTaken;
    }

    public String toString(){
        return "From x:"+from.getX()+ " y:"+from.getY()+"To x:"+to.getX()+ " y:"+to.getY() +" " +piece.getType().name() + " TakeMove "+this.pieceTaken.getType().name();
    }

    @Override
    public Move Copy() {
        return new TakeMove(this.to,this.from, PieceFactory.getPiece(this.piece.getType(),this.piece.getColor()), PieceFactory.getPiece(pieceTaken.getType(),pieceTaken.getColor()));
    }

    @Override
    public int compareTo(Move otherMove) {

        if(super.compareTo(otherMove)==1){
            if(pieceTaken.compareTo(((TakeMove)otherMove).getPieceTaken()) == 1){
                return 1;
            }
        }
        return 0;
    }
}
