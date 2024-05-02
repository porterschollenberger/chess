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

    private boolean isWithinBounds(int row, int col) {
        return row >= 1 && row <=8 && col >= 1 && col <= 8;
    }

    private Collection<ChessMove> getDiagonalMoves(int rowDirection, int colDirection) {
        ArrayList<ChessMove> diagonalMoves = new ArrayList<>();

        int offset = 1;
        while (isWithinBounds(myPosition.getRow() + offset * rowDirection, myPosition.getColumn() + offset * colDirection)) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + offset * rowDirection, myPosition.getColumn() + offset * colDirection);
            ChessPiece pieceAtPosition = board.getPiece(possiblePosition);

            if (pieceAtPosition == null) {
                diagonalMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else {
                if (pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    diagonalMoves.add(new ChessMove(myPosition, possiblePosition, null));
                }
                break;
            }
            offset++;
        }

        return diagonalMoves;
    }

    public Collection<ChessMove> calculateBishopMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        validMoves.addAll(getDiagonalMoves(1,1));
        validMoves.addAll(getDiagonalMoves(-1,1));
        validMoves.addAll(getDiagonalMoves(-1,-1));
        validMoves.addAll(getDiagonalMoves(1,-1));

        return validMoves;
    }
}
