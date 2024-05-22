package request;

import chess.ChessPiece;

public record joinGameRequest(String playerColor, int gameID) {
}
