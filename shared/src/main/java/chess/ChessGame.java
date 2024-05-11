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
        if (board.getPiece(move.getStartPosition()) == null) throw new InvalidMoveException("No piece");
        if (!turn.equals(board.getPiece(move.getStartPosition()).getTeamColor())) throw new InvalidMoveException("Wrong turn");
        ArrayList<ChessMove> validatedMoves = new ArrayList<>(validMoves(move.getStartPosition()));
        boolean valid = false;
        for (ChessMove checkMove : validatedMoves) {
            if (checkMove.equals(move)) {
                board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
                board.addPiece(move.getStartPosition(), null);
                valid = true;
                break;
            }
        }
        if (!valid) throw new InvalidMoveException("Invalid move");
        if (turn.equals(TeamColor.WHITE)) turn = TeamColor.BLACK;
        else if (turn.equals(TeamColor.BLACK)) turn = TeamColor.WHITE;
    }

    private ChessPosition findTheKing(TeamColor teamColor) {
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

    public Collection<ChessMove> getAllMovesByTeam(TeamColor teamColor) {
        ArrayList<ChessMove> allMoves = new ArrayList<>();

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentPosition = new ChessPosition(i,j);
                ChessPiece currentPiece = board.getPiece(currentPosition);

                if (currentPiece != null && currentPiece.getTeamColor() == teamColor) {
                    allMoves.addAll(currentPiece.pieceMoves(board, currentPosition));
                }
            }
        }

        return allMoves;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findTheKing(teamColor);
        Collection<ChessMove> allMoves;

        if (teamColor.equals(TeamColor.WHITE)) {
            allMoves = getAllMovesByTeam(TeamColor.BLACK);
        } else {
            allMoves = getAllMovesByTeam(TeamColor.WHITE);
        }
        for (ChessMove move : allMoves) {
            if (move.getEndPosition().equals(kingPosition)) return true;
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
            Collection<ChessMove> allMoves=getAllMovesByTeam(teamColor);

            for (ChessMove move : allMoves) {
                ChessBoard boardCopy=board.makeCopy();
                boardCopy.addPiece(move.getEndPosition(), boardCopy.getPiece(move.getStartPosition()));
                boardCopy.addPiece(move.getStartPosition(), null);
                ChessGame testGame=new ChessGame();
                testGame.setBoard(boardCopy);
                if (!testGame.isInCheck(teamColor)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> validatedMoves = new ArrayList<>();
        for (ChessMove move : getAllMovesByTeam(teamColor)) {
            validatedMoves.addAll(validMoves(move.getStartPosition()));
        }
        return !isInCheck(teamColor) && (validatedMoves.isEmpty());
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
