package chess.ai.Common.Networked;

import chess.ai.Common.neuralNet.Models.BasicNeuralNetwork;
import chess.ai.Common.neuralNet.Models.NNOutput;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BatchOfEvaluatedBoards {
    @Getter
    @Setter
    private List<NNOutput> nnOutputs;
    @Getter
    @Setter
    private BasicNeuralNetwork basicNeuralNetwork;

    public BatchOfEvaluatedBoards(){

    }

    public BatchOfEvaluatedBoards(List<NNOutput> nnOutputs, BasicNeuralNetwork basicNeuralNetwork){
        this.nnOutputs = nnOutputs;
        this.basicNeuralNetwork = basicNeuralNetwork;
    }

}
