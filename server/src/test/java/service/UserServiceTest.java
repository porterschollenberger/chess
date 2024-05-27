package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import memory.MemoryAuthDAO;
import memory.MemoryUserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private UserService userService;

    @BeforeEach
    void setUp() {
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        userService = new UserService(authDAO, userDAO);
    }

    @Test
    void testRegister_Success() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email");

        AuthData auth = userService.register(userData);

        assertNotNull(auth);
    }

    @Test
    void testRegister_Failure() {
        UserData badUserData = new UserData(null, null, null);

        assertThrows(DataAccessException.class, () -> userService.register(badUserData));
    }

    @Test
    void testLogin_Success() throws DataAccessException {
        String username = "test username";
        String password = "test password";
        String email = "test email";
        UserData user = new UserData(username, password, email);

        userDAO.createUser(user);

        AuthData auth = userService.login(username, password);

        assertNotNull(auth);
    }

    @Test
    void testLogin_Failure() {
        String username = "test username";
        String password = "test password";
        String email = "test email";
        UserData user = new UserData(username, password, email);

        userDAO.createUser(user);

        assertThrows(DataAccessException.class, () -> userService.login(username, "wrong password"));
    }

    @Test
    void testLogout_Success() throws DataAccessException {
        String username = "test username";
        String auth = authDAO.createAuth(username);

        userService.logout(auth);

        assertNull(authDAO.getAuth(auth));
    }

    @Test
    void testLogout_Failure() {
        String auth = "invalid auth";

        assertThrows(DataAccessException.class, () -> userService.logout(auth));
    }
}