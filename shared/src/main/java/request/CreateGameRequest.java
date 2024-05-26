package request;

import spark.Request;

public class CreateGameRequest extends Request {
    private final String gameName;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }
}
