package service;

import dataaccess.*;
import memory.MemoryAuthDAO;
import memory.MemoryGameDAO;
import memory.MemoryUserDAO;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sql.*;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private ClearService clearService;

    @BeforeEach
    void setUp() {
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();
        clearService = new ClearService(authDAO, gameDAO, userDAO);
        userDAO.clear();
    }

    @Test
    void testClearSuccess() {
        authDAO.createAuth("test username");
        gameDAO.createGame("game name");
        userDAO.createUser(new UserData("username", "password", "email"));

        clearService.clear();

        assertTrue(authDAO.isEmpty());
        assertTrue(gameDAO.isEmpty());
        assertTrue(userDAO.isEmpty());
    }
}