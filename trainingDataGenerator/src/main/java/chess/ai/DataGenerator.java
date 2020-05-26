package chess.ai;

import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;
import chess.ai.Common.ChessBoard.Models.MoveLog;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.Networked.PostRequestService;
import chess.ai.Common.montoCarlo.MontoCarloTrainingOutput;
import chess.ai.Common.montoCarlo.MontoCarloTree;
import chess.ai.Common.montoCarlo.TrainingSample;
import chess.ai.Common.montoCarlo.interfaces.IMontoCarloTree;
import chess.ai.Common.neuralNet.Models.BoardState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class DataGenerator {

   Board board;


   Color player = Color.White;
   boolean Checkmate= false;
   List<Move> moves = new ArrayList<>();

   int turnLimit = 20;
   int turnCount = 0;
   long startTime;

   @Autowired
   IMontoCarloTree tree;

   public void GenerateData()  {




      MontoCarloTrainingOutput output;
      List<TrainingSample> allSamples = new ArrayList<>();
      List<TrainingSample> tempSamples = new ArrayList<>();

      int moveCount=0;
      Random rand = new Random();
      int numberOfMovesUntilSampleCollected=5;

     do{
         for (int j = 0; j < 40; j++) {

            board = new Board(new MoveLog());

            Checkmate = false;
            turnCount = 0;
            player = Color.White;
            moveCount = 0;
            Move move;

            do {
               turnCount++;


               moves = board.GetAllAvailableMoves(player);

               if (moves.size() != 0) {
                  moveCount++;
                  if (moveCount == numberOfMovesUntilSampleCollected) {
                     output = tree.findNextMoveTraining(new BoardState(board, player),2000);
                     tempSamples.add(output.getSample());
                     move = output.getNextMove();
                     moveCount = 0;
                     numberOfMovesUntilSampleCollected = rand.nextInt(10);
                  } else {
                     move = tree.findNextMove(new BoardState(board, player),1000);
                  }

                  board.UpdateBoard(move);

               } else {

                  Checkmate = true;
               }

               if (player == Color.White) {
                  player = Color.Black;
               } else {
                  player = Color.White;
               }


               if (turnCount > turnLimit) {
                  break;
               }
            } while (Checkmate == false);


            for (TrainingSample sample : tempSamples) {
               if (turnCount >= turnLimit) {
                  sample.setValue(0);
               }

               if (player == Color.White) {
                  if (sample.getPlayerColor() == Color.White) {
                     sample.setValue(1);
                  } else {
                     sample.setValue(-1);
                  }
               } else {
                  if (sample.getPlayerColor() == Color.Black) {
                     sample.setValue(1);
                  } else {
                     sample.setValue(-1);
                  }
               }
            }

            allSamples.addAll(tempSamples);
            tempSamples.clear();
            if (turnCount >= turnLimit - 20) {
               System.out.println("Draw" + turnCount);
            } else {
               System.out.println("Winner is " + player + turnCount);
            }

            System.out.println("Time taken " + (System.currentTimeMillis() - startTime));

         }

         System.out.println("All samples size" + allSamples.size());
        PostRequestService.PostTrainingSamples("http://"+ Configuration.prop.get("ProcessTrainingSamplesEndpoint"),allSamples);
         tempSamples.clear();
         allSamples.clear();
      }while (true);



   }
}
