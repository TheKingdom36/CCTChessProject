package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.Plane;
import lombok.Getter;
import lombok.Setter;

import java.util.Random;

/**
 * Batch normalization layer is used to normalize the values being passed to activation functions. Reduces internal covariant shift
 */
public class BatchNormLayer extends Layer {
    double[] BatchMean;
    double[] BatchVariance;
    @Getter
    @Setter
    double[] gamma;
    @Getter
    @Setter
    double[] beta;
    Plane[][] batchTransformedplanes;
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
        BatchMean = new double[getPreviousLayer().getOutputPlanes()[0].length];
        BatchVariance = new double[getPreviousLayer().getOutputPlanes()[0].length];
        batchTransformedplanes = new Plane[Layer.getBatchSize()][getPreviousLayer().getOutputPlanes()[0].length];

        for (int i=0;i<batchTransformedplanes.length;i++){
            for(int j=0;j<batchTransformedplanes[i].length;j++){
                batchTransformedplanes[i][j] = new Plane(previousLayer.getOutputPlanes()[0][0].getWidth(),previousLayer.getOutputPlanes()[0][0].getHeight());
            }
        }

        Random rand = new Random();

        if(gamma==null || beta==null){
            gamma = new double[getPreviousLayer().getOutputPlanes()[0].length];
            beta = new double[getPreviousLayer().getOutputPlanes()[0].length];

            for(int i=0; i<gamma.length;i++){
                gamma[i] = (rand.nextDouble() - 0.5) / 10;
                beta[i] = (rand.nextDouble() - 0.5) / 10;
            }
        }
        gammaErrors =  new double[getPreviousLayer().getOutputPlanes()[0].length];
        betaErrors =  new double[getPreviousLayer().getOutputPlanes()[0].length];



        CalculateBatchMean(getPreviousLayer().getOutputPlanes());
        CalculateBatchVariance(getPreviousLayer().getOutputPlanes());

        CalculateBatchNorm(getPreviousLayer().getOutputPlanes());

