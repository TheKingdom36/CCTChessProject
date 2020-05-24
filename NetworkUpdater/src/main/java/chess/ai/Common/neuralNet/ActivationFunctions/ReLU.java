package chess.ai.Common.neuralNet.ActivationFunctions;

public class ReLU implements ActivationFunction {


    @Override
    public double getOutput(double Input) {
        if(Input>0){
            return Input;
        }else{
            return 0;
        }
    }
}
