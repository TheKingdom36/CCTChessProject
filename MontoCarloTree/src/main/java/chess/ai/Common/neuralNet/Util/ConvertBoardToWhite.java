package chess.ai.Common.neuralNet.Util;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.Models.Piece;
import chess.ai.Common.ChessBoard.Util.PieceFactory;

public class ConvertBoardToWhite {
    public static Board Convert(Board board){
        //Always want the input in perspective of white player. after the neural network completes the output is then mapped to blacks perspective
                Board flippedBoard = board.Copy();

                InvertPieceColors(flippedBoard);
                RotateBoard(flippedBoard);

                return  flippedBoard;


    }


    private static void InvertPieceColors(Board board) {
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                if(board.getBoardSquares()[i][j].isHasPiece()){
                    if(board.getBoardSquares()[i][j].getPiece().getColor()== Color.White){
                        board.getBoardSquares()[i][j].setPiece(PieceFactory.getPiece( board.getBoardSquares()[i][j].getPiece().getType(), Color.Black));
                    }else{
                        board.getBoardSquares()[i][j].setPiece(PieceFactory.getPiece( board.getBoardSquares()[i][j].getPiece().getType(), Color.White));
                    }
                }
            }
        }

    }

    private static void RotateBoard(Board board){

        SwapRows(board,0,7);
        SwapRows(board,1,6);
        SwapRows(board,2,5);
        SwapRows(board,3,4);
    }

    private static void SwapRows(Board board, int rowNum1, int rowNum2) {
        Piece tempPiece;
        for(int i = 0; i<=7; i++){
            if(board.getBoardSquares()[rowNum2][i].isHasPiece() == true){
                tempPiece = board.getBoardSquares()[rowNum2][i].getPiece();
            }else {
                tempPiece = null;
            }

            if(board.getBoardSquares()[rowNum1][i].isHasPiece() == true){
                board.getBoardSquares()[rowNum2][i].setPiece(board.getBoardSquares()[rowNum1][i].getPiece());
            }else {
                board.getBoardSquares()[rowNum2][i].clear();
            }

            if(tempPiece != null){
                board.getBoardSquares()[rowNum1][i].setPiece(tempPiece);
            }else {
                board.getBoardSquares()[rowNum2][i].clear();
            }





        }
    }

}
