package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    private static final String EMPTY = " ";
    private static final PrintStream OUT= new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void drawWhiteBoard(ChessGame chessGame) {
        ChessBoard board = chessGame.getBoard();
        board.resetBoard();
        OUT.print(ERASE_SCREEN);
        OUT.println();
        drawHeadersWhite(OUT);
        drawChessBoardWhite(OUT, board);
        drawHeadersWhite(OUT);
        OUT.print(RESET);
    }

    public static void drawWhiteBoard(ChessGame chessGame, ChessPosition checkPosition) {
        ChessBoard board = chessGame.getBoard();
        board.resetBoard();
        OUT.print(ERASE_SCREEN);
        OUT.println();
        drawHeadersWhite(OUT);

        Collection<ChessMove> validMoves;
        if (checkPosition != null) {
            validMoves = chessGame.validMoves(checkPosition);
            drawChessBoardWhite(OUT, board, validMoves);
        } else {
            drawChessBoardWhite(OUT, board);
        }

        drawHeadersWhite(OUT);
        OUT.print(RESET);
    }

    public static void drawBlackBoard(ChessGame chessGame) {
        ChessBoard board = chessGame.getBoard();
        board.resetBoard();
        OUT.print(ERASE_SCREEN);
        OUT.println();
        drawHeadersBlack(OUT);
        drawChessBoardBlack(OUT, board);
        drawHeadersBlack(OUT);
        OUT.print(RESET);
    }

    public static void drawBlackBoard(ChessGame chessGame, ChessPosition checkPosition) {
        ChessBoard board = chessGame.getBoard();
        board.resetBoard();
        OUT.print(ERASE_SCREEN);
        OUT.println();
        drawHeadersBlack(OUT);

        Collection<ChessMove> validMoves;
        if (checkPosition != null) {
            validMoves = chessGame.validMoves(checkPosition);
            drawChessBoardBlack(OUT, board, validMoves);
        } else {
            drawChessBoardBlack(OUT, board);
        }

        drawHeadersWhite(OUT);
        OUT.print(RESET);
    }

    private static void drawHeadersBlack(PrintStream out) {
        setLightGrey(out);
        out.print(EMPTY.repeat(3));

        String[] headers = { "h", "g", "f", "e", "d", "c", "b", "a" };
        for (int col = 1; col <= 8; col++) {
            drawHeader(out, headers[col - 1]);
        }

        out.print(EMPTY.repeat(3));

        setBlack(out);
        out.println();
    }

    private static void drawHeadersWhite(PrintStream out) {
        setLightGrey(out);
        out.print(EMPTY.repeat(3));

        String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h" };
        for (int col = 1; col <= 8; col++) {
            drawHeader(out, headers[col - 1]);
        }

        out.print(EMPTY.repeat(3));

        setBlack(out);
        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        setLightGrey(out);

        out.print(EMPTY);
        printHeaderText(out, headerText);
        out.print(EMPTY);
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
        out.print(SET_TEXT_BOLD);

        out.print(player);
    }

    private static void drawChessBoardBlack(PrintStream out, ChessBoard board) {
        for (int row = 1; row <= 8; row++) {
            drawHeader(out, String.valueOf(row));
            drawRowOfSquaresBlack(out, row, board);
            drawHeader(out, String.valueOf(row));

            setBlack(out);
            out.println();
        }
    }

    private static void drawChessBoardBlack(PrintStream out, ChessBoard board, Collection<ChessMove> validMoves) {
        for (int row = 1; row <= 8; row++) {
            drawHeader(out, String.valueOf(row));
            drawRowOfSquaresBlack(out, row, board, validMoves);
            drawHeader(out, String.valueOf(row));

            setBlack(out);
            out.println();
        }
    }

    private static void drawChessBoardWhite(PrintStream out, ChessBoard board) {
        for (int row = 8; row >= 1; row--) {
            drawHeader(out, String.valueOf(row));
            drawRowOfSquaresWhite(out, row, board);
            drawHeader(out, String.valueOf(row));

            setBlack(out);
            out.println();
        }
    }

    private static void drawChessBoardWhite(PrintStream out, ChessBoard board, Collection<ChessMove> validMoves) {
        for (int row = 8; row >= 1; row--) {
            drawHeader(out, String.valueOf(row));
            drawRowOfSquaresWhite(out, row, board, validMoves);
            drawHeader(out, String.valueOf(row));

            setBlack(out);
            out.println();
        }
    }

    private static void drawRowOfSquaresBlack(PrintStream out, int row, ChessBoard board) {
        for (int col = 1; col <= 8; col++) {
            if ((col + row % 2) % 2 == 0) {
                setWhite(out);
            } else {
                setBlack(out);
            }
            printPieceWithSpacing(out, board.getPiece(new ChessPosition(row, 8 - (col - 1))));
        }
    }

    private static void drawRowOfSquaresBlack(PrintStream out, int row, ChessBoard board, Collection<ChessMove> validMoves) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition currentPosition = new ChessPosition(row, col);
            if ((col + row % 2) % 2 == 0) {
                drawLightSquare(validMoves, currentPosition);
            } else {
                drawDarkSquare(validMoves, currentPosition);
            }
            printPieceWithSpacing(out, board.getPiece(new ChessPosition(row, 8 - (col - 1))));
        }
    }

    private static void drawRowOfSquaresWhite(PrintStream out, int row, ChessBoard board) {
        for (int col = 1; col <= 8; col++) {
            if ((col + row % 2) % 2 == 1) {
                setWhite(out);
            } else {
                setBlack(out);
            }
            printPieceWithSpacing(out, board.getPiece(new ChessPosition(row, col)));
        }
    }

    private static void drawRowOfSquaresWhite(PrintStream out, int row, ChessBoard board, Collection<ChessMove> validMoves) {
        for (int col = 1; col <= 8; col++) {
            ChessPosition currentPosition = new ChessPosition(row, col);
            if ((col + row % 2) % 2 == 1) {
                drawLightSquare(validMoves, currentPosition);
            } else {
                drawDarkSquare(validMoves, currentPosition);
            }
            printPieceWithSpacing(out, board.getPiece(new ChessPosition(row, col)));
        }
    }

    private static void drawLightSquare(Collection<ChessMove> validMoves, ChessPosition currentPosition) {
        if (!validMoves.isEmpty()) {
            for (ChessMove validMove : validMoves) {
                if (validMove.getStartPosition().equals(currentPosition)) {
                    setYellow(OUT);
                } else if (validMove.getEndPosition().equals(currentPosition)) {
                    setGreen(OUT);
                    break;
                } else {
                    setWhite(OUT);
                }
            }
        } else {
            setWhite(OUT);
        }
    }

    private static void drawDarkSquare(Collection<ChessMove> validMoves, ChessPosition currentPosition) {
        if (!validMoves.isEmpty()) {
            for (ChessMove validMove : validMoves) {
                if (validMove.getStartPosition().equals(currentPosition)) {
                    setYellow(OUT);
                } else if (validMove.getEndPosition().equals(currentPosition)) {
                    setDarkGreen(OUT);
                    break;
                } else {
                    setBlack(OUT);
                }
            }
        } else {
            setBlack(OUT);
        }
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setGreen(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setDarkGreen(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setYellow(PrintStream out) {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void setLightGrey(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPieceWithSpacing(PrintStream out, ChessPiece piece) {
        out.print(EMPTY);
        if (piece == null) {
            out.print(EMPTY);
        } else {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                out.print(SET_TEXT_COLOR_RED);
            } else {
                out.print(SET_TEXT_COLOR_BLUE);
            }

            switch (piece.getPieceType()) {
                case KING:
                    out.print("K");
                    break;
                case QUEEN:
                    out.print("Q");
                    break;
                case BISHOP:
                    out.print("B");
                    break;
                case KNIGHT:
                    out.print("N");
                    break;
                case ROOK:
                    out.print("R");
                    break;
                case PAWN:
                    out.print("P");
                    break;
                default:
                    throw new IllegalArgumentException("Piece type not valid");
            }
        }
        out.print(EMPTY);
    }
}