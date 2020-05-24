package chess.ai.Common.neuralNet.Util;


import chess.ai.Common.neuralNet.Layers.Layer;
import chess.ai.Common.neuralNet.Models.Kernel;

public class DummyLayer extends Layer {
    Kernel[] kernels;
    public DummyLayer(){
        kernels = new Kernel[1];

        for(int num=0;num<kernels.length;num++){
            kernels[num]=new Kernel(10,10,1);

            for(int i=0;i<kernels[num].getWidth();i++){
                for(int j=0;j<kernels[num].getHeight();j++) {
                    for(int k=0;k<kernels[num].getDepth();k++) {
                        kernels[num].setValue(k, i, j, 1);
                    }
                }
            }
        }
    }

    @Override
    public void CalculateOutputMaps() {


    }

    @Override
    public Kernel[] getKernels() {
        return kernels;
    }

    @Override
    public void CalculateErrors() {
        errors = new double[Layer.getBatchSize()][1][8][1];

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            for(int kernelNum=0;kernelNum<1;kernelNum++){
                for(int i=0;i<this.getErrors()[batchElement][kernelNum].length;i++){
                    for(int j=0;j<this.getErrors()[batchElement][kernelNum][i].length;j++){
                        errors[batchElement][kernelNum][i][j] = 1;
                    }
                }
            }
        }
    }

    public void UpdateWeights() {

    }
}
