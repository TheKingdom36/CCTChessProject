package chess.ai.Common.ChessBoard.MoveCheckers;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.Models.MoveLog;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Moves.CastleMove;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.ChessBoard.Util.ChessPosFactory;

import java.util.ArrayList;
import java.util.List;

public class CastleCheck {

    static private Position KingStartPos;
    static private Position RookQStartPos;
    static private Position RookKStartPos;


    static private boolean CanCastleQueenSide = true;
    static private boolean CanCastleKingSide = true;


    public static List<Move> Check(Board board, MoveLog movelog, Color color){
        List<Move> moves = new ArrayList<>();

        CanCastleKingSide=true;
        CanCastleQueenSide=true;

        if(color== Color.White){
            KingStartPos = ChessPosFactory.getPostion(0,4);
            RookQStartPos = ChessPosFactory.getPostion(0,0);
            RookKStartPos = ChessPosFactory.getPostion(0,7);
        }else{
            KingStartPos = ChessPosFactory.getPostion(7,4);
            RookQStartPos = ChessPosFactory.getPostion(7,0);
            RookKStartPos = ChessPosFactory.getPostion(7,7);
        }

        //if there is no piece at king pos the king has moved
        if(board.getBoardSquares()[KingStartPos.getX()][KingStartPos.getY()].getPiece() == null){
            return moves;
        }else{
            //if there is a piece at 0 4 but its not king the king has moved
            if(board.getBoardSquares()[KingStartPos.getX()][KingStartPos.getY()].getPiece().getType() != Type.King){
                return moves;
            }else if(SquareSaftyCheck.isSquareSafe(KingStartPos,color,board.getBoardSquares()) == false){
                return moves;
            }
        }

        //Check if the squares between king and rook are empty and safe
        //Check king side
        CanCastleKingSide = isSafeToKingSideCastle(board,color);

        //Check Queen side
        CanCastleQueenSide = isSafeToQueenSideCastle(board,color);


        //check move log if the king or rook moved
        if(CanCastleQueenSide==true || CanCastleKingSide==true){
            for(Move m: movelog.getPastMoves()){
                if(m.getPiece().getType()== Type.Rook && m.getPiece().getColor() == Color.White && m.getFrom() == RookQStartPos){
                    CanCastleQueenSide=false;
                }else if(m.getPiece().getType()== Type.Rook && m.getPiece().getColor()== Color.White && m.getFrom() == RookKStartPos){
                    CanCastleKingSide=false;
                }else if(m.getPiece().getType()== Type.King && m.getPiece().getColor()== Color.White && m.getFrom() == KingStartPos){
                    //no available castle moves because king moved
                    return moves;
                }
            }
        }

        if(CanCastleKingSide==true){
            moves.add(new CastleMove(ChessPosFactory.getPostion(KingStartPos.getX(),2), KingStartPos, ChessPosFactory.getPostion(KingStartPos.getX(),3), RookKStartPos,board.getBoardSquares()[KingStartPos.getX()][KingStartPos.getY()].getPiece(),board.getBoardSquares()[RookKStartPos.getX()][RookKStartPos.getY()].getPiece()));
        }

        if (CanCastleQueenSide==true){
            moves.add(new CastleMove(ChessPosFactory.getPostion(KingStartPos.getX(),6), KingStartPos, ChessPosFactory.getPostion(KingStartPos.getX(),5), RookQStartPos,board.getBoardSquares()[KingStartPos.getX()][KingStartPos.getY()].getPiece(),board.getBoardSquares()[RookQStartPos.getX()][RookQStartPos.getY()].getPiece()));
        }

        return moves;

    }

     private static boolean isSafeToQueenSideCastle(Board board, Color color){

        if(false == SquareSaftyCheck.isSquareSafe(RookQStartPos,color,board.getBoardSquares())){

            return false;
        }

         //Check if the squares between king and rook are empty and safe
         //Check Queen side
         for(int i = KingStartPos.getY()-1; i>0; i--){

             if(board.getBoardSquares()[KingStartPos.getX()][i].isHasPiece()==true){
                 return false;
             }else{
                 if(false == SquareSaftyCheck.isSquareSafe(ChessPosFactory.getPostion(KingStartPos.getX(),i),color,board.getBoardSquares())){
                    return false;
                 }

             }
         }
         return true;

     }

    private static boolean isSafeToKingSideCastle(Board board, Color color){
        if(false == SquareSaftyCheck.isSquareSafe(RookKStartPos,color,board.getBoardSquares())){

            return false;
        }

        for(int i = KingStartPos.getY()+1; i<7; i++){

            if(board.getBoardSquares()[KingStartPos.getX()][i].isHasPiece()==true){

                    return false;
            }else{
                if(SquareSaftyCheck.isSquareSafe(ChessPosFactory.getPostion(KingStartPos.getX(),i),color,board.getBoardSquares())==false){

                    return false;
                }
            }
        }

        return true;
    }


}
