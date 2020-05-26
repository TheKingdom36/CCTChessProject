package chess.ai;

import chess.ai.Common.Networked.NetworkedNeuralNetwork;
import chess.ai.Common.Redis.RedisObjectRetriever;
import chess.ai.Common.montoCarlo.MontoCarloTree;
import chess.ai.Common.montoCarlo.interfaces.IMontoCarloTree;
import chess.ai.Common.neuralNet.Models.BasicNeuralNetwork;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {


    @Bean
    public IMontoCarloTree montoCarloTree(){
        return new MontoCarloTree(new NetworkedNeuralNetwork());
    }
}