        ScaleAndShift(this.outputplanes);



    }

    private void ScaleAndShift(Plane[][] outputplanes) {
        for(int depth = 0; depth<this.previousLayer.getOutputPlanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.previousLayer.getOutputPlanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.previousLayer.getOutputPlanes()[0][0].getHeight(); height++){
                        this.outputplanes[batchElement][depth].setValue(width,height,gamma[depth]*this.outputplanes[batchElement][depth].getValues()[width][height] + beta[depth]);
                    }
                }
            }
        }
    }

    private void CalculateBatchNorm(Plane[][] Planes) {

        this.outputplanes = new Plane[getPreviousLayer().getOutputPlanes().length][getPreviousLayer().getOutputPlanes()[0].length];


        for(int i = 0; i< Layer.getBatchSize(); i++){
            for(int j = 0; j<getPreviousLayer().getOutputPlanes()[0].length; j++){
                this.outputplanes[i][j] = new Plane(getPreviousLayer().getOutputPlanes()[0][0].getWidth(),getPreviousLayer().getOutputPlanes()[0][0].getHeight());
            }
        }

        for(int planeDepth = 0; planeDepth< Planes[0].length; planeDepth++){
            for(int batchElement = 0; batchElement< Planes.length; batchElement++){
                for(int i = 0; i< Planes[batchElement][planeDepth].getWidth(); i++){
                    for(int j = 0; j< Planes[batchElement][planeDepth].getHeight(); j++){

                        double value = ((Planes[batchElement][planeDepth].getValues()[i][j]-BatchMean[planeDepth])/ Math.pow(BatchVariance[planeDepth] + Math.E,0.5));
                        outputplanes[batchElement][planeDepth].getValues()[i][j] = value;
                        batchTransformedplanes[batchElement][planeDepth].getValues()[i][j] =  value;
                    }
                }
            }
        }
    }

    private void CalculateBatchVariance(Plane[][] Planes) {
        double valueSum=0;
        BatchVariance = new double[Planes[0].length];


        for(int planeDepth = 0; planeDepth< Planes[0].length; planeDepth++){
            for(int batchElement = 0; batchElement< Planes.length; batchElement++){
                for(int i = 0; i< Planes[batchElement][planeDepth].getWidth(); i++){
                    for(int j = 0; j< Planes[batchElement][planeDepth].getHeight(); j++){
                        valueSum += Math.pow(Planes[batchElement][planeDepth].getValues()[i][j] - BatchMean[planeDepth],2);
                    }
                }
            }

            BatchVariance[planeDepth] = valueSum/(Planes.length* Planes[0][0].getWidth()* Planes[0][0].getHeight());
            valueSum=0;
        }

    }

    private void CalculateBatchMean(Plane[][] Planes){
        double tempValue=0;
        BatchMean = new double[Planes[0].length];

        for(int planeDepth = 0; planeDepth< Planes[0].length; planeDepth++){
            for(int batchElement = 0; batchElement< Planes.length; batchElement++){
                for(int i = 0; i< Planes[batchElement][planeDepth].getWidth(); i++){
                    for(int j = 0; j< Planes[batchElement][planeDepth].getHeight(); j++){
                        tempValue += Planes[batchElement][planeDepth].getValues()[i][j];
                    }
                }
            }

            BatchMean[planeDepth] = tempValue/(Planes.length* Planes[0][0].getWidth()* Planes[0][0].getHeight());
            tempValue=0;
        }
    }


    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();
        kernels = this.nextLayer.getKernels();

        // number of batches  number of error planes, width of error planes, height of error planes
        double[][][][] batchNormedXErrors = new double[Layer.getBatchSize()][this.previousLayer.getOutputPlanes()[0].length][this.previousLayer.getOutputPlanes()[0][0].getWidth()][this.previousLayer.getOutputPlanes()[0][0].getHeight()];

        // if its a layer with weights/kernels needs to perform  calculation

        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
            for(int depth = 0; depth<this.previousLayer.getOutputPlanes()[0].length; depth++){
                for(int width = 0; width<this.previousLayer.getOutputPlanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.previousLayer.getOutputPlanes()[0][0].getHeight(); height++){
                        batchNormedXErrors[batchElement][depth][width][height] = nextLayer.getErrors()[batchElement][depth][width][height];
                    }
                }
            }
        }

        double[] varianceError = new double[BatchVariance.length];

        for(int depth = 0; depth<this.getOutputPlanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {

                for(int width = 0; width<this.getOutputPlanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputPlanes()[0][0].getHeight(); height++){
                        varianceError[depth] += batchNormedXErrors[batchElement][depth][width][height]*(getPreviousLayer().getOutputPlanes()[batchElement][depth].getValues()[width][height] - this.BatchMean[depth]);
                    }
                }
            }

            varianceError[depth] *= (-0.5* Math.pow(BatchVariance[depth] + Math.E,-1.5));

        }

        double[] meanError = new double[BatchMean.length];

        double value =0;

        for(int depth = 0; depth<this.getOutputPlanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputPlanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputPlanes()[0][0].getHeight(); height++){
                        meanError[depth] += batchNormedXErrors[batchElement][depth][width][height];
                        value += -2*(this.getOutputPlanes()[batchElement][depth].getValues()[width][height]-BatchMean[depth]);
                    }
                }
            }

            meanError[depth] *= (-Math.pow(BatchVariance[depth] + Math.E,0.5));
            meanError[depth] += varianceError[depth]*(value/ Layer.getBatchSize()*this.previousLayer.getOutputPlanes()[0][0].getWidth()*this.previousLayer.getOutputPlanes()[0][0].getHeight());
        }


        errors = new double[Layer.getBatchSize()][this.getOutputPlanes()[0].length][this.getOutputPlanes()[0][0].getWidth()][this.getOutputPlanes()[0][0].getHeight()];

        for(int depth = 0; depth<this.getOutputPlanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputPlanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputPlanes()[0][0].getHeight(); height++){

                        errors[batchElement][depth][width][height] = batchNormedXErrors[batchElement][depth][width][height] * 1/ Math.sqrt(BatchVariance[depth]+ Math.E)
                                + varianceError[depth]*(2*(this.getOutputPlanes()[batchElement][depth].getValues()[width][height])/(value/ Layer.getBatchSize()*this.getOutputPlanes()[0][0].getWidth()*this.previousLayer.getOutputPlanes()[0][0].getHeight())
                                +BatchMean[depth]*1/(value/ Layer.getBatchSize()*this.getOutputPlanes()[0][0].getWidth()*this.getOutputPlanes()[0][0].getHeight()));

                    }
                }
            }
        }


        for(int depth = 0; depth<this.getOutputPlanes()[0].length; depth++){
            for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {
                for(int width = 0; width<this.getOutputPlanes()[0][0].getWidth(); width++){
                    for(int height = 0; height < this.getOutputPlanes()[0][0].getHeight(); height++){
                        gammaErrors[depth] += nextLayer.getErrors()[batchElement][depth][width][height] * batchTransformedplanes[batchElement][depth].getValues()[width][height];
                        betaErrors[depth]+= nextLayer.getErrors()[batchElement][depth][width][height] ;
                    }
                }
            }
        }


    }

    public void UpdateWeights(){
        for(int i=0;i<gamma.length;i++){
            gamma[i] -= gammaErrors[i];
            beta[i] -= betaErrors[i];
        }
    }






}
