package chess.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TrainingDataGeneratorApplication {

	public static void main(String[] args) {

		Configuration.Congifure();

		ConfigurableApplicationContext run = SpringApplication.run(TrainingDataGeneratorApplication.class, args);

		DataGenerator dataGenerator = new DataGenerator();
		dataGenerator.GenerateData();
	}

}
