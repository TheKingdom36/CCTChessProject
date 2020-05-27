package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.Plane;

/**
 * Final layer of the neural network, has a policy and value head used to coordinate the passing back of the weight updates for both heads
 */
public class OutputLayer extends FullyConnectedLayer {


    private ValueHead valueHead;
    private PolicyHead policyHead;

    double[][] SoftmaxValues;
    double learningRate=0.2;

    public OutputLayer(int numOfHiddenNodes,int numOfPolicyOutputNodes) {
        super(numOfHiddenNodes);
        SoftmaxValues = new double[Layer.getBatchSize()][];
        policyHead = new PolicyHead(1180,learningRate,numOfPolicyOutputNodes);
        valueHead = new ValueHead(learningRate);
        policyHead.setPreviousLayer(this);
        valueHead.setPreviousLayer(this);
    }

    public OutputLayer(int numOfHiddenNodes,double[] actualValue,double[][] actualPolicy,int numOfPolicyOutputNodes) {
        super(numOfHiddenNodes);


        SoftmaxValues = new double[Layer.getBatchSize()][];
        policyHead = new PolicyHead(1180,learningRate,numOfPolicyOutputNodes,actualPolicy);
        valueHead = new ValueHead(learningRate,actualValue);

        policyHead.setPreviousLayer(this);
        valueHead.setPreviousLayer(this);


    }

    @Override
    public void CalculateOutputplanes() {

        previousLayer.CalculateOutputplanes();

        super.CalculateOutputplanes();

        policyHead.CalculateOutputplanes();
        valueHead.CalculateOutputplanes();

    }



    @Override
    public void CalculateErrors(){

        policyHead.CalculateErrors();
        valueHead.CalculateErrors();


        // number of batches  number of error planes, width of error planes, height of error planes
        errors = new double[Layer.getBatchSize()][this.getOutputPlanes()[0].length][this.getOutputPlanes()[0][0].getValues().length][this.getOutputPlanes()[0][0].getValues()[0].length];


        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

            for(int width=0;width<this.errors[0][0].length;width++){
                for(int height=0; height < this.errors[0][0][0].length;height++){

                    for(int widthj=0;widthj<this.policyHead.getErrors()[0][0].length;widthj++){
                        errors[batchElement][0][width][height] += policyHead.getErrors()[batchElement][0][widthj][height]*(policyHead).getKernels()[0].getValues()[0][widthj][height];

                    }

                    errors[batchElement][0][width][height] += valueHead.getErrors()[batchElement][0][0][0]* (valueHead).getKernels()[0].getValues()[0][0][height];


                }

            }
        }


        //just copy the errors to propagate backwards
    }


    public void setPolicyKernels(Kernel[] kernels){
        policyHead.setKernels(kernels);
    }

    public void setValueKernels(Kernel[] kernels){
        valueHead.setKernels(kernels);
    }

    public void setActualValue(double[] actualValue){
        valueHead.setActualValue(actualValue);
    }


    public void setPolicyValue(double[][] policyValue){
        policyHead.setPolicy(policyValue);
    }

    public Plane[][] getValues() {
        return valueHead.getOutputPlanes();
    }

    public Plane[][] getPolicy(){
        return policyHead.getOutputPlanes();
    }


    @Override
    public Kernel[] getKernels() {
        return weights;
    }
}