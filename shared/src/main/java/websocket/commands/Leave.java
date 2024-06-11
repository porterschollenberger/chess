package websocket.commands;

public class Leave extends UserGameCommand {
    private CommandType commandType = CommandType.LEAVE;
    private Integer gameID;

    public Leave(String authToken, Integer gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
