package chess.ai.Common.Redis;

import chess.ai.Common.neuralNet.Models.NetworkWeights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisObjectRetriever {
    @Autowired
    private RedisTemplate<String, NetworkWeights> redisTemplate;

    public Object retriveObject(String key){

        return redisTemplate.opsForValue().get(key);
    }
}
