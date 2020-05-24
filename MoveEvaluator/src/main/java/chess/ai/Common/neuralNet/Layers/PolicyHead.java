package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.ActivationFunctions.Softmax;
import chess.ai.Common.neuralNet.Models.Map;

public class PolicyHead extends FullyConnectedLayer {

    double[][] SoftmaxValues;
    double learningRate;
    double[][] actualPolicy;
    int numOfOutputNodes;

    public PolicyHead(int numOfHiddenNodes,double learningRate,int numOfOutputNodes) {
        super(numOfHiddenNodes);
        this.learningRate = learningRate;
        this.numOfOutputNodes = numOfOutputNodes;
    }

    public PolicyHead(int numOfHiddenNodes,double learningRate,int numOfOutputNodes,double[][] actualPolicy) {
        super(numOfHiddenNodes);
        this.learningRate = learningRate;
        this.actualPolicy = actualPolicy;
        this.numOfOutputNodes = numOfOutputNodes;
    }

    public void setPolicy(double[][] actualPolicy){
        this.actualPolicy = actualPolicy;
    }

    @Override
    public void CalculateOutputMaps() {

        SoftmaxValues = new double[Layer.getBatchSize()][numOfOutputNodes];

        if(outputMaps == null) {
            outputMaps = new Map[Layer.getBatchSize()][1];
        }
        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            outputMaps[batchElement][0] = CalculationPerBatchElement(batchElement);
        }



    }

    @Override
    protected Map CalculationPerBatchElement(int batchElement) {

        Map inputMap = Map.ConvertMapsToMap(getPreviousLayer().getOutputMaps()[batchElement],getPreviousLayer().getOutputMaps()[batchElement].length*getPreviousLayer().getOutputMaps()[batchElement][0].getWidth()*getPreviousLayer().getOutputMaps()[batchElement][0].getHeight(),1);
        Map returnMap = new Map(numOfHiddenNodes,1);

        if(weights == null){
            RandomlyInitializeWeights(inputMap.getHeight()*inputMap.getWidth());
        }

        double tempValue;
        for(int i=0;i<weights[0].getWidth();i++){
            tempValue=0;
            for(int j=0;j<weights[0].getHeight();j++){
                tempValue += inputMap.getValues()[j][0]*weights[0].getValues()[0][i][j];
            }

            returnMap.setValue(i,0,tempValue);
        }


        SoftmaxValues[batchElement] = new double[returnMap.getWidth()];

        for(int i=0;i<returnMap.getWidth();i++){
            SoftmaxValues[batchElement][i] = returnMap.getValues()[i][0];
        }


        SoftmaxValues[batchElement] = Softmax.calculate(SoftmaxValues[batchElement]);

        for(int i=0;i<returnMap.getWidth();i++){
            returnMap.setValue(i,0,SoftmaxValues[batchElement][i]);

        }

        return returnMap;
    }


    public void CalculateErrors(){
        // number of batches  number of error planes, width of error planes, height of error planes
        errors = new double[Layer.getBatchSize()][1][this.numOfHiddenNodes][1];

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            for (int currWidth = 0; currWidth < this.numOfHiddenNodes; currWidth++) {

                errors[batchElement][0][currWidth][0] = learningRate*(SoftmaxValues[batchElement][currWidth] - actualPolicy[batchElement][currWidth]);
            }
        }
    }




}
