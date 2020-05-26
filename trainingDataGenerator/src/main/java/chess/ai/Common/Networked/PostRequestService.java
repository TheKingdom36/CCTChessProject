package chess.ai.Common.Networked;

import chess.ai.Common.montoCarlo.TrainingSample;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class PostRequestService {

    public static NNOutput HttpPost(String url, BoardState boardState) {
        //create restTemplate
        RestTemplate restTemplate = new RestTemplate();

        // send POST request

        ResponseEntity<NNOutput> response = restTemplate.postForEntity(url, boardState, NNOutput.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody());
            return response.getBody();
        } else {
            System.out.println("Request Failed");
            return null;
        }
    }

    public static BatchOfEvaluatedBoards HttpPost(String url, List<BoardState> boardStates) {
        //create restTemplate
        RestTemplate restTemplate = new RestTemplate();

        // send POST request


        ResponseEntity<BatchOfEvaluatedBoards> response = restTemplate.postForEntity(url, boardStates, BatchOfEvaluatedBoards.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody() + "now");
            return response.getBody();
        } else {
            System.out.println("Request Failed");
            return null;
        }
    }

    public static void PostTrainingSamples(String url, List<TrainingSample> trainingSamples) {
        //create restTemplate
        RestTemplate restTemplate = new RestTemplate();

        // send POST request
        ResponseEntity<BatchOfEvaluatedBoards> response = restTemplate.postForEntity(url, trainingSamples, BatchOfEvaluatedBoards.class);

    }

    public static Object HttpPost(String url, Map<String, Object> body) {
        //create restTemplate
        RestTemplate restTemplate = new RestTemplate();

        // send POST request

        ResponseEntity<Object> response = restTemplate.postForEntity(url, body, Object.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody() + "now");
            return response.getBody();
        } else {
            System.out.println("Request Failed");
            return null;
        }
    }
}
