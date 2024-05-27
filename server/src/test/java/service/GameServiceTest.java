package service;

import dataaccess.*;
import memory.*;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameService gameService;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(authDAO, gameDAO);
    }

    @Test
    void testListGames_Success() throws DataAccessException {
        String authToken = authDAO.createAuth("test username");

        var games = gameService.listGames(authToken);

        assertNotNull(games);
    }

    @Test
    void testListGames_Failure() {
        String authToken = "invalid auth";

        assertThrows(DataAccessException.class, () -> gameService.listGames(authToken));
    }

    @Test
    void testCreateGame_Success() throws DataAccessException {
        String authToken = authDAO.createAuth("test username");
        String gameName = "test game name";

        GameData gameData = gameService.createGame(authToken, gameName);

        assertNotNull(gameData);
    }

    @Test
    void testCreateGame_Failure() {
        String authToken = "invalid auth";
        String gameName = "test game name";

        assertThrows(DataAccessException.class, () -> gameService.createGame(authToken, gameName));
    }

    @Test
    void testJoinGame_Success() throws DataAccessException {
        String authToken = authDAO.createAuth("test username");
        String playerColor = "WHITE";
        int gameID = gameDAO.createGame("test game name");

        gameService.joinGame(authToken, playerColor, gameID);

        GameData game = gameDAO.getGame(gameID);
        assertNotNull(game);
    }

    @Test
    void testJoinGame_Failure() {
        String authToken = "invalid auth";
        String playerColor = "WHITE";
        int gameID = gameDAO.createGame("test game name");

        assertThrows(DataAccessException.class, () -> gameService.joinGame(authToken, playerColor, gameID));
    }
}