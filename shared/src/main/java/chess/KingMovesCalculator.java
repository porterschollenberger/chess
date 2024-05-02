package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator {
    private ChessBoard board;
    private ChessPosition myPosition;

    public KingMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private Collection<ChessMove> getMoves(int rowDirection, int colDirection) {
        ArrayList<ChessMove> validMoves=new ArrayList<>();

        if (isWithinBounds(myPosition.getRow() + rowDirection, myPosition.getColumn() + colDirection)) {
            ChessPosition possiblePosition=new ChessPosition(myPosition.getRow() + rowDirection, myPosition.getColumn() + colDirection);
            ChessPiece pieceAtPosition=board.getPiece(possiblePosition);

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

    public Collection<ChessMove> calculateKingMoves() {
        ArrayList<ChessMove> validMoves=new ArrayList<>();

        validMoves.addAll(getMoves(1,0)); // up
        validMoves.addAll(getMoves(-1,0)); // down
        validMoves.addAll(getMoves(0,-1)); // left
        validMoves.addAll(getMoves(0,1)); // right
        validMoves.addAll(getMoves(1,1)); // up-right
        validMoves.addAll(getMoves(1,-1)); // up-left
        validMoves.addAll(getMoves(-1,1)); // down-right
        validMoves.addAll(getMoves(-1,-1)); // down-left


        return validMoves;
    }
}
