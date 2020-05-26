package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.plane;

public class InputLayer extends Layer {



    public InputLayer(plane[][] planes){
        this.outputplanes = planes;

    }


    @Override
    public Kernel[] getKernels() {
        return new Kernel[0];
    }

    @Override
    public void CalculateOutputplanes() {
        //Does nothing no operation required on input plane

    }

    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();
    }

}
