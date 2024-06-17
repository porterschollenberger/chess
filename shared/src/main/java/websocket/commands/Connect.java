package websocket.commands;

public class Connect extends UserGameCommand {
    private Integer gameID;
    private String username;
    private String color;

    public Connect(String authToken, Integer gameID, String username, String color) {
        super(authToken);
        commandType = CommandType.CONNECT;
        this.gameID = gameID;
        this.username = username;
        this.color = color;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getUsername() {
        return username;
    }

    public String getColor() {
        return color;
    }
}
