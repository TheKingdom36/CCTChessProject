package chess.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BoardEvaluatorApplication {

	public static void main(String[] args) {
		Configuration.Congifure();
		SpringApplication.run(BoardEvaluatorApplication.class, args);
	}

}
