package chess.ai.Common.ChessBoard.MoveCheckers;



import chess.ai.Common.ChessBoard.Models.BoardSquare;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.ChessBoard.Moves.NormalMove;
import chess.ai.Common.ChessBoard.Moves.TakeMove;
import chess.ai.Common.ChessBoard.Util.ChessPosFactory;
import chess.ai.Common.ChessBoard.Util.Directions;

import java.util.ArrayList;
import java.util.List;

public class SliderMoveChecker extends MoveChecker {
    boolean canMoveDiagonal;
    boolean canMoveVertOrHor;

    public SliderMoveChecker(Piece piece, boolean canMoveDiagonal, boolean canMoveVertOrHor){
        super(piece);

        this.canMoveDiagonal = canMoveDiagonal;
        this.canMoveVertOrHor = canMoveVertOrHor;

    }


    @Override
    public List<Move> checkAllAvailableMoves(Position piecePos, BoardSquare[][] board) {
        List<Move> moves = new ArrayList<>();

        if(canMoveDiagonal==true) {
            //Check Diagonal
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.NE));
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.NW));
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.SE));
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.SW));
        }

        if(this.canMoveVertOrHor ==true) {
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.E));
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.W));
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.N));
            moves.addAll(checkMovesInDirection(piecePos, board, Directions.S));

        }
        return moves;
    }

    private List<Move> checkMovesInDirection(Position piecePos, BoardSquare[][] board, Position direction ) {
        List<Move> moves = new ArrayList<>();
        boolean hitEdge = false;
        boolean hitPiece = false;

        int curXPos = piecePos.getX();
        int curYPos = piecePos.getY();

        do{
             curXPos = curXPos+ direction.getX();
             curYPos = curYPos+ direction.getY();

             if(curXPos <0 || curXPos>7|| curYPos>7||curYPos<0){
                 hitEdge = true;
                 continue;
             }

            if(board[curXPos][curYPos].isHasPiece()==false){
                moves.add(new NormalMove(ChessPosFactory.getPostion(curXPos,curYPos),piecePos,piece ));
            }else{
                if(board[curXPos][curYPos].getPiece().getColor() != piece.getColor()){
                    moves.add(new TakeMove(ChessPosFactory.getPostion(curXPos,curYPos),piecePos,piece,board[curXPos][curYPos].getPiece()));
                }
                hitPiece = true;
            }
        }while(hitEdge==false && hitPiece ==false);

        return moves;
    }
}
