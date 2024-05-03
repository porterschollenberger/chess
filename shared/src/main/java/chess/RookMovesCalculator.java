package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public RookMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    public Collection<ChessMove> calculateRookMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        CalculatorHelper calculator = new CalculatorHelper(board, myPosition);
        validMoves.addAll(calculator.calculateValidMovesMulti(1, 0));
        validMoves.addAll(calculator.calculateValidMovesMulti(-1, 0));
        validMoves.addAll(calculator.calculateValidMovesMulti(0, 1));
        validMoves.addAll(calculator.calculateValidMovesMulti(0, -1));

        return validMoves;
    }
}
