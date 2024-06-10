package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
    @Override
    public String toString() {
        final StringBuilder sb=new StringBuilder();
        sb.append(gameID).append(". ").append(gameName).append("\n");
        sb.append("\tWhite Username: '").append(whiteUsername).append("'\n");
        sb.append("\tBlack Username: '").append(blackUsername).append("'\n");
        return sb.toString();
    }
}
