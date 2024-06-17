package websocket.commands;

public class Resign extends UserGameCommand {
    private Integer gameID;

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        commandType = CommandType.RESIGN;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
