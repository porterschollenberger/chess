package websocket.commands;

public class Connect extends UserGameCommand {
    private Integer gameID;

    public Connect(String authToken, Integer gameID) {
        super(authToken);
        commandType = CommandType.CONNECT;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
