package chess.ai.Controllers;


import chess.ai.Common.ChessBoard.Enums.Color;
import chess.ai.Common.ChessBoard.Models.Board;

import chess.ai.Common.Networked.BatchOfEvaluatedBoards;
import chess.ai.Common.Networked.PostRequestService;
import chess.ai.Common.neuralNet.Models.BoardState;
import chess.ai.Common.neuralNet.Models.NNOutput;
import chess.ai.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
@RestController
public class trainingController {

}
