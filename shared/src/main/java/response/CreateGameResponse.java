package response;

import spark.Response;

public class CreateGameResponse extends Response {
    private Integer gameID;
    private String message;
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    public CreateGameResponse(String message) {
        this.message = message;
    }
}
