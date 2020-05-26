package chess.ai;

import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Models.BasicNeuralNetwork;
import chess.ai.Common.neuralNet.Models.NetworkWeights;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

@Component
public class NetworkChangesListener implements MessageListener {


    BasicNeuralNetwork neuralNetwork;

    @Autowired
    public NetworkChangesListener(BasicNeuralNetwork neuralNetwork){
        System.out.println("Here");
        this.neuralNetwork = neuralNetwork;
    }

    @SneakyThrows
    @Override
    public void onMessage(Message message, byte[] pattern) {

        neuralNetwork.AssignNewWeights((NetworkWeights) Deserialize(message.getBody()));
    }

    private Object Deserialize(byte[] message) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bis = new ByteArrayInputStream(message);
        ObjectInputStream in = new ObjectInputStream(bis);

        // Method for deserialization of object
        Object object = in.readObject();

        return object;
    }


}
