package chess.ai.Common.montoCarlo.interfaces;

import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.montoCarlo.MontoCarloTrainingOutput;
import chess.ai.Common.neuralNet.Models.BoardState;

public interface IMontoCarloTree {
    Move findNextMove(BoardState boardState, long searchTime);

    MontoCarloTrainingOutput findNextMoveTraining(BoardState boardState, long searchTime);
}
