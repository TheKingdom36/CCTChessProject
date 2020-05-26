package chess.ai.Common.ChessBoard.Models;



import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.MoveCheckers.CastleCheck;
import chess.ai.Common.ChessBoard.MoveCheckers.SquareSaftyCheck;
import chess.ai.Common.ChessBoard.Moves.*;
import chess.ai.Common.ChessBoard.Util.ChessPosFactory;
import chess.ai.Common.ChessBoard.Util.PieceFactory;

import java.util.ArrayList;
import java.util.List;

public class Board implements Comparable<Board>{
    private BoardSquare[][] boardSquares;
    private Position WhiteKing;
    private Position BlackKing;
    private MoveLog moveLog;

    public void setBoardSquares(BoardSquare[][] boardSquares) {
        this.boardSquares = boardSquares;
    }

    public MoveLog getMoveLog() {
        return moveLog;
    }

    public void setMoveLog(MoveLog moveLog) {
        this.moveLog = moveLog;
    }

    public BoardSquare[][] getBoardSquares() {
        return boardSquares;
    }

    public Board(MoveLog moveLog){
            Setup();
            this.moveLog = moveLog.Copy();
    }

    public Board(){
            Setup();
            this.moveLog = new MoveLog();
    }

    public Board(BoardSquare[][] boardSquares, List<Move> lastMoves)  {
        this.boardSquares = new BoardSquare[8][8];
        this.moveLog = moveLog;

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                this.boardSquares[i][j] = new BoardSquare();
                if(boardSquares[i][j].isHasPiece()==true){

                    if(boardSquares[i][j].getPiece().getType() == Type.King){
                        if(boardSquares[i][j].getPiece().getColor() == Color.White){
                            WhiteKing = ChessPosFactory.getPostion(i,j);
                        }else {
                            BlackKing = ChessPosFactory.getPostion(i,j);
                        }
                    }

                    this.boardSquares[i][j].setPiece(PieceFactory.getPiece(boardSquares[i][j].getPiece().getType(),boardSquares[i][j].getPiece().getColor()));

                }
            }
        }

        moveLog = new MoveLog();

        for(Move m:lastMoves){
            if(m instanceof NormalMove){

                moveLog.AddMove(new NormalMove(m.getTo(),m.getFrom(), PieceFactory.getPiece(m.getPiece().getType(),m.getPiece().getColor())));
            }else if(m instanceof TakeMove){
                moveLog.AddMove(new TakeMove(m.getTo(),m.getFrom(),
                        PieceFactory.getPiece(m.getPiece().getType(),m.getPiece().getColor()),
                        PieceFactory.getPiece(((TakeMove) m).getPieceTaken().getType(),((TakeMove) m).getPieceTaken().getColor())));
            }else if(m instanceof NormalPromotionMove){
                NormalPromotionMove NPMove = new NormalPromotionMove(m.getTo(),m.getFrom(), PieceFactory.getPiece(m.getPiece().getType(),m.getPiece().getColor()));
                NPMove.setPromotedTo(PieceFactory.getPiece(((NormalPromotionMove)m).getPromotedTo().getType(),((NormalPromotionMove)m).getPromotedTo().getColor()));
                moveLog.AddMove(NPMove);
            }else if(m instanceof TakePromotionMove){
                TakePromotionMove TPMove = new TakePromotionMove(m.getTo(),m.getFrom(),
                        PieceFactory.getPiece(m.getPiece().getType(),m.getPiece().getColor()),
                        PieceFactory.getPiece(((TakePromotionMove) m).getPieceTaken().getType(),((TakePromotionMove) m).getPieceTaken().getColor()));
                TPMove.setPromotedTo(PieceFactory.getPiece(((TakePromotionMove)m).getPromotedTo().getType(),((TakePromotionMove)m).getPromotedTo().getColor()));
                moveLog.AddMove(TPMove);
            }else if(m instanceof CastleMove){
                moveLog.AddMove(new CastleMove(m.getTo(),
                        m.getFrom(),
                        ((CastleMove)m).getRookToPos(),
                        ((CastleMove)m).getRookToPos(),
                        PieceFactory.getPiece(m.getPiece().getType(),m.getPiece().getColor()),
                        PieceFactory.getPiece(((CastleMove) m).getSwappedRook().getType(),((CastleMove) m).getSwappedRook().getColor())));
            }
        }


    }

    public List<Move> GetAllAvailableMoves(Color color)  {

        return CheckForMovesToProtectKing(color);
    }

    private boolean isKingSafe(Color color) {
        if(color == Color.White){
            return SquareSaftyCheck.isSquareSafe(WhiteKing,color,boardSquares);
        }else{
            return SquareSaftyCheck.isSquareSafe(BlackKing,color,boardSquares);
        }
    }

    private List<Move> CheckForMovesToProtectKing(Color color)  {
        List<Move> moves = CheckMovesOfAllPieces(color);
        moves.addAll(CastleCheck.Check(this,moveLog,color));
        List<Move> safeMoves = new ArrayList<>();



        for(Move m: moves){

            this.UpdateBoard(m);

            if(this.isKingSafe(color)==true){
                safeMoves.add(m);
            }
            this.UndoMove(m);

        }

        return safeMoves;
    }

    public void UndoMove(Move move) {
        moveLog.getPastMoves().remove(moveLog.getPastMoves().size()-1);

        if(move instanceof NormalPromotionMove){
            boardSquares[move.getFrom().x][move.getFrom().y].setPiece(((NormalPromotionMove) move).getPiece());
            boardSquares[move.getTo().x][move.getTo().y].clear();
        }else if(move instanceof TakePromotionMove){
            boardSquares[move.getFrom().x][move.getFrom().y].setPiece(((TakePromotionMove) move).getPiece());
            boardSquares[move.getTo().x][move.getTo().y].setPiece(((TakePromotionMove) move).getPieceTaken());
        }else if(move instanceof CastleMove){
            boardSquares[move.getFrom().getX()][move.getFrom().getY()].setPiece(move.getPiece());
            boardSquares[move.getTo().getX()][move.getTo().getY()].clear();

            boardSquares[((CastleMove) move).getRookFromPos().getX()][((CastleMove) move).getRookFromPos().getY()].setPiece(((CastleMove) move).getSwappedRook());
            boardSquares[((CastleMove) move).getRookToPos().getX()][((CastleMove) move).getRookToPos().getY()].clear();
        }else {
            boardSquares[move.getFrom().x][move.getFrom().y].setPiece(move.getPiece());
            if(move instanceof TakeMove){
                boardSquares[move.getTo().x][move.getTo().y].setPiece(((TakeMove) move).getPieceTaken());
            }else {
                boardSquares[move.getTo().x][move.getTo().y].clear();
            }
        }

        if(move.getPiece().getType() == Type.King){
            if(move.getPiece().getColor() == Color.White){
                WhiteKing = move.getFrom();
            }else{
                BlackKing = move.getFrom();
            }
        }


    }

    private List<Move> CheckMovesOfAllPieces(Color color)  {
        List<Move> moves = new ArrayList<>();



        for(int i=0;i<boardSquares.length;i++){
            for(int j=0;j<boardSquares[i].length;j++){
                if(boardSquares[i][j].isHasPiece()){
                    if(boardSquares[i][j].getPiece().getColor()==color){
                        moves.addAll(boardSquares[i][j].getPiece().GetAvailableMoves(ChessPosFactory.getPostion(i,j),boardSquares));

                    }
                }

            }
        }
        return moves;
    }

    public void UpdateBoard(Move move){
        moveLog.AddMove(move);

        if(move instanceof NormalPromotionMove){
            boardSquares[move.getTo().x][move.getTo().y].setPiece(((NormalPromotionMove) move).getPromotedTo());
            boardSquares[move.getFrom().x][move.getFrom().y].clear();
        }else if(move instanceof TakePromotionMove){
            boardSquares[move.getTo().x][move.getTo().y].setPiece(((TakePromotionMove) move).getPromotedTo());
            boardSquares[move.getFrom().x][move.getFrom().y].clear();
        }else if(move instanceof CastleMove){
            boardSquares[move.getTo().getX()][move.getTo().getY()].setPiece(((CastleMove) move).getPiece());
            boardSquares[move.getFrom().getX()][move.getFrom().getY()].clear();
            boardSquares[((CastleMove)move).getRookToPos().getX()][((CastleMove)move).getRookToPos().getY()].setPiece(((CastleMove) move).getSwappedRook());
            boardSquares[((CastleMove)move).getRookFromPos().getX()][((CastleMove)move).getRookFromPos().getY()].clear();
        }else {
            boardSquares[move.getTo().x][move.getTo().y].setPiece(move.getPiece());
            boardSquares[move.getFrom().x][move.getFrom().y].clear();
        }

        if(move.getPiece().getType() == Type.King){
            if(move.getPiece().getColor() == Color.White){
                WhiteKing = move.getTo();
            }else{
                BlackKing = move.getTo();
            }
        }
    }

    public void PrintBoard(){
        System.out.println();
        for(int i=boardSquares.length-1;i>=0;i--){
            System.out.println();
            for(int j=0;j<boardSquares[i].length;j++){
                if(boardSquares[i][j].isHasPiece()==true){
                    System.out.print(" " +boardSquares[i][j].getPiece().getType().name().charAt(0)+ boardSquares[i][j].getPiece().getType().name().charAt(1) + boardSquares[i][j].getPiece().getColor().name().charAt(0));
                }else{
                    System.out.print(" Emp");
                }

            }
        }
    }

    public void Reset() {
        for(int i=0;i<boardSquares.length;i++){
            for(int j=0;j<boardSquares[i].length;j++){
                boardSquares[i][j].clear();
            }
        }

        for(int i=0;i<8;i++){
            boardSquares[1][i].setPiece(PieceFactory.getPiece(Type.Pawn, Color.White));
        }
        boardSquares[0][0].setPiece(PieceFactory.getPiece(Type.Rook, Color.White));
        boardSquares[0][1].setPiece(PieceFactory.getPiece(Type.Knight, Color.White));
        boardSquares[0][2].setPiece(PieceFactory.getPiece(Type.Bishop, Color.White));
        boardSquares[0][3].setPiece(PieceFactory.getPiece(Type.King, Color.White));
        boardSquares[0][4].setPiece(PieceFactory.getPiece(Type.Queen, Color.White));
        boardSquares[0][5].setPiece(PieceFactory.getPiece(Type.Bishop, Color.White));
        boardSquares[0][6].setPiece(PieceFactory.getPiece(Type.Knight, Color.White));
        boardSquares[0][7].setPiece(PieceFactory.getPiece(Type.Rook, Color.White));

        for(int i=0;i<8;i++){
            boardSquares[6][i].setPiece(PieceFactory.getPiece(Type.Pawn, Color.Black));
        }
        boardSquares[7][0].setPiece(PieceFactory.getPiece(Type.Rook, Color.Black));
        boardSquares[7][1].setPiece(PieceFactory.getPiece(Type.Knight, Color.Black));
        boardSquares[7][2].setPiece(PieceFactory.getPiece(Type.Bishop, Color.Black));
        boardSquares[7][3].setPiece(PieceFactory.getPiece(Type.King, Color.Black));
        boardSquares[7][4].setPiece(PieceFactory.getPiece(Type.Queen, Color.Black));
        boardSquares[7][5].setPiece(PieceFactory.getPiece(Type.Bishop, Color.Black));
        boardSquares[7][6].setPiece(PieceFactory.getPiece(Type.Knight, Color.Black));
        boardSquares[7][7].setPiece(PieceFactory.getPiece(Type.Rook, Color.Black));

    }

    public void Setup() {
        boardSquares = new BoardSquare[8][8];

        for(int i=0;i<boardSquares.length;i++){
            for(int j=0;j<boardSquares[i].length;j++){
                boardSquares[i][j] = new BoardSquare();
            }
        }

        for(int i=0;i<8;i++){
            boardSquares[1][i].setPiece(PieceFactory.getPiece(Type.Pawn, Color.White));
        }
        boardSquares[0][0].setPiece(PieceFactory.getPiece(Type.Rook, Color.White));
        boardSquares[0][1].setPiece(PieceFactory.getPiece(Type.Knight, Color.White));
        boardSquares[0][2].setPiece(PieceFactory.getPiece(Type.Bishop, Color.White));
        boardSquares[0][3].setPiece(PieceFactory.getPiece(Type.Queen, Color.White));
        boardSquares[0][4].setPiece(PieceFactory.getPiece(Type.King, Color.White));
        boardSquares[0][5].setPiece(PieceFactory.getPiece(Type.Bishop, Color.White));
        boardSquares[0][6].setPiece(PieceFactory.getPiece(Type.Knight, Color.White));
        boardSquares[0][7].setPiece(PieceFactory.getPiece(Type.Rook, Color.White));

        WhiteKing = ChessPosFactory.getPostion(0,4);

        for(int i=0;i<8;i++){
            boardSquares[6][i].setPiece(PieceFactory.getPiece(Type.Pawn, Color.Black));
        }
        boardSquares[7][0].setPiece(PieceFactory.getPiece(Type.Rook, Color.Black));
        boardSquares[7][1].setPiece(PieceFactory.getPiece(Type.Knight, Color.Black));
        boardSquares[7][2].setPiece(PieceFactory.getPiece(Type.Bishop, Color.Black));
        boardSquares[7][3].setPiece(PieceFactory.getPiece(Type.Queen, Color.Black));
        boardSquares[7][4].setPiece(PieceFactory.getPiece(Type.King, Color.Black));
        boardSquares[7][5].setPiece(PieceFactory.getPiece(Type.Bishop, Color.Black));
        boardSquares[7][6].setPiece(PieceFactory.getPiece(Type.Knight, Color.Black));
        boardSquares[7][7].setPiece(PieceFactory.getPiece(Type.Rook, Color.Black));

        BlackKing = ChessPosFactory.getPostion(7,4);

    }

    public Board Copy()  {
        BoardSquare[][] newBoardSquares = new BoardSquare[8][8];


        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                newBoardSquares[i][j] = new BoardSquare();
                if(this.boardSquares[i][j].isHasPiece()==true){
                    boardSquares[i][j].getPiece();
                    boardSquares[i][j].getPiece().getColor();
                    boardSquares[i][j].getPiece().getType();
                    PieceFactory.getPiece(boardSquares[i][j].getPiece().getType(),boardSquares[i][j].getPiece().getColor());
                    newBoardSquares[i][j].setPiece(PieceFactory.getPiece(boardSquares[i][j].getPiece().getType(),boardSquares[i][j].getPiece().getColor()));
                }
            }
        }

        MoveLog newMoveLog= this.moveLog.Copy();



        return new Board(newBoardSquares,  newMoveLog.getPastMoves());
    }


    @Override
    public int compareTo(Board otherBoard) {

        for(int i=0;i<7;i++){
            for(int j=0;j<7;j++){
                //if a boardSquare is not equal the boards cannot be equal
                if(1 == this.boardSquares[i][j].compareTo(otherBoard.getBoardSquares()[i][j])){
                    return 1;
                }
            }
        }

        //if all boardSquares are equal then the board is equal
        return 0;
    }
}