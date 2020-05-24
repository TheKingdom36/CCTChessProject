package chess.ai.Common.neuralNet.Models;

import lombok.Getter;
import lombok.Setter;

public class LayerOutputPlanes {


    @Getter
    @Setter
    Map[][] InputMaps;


    @Getter
    @Setter
    Map[][] Conv1Maps;

    @Getter
    @Setter
    Map[][] BatchNorm1Maps;

    @Getter
    @Setter
    Map[][] ReLU1Maps;


    @Getter
    @Setter
    Map[][] Conv2Maps;

    @Getter
    @Setter
    Map[][] BatchNorm2Maps;

    @Getter
    @Setter
    Map[][] ReLU2Maps;



    @Getter
    @Setter
    Map[][] Conv3Maps;

    @Getter
    @Setter
    Map[][] BatchNorm3Maps;

    @Getter
    @Setter
    Map[][] ReLU3Maps;



    @Getter
    @Setter
    Map[][] FCMaps;

    @Getter
    @Setter
    Map[][] BatchNorm4Maps;

    @Getter
    @Setter
    Map[][] ReLU4Maps;


    @Getter
    @Setter
    Map[][] outputMaps;

    @Getter
    @Setter
    Map[][] policyHeadMaps;

    @Getter
    @Setter
    Map[][] valueHeadMaps;

}
