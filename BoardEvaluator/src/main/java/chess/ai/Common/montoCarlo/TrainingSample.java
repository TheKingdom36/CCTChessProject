package chess.ai.Common.montoCarlo;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;

import java.util.Arrays;

public class TrainingSample {
    private Board board;
    private double[] policy;
    private int value;
    private Color playerColor;

    public TrainingSample(Board board, double[] policy, int value, Color playerColor){
        this.board = board;
        this.policy= policy;
        this.value = value;
        this.playerColor = playerColor;
    }



    public TrainingSample(){

    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public double[] getPolicy() {
        return policy;
    }

    public void setPolicy(double[] policy) {
        this.policy = policy;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    @Override
    public String toString() {
        return "TrainingSample{" +
                "board=" + board +
                ", policy=" + Arrays.toString(policy) +
                ", value=" + value +
                ", playerColor=" + playerColor +
                '}';
    }
}
