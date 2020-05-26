package chess.ai;

import chess.ai.Common.Networked.NetworkedMontoCarloTree;
import chess.ai.Common.Networked.NetworkedNeuralNetwork;
import chess.ai.Common.montoCarlo.MontoCarloTree;
import chess.ai.Common.montoCarlo.interfaces.IMontoCarloTree;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MoveGeneratorConfiguration {

    @Bean("local")
    public IMontoCarloTree montoCarloTree(){
        return new MontoCarloTree(new NetworkedNeuralNetwork());
    }

    @Bean("network")
    public NetworkedMontoCarloTree networkedMontoCarloTree(){
        return new NetworkedMontoCarloTree();
    }
}
