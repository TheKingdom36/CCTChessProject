package chess.ai.Common.montoCarlo;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Moves.Move;
import chess.ai.Common.montoCarlo.interfaces.IMontoCarloTree;
import chess.ai.Common.montoCarlo.tree.Node;
import chess.ai.Common.montoCarlo.tree.Tree;
import chess.ai.Common.neuralNet.Interfaces.INeuralNetwork;
import chess.ai.Common.neuralNet.Models.BasicNeuralNetwork;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import chess.ai.Common.neuralNet.Output.AllPieceMoveOptions;
import chess.ai.Common.neuralNet.Util.ConvertBoardToWhite;
import chess.ai.Common.neuralNet.Util.ConvertMoveToBlack;
import org.springframework.stereotype.Component;


public class MontoCarloTree implements IMontoCarloTree {


    INeuralNetwork nn;

    public MontoCarloTree(INeuralNetwork neuralNetwork) {
        if(State.getMovesOptions() == null){
            State.setMovesOptions(AllPieceMoveOptions.getMoveOptions());
        }

        nn = neuralNetwork;
    }


    public MontoCarloTree() {
        if(State.getMovesOptions() == null){
            State.setMovesOptions(AllPieceMoveOptions.getMoveOptions());
        }

        nn = new BasicNeuralNetwork();
    }


    public Move findNextMove(BoardState boardState, long searchTime) {

        long start = System.currentTimeMillis();
        long end = start + searchTime;

        if(boardState.getPlayerColor() == Color.Black) {
            //input is always in perspective of the white player, moves are flipped for black player when outputing move
            boardState.setBoard(ConvertBoardToWhite.Convert(boardState.getBoard()));
        }

        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        ((Node) rootNode).setState(new State(boardState));
        rootNode.getState().setIsActive(true);
        rootNode.getState().setPlayerColor(boardState.getPlayerColor());


        MCSTProcess(boardState, boardState.getPlayerColor(), end, rootNode);

        Node winnerNode = rootNode.getChildWithMaxScore();

        tree.setRoot(winnerNode);

        if(boardState.getPlayerColor() == Color.White){
            return winnerNode.getState().getMove();

        }else {
            return ConvertMoveToBlack.Convert(winnerNode.getState().getMove());

        }


    }

    public MontoCarloTrainingOutput findNextMoveTraining(BoardState boardState, long searchTime) {

        long start = System.currentTimeMillis();
        long end = start + searchTime;

        if(boardState.getPlayerColor() == Color.Black) {
            //input is always in perspective of the white player, moves are flipped for black player when outputing move
            boardState.setBoard(ConvertBoardToWhite.Convert(boardState.getBoard()));
        }

        Tree tree = new Tree();
        Node rootNode = tree.getRoot();
        rootNode.setState(new State(boardState));
        rootNode.getState().setIsActive(true);
        rootNode.getState().setPlayerColor(boardState.getPlayerColor());


        MCSTProcess(boardState, boardState.getPlayerColor(), end, rootNode);

        Node winnerNode = rootNode.getChildWithMaxScore();

        tree.setRoot(winnerNode);

        MontoCarloTrainingOutput output ;

        if(boardState.getPlayerColor() == Color.White){
            output = new MontoCarloTrainingOutput(new TrainingSample(),winnerNode.getState().getMove());
        }else {
            output = new MontoCarloTrainingOutput(new TrainingSample(), ConvertMoveToBlack.Convert(winnerNode.getState().getMove()));
        }

        double[] genPolicy = new double[State.getMovesOptions().size()];
        double genPolicyTotal=0;

        for(Node child: rootNode.getChildArray()){
            genPolicy[child.getState().getIdMove()] = UCT.uctValue(child.getState().getVisitCount(),child.getState().getWinScore(),child.getParent().getState().getVisitCount(),child.getState().getBestMoveProbability(),child.getState().getIsActive());
            genPolicyTotal += genPolicy[child.getState().getIdMove()];
        }

        for(Node child: rootNode.getChildArray()){
            genPolicy[child.getState().getIdMove()] =genPolicy[child.getState().getIdMove()]/genPolicyTotal;

        }
        output.getSample().setPolicy(genPolicy);
        output.getSample().setBoard(boardState.getBoard());
        output.getSample().setPlayerColor(boardState.getPlayerColor());

       return output;


    }

    private void MCSTProcess(BoardState boardState, Color playerColor, long endTime, Node rootNode) {
        NNOutput nnOutput;


        while (System.currentTimeMillis() < endTime) {


            // Phase 1 - Selection

            Node promisingNode = selectPromisingNode(rootNode);


            // Phase 2 - Expansion

            expandNode(promisingNode);

            // Phase 3 - Neural Network evaluation


            if (promisingNode.getParent() != null) {

                nnOutput = nn.EvaluateBoard(promisingNode.getState().getBoardState());

            } else {



                nnOutput = nn.EvaluateBoard(promisingNode.getState().getBoardState());
            }
         /*   System.out.println();
            for (int i = 0; i < nnOutput.getProbabilities().length; i++) {
                System.out.print(i+": "+nnOutput.getProbabilities()[i]+" ");

            }*/


            AssignProbabilities(promisingNode, nnOutput.getProbabilities());


            // Phase 4 - Update

            backPropogation(promisingNode, playerColor, nnOutput);


        }
    }

    private void AssignProbabilities(Node node, double[] probabilities) {
        for(Node child:node.getChildArray()){
            child.getState().setBestMoveProbability(child.getState().getIdMove());
        }
    }

    private Node selectPromisingNode(Node rootNode) {
        Node node = rootNode;


        while (node.getChildArray().size() != 0) {

            node = UCT.findBestNodeWithUCT(node);
        }




        return node;
    }

    private void expandNode(Node node) {

           State[] possibleStates = node.getState().getAllPossibleStates();


           for(State state: possibleStates){

               if(state.getIsActive()) {
                   Node newNode = new Node(state);
                   newNode.setParent(node);
                   node.getChildArray().add(newNode);
               }
           }




    }

    private void backPropogation(Node nodeToExplore, Color playerColor, NNOutput nnOutput) {
        Node tempNode = nodeToExplore;
        while (tempNode != null) {

            tempNode.getState().incrementVisit();
            if (tempNode.getState().getPlayerColor() == playerColor)
                tempNode.getState().updateWinScore(nnOutput.getWin_score());
            tempNode = tempNode.getParent();
        }
    }

}