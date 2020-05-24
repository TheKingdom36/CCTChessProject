package chess.ai.Common.ChessBoard.MoveCheckers;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.BoardSquare;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Moves.*;
import chess.ai.Common.ChessBoard.Util.ChessPosFactory;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveChecker extends MoveChecker {

    //If piece is white its moving positive i.e 1 else its black moving negitive i.e -1
    private int forwardDir;

    public PawnMoveChecker(Piece piece){
        super(piece);

        if(piece.getColor()== Color.White){
            forwardDir = 1;
        }else{
            forwardDir = -1;
        }
    }

    @Override
    public List<Move> checkAllAvailableMoves(Position piecePos, BoardSquare[][] board)  {


        List<Move> moves = new ArrayList<>();

        if(piece.getColor()== Color.White){

            //check single move forward
            if(board[piecePos.getX()+forwardDir][piecePos.getY()].isHasPiece()==false){
                //Check Rank
                if(piecePos.getX()+forwardDir == 7){
                    moves.add(new NormalPromotionMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()),piecePos,piece));
                }else {
                    moves.add(new NormalMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()),piecePos,piece));
                }

                //if on rank 1 check double move forward
                if(piecePos.getX()==1){
                    if(board[piecePos.getX()+forwardDir*2][piecePos.getY()].isHasPiece()==false){
                        //Check Rank

                        moves.add(new NormalMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir*2,piecePos.getY()),piecePos,piece));
                    }
                }
            }

        }else{

            //check single move forward
            if(board[piecePos.getX()+forwardDir][piecePos.getY()].isHasPiece()==false){
                //Check Rank
                if(piecePos.getX()+forwardDir == 0){
                    moves.add(new NormalPromotionMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()),piecePos,piece));
                }else {
                    moves.add(new NormalMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()),piecePos,piece));
                }

                //if on rank 6 check double move forward
                if(piecePos.getX()==6){

                    if(board[piecePos.getX()+forwardDir*2][piecePos.getY()].isHasPiece()==false){
                        //Check Rank
                        moves.add(new NormalMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir*2,piecePos.getY()),piecePos,piece));
                    }
                }
            }


        }

        //check attacks on diagonals
        if(piecePos.getY()+1<=7 && board[piecePos.getX()+forwardDir][piecePos.getY()+1].isHasPiece()==true){
            if(board[piecePos.getX()+forwardDir][piecePos.getY()+1].getPiece().getColor() != piece.getColor()){

                if(piecePos.getX()+forwardDir == 0 || piecePos.getX()+forwardDir == 7){
                    moves.add(new TakePromotionMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()+1),piecePos,piece,board[piecePos.getX()+forwardDir][piecePos.getY()+1].getPiece()));
                }else {
                    moves.add(new TakeMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()+1),piecePos,piece,board[piecePos.getX()+forwardDir][piecePos.getY()+1].getPiece()));
                }

            }
        }


        if(piecePos.getY()-1>=0 && board[piecePos.getX()+forwardDir][piecePos.getY()-1].isHasPiece()==true){
            if(board[piecePos.getX()+forwardDir][piecePos.getY()-1].getPiece().getColor() != piece.getColor()){
                if(piecePos.getX()+forwardDir == 0 || piecePos.getX()+forwardDir == 7){
                    moves.add(new TakePromotionMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()-1),piecePos,piece,board[piecePos.getX()+forwardDir][piecePos.getY()-1].getPiece()));
                }else {
                    moves.add(new TakeMove(ChessPosFactory.getPostion(piecePos.getX()+forwardDir,piecePos.getY()-1),piecePos,piece,board[piecePos.getX()+forwardDir][piecePos.getY()-1].getPiece()));
                }
            }
        }

        return moves;

    }
}
