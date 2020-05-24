package chess.ai.Common.neuralNet.Layers;


import chess.ai.Common.neuralNet.Models.Kernel;
import chess.ai.Common.neuralNet.Models.Map;

public class InputLayer extends Layer {



    public InputLayer(Map[][] maps){
        this.outputMaps = maps;

    }


    @Override
    public Kernel[] getKernels() {
        return new Kernel[0];
    }

    @Override
    public void CalculateOutputMaps() {
        //Does nothing no operation required on input map

    }

    public void CalculateErrors(){
        this.nextLayer.CalculateErrors();
    }

}
