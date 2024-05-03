package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public KnightMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    public Collection<ChessMove> calculateKnightMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        CalculatorHelper calculator = new CalculatorHelper(board, myPosition);
        validMoves.addAll(calculator.calculateValidMovesSingle(1, 1, 2, 1)); // up-right
        validMoves.addAll(calculator.calculateValidMovesSingle(1, -1, 2, 1)); // up-left
        validMoves.addAll(calculator.calculateValidMovesSingle(1, 1, 1, 2)); // right-up
        validMoves.addAll(calculator.calculateValidMovesSingle(-1, 1, 1, 2)); // right-down
        validMoves.addAll(calculator.calculateValidMovesSingle(-1, 1, 2, 1)); // down-right
        validMoves.addAll(calculator.calculateValidMovesSingle(-1, -1, 2, 1)); // down-left
        validMoves.addAll(calculator.calculateValidMovesSingle(1, -1, 1, 2)); // left-up
        validMoves.addAll(calculator.calculateValidMovesSingle(-1, -1, 1, 2)); // left-down

        return validMoves;
    }
}
