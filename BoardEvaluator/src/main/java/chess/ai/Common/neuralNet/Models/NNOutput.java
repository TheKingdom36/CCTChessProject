package chess.ai.Common.neuralNet.Models;

import java.util.Arrays;

/**
 * Model used to store the output of the neural network
 */
public class NNOutput {
    double win_score;
    double[] probabilities;

    public NNOutput(){

    }

    public double getWin_score() {
        return win_score;
    }

    public void setWin_score(double win_score) {
        this.win_score = win_score;
    }

    public double[] getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
    }

    public NNOutput(int win_score, double[] probabilities){
        this.win_score = win_score;
        this.probabilities = probabilities;
    }

    @Override
    public String toString() {
        return "NNOutput{" +
                "win_score=" + win_score +
                ", probabilities=" + Arrays.toString(probabilities) +
                '}';
    }
}
