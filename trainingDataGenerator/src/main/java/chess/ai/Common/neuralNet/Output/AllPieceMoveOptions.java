package chess.ai.Common.neuralNet.Output;


import chess.ai.Common.ChessBoard.Enums.Type;
import chess.ai.Common.ChessBoard.Models.Position;
import chess.ai.Common.ChessBoard.Util.Directions;

import java.util.ArrayList;
import java.util.List;

public class AllPieceMoveOptions {
    static private List<MoveOption> moveOptions;

    public static List<MoveOption> getMoveOptions() {
        if(moveOptions==null){
            GenerateAllPieceMoveOptions();
        }
        return moveOptions;
    }

    private static void GenerateAllPieceMoveOptions(){
        moveOptions = new ArrayList<>();

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){

                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.N);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.NE);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.E);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.SE);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.S);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.SW);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.W);
                addAllQueenMovesInDirectionToMoveOptions(i,j, Directions.NW);

            }
        }

        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++) {
                addAllKnightMovesToMoveOptions(i, j);
            }
        }

        for(int i=0;i<8;i++){
            addAllPawnPromotionMoves(i);
        }


    }

    private static void addAllPawnPromotionMoves(int yPos) {
        moveOptions.add(new PawnPromotionMoveOption(new Position(7,yPos), Directions.N, Type.Queen));
        moveOptions.add(new PawnPromotionMoveOption(new Position(7,yPos), Directions.N, Type.Rook));
        moveOptions.add(new PawnPromotionMoveOption(new Position(7,yPos), Directions.N, Type.Bishop));
        moveOptions.add(new PawnPromotionMoveOption(new Position(7,yPos), Directions.N, Type.Knight));

        if(yPos != 7) {
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NE, Type.Queen));
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NE, Type.Rook));
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NE, Type.Bishop));
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NE, Type.Knight));
        }

        if(yPos !=0) {
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NW, Type.Queen));
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NW, Type.Rook));
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NW, Type.Bishop));
            moveOptions.add(new PawnPromotionMoveOption(new Position(7, yPos), Directions.NW, Type.Knight));
        }
    }

    private static void addAllKnightMovesToMoveOptions(int i, int j) {
        CheckIfValidKnightMove(i,j,new Position( (Directions.N).getX() + Directions.NE.getX(),(Directions.N).getY() + Directions.NE.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.N).getX() + Directions.NW.getX(),(Directions.N).getY() + Directions.NW.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.E).getX() + Directions.NE.getX(),(Directions.E).getY() + Directions.NE.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.E).getX() + Directions.SE.getX(),(Directions.E).getY() + Directions.SE.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.S).getX() + Directions.SE.getX(),(Directions.S).getY() + Directions.SE.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.S).getX() + Directions.SW.getX(),(Directions.S).getY() + Directions.SW.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.W).getX() + Directions.SW.getX(),(Directions.W).getY() + Directions.SW.getY()));
        CheckIfValidKnightMove(i,j,new Position( (Directions.W).getX() + Directions.NW.getX(),(Directions.W).getY() + Directions.NW.getY()));

    }


    private static void CheckIfValidKnightMove(int xPos, int yPos, Position direction){
        Position SquarePosition = new Position(xPos+direction.getX(),yPos+direction.getY());
        if(SquarePosition.getX()<=7 && SquarePosition.getX()>=0&&SquarePosition.getY()<=7&&SquarePosition.getY()>=0){
               moveOptions.add(new KnightMoveOption(new Position(xPos,yPos),direction));
        }
    }

    private static void addAllQueenMovesInDirectionToMoveOptions(int xPos, int yPos, Position direction) {
        boolean hitEdge = false;

        int curXPos = xPos;
        int curYPos = yPos;
        int distanceFromPiecePos=0;
        do{
            curXPos = curXPos+ direction.getX();
            curYPos = curYPos+ direction.getY();
            distanceFromPiecePos++;

            if(curXPos <0 || curXPos>7|| curYPos>7||curYPos<0){
                hitEdge = true;
                continue;
            }

            moveOptions.add(new QueenMoveOption(new Position(xPos,yPos),new Position(curXPos-xPos,curYPos-yPos),distanceFromPiecePos));

        }while(hitEdge==false);


    }
}
