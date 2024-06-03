package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sql.SQLAuthDAO;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {
    private AuthDAO authDAO;

    @BeforeEach
    void setUp() {
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    void testCreateAuthSuccess() {
        String username = "test username";
        String authToken = authDAO.createAuth(username);

        assertNotNull(authToken);
    }

    @Test
    void testCreateAuthFailure() {
        assertThrows(RuntimeException.class, () -> authDAO.createAuth(null));
    }

    @Test
    void testGetAuthSuccess() {
        String username = "test username";
        String authToken = authDAO.createAuth(username);

        AuthData authData = authDAO.getAuth(authToken);

        assertNotNull(authData);
    }

    @Test
    void testGetAuthFailure() {
        assertThrows(RuntimeException.class, () -> authDAO.getAuth(null));
    }

    @Test
    void testDeleteAuthSuccess() {
        String username = "test username";
        String authToken = authDAO.createAuth(username);

        authDAO.deleteAuth(authToken);

        assertNull(authDAO.getAuth(authToken));
    }

    @Test
    void testDeleteAuthFailure() {
        assertThrows(RuntimeException.class, () -> authDAO.deleteAuth(null));
    }

    @Test
    void testClearSuccess() {
        authDAO.createAuth("test username");

        authDAO.clear();

        assertTrue(authDAO.isEmpty());
    }
}