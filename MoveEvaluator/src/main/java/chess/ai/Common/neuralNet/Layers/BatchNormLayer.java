package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.Map;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

public class BatchNormLayer extends Layer {
    double[] BatchMean;
    double[] BatchVariance;
    @Getter
    @Setter
    double[] gamma;
    @Getter
    @Setter
    double[] beta;
    Map[][] batchTransformedMaps;
    double[] gammaErrors;
    double[] betaErrors;

    Kernel[] kernels;

    public BatchNormLayer(){

    }

    @Override
    public Kernel[] getKernels() {
        return kernels;
    }

    @Override
    public void CalculateOutputMaps() {
        getPreviousLayer().CalculateOutputMaps();

        //Each map depth will have a different mean and varience
        BatchMean = new double[getPreviousLayer().getOutputMaps()[0].length];
        BatchVariance = new double[getPreviousLayer().getOutputMaps()[0].length];
        batchTransformedMaps = new Map[Layer.getBatchSize()][getPreviousLayer().getOutputMaps()[0].length];

        for (int i=0;i<batchTransformedMaps.length;i++){
            for(int j=0;j<batchTransformedMaps[i].length;j++){
                batchTransformedMaps[i][j] = new Map(previousLayer.getOutputMaps()[0][0].getWidth(),previousLayer.getOutputMaps()[0][0].getHeight());
            }
        }

        Random rand = new Random();

        if(gamma==null || beta==null){
            gamma = new double[getPreviousLayer().getOutputMaps()[0].length];
            beta = new double[getPreviousLayer().getOutputMaps()[0].length];

            for(int i=0; i<gamma.length;i++){
                gamma[i] = (rand.nextDouble() - 0.5) / 10;
                beta[i] = (rand.nextDouble() - 0.5) / 10;
            }
        }
        gammaErrors =  new double[getPreviousLayer().getOutputMaps()[0].length];
        betaErrors =  new double[getPreviousLayer().getOutputMaps()[0].length];



        CalculateBatchMean(getPreviousLayer().getOutputMaps());
        CalculateBatchVariance(getPreviousLayer().getOutputMaps());

        CalculateBatchNorm(getPreviousLayer().getOutputMaps());

        ScaleAndShift(this.outputMaps);



    }

