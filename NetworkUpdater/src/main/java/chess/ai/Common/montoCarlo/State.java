package chess.ai.Common.montoCarlo;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.ChessBoard.Moves.NormalPromotionMove;
import chess.ai.Common.ChessBoard.Moves.TakePromotionMove;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Output.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class State {


    private static List<MoveOption> movesOptions;
    private int visitCount;

    @Getter @Setter private Boolean isActive;
    @Getter @Setter private double winScore;

    private double isBestMoveProbability;
    private BoardState boardState;
    private Move move;
    private int idMove;

    public Move getMove() {
        return move;
    }

    public void setMove(Move move) {
        this.move = move;
    }

    public State(){
        isActive = false;
        visitCount=0;
        winScore=0;
        isActive=false;
        this.isBestMoveProbability = 0;
        this.visitCount =0;
    }

    public State(State state) {
        this.boardState = new BoardState(state.getBoardState());
        this.visitCount = state.getVisitCount();
        this.isBestMoveProbability = state.getBestMoveProbability();
        visitCount=0;
        winScore=0;
        isActive=false;
    }

    public State(BoardState boardState){
        this.boardState = boardState;
        this.visitCount = 0;
        this.isBestMoveProbability = 0;
        visitCount=0;
        winScore=0;
        isActive=false;
    }




    public static List<MoveOption> getMovesOptions() {
        return movesOptions;
    }
    public static void setMovesOptions(List<MoveOption> movesOptions) {
        State.movesOptions = movesOptions;
    }

    public double getBestMoveProbability() {
        return isBestMoveProbability;
    }
    public void setBestMoveProbability(double bestMoveProbability) {
        this.isBestMoveProbability = bestMoveProbability;
    }


    public void updateWinScore(double value){
        winScore += value;
    }

    public BoardState getBoardState() {
        return boardState;
    }
    public void setBoardState(BoardState boardState) {
        this.boardState = boardState;
    }

    Board getBoard() {
        return this.boardState.getBoard();
    }
    void setBoard(Board board) {
        this.boardState.setBoard(board);
    }

    Color getPlayerColor() {
        return this.boardState.getPlayerColor();
    }
    void setPlayerColor(Color playerColor) {
        this.boardState.setPlayerColor(playerColor);
    }

    Color getOpponent() {

        if(boardState.getPlayerColor()== Color.White){return Color.Black;
        }else{
            return Color.White;
        }
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public State[] getAllPossibleStates() {
                                                                    //Board is always from white persepective
        List<Move> moves = boardState.getBoard().GetAllAvailableMoves(Color.White);
        State[] states = new State[State.getMovesOptions().size()];

        for(int i = 0; i< State.getMovesOptions().size(); i++){
            states[i] = new State();
            states[i].setIsActive(false);

        }

        int stateCounter=0;

        for(Move m:moves){

            List<MoveOption> moveOptions = AllPieceMoveOptions.getMoveOptions();
            for (int i = 0; i < moveOptions.size(); i++) {
                MoveOption moveOption = moveOptions.get(i);
                if (moveOption instanceof QueenMoveOption) {

                    if (m.getFrom().compareTo(moveOption.getPiecePos()) == 0 &&
                            (m.getTo().getX() - m.getFrom().getX()) == moveOption.getDirection().getX() &&
                            (m.getTo().getY() - m.getFrom().getY()) == moveOption.getDirection().getY()
                    ) {

                        int higher;
                        if (Math.abs(m.getTo().getX() - m.getFrom().getX()) > Math.abs(m.getTo().getY() - m.getFrom().getY())) {

                            higher = Math.abs(m.getTo().getX() - m.getFrom().getX());
                        } else {


                            higher = Math.abs(m.getTo().getY() - m.getFrom().getY());
                        }

                        if (higher == ((QueenMoveOption) moveOption).getDistanceFromPiecePos()) {
                            CreateStateFromCurrentState(states[stateCounter], m,i);
                            break;
                        }
                    }
                } else if (moveOption instanceof KnightMoveOption) {
                    if (m.getFrom().compareTo(moveOption.getPiecePos()) == 0 && m.getPiece().getType() == Type.Knight) {
                        if ((m.getTo().getX() - m.getFrom().getX()) == moveOption.getDirection().getX() &&
                                (m.getTo().getY() - m.getFrom().getY()) == moveOption.getDirection().getY()) {
                            CreateStateFromCurrentState(states[stateCounter], m,i);
                            break;
                        }
                    }
                } else if (moveOption instanceof PawnPromotionMoveOption) {
                    if (m.getFrom().compareTo(moveOption.getPiecePos()) == 0 && (m instanceof NormalPromotionMove || m instanceof TakePromotionMove) && m.getPiece().getType() == Type.Pawn) {
                        if ((m.getTo().getX() - m.getFrom().getX()) == moveOption.getDirection().getX() &&
                                (m.getTo().getY() - m.getFrom().getY()) == moveOption.getDirection().getY()) {
                            CreateStateFromCurrentState(states[stateCounter], m,i);
                            break;
                        }
                    }

                } else {
                    states[stateCounter].setBestMoveProbability(0);
                }

            }
            stateCounter++;
        }


;
        return states;
    }

    public double getIsBestMoveProbability() {
        return isBestMoveProbability;
    }

    public void setIsBestMoveProbability(double isBestMoveProbability) {
        this.isBestMoveProbability = isBestMoveProbability;
    }

    public int getIdMove() {
        return idMove;
    }

    public void setIdMove(int idMove) {
        this.idMove = idMove;
    }

    private void CreateStateFromCurrentState(State newState, Move m, int idMove) {
        newState.setIsActive(true);
        newState.setIdMove(idMove);
        newState.setMove(m);
        newState.setBoardState(new BoardState(this.boardState,m));
    }

    void incrementVisit() {
        this.visitCount++;
    }



    void togglePlayer() {
        if(boardState.getPlayerColor()== Color.White){
            boardState.setPlayerColor(Color.Black);
        }else{
            boardState.setPlayerColor(Color.White);
        }
    }
}