package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.plane;
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
    plane[][] batchTransformedplanes;
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
    public void CalculateOutputplanes() {
        getPreviousLayer().CalculateOutputplanes();

        //Each plane depth will have a different mean and varience
        BatchMean = new double[getPreviousLayer().getOutputplanes()[0].length];
        BatchVariance = new double[getPreviousLayer().getOutputplanes()[0].length];
        batchTransformedplanes = new plane[Layer.getBatchSize()][getPreviousLayer().getOutputplanes()[0].length];

        for (int i=0;i<batchTransformedplanes.length;i++){
            for(int j=0;j<batchTransformedplanes[i].length;j++){
                batchTransformedplanes[i][j] = new plane(previousLayer.getOutputplanes()[0][0].getWidth(),previousLayer.getOutputplanes()[0][0].getHeight());
            }
        }

        Random rand = new Random();

        if(gamma==null || beta==null){
            gamma = new double[getPreviousLayer().getOutputplanes()[0].length];
            beta = new double[getPreviousLayer().getOutputplanes()[0].length];

            for(int i=0; i<gamma.length;i++){
                gamma[i] = (rand.nextDouble() - 0.5) / 10;
                beta[i] = (rand.nextDouble() - 0.5) / 10;
            }
        }
        gammaErrors =  new double[getPreviousLayer().getOutputplanes()[0].length];
        betaErrors =  new double[getPreviousLayer().getOutputplanes()[0].length];



        CalculateBatchMean(getPreviousLayer().getOutputplanes());
        CalculateBatchVariance(getPreviousLayer().getOutputplanes());

        CalculateBatchNorm(getPreviousLayer().getOutputplanes());

        ScaleAndShift(this.outputplanes);



    }

    private void ScaleAndShift(plane[][] outputplanes) {
        for(int depth = 0; depth<this.previousLayer.getOutputplanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.previousLayer.getOutputplanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.previousLayer.getOutputplanes()[0][0].getHeight(); height++){
                        this.outputplanes[batchElement][depth].setValue(width,height,gamma[depth]*this.outputplanes[batchElement][depth].getValues()[width][height] + beta[depth]);
                    }
                }
            }
        }
    }

    private void CalculateBatchNorm(plane[][] planes) {

        this.outputplanes = new plane[getPreviousLayer().getOutputplanes().length][getPreviousLayer().getOutputplanes()[0].length];


        for(int i = 0; i< Layer.getBatchSize(); i++){
            for(int j = 0; j<getPreviousLayer().getOutputplanes()[0].length; j++){
                this.outputplanes[i][j] = new plane(getPreviousLayer().getOutputplanes()[0][0].getWidth(),getPreviousLayer().getOutputplanes()[0][0].getHeight());
            }
        }


        for(int planeDepth =0 ; planeDepth<planes[0].length;planeDepth++){
            for(int batchElement=0;batchElement<planes.length;batchElement++){
                for(int i=0;i<planes[batchElement][planeDepth].getWidth();i++){
                    for(int j=0;j<planes[batchElement][planeDepth].getHeight();j++){

                        double value = ((planes[batchElement][planeDepth].getValues()[i][j]-BatchMean[planeDepth])/ Math.pow(BatchVariance[planeDepth] + Math.E,0.5));
                        outputplanes[batchElement][planeDepth].getValues()[i][j] = value;
                        batchTransformedplanes[batchElement][planeDepth].getValues()[i][j] =  value;
                    }
                }
            }
        }
    }

    private void CalculateBatchVariance(plane[][] planes) {
        double valueSum=0;
        BatchVariance = new double[planes[0].length];


        for(int planeDepth =0 ; planeDepth<planes[0].length;planeDepth++){
            for(int batchElement=0;batchElement<planes.length;batchElement++){
                for(int i=0;i<planes[batchElement][planeDepth].getWidth();i++){
                    for(int j=0;j<planes[batchElement][planeDepth].getHeight();j++){
                        valueSum += Math.pow(planes[batchElement][planeDepth].getValues()[i][j] - BatchMean[planeDepth],2);
                    }
                }
            }

            BatchVariance[planeDepth] = valueSum/(planes.length*planes[0][0].getWidth()*planes[0][0].getHeight());
            valueSum=0;
        }

    }

    private void CalculateBatchMean(plane[][] planes){
        double tempValue=0;
        BatchMean = new double[planes[0].length];

        for(int planeDepth =0 ; planeDepth<planes[0].length;planeDepth++){
            for(int batchElement=0;batchElement<planes.length;batchElement++){
                for(int i=0;i<planes[batchElement][planeDepth].getWidth();i++){
                    for(int j=0;j<planes[batchElement][planeDepth].getHeight();j++){
                        tempValue += planes[batchElement][planeDepth].getValues()[i][j];
                    }
                }
            }

            BatchMean[planeDepth] = tempValue/(planes.length*planes[0][0].getWidth()*planes[0][0].getHeight());
            tempValue=0;
        }
    }


    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();
        kernels = this.nextLayer.getKernels();

        // number of batches  number of error planes, width of error planes, height of error planes
        double[][][][] batchNormedXErrors = new double[Layer.getBatchSize()][this.previousLayer.getOutputplanes()[0].length][this.previousLayer.getOutputplanes()[0][0].getWidth()][this.previousLayer.getOutputplanes()[0][0].getHeight()];

        // if its a layer with weights/kernels needs to perform  calculation

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            for(int depth = 0; depth<this.previousLayer.getOutputplanes()[0].length; depth++){
                for(int width = 0; width<this.previousLayer.getOutputplanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.previousLayer.getOutputplanes()[0][0].getHeight(); height++){
                        batchNormedXErrors[batchElement][depth][width][height] = nextLayer.getErrors()[batchElement][depth][width][height];
                    }
                }
            }
        }

        double[] varianceError = new double[BatchVariance.length];

        for(int depth = 0; depth<this.getOutputplanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

                for(int width = 0; width<this.getOutputplanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputplanes()[0][0].getHeight(); height++){
                        varianceError[depth] += batchNormedXErrors[batchElement][depth][width][height]*(getPreviousLayer().getOutputplanes()[batchElement][depth].getValues()[width][height] - this.BatchMean[depth]);
                    }
                }
            }

            varianceError[depth] *= (-0.5*Math.pow(BatchVariance[depth] + Math.E,-1.5));

        }

        double[] meanError = new double[BatchMean.length];

        double value =0;

        for(int depth = 0; depth<this.getOutputplanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputplanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputplanes()[0][0].getHeight(); height++){
                        meanError[depth] += batchNormedXErrors[batchElement][depth][width][height];
                        value += -2*(this.getOutputplanes()[batchElement][depth].getValues()[width][height]-BatchMean[depth]);
                    }
                }
            }

            meanError[depth] *= (-Math.pow(BatchVariance[depth] + Math.E,0.5));
            meanError[depth] += varianceError[depth]*(value/ Layer.getBatchSize()*this.previousLayer.getOutputplanes()[0][0].getWidth()*this.previousLayer.getOutputplanes()[0][0].getHeight());
        }


        errors = new double[Layer.getBatchSize()][this.getOutputplanes()[0].length][this.getOutputplanes()[0][0].getWidth()][this.getOutputplanes()[0][0].getHeight()];

        for(int depth = 0; depth<this.getOutputplanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputplanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputplanes()[0][0].getHeight(); height++){

                        errors[batchElement][depth][width][height] = batchNormedXErrors[batchElement][depth][width][height] * 1/Math.sqrt(BatchVariance[depth]+Math.E)
                                + varianceError[depth]*(2*(this.getOutputplanes()[batchElement][depth].getValues()[width][height])/(value/ Layer.getBatchSize()*this.getOutputplanes()[0][0].getWidth()*this.previousLayer.getOutputplanes()[0][0].getHeight())
                                +BatchMean[depth]*1/(value/ Layer.getBatchSize()*this.getOutputplanes()[0][0].getWidth()*this.getOutputplanes()[0][0].getHeight()));

                    }
                }
            }
        }


        for(int depth = 0; depth<this.getOutputplanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputplanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputplanes()[0][0].getHeight(); height++){
                        gammaErrors[depth] += nextLayer.getErrors()[batchElement][depth][width][height] * batchTransformedplanes[batchElement][depth].getValues()[width][height];
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
