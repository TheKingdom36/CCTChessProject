package chess.ai.Common.Redis;



import chess.ai.Common.neuralNet.Models.NetworkWeights;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;


@Configuration
class RedisConfiguration {

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(chess.ai.Configuration.prop.get("RedisDatabaseIP").toString(),
                Integer.parseInt(chess.ai.Configuration.prop.get("RedisDatabasePort").toString()));
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, NetworkWeights> redisTemplate() {
        RedisTemplate<String, NetworkWeights> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

/*
    @Bean
    RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }
  @Autowired
  NetworkChangesListener networkChangesListener;

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(networkChangesListener);
    }*/

    @Bean
    RedisMessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }



    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("NetworkWeights");
    }
}