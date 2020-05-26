package chess.ai;

import chess.ai.Common.Networked.NetworkedMontoCarloTree;
import chess.ai.Common.Redis.RedisObjectRetriever;
import chess.ai.Common.neuralNet.Models.BasicNeuralNetwork;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {


    @Bean
    @Qualifier("network")
    public NetworkedMontoCarloTree networkedMontoCarloTree(){
        return new NetworkedMontoCarloTree();
    }
}
