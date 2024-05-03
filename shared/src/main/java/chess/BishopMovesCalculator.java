package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public BishopMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    public Collection<ChessMove> calculateBishopMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        CalculatorHelper calculator = new CalculatorHelper(board, myPosition);
        validMoves.addAll(calculator.calculateValidMovesMulti(1,1));
        validMoves.addAll(calculator.calculateValidMovesMulti(-1,1));
        validMoves.addAll(calculator.calculateValidMovesMulti(-1,-1));
        validMoves.addAll(calculator.calculateValidMovesMulti(1,-1));

        return validMoves;
    }
}
