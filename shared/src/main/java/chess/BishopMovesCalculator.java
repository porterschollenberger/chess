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

        // up and to the right
        int offset = 1;
        while (myPosition.getRow() + offset <= 8 && myPosition.getColumn() + offset <= 8) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + offset, myPosition.getColumn() + offset);
            if (board.getPiece(possiblePosition) == null) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                break;
            } else {
                // blocked by team
                break;
            }
            offset++;
        }

        // down and to the right
        offset = 1;
        while (myPosition.getRow() - offset >= 1 && myPosition.getColumn() + offset <= 8) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() - offset, myPosition.getColumn() + offset);
            if (board.getPiece(possiblePosition) == null) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                break;
            } else {
                break;
            }
            offset++;
        }

        // down and to the left
        offset = 1;
        while (myPosition.getRow() - offset >= 1 && myPosition.getColumn() - offset >= 1) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() - offset, myPosition.getColumn() - offset);
            if (board.getPiece(possiblePosition) == null) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                break;
            } else {
                break;
            }
            offset++;
        }

        // up and to the left
        offset = 1;
        while (myPosition.getRow() + offset <= 8 && myPosition.getColumn() - offset >= 1) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + offset, myPosition.getColumn() - offset);
            if (board.getPiece(possiblePosition) == null) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                break;
            } else {
                break;
            }
            offset++;
        }

        return validMoves;
    }
}
