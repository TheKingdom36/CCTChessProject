package chess.ai.Common.neuralNet.Models;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.MoveCheckers.CastleCheck;
import chess.ai.Common.ChessBoard.Moves.CastleMove;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.ChessBoard.Util.ChessPosFactory;
import chess.ai.Common.neuralNet.Util.ConvertBoardToWhite;

import java.util.List;

/**
 * Used to store board and metadata on the board
 */
public class BoardState {

    private Board board;
    private Color playerColor;
    private boolean CastleOueenSide;
    private boolean CastleKingSide;
    private boolean OppCastleQueenSide;
    private boolean OppCastleKingSide;
    private int noProgressCount;
    private int totalMoveCount;

    public BoardState(){

    }



    public BoardState(BoardState boardState){
        this.board = boardState.getBoard().Copy();
        this.playerColor = boardState.getPlayerColor();
        this.CastleOueenSide = boardState.isCastleOueenSide();
        this.CastleKingSide = boardState.isCastleKingSide();
        this.noProgressCount = boardState.getNoProgressCount();
        this.totalMoveCount = boardState.getTotalMoveCount();
        this.OppCastleQueenSide = boardState.isOppCastleQueenSide();
        this.CastleKingSide = boardState.isOppCastleKingSide();
    }

    public BoardState(Board board, Color playerColor) {
        Color opponent;
        if(playerColor == Color.White){
            this.board = board.Copy();
            opponent = Color.Black;
        }else {
            this.board = ConvertBoardToWhite.Convert(board.Copy());
            opponent = Color.White;
        }
        this.board = board;
        this.playerColor = playerColor;

        //Check if castleing is available
        List<Move> castleMoves= CastleCheck.Check(board,board.getMoveLog(),this.getPlayerColor());
        for(Move castlem:castleMoves){
                if(((CastleMove)castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(0,0))==1){
                    CastleOueenSide = true;
                }else if(((CastleMove)castlem).getFrom().compareTo(ChessPosFactory.getPostion(0,7))==1){
                    CastleKingSide = true;
                }
        }

        castleMoves.clear();


        //Check id Opponent castling is available
        castleMoves = CastleCheck.Check(getBoard(), board.getMoveLog(), opponent);
        for (Move castlem : castleMoves) {
            if (opponent == Color.White) {
                if (((CastleMove) castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(0, 0)) == 1) {
                    OppCastleQueenSide = true;
                } else if (((CastleMove) castlem).getFrom().compareTo(ChessPosFactory.getPostion(0, 7)) == 1) {
                    OppCastleKingSide = true;
                }
            } else {
                if (((CastleMove) castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(7, 0)) == 1) {
                    OppCastleQueenSide = true;
                } else if (((CastleMove) castlem).getFrom().compareTo(ChessPosFactory.getPostion(7, 7)) == 1) {
                    OppCastleKingSide = true;
                }
            }
        }

        this.totalMoveCount = board.getMoveLog().getPastMoves().size();

        List<Move> pastMoves = this.getBoard().getMoveLog().getPastMoves();


        if(pastMoves.size()>3){
            Board tempBoard = this.getBoard().Copy();
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-1));
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-2));
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-3));
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-4));
            if(tempBoard.compareTo(this.getBoard())==1){
                this.setNoProgressCount(1);
            }else {
                this.setNoProgressCount(this.noProgressCount+1);
            }
        }else{
            this.setNoProgressCount(0);
        }



    }

    public BoardState(BoardState boardState, Move m){
        Color opponent;
        //Assign new board
        this.setBoard(boardState.getBoard().Copy());
        this.getBoard().UpdateBoard(m);

        //Assign next player to move
        if(boardState.getPlayerColor() == Color.White){
            this.setPlayerColor(Color.Black);
            opponent = Color.White;
        }else{
            this.setPlayerColor(Color.White);
            opponent = Color.Black;
        }

        //Check if castleing is available
        List<Move> castleMoves= CastleCheck.Check(this.getBoard(),this.getBoard().getMoveLog(),this.getPlayerColor());
        for(Move castlem:castleMoves){
            if(this.getPlayerColor()== Color.White){
                if(((CastleMove)castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(0,0))==1){
                    this.setCastleOueenSide(true);
                }else if(((CastleMove)castlem).getFrom().compareTo(ChessPosFactory.getPostion(0,7))==1){
                    this.setCastleKingSide(true);
                }
            }else{
                if(((CastleMove)castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(7,0))==1){
                    this.setCastleOueenSide(true);
                }else if(((CastleMove)castlem).getFrom().compareTo(ChessPosFactory.getPostion(7,7))==1){
                    this.setCastleKingSide(true);
                }
            }
        }

        castleMoves.clear();


        //Check id Opponent castling is available
        castleMoves = CastleCheck.Check(getBoard(), board.getMoveLog(), opponent);
        for (Move castlem : castleMoves) {
            if (opponent == Color.White) {
                if (((CastleMove) castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(0, 0)) == 1) {
                    OppCastleQueenSide = true;
                } else if (((CastleMove) castlem).getFrom().compareTo(ChessPosFactory.getPostion(0, 7)) == 1) {
                    OppCastleKingSide = true;
                }
            } else {
                if (((CastleMove) castlem).getRookFromPos().compareTo(ChessPosFactory.getPostion(7, 0)) == 1) {
                    OppCastleQueenSide = true;
                } else if (((CastleMove) castlem).getFrom().compareTo(ChessPosFactory.getPostion(7, 7)) == 1) {
                    OppCastleKingSide = true;
                }
            }
        }


        //update total move count
        this.setTotalMoveCount(boardState.getTotalMoveCount()+1);

        List<Move> pastMoves = this.getBoard().getMoveLog().getPastMoves();

        //if the board from 4 moves ago was the same as the current board there has been no progress
        if(pastMoves.size()>3){
            Board tempBoard = this.getBoard().Copy();
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-1));
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-2));
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-3));
            tempBoard.UndoMove(pastMoves.get(pastMoves.size()-4));
            if(tempBoard.compareTo(this.getBoard())==1){
                this.setNoProgressCount(boardState.getNoProgressCount()+1);
            }else {

            }
        }else{
            this.setNoProgressCount(0);
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public int getNoProgressCount() {
        return noProgressCount;
    }

    public void setNoProgressCount(int noProgressCount) {
        this.noProgressCount = noProgressCount;
    }

    public int getTotalMoveCount() {
        return totalMoveCount;
    }

    public void setTotalMoveCount(int totalMoveCount) {
        this.totalMoveCount = totalMoveCount;
    }

    public boolean isCastleOueenSide() {
        return CastleOueenSide;
    }

    public void setCastleOueenSide(boolean castleOueenSide) {
        CastleOueenSide = castleOueenSide;
    }

    public boolean isCastleKingSide() {
        return CastleKingSide;
    }

    public void setCastleKingSide(boolean castleKingSide) {
        CastleKingSide = castleKingSide;
    }

    public boolean isOppCastleQueenSide() {
        return OppCastleQueenSide;
    }

    public void setOppCastleQueenSide(boolean oppCastleQueenSide) {
        OppCastleQueenSide = oppCastleQueenSide;
    }

    public boolean isOppCastleKingSide() {
        return OppCastleKingSide;
    }

    public void setOppCastleKingSide(boolean oppCastleKingSide) {
        OppCastleKingSide = oppCastleKingSide;
    }
}
