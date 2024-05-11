package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public QueenMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    public Collection<ChessMove> calculateQueenMoves() {
        Collection<ChessMove> validMoves = new ArrayList<>();

        CalculatorHelper calculator = new CalculatorHelper(board, myPosition);
        validMoves.addAll(calculator.calculateValidMovesMulti(1,0)); // up
        validMoves.addAll(calculator.calculateValidMovesMulti(-1,0)); // down
        validMoves.addAll(calculator.calculateValidMovesMulti(0,-1)); // left
        validMoves.addAll(calculator.calculateValidMovesMulti(0,1)); // right
        validMoves.addAll(calculator.calculateValidMovesMulti(1,1)); // up-right
        validMoves.addAll(calculator.calculateValidMovesMulti(1,-1)); // up-left
        validMoves.addAll(calculator.calculateValidMovesMulti(-1,1)); // down-right
        validMoves.addAll(calculator.calculateValidMovesMulti(-1,-1)); // down-left

        return validMoves;
    }
}
