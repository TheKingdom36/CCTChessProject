package chess.ai.Common.neuralNet.Util;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Moves.*;
import chess.ai.Common.ChessBoard.Util.ChessPosFactory;
import chess.ai.Common.ChessBoard.Util.PieceFactory;

/**
 * Used to Convert a Move to Black form White
 */
public class ConvertMoveToBlack {
    public static Move Convert(Move move){
        Move newMove;

      if(move instanceof TakePromotionMove){
          newMove = new TakePromotionMove(flipPosition(move.getTo()), flipPosition(move.getFrom()), PieceFactory.getPiece(move.getPiece().getType(), Color.Black), PieceFactory.getPiece(((TakePromotionMove)move).getPieceTaken().getType(), Color.White));
          ((TakePromotionMove)newMove).setPromotedTo(PieceFactory.getPiece(((TakePromotionMove)newMove).getPromotedTo().getType(),((TakePromotionMove)newMove).getPromotedTo().getColor()));
      }else if(move instanceof CastleMove) {
          newMove = new CastleMove(flipPosition(move.getTo()), flipPosition(move.getFrom()), flipPosition(((CastleMove) move).getRookToPos()), flipPosition(((CastleMove) move).getRookFromPos()), PieceFactory.getPiece(move.getPiece().getType(), Color.Black), PieceFactory.getPiece(((CastleMove) move).getSwappedRook().getType(), Color.Black));
      }else if(move instanceof TakeMove){
          newMove = new TakeMove(flipPosition(move.getTo()), flipPosition(move.getFrom()), PieceFactory.getPiece(move.getPiece().getType(), Color.Black), PieceFactory.getPiece(((TakeMove) move).getPieceTaken().getType(), Color.White));
      }else  if(move instanceof NormalPromotionMove){
          newMove = new NormalPromotionMove(flipPosition(move.getTo()), flipPosition(move.getFrom()), PieceFactory.getPiece(move.getPiece().getType(), Color.Black));
          ((NormalPromotionMove)newMove).setPromotedTo(PieceFactory.getPiece(((NormalPromotionMove)newMove).getPromotedTo().getType(),((NormalPromotionMove)newMove).getPromotedTo().getColor()));
      }else {
          newMove = new NormalMove(flipPosition(move.getTo()), flipPosition(move.getFrom()), PieceFactory.getPiece(move.getPiece().getType(), Color.Black));
      }

      return newMove;
    }

    private static Position flipPosition(Position pos){
        int posX = 7- pos.getX();
        int posY = pos.getY();

        return ChessPosFactory.getPostion(posX,posY);
    }
}
