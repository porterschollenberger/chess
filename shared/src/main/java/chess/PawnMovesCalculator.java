package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public PawnMovesCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board=board;
        this.myPosition=myPosition;
    }

    private boolean isWithinBounds(int row, int col) {
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private boolean canPromote(ChessPosition possiblePosition) {
        return (possiblePosition.getRow() == 8 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE)
                || (possiblePosition.getRow() == 1 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK);
    }

    private boolean canDoubleMove(ChessPosition myPosition) {
        boolean firstSquareFree = false;
        boolean atStartSquare = (myPosition.getRow() == 2 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE)
                || (myPosition.getRow() == 7 && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK);
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn())) == null) {
                firstSquareFree = true;
            }
        } else {
            if (board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn())) == null) {
                firstSquareFree = true;
            }
        }
        return atStartSquare && firstSquareFree;
    }

    private Collection<ChessMove> addAllPromotionPieces(Collection<ChessMove> validMoves, ChessPosition possiblePosition) {
        validMoves.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(myPosition, possiblePosition, ChessPiece.PieceType.KNIGHT));
        return validMoves;
    }

    private Collection<ChessMove> getForwardMoves(int rowDirection, int rowDistance) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (isWithinBounds(myPosition.getRow() + rowDistance * rowDirection, myPosition.getColumn())) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + rowDistance * rowDirection, myPosition.getColumn());
            ChessPiece pieceAtPosition = board.getPiece(possiblePosition);

            if (pieceAtPosition == null) {
                if (canPromote(possiblePosition)) {
                    addAllPromotionPieces(validMoves, possiblePosition);
                } else {
                    validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                }
            }
        }

        return validMoves;
    }

    private Collection<ChessMove> getDiagonalMoves(int rowDirection, int colDirection) {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (isWithinBounds(myPosition.getRow() + rowDirection, myPosition.getColumn() + colDirection)) {
            ChessPosition possiblePosition = new ChessPosition(myPosition.getRow() + rowDirection, myPosition.getColumn() + colDirection);
            ChessPiece pieceAtPosition = board.getPiece(possiblePosition);

            if (pieceAtPosition != null && pieceAtPosition.getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                if (canPromote(possiblePosition)) {
                    addAllPromotionPieces(validMoves, possiblePosition);
                } else {
                    validMoves.add(new ChessMove(myPosition, possiblePosition, null));
                }
            }
        }
        return validMoves;
    }

    public Collection<ChessMove> calculatePawnMoves() {
        ArrayList<ChessMove> validMoves = new ArrayList<>();

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            if (canDoubleMove(myPosition)) {
                validMoves.addAll(getForwardMoves(1,2));
            }
            validMoves.addAll(getForwardMoves(1,1));
            validMoves.addAll(getDiagonalMoves(1,1));
            validMoves.addAll(getDiagonalMoves(1,-1));
        } else {
            if (canDoubleMove(myPosition)) {
                validMoves.addAll(getForwardMoves(-1,2));
            }
            validMoves.addAll(getForwardMoves(-1,1));
            validMoves.addAll(getDiagonalMoves(-1,1));
            validMoves.addAll(getDiagonalMoves(-1,-1));
        }


        return validMoves;
    }
}
