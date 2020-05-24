package chess.ai;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;

import chess.ai.Common.Networked.BatchOfEvaluatedBoards;
import chess.ai.Common.Networked.PostRequestService;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@RestController
public class trainingController {
    Properties properties = new Properties();

    @GetMapping("sendBoard")
    public void sendMoveEvaluate(){
        String string = Configuration.prop.getProperty("BoardEvaluateEndpoint");
        BoardState boardState = new BoardState(new Board(), Color.White);
        Board board = boardState.getBoard().Copy();
        board.PrintBoard();
        NNOutput nnOutput= (NNOutput) PostRequestService.HttpPost("http://"+string,boardState);
        nnOutput.toString();
    }

    @GetMapping("sendBatchBoard")
    public void sendBatchMoveEvaluate(){
        String string = Configuration.prop.getProperty("BatchOfBoardEvaluateEndpoint");

        List<BoardState> boardStates = new ArrayList<>();
        for(int i=0;i<10;i++){
            boardStates.add(new BoardState(new Board(), Color.White));
        }

        BatchOfEvaluatedBoards nnOutput= (BatchOfEvaluatedBoards) PostRequestService.HttpPost("http://"+string,boardStates);
        for(NNOutput nnOutputd:nnOutput.getNnOutputs()){
            System.out.println(nnOutputd.toString());
        }
    }
}
