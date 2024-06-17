package websocket.commands;

public class Resign extends UserGameCommand {
    private Integer gameID;
    private String username;

    public Resign(String authToken, Integer gameID, String username) {
        super(authToken);
        commandType = CommandType.RESIGN;
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
