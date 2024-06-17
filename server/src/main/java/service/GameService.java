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
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("authToken " + authToken + "does not exist.");
        }
        return gameDAO.listGames();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        if (authToken == null || gameName == null) {
            throw new BadRequestException("request did not contain correct information.");
        }
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("authToken " + authToken + "does not exist.");
        }
        int gameID = gameDAO.createGame(gameName);
        return gameDAO.getGame(gameID);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("authToken " + authToken + "does not exist.");
        }
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new BadRequestException("gameID " + gameID + " does not exist.");
        }
        if ("WHITE".equalsIgnoreCase(playerColor)) {
            if (game.whiteUsername() == null) {
                gameDAO.updateGame(gameID, "whiteUsername", auth.username());
            } else {
                throw new AlreadyTakenException("Player color " + playerColor + " is already taken.");
            }
        } else if ("BLACK".equalsIgnoreCase(playerColor)) {
            if (game.blackUsername() == null) {
                gameDAO.updateGame(gameID, "blackUsername", auth.username());
            } else {
                throw new AlreadyTakenException("Player color " + playerColor + " is already taken.");
            }
        } else {
            throw new BadRequestException("Player color " + playerColor + " is not valid.");
        }
    }

    public void leaveGame(int gameID, String playerColor) {
        if (playerColor.equalsIgnoreCase("white")) {
            gameDAO.updateGame(gameID, "whiteUsername", null);
        } else if (playerColor.equalsIgnoreCase("black")) {
            gameDAO.updateGame(gameID, "blackUsername", null);
        }
    }
}
