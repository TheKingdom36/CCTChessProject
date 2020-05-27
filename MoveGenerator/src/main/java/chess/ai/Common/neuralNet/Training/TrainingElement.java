package chess.ai.Common.neuralNet.Training;

/**
 * An element used for training a neural network
 */
public class TrainingElement {
    private double guessedResult;
    private double actualResult;
    private double[] guessedPolicy;
    private double[] targetPolicy;

    public double getGuessedResult() {
        return guessedResult;
    }

    public double getActualResult() {
        return actualResult;
    }

    public double[] getGuessedPolicy() {
        return guessedPolicy;
    }

    public double[] getTargetPolicy() {
        return targetPolicy;
    }

    public TrainingElement(double guessedResult, double actualResult, double[] guessedPolicy, double[] targetPolicy){
        this.guessedResult = guessedResult;
        this.actualResult = actualResult;
        this.guessedPolicy = guessedPolicy;
        this.targetPolicy = targetPolicy;
    }


}
