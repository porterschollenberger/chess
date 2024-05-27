package response;

import model.GameData;

import java.util.Collection;

public class ListGamesResponse {
    private Collection<GameData> games;
    private String message;

    public ListGamesResponse(Collection<GameData> games) {
        this.games = games;
    }

    public ListGamesResponse(String message) {
        this.message = message;
    }
}
