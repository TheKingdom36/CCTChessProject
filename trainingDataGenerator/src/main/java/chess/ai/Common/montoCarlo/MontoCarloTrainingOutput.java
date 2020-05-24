package chess.ai.Common.montoCarlo;


import chess.ai.Common.ChessBoard.Moves.Move;

public class MontoCarloTrainingOutput {
   TrainingSample sample;
   Move nextMove;

   public MontoCarloTrainingOutput(){

   }

    public MontoCarloTrainingOutput(TrainingSample sample, Move nextMove){
        this.sample = sample;
        this.nextMove = nextMove;
    }

    public TrainingSample getSample() {
        return sample;
    }

    public void setSample(TrainingSample sample) {
        this.sample = sample;
    }

    public Move getNextMove() {
        return nextMove;
    }

    public void setNextMove(Move nextMove) {
        this.nextMove = nextMove;
    }
}
