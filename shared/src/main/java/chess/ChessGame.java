package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor turn;

    public ChessGame() {
        this.turn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        Collection<ChessMove> validatedMoves = new ArrayList<>();

        if (piece == null) return null;
        else {
            Collection<ChessMove> unvalidatedMoves = piece.pieceMoves(board, startPosition);

            for (ChessMove move : unvalidatedMoves) {
                ChessBoard boardCopy = board.makeCopy();
                boardCopy.addPiece(move.getEndPosition(), piece);
                boardCopy.addPiece(move.getStartPosition(), null);

                ChessGame testGame = new ChessGame();
                testGame.setBoard(boardCopy);
                if (!testGame.isInCheck(piece.getTeamColor())) {
                    validatedMoves.add(move);
                }

            }
        }
        return validatedMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getStartPosition(), null);
    }

    public ChessPosition findTheKing(TeamColor teamColor) {
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() == teamColor && (currentPiece.getPieceType() == ChessPiece.PieceType.KING)) {
                    kingPosition = currentPosition;
                    break;
                }
            }
        }
        return kingPosition;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findTheKing(teamColor);

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() != teamColor) {
                    Collection<ChessMove> currentMoves = currentPiece.pieceMoves(board, currentPosition);

                    for (ChessMove move : currentMoves) {
                        if (move.getEndPosition().equals(kingPosition)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j=1; j <= 8; j++) {
                    ChessPosition currentPosition = new ChessPosition(i, j);
                    ChessPiece currentPiece = board.getPiece(currentPosition);

                    if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> currentMoves = currentPiece.pieceMoves(board, currentPosition);

                        for (ChessMove move : currentMoves) {
                            ChessBoard boardCopy = board.makeCopy();
                            boardCopy.addPiece(move.getEndPosition(), currentPiece);
                            boardCopy.addPiece(move.getStartPosition(), null);
                            ChessGame testGame = new ChessGame();
                            testGame.setBoard(boardCopy);
                            if (!testGame.isInCheck(teamColor)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return false;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
