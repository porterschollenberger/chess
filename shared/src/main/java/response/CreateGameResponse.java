package response;

public class CreateGameResponse {
    private Integer gameID;
    private String message;
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    public CreateGameResponse(String message) {
        this.message = message;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }
}
