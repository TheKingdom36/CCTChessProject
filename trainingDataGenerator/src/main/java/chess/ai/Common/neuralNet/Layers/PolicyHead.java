package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.ActivationFunctions.Softmax;
import chess.ai.Common.neuralNet.Models.Plane;

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
    public void CalculateOutputplanes() {

        SoftmaxValues = new double[Layer.getBatchSize()][numOfOutputNodes];

        if(outputplanes == null) {
            outputplanes = new Plane[Layer.getBatchSize()][1];
        }
        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            outputplanes[batchElement][0] = CalculationPerBatchElement(batchElement);
        }



    }

    @Override
    protected Plane CalculationPerBatchElement(int batchElement) {
        Plane inputplane = Plane.ConvertPlanesToPlane(getPreviousLayer().getOutputPlanes()[batchElement],
                getPreviousLayer().getOutputPlanes()[batchElement].length*getPreviousLayer().getOutputPlanes()[batchElement][0].getWidth()*getPreviousLayer().getOutputPlanes()[batchElement][0].getHeight(),
                1);
        Plane returnplane = new Plane(numOfHiddenNodes,1);
        if(weights == null){
            RandomlyInitializeWeights(inputplane.getHeight()*inputplane.getWidth());
        }

        double tempValue;
        for(int i=0;i<weights[0].getWidth();i++){
            tempValue=0;
            for(int j=0;j<weights[0].getHeight();j++){
                tempValue += inputplane.getValues()[j][0]*weights[0].getValues()[0][i][j];
            }

            returnplane.setValue(i,0,tempValue);
        }
        SoftmaxValues[batchElement] = new double[returnplane.getWidth()];

        for(int i=0;i<returnplane.getWidth();i++){
            SoftmaxValues[batchElement][i] = returnplane.getValues()[i][0];
        }

        SoftmaxValues[batchElement] = Softmax.calculate(SoftmaxValues[batchElement]);

        for(int i=0;i<returnplane.getWidth();i++){
            returnplane.setValue(i,0,SoftmaxValues[batchElement][i]);

        }

        return returnplane;
    }


    public void CalculateErrors(){
        // number of batches  number of error planes, width of error planes, height of error planes
        errors = new double[Layer.getBatchSize()][1][this.numOfHiddenNodes][1];

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            for (int currWidth = 0; currWidth < this.numOfHiddenNodes; currWidth++) {

                errors[batchElement][0][currWidth][0] = learningRate*(1/Layer.getBatchSize())*(SoftmaxValues[batchElement][currWidth] - actualPolicy[batchElement][currWidth]);
            }
        }
    }




}
