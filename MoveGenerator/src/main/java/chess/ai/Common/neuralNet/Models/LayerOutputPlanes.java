package chess.ai.Common.neuralNet.Models;

import lombok.Getter;
import lombok.Setter;

public class LayerOutputPlanes {


    @Getter
    @Setter
    plane[][] Inputplanes;


    @Getter
    @Setter
    plane[][] Conv1planes;

    @Getter
    @Setter
    plane[][] BatchNorm1planes;

    @Getter
    @Setter
    plane[][] ReLU1planes;


    @Getter
    @Setter
    plane[][] Conv2planes;

    @Getter
    @Setter
    plane[][] BatchNorm2planes;

    @Getter
    @Setter
    plane[][] ReLU2planes;



    @Getter
    @Setter
    plane[][] Conv3planes;

    @Getter
    @Setter
    plane[][] BatchNorm3planes;

    @Getter
    @Setter
    plane[][] ReLU3planes;



    @Getter
    @Setter
    plane[][] FCplanes;

    @Getter
    @Setter
    plane[][] BatchNorm4planes;

    @Getter
    @Setter
    plane[][] ReLU4planes;


    @Getter
    @Setter
    plane[][] outputplanes;

    @Getter
    @Setter
    plane[][] policyHeadplanes;

    @Getter
    @Setter
    plane[][] valueHeadplanes;

}
