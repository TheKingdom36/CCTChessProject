package chess.ai;

import chess.ai.Common.Redis.RedisObjectStorer;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NetworkUpdaterApplication {



	public static void main(String[] args) {

		Configuration.Congifure();

		SpringApplication.run(NetworkUpdaterApplication.class, args);
	}

}
