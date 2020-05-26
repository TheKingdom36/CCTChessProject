package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.ActivationFunctions.ReLU;
import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.plane;

public class ReLULayer extends Layer {
    ReLU reLU;
    Kernel[] kernels;
    public ReLULayer(){
        reLU = new ReLU();
    }

    @Override
    public Kernel[] getKernels() {
        return  kernels;
    }

    @Override
    public void CalculateOutputplanes() {

        previousLayer.CalculateOutputplanes();

        outputplanes = new plane[Layer.getBatchSize()][previousLayer.getOutputplanes().length];


        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            outputplanes[batchElement]=CalculationPerBatchElement(batchElement);
        }
    }


    private plane[] CalculationPerBatchElement(int batchElement) {
        plane[] planes = previousLayer.getOutputplanes()[batchElement];

        //Each value is being put through the leakyReLU to assign a new value
        for(plane m: planes){
            for(int i=0;i<m.getWidth();i++){
                for(int j=0;j<m.getHeight();j++){
                    m.setValue(i,j,reLU.getOutput(m.getValues()[i][j]));
                }
            }
        }

        return planes;
    }

    public void CalculateErrors(){

        this.nextLayer.CalculateErrors();

        //If its a conv layer do the conv error process
        if(nextLayer instanceof ConvLayer) {
            // number of batches  number of error planes, width of error planes, height of error planes
            errors = new double[Layer.getBatchSize()][this.getOutputplanes()[0].length][this.getOutputplanes()[0][0].getWidth()][this.getOutputplanes()[0][0].getHeight()];

            // if its a layer with weights/kernels needs to perform  calculation

            Kernel[] nextKernels = nextLayer.getKernels();
            double[][][][] nextErrors = new double[nextLayer.getErrors().length][nextLayer.getErrors()[0].length][nextLayer.getErrors()[0][0].length][nextLayer.getErrors()[0][0][0].length];

            for(int i=0;i<nextErrors.length;i++){
                for(int j=0;j<nextErrors[0].length;j++){
                    for(int k=0;k<nextErrors[0][0].length;k++){
                        for(int d=0;d<nextErrors[0][0][0].length;d++){
                            nextErrors[i][j][k][d]= nextLayer.getErrors()[i][j][k][d];
                        }
                    }
                }
            }

            for (int batchElement = 0; batchElement < nextErrors.length; batchElement++) {
                for (int depth = 0; depth < nextErrors[0].length; depth++) {
                    nextErrors[batchElement][depth] = extendMatrix(nextErrors[batchElement][depth], nextKernels[0].getWidth() - 1, nextKernels[0].getHeight()-1);
                }
            }

            double totalError;
            System.out.println();
            for (int batchElement = 0; batchElement < Layer.getBatchSize(); batchElement++) {
                for (int kernelNum = 0; kernelNum < this.outputplanes[0].length; kernelNum++) {

                    System.out.println();
                    // System.out.println(batchElement + " " + kernelNum + " " + getErrors()[batchElement][kernelNum].length);
                    for (int i = 0; i < this.getErrors()[batchElement][kernelNum].length; i++) {
                        for (int j = 0; j < this.getErrors()[batchElement][kernelNum][i].length; j++) {
                            totalError = 0;

                            if (this.outputplanes[batchElement][kernelNum].getValues()[i][j] > 0) {
                                for (int nextLayerKernelNum = 0; nextLayerKernelNum < nextKernels.length; nextLayerKernelNum++) {
                                    for (int weighti = -nextKernels[0].getWidth() / 2; weighti <= nextKernels[0].getWidth() / 2; weighti++) {
                                        for (int weightj = -nextKernels[0].getHeight() / 2; weightj <= nextKernels[0].getHeight() / 2; weightj++) {
                                                totalError += nextErrors[batchElement][nextLayerKernelNum][i +nextKernels[0].getWidth() / 2+ weighti][j + nextKernels[0].getHeight() / 2 + weightj] * nextKernels[nextLayerKernelNum].getValues()[nextLayerKernelNum][nextKernels[0].getWidth() / 2 - weighti][nextKernels[0].getHeight() / 2 - weightj];

                                        }
                                    }
                                    errors[batchElement][kernelNum][i][j] = errors[batchElement][kernelNum][i][j] - totalError;
                                }
                            } else {
                                errors[batchElement][kernelNum][i][j] = 0;
                            }


                        }

                    }
                }
            }
        }else if(nextLayer instanceof FullyConnectedLayer){


            // number of batches  number of error planes, width of error planes, height of error planes
            errors = new double[Layer.getBatchSize()][1][this.getOutputplanes()[0].length*this.getOutputplanes()[0][0].getWidth()*this.getOutputplanes()[0][0].getHeight()][1];

            // if its a layer with weights/kernels needs to perform  calculation

            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

                for(int width=0;width<this.errors[0][0].length;width++){
                    for(int widthNextLayerErrors=0;widthNextLayerErrors<this.nextLayer.getErrors()[0][0].length;widthNextLayerErrors++){
                        errors[batchElement][0][width][0] += nextLayer.getErrors()[batchElement][0][widthNextLayerErrors][0]*(nextLayer).getKernels()[0].getValues()[0][widthNextLayerErrors][width];

                    }

                }

            }

            //if a conv connected fc layer need to reshape the errors into a plane stack
            if(((FullyConnectedLayer)nextLayer).connectedToConvLayer == true){
                errors = ReshapeError(errors);
            }

        }



    }


    private double[][][][] ReshapeError(double[][][][] errors){
        double[][][][] reshapeErrors = new double[Layer.getBatchSize()][previousLayer.getOutputplanes()[0].length][previousLayer.getOutputplanes()[0][0].getWidth()][previousLayer.getOutputplanes()[0][0].getHeight()];

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            for(int planeDepth=0;planeDepth<previousLayer.getOutputplanes()[0].length;planeDepth++){
                for(int planeWidth=0;planeWidth<previousLayer.getOutputplanes()[0][0].getWidth();planeWidth++){
                    for(int planeHeight=0;planeHeight<previousLayer.getOutputplanes()[0][0].getHeight();planeHeight++){
                        reshapeErrors[batchElement][planeDepth][planeWidth][planeHeight] = errors[batchElement][0][planeDepth+planeWidth+planeHeight][0];
                    }
                }
            }
        }

        return  reshapeErrors;
    }
}
