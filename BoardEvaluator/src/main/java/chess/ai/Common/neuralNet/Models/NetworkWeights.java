package chess.ai.Common.neuralNet.Models;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Random;

public class NetworkWeights implements Serializable {
    @Getter
    @Setter
    Kernel[] Conv1Kernels;

    @Getter
    @Setter
    Kernel[] Conv2Kernels;

    @Getter
    @Setter
    Kernel[] FCKernels;

    @Getter
    @Setter
    Kernel[] outputKernels;

    @Getter
    @Setter
    Kernel[] policyHeadKernels;

    @Getter
    @Setter
    Kernel[] valueHeadKernels;

    @Getter
    @Setter
    double[] batchNorm1GammaValues;

    @Getter
    @Setter
    double[] batchNorm2GammaValues;

    @Getter
    @Setter
    double[] batchNorm3GammaValues;

    @Getter
    @Setter
    double[] batchNorm1BetaValues;

    @Getter
    @Setter
    double[] batchNorm2BetaValues;

    @Getter
    @Setter
    double[] batchNorm3BetaValues;



    public NetworkWeights(){
    }

    public NetworkWeights(Boolean random){
        Conv1Kernels = intializeKernels(10,3,3,21);
        Conv2Kernels = intializeKernels(10,3,3,10);
        FCKernels = intializeKernels(1,20,640,1);
        outputKernels = intializeKernels(1,20,20,1);
        policyHeadKernels = intializeKernels(1,20,20,1);
        valueHeadKernels = intializeKernels(1,1,20,1);

        batchNorm1BetaValues = new double[10];
        batchNorm2BetaValues = new double[10];
        batchNorm3BetaValues = new double[1];
        batchNorm1GammaValues = new double[10];
        batchNorm2GammaValues = new double[10];
        batchNorm3GammaValues = new double[1];

        Random rand = new Random();

        for(int i=0; i<batchNorm1BetaValues.length;i++){
            batchNorm1BetaValues[i] = 0.3*rand.nextDouble() + 0.2;
            batchNorm1GammaValues[i]=  0.3*rand.nextDouble() + 0.2;
        }

        for(int i=0; i<batchNorm2BetaValues.length;i++){
            batchNorm1BetaValues[i] = 0.3*rand.nextDouble() + 0.2;
            batchNorm1GammaValues[i]=  0.3*rand.nextDouble() + 0.2;
        }

        for(int i=0; i<batchNorm3BetaValues.length;i++){
            batchNorm1BetaValues[i] = 0.3*rand.nextDouble() + 0.2;
            batchNorm1GammaValues[i]=  0.3*rand.nextDouble() + 0.2;
        }
    }

    private Kernel[] intializeKernels(int numOfKernels, int kernalWidth, int kernalHeight, int kernalDepth){

        Kernel[] kernels = new Kernel[numOfKernels];

        for(int i=0;i<numOfKernels;i++){
            kernels[i] = new Kernel(kernalWidth,kernalHeight,kernalDepth);
            kernels[i].InitializeRandomValues();
        }

        return kernels;
    }

}
