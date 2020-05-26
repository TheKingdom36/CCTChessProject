package chess.ai.Common.ChessBoard.MoveCheckers;


import chess.ai.Common.ChessBoard.Models.BoardSquare;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.ChessBoard.Moves.NormalMove;
import chess.ai.Common.ChessBoard.Moves.TakeMove;
import chess.ai.Common.ChessBoard.Util.Directions;

import java.util.ArrayList;
import java.util.List;

public class KnightMoveChecker extends MoveChecker {


    public KnightMoveChecker(Piece piece){
        super(piece);
    }
    @Override
    public List<Move> checkAllAvailableMoves(Position piecePos, BoardSquare[][] board) {
        List<Move> moves = new ArrayList<>();

        CheckIfValidMove(piecePos,new Position( (Directions.N).getX() + Directions.NE.getX(),(Directions.N).getY() + Directions.NE.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.N).getX() + Directions.NW.getX(),(Directions.N).getY() + Directions.NW.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.E).getX() + Directions.NE.getX(),(Directions.E).getY() + Directions.NE.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.E).getX() + Directions.SE.getX(),(Directions.E).getY() + Directions.SE.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.S).getX() + Directions.SE.getX(),(Directions.S).getY() + Directions.SE.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.S).getX() + Directions.SW.getX(),(Directions.S).getY() + Directions.SW.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.W).getX() + Directions.SW.getX(),(Directions.W).getY() + Directions.SW.getY()),board,moves);
        CheckIfValidMove(piecePos,new Position( (Directions.W).getX() + Directions.NW.getX(),(Directions.W).getY() + Directions.NW.getY()),board,moves);

        return moves;
    }

    private void CheckIfValidMove(Position piecePos, Position direction, BoardSquare[][] board, List<Move> moves){
        Position SquarePosition = new Position(piecePos.getX()+direction.getX(),piecePos.getY()+direction.getY());
        if(SquarePosition.getX()<=7 && SquarePosition.getX()>=0&&SquarePosition.getY()<=7&&SquarePosition.getY()>=0){
            if(board[SquarePosition.getX()][SquarePosition.getY()].isHasPiece() == true){
                if(board[SquarePosition.getX()][SquarePosition.getY()].getPiece().getColor() != piece.getColor()){
                    moves.add(new TakeMove(SquarePosition,piecePos,piece,board[SquarePosition.getX()][SquarePosition.getY()].getPiece()));
                }
            }else {
                moves.add(new NormalMove(SquarePosition,piecePos,piece));
            }
        }
    }
}


