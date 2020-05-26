package chess.ai.Common.neuralNet.Layers;

import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.plane;

public class ConvLayer extends Layer {
    private int numOfKernels;
    private int padding;
    private Kernel[] kernels;
    private int kernelWidth;
    private int kernelHeight;
    private int kernelDepth;
    private int stride;

    public Kernel[] getKernels() {
        return kernels;
    }

    public void setKernels(Kernel[] kernels) {
        this.kernels = kernels;
    }

    public ConvLayer(int numberOfKernals, int stride, int kernelWidth, int kernelHeight, int kernelDepth, int padding){


        this.numOfKernels = numberOfKernals;

        this.kernelWidth = kernelWidth;

        this.kernelHeight = kernelHeight;

        this.kernelDepth = kernelDepth;

        this.padding = padding;

        this.stride = stride;

    }



    public void CalculateOutputplanes() {

        previousLayer.CalculateOutputplanes();

        plane[][] inputplanes = previousLayer.outputplanes;

        if(kernels==null){
            System.out.println("In kernels");
            kernels = new Kernel[numOfKernels];
            for (int i=0;i<numOfKernels;i++){
                kernels[i]=new Kernel(kernelWidth, kernelHeight, kernelDepth);
                kernels[i].InitializeRandomValues();
            }
        }


        outputplanes = new plane[Layer.getBatchSize()][numOfKernels];

        for(int i = 0; i< Layer.getBatchSize(); i++){
            for(int j=0;j<kernels.length;j++){
                this.outputplanes[i][j] = new plane((inputplanes[0][0].getWidth()- kernels[0].getWidth()/stride)+1,(inputplanes[0][0].getHeight()- kernels[0].getHeight()/stride)+1);
            }
        }

        double newValue;
        int outx=0;
        int outy=0;
        for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++){
            for(int kernelNum=0;kernelNum<kernels.length;kernelNum++){
                outy=0;
                for(int i=1;i<inputplanes[batchElement][0].getWidth()-1;i=i+stride){
                    outx=0;
                    for(int j=1;j<inputplanes[batchElement][0].getHeight()-1;j=j+stride){
                        newValue=0;

                        for(int k=0;k<inputplanes[batchElement].length;k++){
                            for(int kerneli=-kernels[kernelNum].getWidth()/2;kerneli<=kernels[kernelNum].getWidth()/2;kerneli++){
                                for(int kernelj=-kernels[kernelNum].getHeight()/2;kernelj<=kernels[kernelNum].getHeight()/2;kernelj++){

                                    newValue += inputplanes[batchElement][k].getValues()[i + kerneli][j + kernelj] * kernels[kernelNum].getValues()[k][(kernels[kernelNum].getWidth() / 2) + kerneli][(kernels[kernelNum].getHeight() / 2) + kernelj];
                                }
                            }
                        }

                        outputplanes[batchElement][kernelNum].setValue(outx,outy,newValue);
                        outx++;
                    }
                    outy++;

                }

            }

}

        for(int i = 0; i< Layer.getBatchSize(); i++){
            for(int j=0;j<kernels.length;j++){
                this.outputplanes[i][j].addPadding(padding);
            }
        }


    }


    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();

        // number of batches  number of error planes, width of error planes, height of error planes
        errors = nextLayer.getErrors();


    }


    public void UpdateWeights(){
        double cal=0;
        for(int kernelNum=0;kernelNum<kernels.length;kernelNum++){
            for(int kernelDepth=0;kernelDepth<this.kernels[0].getDepth();kernelDepth++){
                for(int batchElement = 0; batchElement< Layer.getBatchSize(); batchElement++) {


                    for(int width=-kernels[0].getWidth()/2;width<=this.kernels[0].getWidth()/2;width++){
                        for(int height=-kernels[0].getHeight()/2; height <= this.kernels[0].getHeight()/2;height++){
                            cal=0;

                            //width of kernel                                                       //2=the kernel width-1            //Stride
                            for(int errorWidth = 0; errorWidth<(errors[batchElement][kernelNum].length)-2; errorWidth++){
                                                                                                    //2=the kernel height-1
                                for(int errorHeight = 0; errorHeight<(errors[batchElement][kernelNum][0].length)-2; errorHeight++) {
                                 cal += errors[batchElement][kernelNum][errorWidth][errorHeight] * previousLayer.getOutputplanes()[batchElement][kernelDepth].getValues()[errorWidth + (1 + width)][errorHeight + (1+height)];
                                }
                            }

                            this.kernels[kernelNum].setValue(kernelDepth,1 + width,1 + height,kernels[kernelNum].getValues()[kernelDepth][1 + width][1 + height] - cal);

                        }
                    }
                }
            }
        }

    }




}