    private void ScaleAndShift(Map[][] outputMaps) {
        for(int depth = 0; depth<this.previousLayer.getOutputMaps()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.previousLayer.getOutputMaps()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.previousLayer.getOutputMaps()[0][0].getHeight(); height++){
                        this.outputMaps[batchElement][depth].setValue(width,height,gamma[depth]*this.outputMaps[batchElement][depth].getValues()[width][height] + beta[depth]);
                    }
                }
            }
        }
    }

    private void CalculateBatchNorm(Map[][] Maps) {

        this.outputMaps = new Map[getPreviousLayer().getOutputMaps().length][getPreviousLayer().getOutputMaps()[0].length];


        for(int i = 0; i< Layer.getBatchSize(); i++){
            for(int j = 0; j<getPreviousLayer().getOutputMaps()[0].length; j++){
                this.outputMaps[i][j] = new Map(getPreviousLayer().getOutputMaps()[0][0].getWidth(),getPreviousLayer().getOutputMaps()[0][0].getHeight());
            }
        }


        for(int mapDepth =0 ; mapDepth<Maps[0].length;mapDepth++){
            for(int batchElement=0;batchElement<Maps.length;batchElement++){
                for(int i=0;i<Maps[batchElement][mapDepth].getWidth();i++){
                    for(int j=0;j<Maps[batchElement][mapDepth].getHeight();j++){

                        double value = ((Maps[batchElement][mapDepth].getValues()[i][j]-BatchMean[mapDepth])/ Math.pow(BatchVariance[mapDepth] + Math.E,0.5));
                        outputMaps[batchElement][mapDepth].getValues()[i][j] = value;
                        batchTransformedMaps[batchElement][mapDepth].getValues()[i][j] =  value;
                    }
                }
            }
        }
    }

    private void CalculateBatchVariance(Map[][] Maps) {
        double valueSum=0;
        BatchVariance = new double[Maps[0].length];


        for(int mapDepth =0 ; mapDepth<Maps[0].length;mapDepth++){
            for(int batchElement=0;batchElement<Maps.length;batchElement++){
                for(int i=0;i<Maps[batchElement][mapDepth].getWidth();i++){
                    for(int j=0;j<Maps[batchElement][mapDepth].getHeight();j++){
                        valueSum += Math.pow(Maps[batchElement][mapDepth].getValues()[i][j] - BatchMean[mapDepth],2);
                    }
                }
            }

            BatchVariance[mapDepth] = valueSum/(Maps.length*Maps[0][0].getWidth()*Maps[0][0].getHeight());
            valueSum=0;
        }

    }

    private void CalculateBatchMean(Map[][] Maps){
        double tempValue=0;
        BatchMean = new double[Maps[0].length];

        for(int mapDepth =0 ; mapDepth<Maps[0].length;mapDepth++){
            for(int batchElement=0;batchElement<Maps.length;batchElement++){
                for(int i=0;i<Maps[batchElement][mapDepth].getWidth();i++){
                    for(int j=0;j<Maps[batchElement][mapDepth].getHeight();j++){
                        tempValue += Maps[batchElement][mapDepth].getValues()[i][j];
                    }
                }
            }

            BatchMean[mapDepth] = tempValue/(Maps.length*Maps[0][0].getWidth()*Maps[0][0].getHeight());
            tempValue=0;
        }
    }


    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();
        kernels = this.nextLayer.getKernels();

        // number of batches  number of error planes, width of error planes, height of error planes
        double[][][][] batchNormedXErrors = new double[Layer.getBatchSize()][this.previousLayer.getOutputMaps()[0].length][this.previousLayer.getOutputMaps()[0][0].getWidth()][this.previousLayer.getOutputMaps()[0][0].getHeight()];

        // if its a layer with weights/kernels needs to perform  calculation

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            for(int depth = 0; depth<this.previousLayer.getOutputMaps()[0].length; depth++){
                for(int width = 0; width<this.previousLayer.getOutputMaps()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.previousLayer.getOutputMaps()[0][0].getHeight(); height++){
                        batchNormedXErrors[batchElement][depth][width][height] = nextLayer.getErrors()[batchElement][depth][width][height];
                    }
                }
            }
        }

        double[] varianceError = new double[BatchVariance.length];

        for(int depth = 0; depth<this.getOutputMaps()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

                for(int width = 0; width<this.getOutputMaps()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputMaps()[0][0].getHeight(); height++){
                        varianceError[depth] += batchNormedXErrors[batchElement][depth][width][height]*(getPreviousLayer().getOutputMaps()[batchElement][depth].getValues()[width][height] - this.BatchMean[depth]);
                    }
                }
            }

            varianceError[depth] *= (-0.5*Math.pow(BatchVariance[depth] + Math.E,-1.5));

        }

        double[] meanError = new double[BatchMean.length];

        double value =0;

        for(int depth = 0; depth<this.getOutputMaps()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputMaps()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputMaps()[0][0].getHeight(); height++){
                        meanError[depth] += batchNormedXErrors[batchElement][depth][width][height];
                        value += -2*(this.getOutputMaps()[batchElement][depth].getValues()[width][height]-BatchMean[depth]);
                    }
                }
            }

            meanError[depth] *= (-Math.pow(BatchVariance[depth] + Math.E,0.5));
            meanError[depth] += varianceError[depth]*(value/ Layer.getBatchSize()*this.previousLayer.getOutputMaps()[0][0].getWidth()*this.previousLayer.getOutputMaps()[0][0].getHeight());
        }


        errors = new double[Layer.getBatchSize()][this.getOutputMaps()[0].length][this.getOutputMaps()[0][0].getWidth()][this.getOutputMaps()[0][0].getHeight()];

        for(int depth = 0; depth<this.getOutputMaps()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputMaps()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputMaps()[0][0].getHeight(); height++){

                        errors[batchElement][depth][width][height] = batchNormedXErrors[batchElement][depth][width][height] * 1/Math.sqrt(BatchVariance[depth]+Math.E)
                                + varianceError[depth]*(2*(this.getOutputMaps()[batchElement][depth].getValues()[width][height])/(value/ Layer.getBatchSize()*this.getOutputMaps()[0][0].getWidth()*this.previousLayer.getOutputMaps()[0][0].getHeight())
                                +BatchMean[depth]*1/(value/ Layer.getBatchSize()*this.getOutputMaps()[0][0].getWidth()*this.getOutputMaps()[0][0].getHeight()));

                    }
                }
            }
        }


        for(int depth = 0; depth<this.getOutputMaps()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputMaps()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputMaps()[0][0].getHeight(); height++){
                        gammaErrors[depth] += nextLayer.getErrors()[batchElement][depth][width][height] * batchTransformedMaps[batchElement][depth].getValues()[width][height];
                        betaErrors[depth]+= nextLayer.getErrors()[batchElement][depth][width][height] ;
                    }
                }
            }
        }


    }

    public void UpdateWeights(){
//Update values
    }






}
