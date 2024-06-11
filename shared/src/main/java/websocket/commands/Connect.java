package websocket.commands;

public class Connect extends UserGameCommand {
    private CommandType commandType = CommandType.CONNECT;
    private Integer gameID;


    public Connect(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
