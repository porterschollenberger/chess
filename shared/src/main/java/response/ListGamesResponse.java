package response;

import model.GameData;
import spark.Response;

import java.util.Collection;

public class ListGamesResponse extends Response {
    private Collection<GameData> games;
    private String message;

    public ListGamesResponse(Collection<GameData> games) {
        this.games = games;
    }

    public ListGamesResponse(String message) {
        this.message = message;
    }
}
