package chess.ai.Common.ChessBoard.Util;


import chess.ai.Common.ChessBoard.Models.Position;

public class Directions {

    //North is from board in perspective of white player


    public static final Position N     =   new Position(1, 0);
    public static final Position NE    =   new Position(1, 1);
    public static final Position E     =   new Position(0, 1);
    public static final Position SE    =   new Position(-1, 1);
    public static final Position S     =   new Position(-1, 0);
    public static final Position SW    =   new Position(-1, -1);
    public static final Position W     =   new Position(0, -1);
    public static final Position NW    =   new Position(1, -1);

}
