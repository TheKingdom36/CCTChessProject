package chess.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MoveEvaluatorApplication {

	public static void main(String[] args) {
		Configuration.Congifure();
		SpringApplication.run(MoveEvaluatorApplication.class, args);
	}

}
