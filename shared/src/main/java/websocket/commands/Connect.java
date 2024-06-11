package websocket.commands;

public class Connect extends UserGameCommand {
    private CommandType commandType;
    private Integer gameID;


    public Connect(String authToken, CommandType commandType, Integer gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }
}
