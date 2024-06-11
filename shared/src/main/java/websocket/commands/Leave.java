package websocket.commands;

public class Leave extends UserGameCommand {
    private CommandType commandType;
    private Integer gameID;

    public Leave(String authToken, CommandType commandType, Integer gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }
}
