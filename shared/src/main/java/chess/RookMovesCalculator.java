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

    private boolean isWithinBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private Collection<ChessMove> getHorizontalMoves(int rowDirection, int colDirection) {
        ArrayList<ChessMove> horizontalMoves = new ArrayList<>();

        int offset = 1;
        while (isWithinBounds(myPosition.getRow() + offset * rowDirection, myPosition.getColumn() + offset * colDirection)) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + offset * rowDirection, myPosition.getColumn() + offset * colDirection);
            ChessPiece pieceAtPosition = board.getPiece(possiblePosition);

            if (pieceAtPosition == null) {
                horizontalMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else {
                if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    horizontalMoves.add(new ChessMove(myPosition, possiblePosition, null));
                }
                break;
            }
            offset++;
        }

        return horizontalMoves;
    }

    public Collection<ChessMove> calculateRookMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getHorizontalMoves(1, 0));
        validMoves.addAll(getHorizontalMoves(-1, 0));
        validMoves.addAll(getHorizontalMoves(0, 1));
        validMoves.addAll(getHorizontalMoves(0, -1));

        return validMoves;
    }
}
