package chess.ai.Controllers;

import chess.ai.Common.Redis.RedisObjectRetriever;
import chess.ai.Common.Redis.RedisObjectStorer;
import chess.ai.Common.montoCarlo.TrainingSample;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import chess.ai.Common.Redis.RedisMessagePublisher;
import chess.ai.Services.NetworkUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NetworkUpdaterController {

    @Autowired
    NetworkUpdateService networkUpdateService;



    @PostMapping("ProcessTrainingSamples")
    public void ProcessTrainingData(List<TrainingSample> trainingSamples){
        networkUpdateService.processSamples(trainingSamples);
    }
}
