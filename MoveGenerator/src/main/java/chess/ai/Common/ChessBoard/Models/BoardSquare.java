package chess.ai.Common.ChessBoard.Models;

import lombok.Getter;
import lombok.Setter;

public class BoardSquare implements Comparable<BoardSquare> {
    @Getter private Piece piece;
    @Getter @Setter
    private boolean hasPiece;

    public void setPiece(Piece piece) {
        if(piece !=null){
        this.piece = piece;
        hasPiece = true;
        }
    }

    public void clear(){
        piece = null;
        hasPiece = false;
    }

    public BoardSquare(){
        hasPiece=false;
    }

    @Override
    public int compareTo(BoardSquare otherBoardSquare) {
        if(this.hasPiece  == otherBoardSquare.hasPiece){

            if(otherBoardSquare.hasPiece==true){
                if(this.piece.getType() == otherBoardSquare.getPiece().getType()
                        && this.piece.getColor() == otherBoardSquare.getPiece().getColor()
                ){
                    return 0;
                }
            }else {
                return 0;
            }
        }
        return 1;
    }
}
