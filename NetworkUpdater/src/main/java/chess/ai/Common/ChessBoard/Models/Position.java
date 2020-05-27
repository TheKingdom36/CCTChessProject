package chess.ai.Common.ChessBoard.Models;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Position implements Comparable<Position> {
    int x;
    int y;

    public Position(int x, int y){
        this.x=x;
        this.y =y;
    }

    @Override
    public int compareTo(Position otherPos) {

        if(this.x == otherPos.getX() && this.y == otherPos.getY()){
            return 0;
        }else {
            return 1;
        }

    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
