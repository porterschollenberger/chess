package websocket.commands;

public class Resign extends UserGameCommand {
    private CommandType commandType;
    private Integer gameID;

    public Resign(String authToken, CommandType commandType, Integer gameID) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
    }
}
