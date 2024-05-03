package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    public Collection<ChessMove> calculateKingMoves() {
        ArrayList<ChessMove> validMoves=new ArrayList<>();

        CalculatorHelper calculator = new CalculatorHelper(board, myPosition);
        validMoves.addAll(calculator.calculateValidMovesSingle(1,0,1,1)); // up
        validMoves.addAll(calculator.calculateValidMovesSingle(-1,0,1,1)); // down
        validMoves.addAll(calculator.calculateValidMovesSingle(0,-1,1,1)); // left
        validMoves.addAll(calculator.calculateValidMovesSingle(0,1,1,1)); // right
        validMoves.addAll(calculator.calculateValidMovesSingle(1,1,1,1)); // up-right
        validMoves.addAll(calculator.calculateValidMovesSingle(1,-1,1,1)); // up-left
        validMoves.addAll(calculator.calculateValidMovesSingle(-1,1,1,1)); // down-right
        validMoves.addAll(calculator.calculateValidMovesSingle(-1,-1,1,1)); // down-left


        return validMoves;
    }
}
