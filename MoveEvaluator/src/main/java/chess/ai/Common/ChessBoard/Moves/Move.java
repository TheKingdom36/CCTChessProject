package chess.ai.Common.ChessBoard.Moves;


import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import lombok.Getter;
import lombok.Setter;

public abstract class Move implements Comparable<Move>{

    @Getter @Setter protected Piece piece;
    @Getter @Setter protected Position to;
    @Getter @Setter protected Position from;

    public Move(Position to , Position from, Piece piece){
        this.to = to;
        this.from = from;
        this.piece = piece;
    }


    @Override
    public int compareTo(Move otherMove) {

        if(this.getClass().equals(otherMove.getClass()) == true){
            if(this.to.getX() == otherMove.getTo().getX() && this.to.getY() == otherMove.getTo().getY()){
                if(this.from.getX() == otherMove.getFrom().getX() && this.from.getY() == otherMove.getFrom().getY()){
                    if(piece.compareTo(otherMove.getPiece()) == 1){
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Move{" +
                "piece=" + piece +
                ", to=" + to +
                ", from=" + from +
                '}';
    }

    public abstract Move Copy();


}
