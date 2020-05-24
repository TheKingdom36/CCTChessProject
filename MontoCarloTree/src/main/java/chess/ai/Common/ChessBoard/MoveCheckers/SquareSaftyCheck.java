package chess.ai.Common.ChessBoard.MoveCheckers;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.BoardSquare;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.Directions;

public class SquareSaftyCheck {
    public static boolean isSquareSafe(Position posOfSqr, Color color, BoardSquare[][] board){

        if(CheckAttackersDiagonal(posOfSqr,color,board)==true){

            return false;
        }
        if(CheckAttackerKnight(posOfSqr,color,board) == true){
            return false;
        }

        if(CheckAttackersVertAndHor(posOfSqr,color,board) == true){
            return false;
        }

        if(CheckPawnAttacks(posOfSqr,color,board)==true){
            return false;
        }


        return true;
    }



    private static boolean CheckPawnAttacks(Position pos, Color color, BoardSquare[][] board){
        if(color== Color.White){
            int sqrToCheckX = pos.getX()+ Directions.NW.getX();
            int sqrToCheckY = pos.getY()+ Directions.NW.getY();

            if(sqrToCheckX>=0&&sqrToCheckX<=7&&sqrToCheckY>=0&&sqrToCheckY<=7){
                if(board[sqrToCheckX][sqrToCheckY].isHasPiece()){
                    if(board[sqrToCheckX][sqrToCheckY].getPiece().getColor()!=color && board[sqrToCheckX][sqrToCheckY].getPiece().getType()== Type.Pawn){
                        return true;
                    }
                }
            }

            sqrToCheckX = pos.getX()+ Directions.NE.getX();
            sqrToCheckY = pos.getY()+ Directions.NE.getY();

            if(sqrToCheckX>=0&&sqrToCheckX<=7&&sqrToCheckY>=0&&sqrToCheckY<=7){
                if(board[sqrToCheckX][sqrToCheckY].isHasPiece()){
                    if(board[sqrToCheckX][sqrToCheckY].getPiece().getColor()!=color && board[sqrToCheckX][sqrToCheckY].getPiece().getType()== Type.Pawn){
                        return true;
                    }
                }
            }
        }else{
            if(color== Color.White){
                int sqrToCheckX = pos.getX()+ Directions.SW.getX();
                int sqrToCheckY = pos.getY()+ Directions.SW.getY();

                if(sqrToCheckX>=0&&sqrToCheckX<=7&&sqrToCheckY>=0&&sqrToCheckY<=7){
                    if(board[sqrToCheckX][sqrToCheckY].isHasPiece()){
                        if(board[sqrToCheckX][sqrToCheckY].getPiece().getColor()!=color&& board[sqrToCheckX][sqrToCheckY].getPiece().getType()== Type.Pawn){
                            return true;
                        }
                    }
                }

                sqrToCheckX = pos.getX()+ Directions.SE.getX();
                sqrToCheckY = pos.getY()+ Directions.SE.getY();

                if(sqrToCheckX>=0&&sqrToCheckX<=7&&sqrToCheckY>=0&&sqrToCheckY<=7){
                    if(board[sqrToCheckX][sqrToCheckY].isHasPiece()){
                        if(board[sqrToCheckX][sqrToCheckY].getPiece().getColor()!=color&& board[sqrToCheckX][sqrToCheckY].getPiece().getType()== Type.Pawn){
                            return true;
                        }
                    }
                }
            }


        }
        return false;
    }

