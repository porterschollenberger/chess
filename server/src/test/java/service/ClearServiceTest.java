package service;

import dataaccess.*;
import memory.MemoryAuthDAO;
import memory.MemoryGameDAO;
import memory.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private ClearService clearService;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        userDAO = new MemoryUserDAO();
        clearService = new ClearService(authDAO, gameDAO, userDAO);
    }

    @Test
    void testClear_Success() {
        authDAO.createAuth("test username");
        gameDAO.createGame("game name");
        userDAO.createUser(new UserData("username", "password", "email"));

        clearService.clear();

        assertTrue(authDAO.isEmpty());
        assertTrue(gameDAO.isEmpty());
        assertTrue(userDAO.isEmpty());
    }
}