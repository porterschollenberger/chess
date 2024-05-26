package response;

import model.GameData;
import spark.Response;

public class CreateGameResponse extends Response {
    private int gameID;
    private String message;
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    public CreateGameResponse(String message) {
        this.message = message;
    }
}
