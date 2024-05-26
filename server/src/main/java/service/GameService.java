package service;

import dataaccess.*;
import model.*;

import java.util.Collection;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException {
        authDAO.getAuth(authToken);
        return gameDAO.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        authDAO.getAuth(authToken);
        int gameID = gameDAO.createGame(gameName);
        return gameDAO.getGame(gameID);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        GameData game = gameDAO.getGame(gameID);
        if ("WHITE".equals(playerColor)) {
            if (game.whiteUsername() == null) {
                gameDAO.updateGame(gameID, "whiteUsername", auth.username());
            } else {
                throw new DataAccessException("Player color " + playerColor + " is already taken");
            }
        } else if ("BLACK".equals(playerColor)) {
            if (game.blackUsername() == null) {
                gameDAO.updateGame(gameID, "blackUsername", auth.username());
            } else {
                throw new DataAccessException("Player color " + playerColor + " is already taken");
            }
        } else {
            throw new DataAccessException("Player color " + playerColor + " is not valid");
        }
    }
}
