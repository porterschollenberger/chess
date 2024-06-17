package websocket.commands;

public class Leave extends UserGameCommand {
    private Integer gameID;

    public Leave(String authToken, Integer gameID) {
        super(authToken);
        commandType = CommandType.LEAVE;
        this.gameID = gameID;
    }

    public Integer getGameID() {
        return gameID;
    }
}
