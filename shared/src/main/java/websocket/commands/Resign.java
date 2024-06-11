package websocket.commands;

public class Resign extends UserGameCommand {
    private CommandType commandType = CommandType.RESIGN;
    private Integer gameID;

    public Resign(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
