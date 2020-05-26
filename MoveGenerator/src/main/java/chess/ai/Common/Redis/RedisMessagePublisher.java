package chess.ai.Common.Redis;

import chess.ai.Common.neuralNet.Models.NetworkWeights;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, NetworkWeights> redisTemplate;
    @Autowired
    private ChannelTopic topic;

    public RedisMessagePublisher() {
    }

    public RedisMessagePublisher(RedisTemplate<String, NetworkWeights> redisTemplate, ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

    public void publish(NetworkWeights message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}