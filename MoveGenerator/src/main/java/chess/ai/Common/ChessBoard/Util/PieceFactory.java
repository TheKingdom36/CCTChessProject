package chess.ai.Common.ChessBoard.Util;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.MoveCheckers.KingMoveChecker;
import chess.ai.Common.ChessBoard.MoveCheckers.KnightMoveChecker;
import chess.ai.Common.ChessBoard.MoveCheckers.PawnMoveChecker;
import chess.ai.Common.ChessBoard.MoveCheckers.SliderMoveChecker;

import java.util.HashMap;
import java.util.Map;

public class PieceFactory {

    //TODO create two piece lists
    static Map<Type, Piece> pieceWhiteplane = new HashMap<>();
    static Map<Type, Piece> pieceBlackplane = new HashMap<>();

    public PieceFactory(){
    }

    public static Piece getPiece(Type type, Color color)  {

        if(color == Color.White) {
            if (pieceWhiteplane.containsKey(type) == false) {
                pieceWhiteplane.put(type, CreatePiece(type, Color.White));
            }

            return pieceWhiteplane.get(type);
        }else {
            if (pieceBlackplane.containsKey(type) == false) {
                pieceBlackplane.put(type, CreatePiece(type, Color.Black));
            }

            return pieceBlackplane.get(type);
        }


    }

    private static Piece CreatePiece(Type type, Color color){
        Piece piece = new Piece();
        piece.setColor(color);
        piece.setType(type);

        switch (type) {
            case King:{

                piece.setMvChecker(new KingMoveChecker(piece));
                break;
            }
            case Knight:{
                piece.setMvChecker(new KnightMoveChecker(piece));
                break;
            }
            case Bishop:{
                piece.setMvChecker(new SliderMoveChecker(piece,true,false));
                break;
            }
            case Pawn:{
                piece.setMvChecker(new PawnMoveChecker(piece));
                break;
            }
            case Rook:{
                piece.setMvChecker(new SliderMoveChecker(piece,false,true));
                break;
            }
            case Queen:{
                piece.setMvChecker(new SliderMoveChecker(piece,true,true));
                break;
            }
            default:{
                System.out.println(piece.getType() + " piece has no defined type");

            }
        }

        return piece;
    }




}
