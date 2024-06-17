package websocket.commands;

public class Leave extends UserGameCommand {
    private Integer gameID;
    private String username;

    public Leave(String authToken, Integer gameID, String username) {
        super(authToken);
        commandType = CommandType.LEAVE;
        this.gameID = gameID;
        this.username = username;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getUsername() {
        return username;
    }
}
