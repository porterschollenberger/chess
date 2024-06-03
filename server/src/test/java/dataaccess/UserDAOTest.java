package dataaccess;

import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sql.SQLUserDAO;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @Test
    void testCreateUserSuccess() {
        UserData user = new UserData("test username", "test password", "testEmail@email.com");

        assertDoesNotThrow(() -> userDAO.createUser(user));
    }


    @Test
    void testCreateUserFailure() {
        UserData halfUser = new UserData("username", null, null);

        assertThrows(RuntimeException.class, () -> userDAO.createUser(halfUser));
    }

    @Test
    void testGetUserSuccess() {
        UserData user = new UserData("test username", "test password", "testEmail@email.com");
        userDAO.createUser(user);

        UserData foundUser = userDAO.getUser("test username");

        assertNotNull(foundUser);
    }

    @Test
    void testGetUserFailure() {
        UserData user = new UserData("test username", "test password", "testEmail@email.com");
        userDAO.createUser(user);

        assertThrows(RuntimeException.class, () -> userDAO.getUser(null));
    }

    @Test
    void testClearSuccess() {
        userDAO.createUser(new UserData("test username", "test password", "testEmail@email.com"));

        userDAO.clear();

        assertTrue(userDAO.isEmpty());
    }
}