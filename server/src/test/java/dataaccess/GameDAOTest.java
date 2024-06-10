package dataaccess;

import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sql.SQLGameDAO;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() {
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    void testCreateGameSuccess() {
        String gameName = "test game name";

        Integer gameID = gameDAO.createGame(gameName);

        assertNotNull(gameID);
    }

    @Test
    void testCreateGameFailure() {
        assertThrows(RuntimeException.class, () -> gameDAO.createGame(null));
    }



    @Test
    void testGetGameSuccess() {
        String gameName = "test game name";
        int gameID = gameDAO.createGame(gameName);

        GameData game = gameDAO.getGame(gameID);

        assertNotNull(game);
    }

    @Test
    void testGetGameFailure() {
        //find a way that this fails so I can test for that
    }

    @Test
    void testListGamesSuccess() {
        Collection<GameData> games = gameDAO.listGames();

        assertNotNull(games);
    }

    @Test
    void testListGamesFailure() {
        //find a way that this fails so I can test for that
    }

    @Test
    void testUpdateGameSuccess() {
        int gameID = gameDAO.createGame("test game name");
        String username = "test username";

        gameDAO.updateGame(gameID, "whiteUsername", username);

        assertEquals(gameDAO.getGame(gameID).whiteUsername(), username);
    }

    @Test
    void testUpdateGameFailure() {
        int gameID = gameDAO.createGame("test game name");
        String username = "test username";

        assertThrows(IllegalArgumentException.class, () -> gameDAO.updateGame(gameID, "ErrorInducingMistake", username));
    }

    @Test
    void testClearSuccess() {
        gameDAO.createGame("test game");

        gameDAO.clear();

        assertTrue(gameDAO.isEmpty());
    }
}