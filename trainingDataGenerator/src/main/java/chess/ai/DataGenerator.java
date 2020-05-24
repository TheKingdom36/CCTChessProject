package chess.ai;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public String EvaluateMove() {
        String url = Configuration.prop.getProperty("moveEvaluatorURL") + "/evaluateMove";
        System.out.println(url);
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("theBoard", "s");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        //create restTemplate
        RestTemplate restTemplate = new RestTemplate();

        // send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println(response.getBody() + "now");
            return response.getBody();
        } else {
            System.out.println("here");
            return null;
        }
    }
}
