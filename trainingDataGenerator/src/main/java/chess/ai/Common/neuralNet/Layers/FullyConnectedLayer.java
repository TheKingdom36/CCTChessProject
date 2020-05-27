package chess.ai.Common.neuralNet.Layers;

import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.Plane;

public class FullyConnectedLayer extends Layer {
    Kernel[] weights;
    int numOfHiddenNodes;
    int depthOfplanes;
    Plane[] inputplanesPerBatches;
    boolean connectedToConvLayer;
    public Kernel[] getKernals() {
        return weights;
    }

    public void setKernels(Kernel[] weights) {
        this.weights = weights;
    }

    public FullyConnectedLayer(int numOfHiddenNodes){
        this.numOfHiddenNodes = numOfHiddenNodes;
        depthOfplanes = 1;
    }


    @Override
    public void CalculateOutputplanes() {

        previousLayer.CalculateOutputplanes();

        if(weights==null){
            RandomlyInitializeWeights(this.previousLayer.getOutputPlanes()[0].length*previousLayer.getOutputPlanes()[0][0].getWidth()*previousLayer.getOutputPlanes()[0][0].getHeight());
        }

        outputplanes = new Plane[Layer.getBatchSize()][1];

        if(previousLayer.getOutputPlanes()[0].length >1){
            connectedToConvLayer=true;
        }


        inputplanesPerBatches = new Plane[Layer.getBatchSize()];
        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            outputplanes[batchElement][0] = CalculationPerBatchElement(batchElement);
        }



    }

    protected Plane CalculationPerBatchElement(int batchElement) {
        Plane returnplane = new Plane(numOfHiddenNodes,1);
        Plane inputplane = Plane.ConvertPlanesToPlane(getPreviousLayer().getOutputPlanes()[batchElement],getPreviousLayer().getOutputPlanes()[batchElement].length*getPreviousLayer().getOutputPlanes()[batchElement][0].getWidth()*getPreviousLayer().getOutputPlanes()[batchElement][0].getHeight(),1);

        inputplanesPerBatches[batchElement] = inputplane;


        double tempValue;
        for(int i=0;i<numOfHiddenNodes;i++){
            tempValue=0;
            for(int j=0;j<inputplane.getHeight()*inputplane.getWidth();j++){
                   tempValue += inputplane.getValues()[j][0]*weights[0].getValues()[0][i][j];
            }

            returnplane.setValue(i,0,tempValue);
        }

        return returnplane;
    }

    protected void RandomlyInitializeWeights(int numOfInputNeurons) {

        weights = new Kernel[1];
        weights[0] = new Kernel(numOfHiddenNodes,numOfInputNeurons,1);
        weights[0].InitializeRandomValues();

    }

    @Override
    public Kernel[] getKernels() {
        return weights;
    }

    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();
        if(nextLayer instanceof FullyConnectedLayer){
            // number of batches  number of error planes, width of error planes, height of error planes
            errors = new double[Layer.getBatchSize()][this.getOutputPlanes()[0].length][this.getOutputPlanes()[0][0].getValues().length][this.getOutputPlanes()[0][0].getValues()[0].length];

            // if its a layer with weights/kernels needs to perform  calculation
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

                for(int width=0;width<this.errors[0][0].length;width++){
                    for(int widthj=0;widthj<this.nextLayer.getErrors()[0][0].length;widthj++){
                        errors[batchElement][0][width][0] += nextLayer.getErrors()[batchElement][0][widthj][0]*(nextLayer).getKernels()[0].getValues()[0][widthj][width];
                    }

                }

            }
        }else{
            errors= nextLayer.getErrors();
        }



    }


    public void UpdateWeights(){
        for(int width=0;width<this.weights[0].getWidth();width++){
            for(int height=0; height < this.weights[0].getHeight();height++){
                for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

                    for(int errorWidth=0;errorWidth<errors[batchElement][0].length;errorWidth++){
                        double cal = weights[0].getValues()[0][errorWidth][height] - errors[batchElement][0][errorWidth][0]* inputplanesPerBatches[batchElement].getValues()[width][0];
                        this.weights[0].setValue(0,width,height,cal);
                    }
                }
            }
        }


    }




}
