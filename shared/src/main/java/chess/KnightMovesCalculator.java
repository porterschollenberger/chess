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

    private boolean isWithinBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private Collection<ChessMove> getMoves(int rowDirection, int colDirection, int rowDistance, int colDistance) {
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

    public Collection<ChessMove> calculateKnightMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getMoves(1, 1, 2, 1)); // up-right
        validMoves.addAll(getMoves(1, -1, 2, 1)); // up-left
        validMoves.addAll(getMoves(1, 1, 1, 2)); // right-up
        validMoves.addAll(getMoves(-1, 1, 1, 2)); // right-down
        validMoves.addAll(getMoves(-1, 1, 2, 1)); // down-right
        validMoves.addAll(getMoves(-1, -1, 2, 1)); // down-left
        validMoves.addAll(getMoves(1, -1, 1, 2)); // left-up
        validMoves.addAll(getMoves(-1, -1, 1, 2)); // left-down

        return validMoves;
    }
}
