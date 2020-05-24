package chess.ai.Services;

import chess.ai.Common.neuralNet.Models.NetworkWeights;
import chess.ai.Common.Redis.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    RedisMessagePublisher redisMessagePublisher;

    @Autowired
    RedisMessageSubscriber redisMessageSubscriber;

    @Autowired
    RedisObjectStorer redisObjectStorer;

    @Autowired
    RedisObjectRetriever redisObjectRetriever;

   public void PublishMessage(NetworkWeights networkWeights){
       redisMessagePublisher.publish(networkWeights);
   }

   public void StoreInRedis(String key , NetworkWeights networkWeights){
       redisObjectStorer.storeObject(key, networkWeights);
   }
}
