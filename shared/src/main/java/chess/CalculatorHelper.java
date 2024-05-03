package chess;

import java.util.ArrayList;
import java.util.Collection;

public class CalculatorHelper {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public CalculatorHelper(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    boolean isWithinBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    public Collection<ChessMove> calculateValidMovesMulti(int rowDirection, int colDirection) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        int offset = 1;
        while (isWithinBounds(myPosition.getRow() + offset * rowDirection, myPosition.getColumn() + offset * colDirection)) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + offset * rowDirection, myPosition.getColumn() + offset * colDirection);
            ChessPiece pieceAtPosition = board.getPiece(possiblePosition);

            if (pieceAtPosition == null) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else {
                if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                }
                break;
            }
            offset++;
        }

        return validMoves;
    }

    public Collection<ChessMove> calculateValidMovesSingle(int rowDirection, int colDirection, int rowDistance, int colDistance) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (isWithinBounds(myPosition.getRow() + rowDistance * rowDirection, myPosition.getColumn() + colDistance * colDirection)) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + rowDistance * rowDirection, myPosition.getColumn() + colDistance * colDirection);
            ChessPiece pieceAtPosition = board.getPiece(possiblePosition);

            if (pieceAtPosition == null) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else {
                if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                }
            }
        }

        return validMoves;
    }
}
