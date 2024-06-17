package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class BoardDrawer {
    private static final String EMPTY = " ";
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


    public static void drawWhiteBoard(ChessBoard board) {
        board.resetBoard();
        out.print(ERASE_SCREEN);
        out.println();
        drawHeadersWhite(out);
        drawChessBoardWhite(out,board);
        drawHeadersWhite(out);
        out.print(RESET);
    }

    public static void drawBlackBoard(ChessBoard board) {
        board.resetBoard();
        out.print(ERASE_SCREEN);
        out.println();
        drawHeadersBlack(out);
        drawChessBoardBlack(out, board);
        drawHeadersBlack(out);
        out.print(RESET);
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

    private static void drawChessBoardWhite(PrintStream out, ChessBoard board) {
        for (int row = 8; row >= 1; row--) {
            drawHeader(out, String.valueOf(row));
            drawRowOfSquaresWhite(out, row, board);
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

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
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