    private static boolean CheckAttackersDiagonal(Position pos, Color color, BoardSquare[][] board){
        Piece result = isAttackerInDirection(pos,board,color, Directions.NE);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Bishop|| result.getType() == Type.King){
                return true;
            }
        }

        result = isAttackerInDirection(pos,board,color, Directions.SE);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Bishop|| result.getType() == Type.King){
                return true;
            }
        }

        result = isAttackerInDirection(pos,board,color, Directions.NW);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Bishop|| result.getType() == Type.King){
                return true;
            }
        }

        result = isAttackerInDirection(pos,board,color, Directions.SW);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Bishop|| result.getType() == Type.King){
                return true;
            }
        }
        return false;
    }

    private static boolean CheckAttackersVertAndHor(Position pos, Color color, BoardSquare[][] board){
        Piece result = isAttackerInDirection(pos,board,color, Directions.N);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Rook|| result.getType() == Type.King){
                return true;
            }
        }

        result = isAttackerInDirection(pos,board,color, Directions.S);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Rook|| result.getType() == Type.King){
                return true;
            }
        }

        result = isAttackerInDirection(pos,board,color, Directions.E);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Rook|| result.getType() == Type.King){
                return true;
            }
        }

        result = isAttackerInDirection(pos,board,color, Directions.W);
        if(result != null){
            if(result.getType()== Type.Queen ||result.getType() == Type.Rook|| result.getType() == Type.King){
                return true;
            }
        }
        return false;
    }

    private static boolean CheckAttackerKnight(Position pos, Color color, BoardSquare[][] board){


        if(isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.N).getX() + Directions.NE.getX(),pos.getY()+(Directions.N).getY() + Directions.NE.getY()),color,board) == true){
            return true;

        }
        if(isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.N).getX() + Directions.NW.getX(),pos.getY()+(Directions.N).getY() + Directions.NW.getY()),color,board)== true){
            return true;

        }

        if(        isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.E).getX() + Directions.NE.getX(),pos.getY()+(Directions.E).getY() + Directions.NE.getY()),color,board)== true){
            return true;

        }
        if(        isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.E).getX() + Directions.SE.getX(),pos.getY()+(Directions.E).getY() + Directions.SE.getY()),color,board)== true){
            return true;

        }
        if(        isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.S).getX() + Directions.SE.getX(),pos.getY()+(Directions.S).getY() + Directions.SE.getY()),color,board)== true){
            return true;

        }
        if(        isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.S).getX() + Directions.SW.getX(),pos.getY()+(Directions.S).getY() + Directions.SW.getY()),color,board)== true){
            return true;

        }
        if(        isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.W).getX() + Directions.SW.getX(),pos.getY()+(Directions.W).getY() + Directions.SW.getY()),color,board)== true){
            return true;

        }
        if(         isAttackerKnightAtPosition(new Position( pos.getX()+(Directions.W).getX() + Directions.NW.getX(),pos.getY()+(Directions.W).getY() + Directions.NW.getY()),color,board)== true){
            return true;

        }

        return false;

    }

    private static boolean isAttackerKnightAtPosition(Position posToCheck, Color color, BoardSquare[][] board){
        if(posToCheck.getX()<=7 && posToCheck.getX()>=0&&posToCheck.getY()<=7&&posToCheck.getY()>=0){
            if(board[posToCheck.getX()][posToCheck.getY()].isHasPiece() == true){
                if(board[posToCheck.getX()][posToCheck.getY()].getPiece().getType() == Type.Knight && board[posToCheck.getX()][posToCheck.getY()].getPiece().getColor() != color){
                    return true;
                }
            }
        }
        return false;
    }

    private static Piece isAttackerInDirection(Position piecePos, BoardSquare[][] board, Color color, Position direction) {

        int curXPos = piecePos.getX();
        int curYPos = piecePos.getY();



        if(curXPos+ direction.getX()>=0 && curXPos+ direction.getX()<=7&& curYPos+ direction.getY()<=7&&curYPos+ direction.getY()>=0) {
            if (board[curXPos + direction.getX()][curYPos + direction.getY()].isHasPiece() == true) {
                if (board[curXPos + direction.getX()][curYPos + direction.getY()].getPiece().getType() == Type.King && board[curXPos + direction.getX()][curYPos + direction.getY()].getPiece().getColor() != color) {
                    return board[curXPos + direction.getX()][curYPos + direction.getY()].getPiece();
                }
            }
        }

        do{
            curXPos = curXPos+ direction.getX();
            curYPos = curYPos+ direction.getY();

            if(curXPos <0 || curXPos>7|| curYPos>7||curYPos<0){
                return null;
            }

            if(board[curXPos][curYPos].isHasPiece()==true){
                if(board[curXPos][curYPos].getPiece().getType() != Type.King&&board[curXPos][curYPos].getPiece().getColor() != color){
                    return board[curXPos][curYPos].getPiece();

                }   else{
                    return null;
                }
            }
        }while(true);

    }

}
