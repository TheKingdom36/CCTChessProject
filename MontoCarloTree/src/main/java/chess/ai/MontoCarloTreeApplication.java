package chess.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class MontoCarloTreeApplication {


	public static void main(String[] args) {
		Configuration.Congifure();
		SpringApplication.run(MontoCarloTreeApplication.class, args);
	}
}
