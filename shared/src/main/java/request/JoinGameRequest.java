package request;

import spark.Request;

public class JoinGameRequest extends Request {
    private final String playerColor;
    private final int gameID;

    public JoinGameRequest(String playerColor, int gameID) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }
}
