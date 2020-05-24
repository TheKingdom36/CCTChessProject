package chess.ai.Common.ChessBoard.Moves;


import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.PieceFactory;
import lombok.Getter;
import lombok.Setter;

public class CastleMove extends Move {

    @Getter @Setter private Piece swappedRook;
    @Getter @Setter private Position rookToPos;
    @Getter @Setter private Position rookFromPos;

    public CastleMove(Position kingToPos, Position kingFromPos, Position rookToPos, Position rookFromPos, Piece king, Piece Rook) {
        super(kingToPos, kingFromPos, king);
        swappedRook = Rook;
        this.rookFromPos = rookFromPos;
        this.rookToPos = rookToPos;
    }




    @Override
    public int compareTo(Move otherMove) {

            if(super.compareTo(otherMove) == 1 ){
                if(this.rookToPos.getX() == ((CastleMove)otherMove).getRookToPos().getX() && this.to.getY() == ((CastleMove)otherMove).getRookToPos().getY()){
                    if(this.from.getX() == ((CastleMove)otherMove).getRookFromPos().getX() && this.from.getY() == ((CastleMove)otherMove).getRookFromPos().getX()){
                        if(swappedRook.compareTo(((CastleMove)otherMove).getSwappedRook()) == 1){
                            return 1;
                        }
                    }
                }
            }

            return 0;
    }

    @Override
    public Move Copy() {
        return new CastleMove(this.to,this.from,this.rookToPos,this.rookFromPos, PieceFactory.getPiece(this.piece.getType(),this.piece.getColor()), PieceFactory.getPiece(this.swappedRook.getType(),this.swappedRook.getColor()));
    }

}
