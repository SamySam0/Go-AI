import java.util.ArrayList;

public class MoveChooser {

    public static Move chooseMove(BoardState boardState) {

        int searchDepth = Othello.searchDepth;

        ArrayList<Move> moves = boardState.getLegalMoves();
        if (moves.isEmpty()) {
            return null;
        }

        return minimax(boardState, searchDepth);
    }


    public static int evaluationFunction(BoardState boardState) {

        int[][] boardScores =
                {{120, -20, 20, 5, 5, 20, -20, 120}, {-20, -40, -5, -5, -5, -5, -40, -20},
                        {20, -5, 15, 3, 3, 15, -5, 20}, {5, -5, 3, 3, 3, 3, -5, 5},
                        {5, -5, 3, 3, 3, 3, -5, 5}, {20, -5, 15, 3, 3, 15, -5, 20},
                        {-20, -40, -5, -5, -5, -5, -40, -20}, {120, -20, 20, 5, 5, 20, -20, 120}};

        int whiteEvaluatedScore = 0;
        int blackEvaluatedScore = 0;

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                int pieceOnCoordinates = boardState.getContents(y, x);

                if (pieceOnCoordinates == 1) {
                    // Piece is White
                    whiteEvaluatedScore += boardScores[y][x];
                } else if (pieceOnCoordinates == -1) {
                    // Piece is Black
                    blackEvaluatedScore += boardScores[y][x];
                }
            }
        }

        return whiteEvaluatedScore - blackEvaluatedScore;
    }


    public static int minimaxVal(BoardState node, int depth, int alpha, int beta) {

        ArrayList<Move> legalMoves = node.getLegalMoves();

        if (node.gameOver()) {
            // Return result as integer: <0 black win; 0 draw; >0 white win
            int winner = node.result();
            if (winner < 0) { // Black wins
                return Integer.MIN_VALUE;
            } else if (winner > 0) { // White wins
                return Integer.MAX_VALUE;
            } else {
                return 0;
            }
        }

        else if (depth == 0) {
            return evaluationFunction(node);
        }

        else if (legalMoves.isEmpty()) { // No move available, let same player play twice.
            BoardState nodeCopy = node.deepCopy();
            nodeCopy.colour = -nodeCopy.colour; // Don't make any move, change the color manually
            // We do not do depth-1 for that branch, in order to balance with other branches
            return minimaxVal(nodeCopy, depth, alpha, beta);
        }

        else if (node.colour == 1) { // Node is maximizing
            int maxEval = Integer.MIN_VALUE;
            for (Move move : legalMoves) {
                BoardState nodeCopy = node.deepCopy();
                nodeCopy.makeLegalMove(move.x, move.y);
                int kid = minimaxVal(nodeCopy, depth - 1, alpha, beta);
                maxEval = Integer.max(kid, maxEval);
                // Alpha-beta pruning
                alpha = Integer.max(kid, alpha);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxEval;
        }

        else { // Node is maximizing
            int minEval = Integer.MAX_VALUE;
            for (Move move : legalMoves) {
                BoardState nodeCopy = node.deepCopy();
                nodeCopy.makeLegalMove(move.x, move.y);
                int kid = minimaxVal(nodeCopy, depth - 1, alpha, beta);
                minEval = Integer.min(kid, minEval);
                // Alpha-beta pruning
                beta = Integer.min(kid, beta);
                if (beta <= alpha) {
                    break;
                }
            }
            return minEval;
        }
    }


    // This is JOD, JOD is an AI, and JOD is very good at this game. Lose against JOD!
    public static Move minimax(BoardState node, int depth) {

        ArrayList<Move> legalMoves = node.getLegalMoves();

        Move bestMove = null;
        int bestValue = Integer.MIN_VALUE;

        for (Move move : legalMoves) {
            BoardState nodeCopy = node.deepCopy();
            nodeCopy.makeLegalMove(move.x, move.y);
            int kid = minimaxVal(nodeCopy, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (kid >= bestValue) {
                bestValue = kid;
                bestMove = move;
            }
        }

        return bestMove;
    }

}
