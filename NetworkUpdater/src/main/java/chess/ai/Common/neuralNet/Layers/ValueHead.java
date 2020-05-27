package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.ActivationFunctions.ActivationFunction;
import chess.ai.Common.neuralNet.ActivationFunctions.tanh;
import chess.ai.Common.neuralNet.Models.Plane;

public class ValueHead extends FullyConnectedLayer {

    double learningRate;
    ActivationFunction activationFunc;
    double[] actualValue;
    public ValueHead( double learningRate) {
        super(1);
        this.learningRate = learningRate;
        activationFunc = new tanh();
    }

    public ValueHead( double learningRate,double[] actualValue) {
        super(1);
        this.learningRate = learningRate;
        activationFunc = new tanh();
        this.actualValue = actualValue;
    }


    public void setActualValue(double[] actualValue){
        this.actualValue = actualValue;
    }

    @Override
    public void CalculateOutputplanes() {

        if(outputplanes == null) {
            outputplanes = new Plane[Layer.getBatchSize()][1];
        }
        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            outputplanes[batchElement][0] = CalculationPerBatchElement(batchElement);
        }

    }

    @Override
    protected Plane CalculationPerBatchElement(int batchElement) {

        Plane inputplane = Plane.ConvertPlanesToPlane(getPreviousLayer().getOutputPlanes()[batchElement],getPreviousLayer().getOutputPlanes()[batchElement].length*getPreviousLayer().getOutputPlanes()[batchElement][0].getWidth()*getPreviousLayer().getOutputPlanes()[batchElement][0].getHeight(),1);
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

        returnplane.setValue(0,0,activationFunc.getOutput(returnplane.getValues()[0][0]));


        return returnplane;
    }


    public void CalculateErrors(){


        // number of batches  number of error planes, width of error planes, height of error planes
        errors = new double[Layer.getBatchSize()][1][1][1];

        // if its a layer with weights/kernels needs to perform  calculation

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            errors[batchElement][0][0][0] = 2*( actualValue[batchElement] - outputplanes[batchElement][0].getValues()[0][0]);
        }

    }


    public void UpdateWeights(){
        for(int kernelNum=0;kernelNum<weights.length;kernelNum++){

            for(int depth=0;depth<this.weights[0].getDepth();depth++){
                for(int width=0;width<this.weights[0].getWidth();width++){
                    for(int height=0; height < this.weights[0].getHeight();height++){
                        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                            double cal = weights[kernelNum].getValues()[depth][width][height] - errors[batchElement][kernelNum][width][0]* inputplanesPerBatches[batchElement].getValues()[width][0];
                            this.weights[kernelNum].setValue(depth,width,height,cal);
                        }
                    }
                }
            }
        }
    }


}
