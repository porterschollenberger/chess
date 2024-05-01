package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        // Bishops' moves:
        ArrayList<ChessMove> validMoves = new ArrayList<>();
        switch (board.getPiece(myPosition).getPieceType()) {
            case BISHOP:
                // up and to the right
                int offset = 1;
                while (myPosition.getRow() + offset <= 8 && myPosition.getColumn() + offset <= 8) {
                    if (board.getPiece(myPosition) != null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + offset, myPosition.getColumn() + offset), PieceType.BISHOP));
                    } else {
                        // blocked so can't go further
                        break;
                    }
                    offset++;
                }

                // up and to the left
                offset = 1;
                while (myPosition.getRow() + offset <= 8 && myPosition.getColumn() - offset >= 1) {
                    if (board.getPiece(myPosition) != null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + offset, myPosition.getColumn() - offset), PieceType.BISHOP));
                    } else {
                        break;
                    }
                    offset++;
                }

                // down and to the right
                offset = 1;
                while (myPosition.getRow() - offset >= 1 && myPosition.getColumn() + offset <= 8) {
                    if (board.getPiece(myPosition) != null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - offset, myPosition.getColumn() + offset), PieceType.BISHOP));
                    } else {
                        break;
                    }
                    offset++;
                }

                // down and to the left
                offset = 1;
                while (myPosition.getRow() - offset >= 1 && myPosition.getColumn() - offset >= 1) {
                    if (board.getPiece(myPosition) != null) {
                        validMoves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - offset, myPosition.getColumn() - offset), PieceType.BISHOP));
                    } else {
                        break;
                    }
                    offset++;
                }
                return validMoves;
            default:
                return new ArrayList<>();
        }
    }
}
