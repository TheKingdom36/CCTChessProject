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

public class KingMoveChecker extends MoveChecker {

    public KingMoveChecker(Piece piece) {
        super(piece);
    }

    @Override
    public List<Move> checkAllAvailableMoves(Position piecePos, BoardSquare[][] board) {
        List<Move> moves = new ArrayList<>();
        checkMovesInDirection(piecePos, board, Directions.N, moves);
        checkMovesInDirection(piecePos, board, Directions.NE, moves);
        checkMovesInDirection(piecePos, board, Directions.E, moves);
        checkMovesInDirection(piecePos, board, Directions.SE, moves);
        checkMovesInDirection(piecePos, board, Directions.S, moves);
        checkMovesInDirection(piecePos, board, Directions.SW, moves);
        checkMovesInDirection(piecePos, board, Directions.W, moves);
        checkMovesInDirection(piecePos, board, Directions.NW, moves);

        return moves;
    }

    private void checkMovesInDirection(Position piecePos, BoardSquare[][] board, Position direction, List<Move> moves) {

        int XPosToCheck = piecePos.getX() + direction.getX();
        int YPosToCheck = piecePos.getY() + direction.getY();


        if (SquareSaftyCheck.isSquareSafe(new Position(XPosToCheck, YPosToCheck), piece.getColor(), board) == true) {


            if (XPosToCheck >= 0 && XPosToCheck <= 7 && YPosToCheck <= 7 && YPosToCheck >= 0) {
                if (board[XPosToCheck][YPosToCheck].isHasPiece() == false) {
                    moves.add(new NormalMove(new Position(XPosToCheck, YPosToCheck), piecePos, piece));
                } else {
                    if (board[XPosToCheck][YPosToCheck].getPiece().getColor() != piece.getColor()) {
                        moves.add(new TakeMove(new Position(XPosToCheck, YPosToCheck), piecePos, piece, board[XPosToCheck][YPosToCheck].getPiece()));
                    }
                }
            }
        }
    }

}
