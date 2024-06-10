package client;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    UserData testUser= new UserData("player1", "password", "p1@email.com");

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void before() {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void testRegisterSuccess() throws Exception {
        var registerResponse = facade.register(testUser);
        assertNotNull(registerResponse.getAuthToken());
    }

    @Test
    void testRegisterFailure() throws Exception {
        facade.register(testUser);
        assertThrows(ResponseException.class, () -> facade.register(testUser));
    }

    @Test
    void testLoginSuccess() throws Exception {
        facade.register(testUser);
        facade.logout();

        var loginResponse = facade.login("player1", "password");
        assertNotNull(loginResponse.getAuthToken());
    }

    @Test
    void testLoginFailure() {
        assertThrows(ResponseException.class, () -> facade.login("player1", "password"));
    }

    @Test
    void testLogoutSuccess() throws Exception {
        facade.register(testUser);

        var GenericResponse = facade.logout();
        assertNull(GenericResponse.getMessage());
    }

    @Test
    void testLogoutFailure() {
        assertThrows(ResponseException.class, () -> facade.logout());
    }

    @Test
    void testCreateGameSuccess() throws Exception {
        facade.register(testUser);

        var response = facade.createGame("test");
        assertNotNull(response.getGameID());
    }

    @Test
    void testCreateGameFailure() {
        assertThrows(ResponseException.class, () -> facade.createGame("test"));
    }

    @Test
    void testListGamesSuccess() throws Exception {
        facade.register(testUser);
        facade.createGame("test");

        var response = facade.listGames();
        assertNotNull(response.getGames());
    }

    @Test
    void testListGamesFailure() {
        assertThrows(ResponseException.class, () -> facade.listGames());
    }

    @Test
    void testJoinSuccess() throws Exception {
        facade.register(testUser);
        facade.createGame("test");

        var response = facade.joinGame("white", 1);
        assertNotNull(response.getMessage()); // TODO: change this after implementation
    }

    @Test
    void testJoinFailure() throws Exception {
        assertNotNull(facade.joinGame("white", 1)); // TODO: change this after implementation
    }

    @Test
    void testClearSuccess() throws Exception {
        facade.register(testUser);
        facade.createGame("test");

        var response = facade.clear();
        assertNull(response.getMessage());
    }
}
