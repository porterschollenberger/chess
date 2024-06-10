package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


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
        var registerResponse = facade.register(new UserData("player1", "password", "p1@email.com"));
        assertNotNull(registerResponse.getAuthToken());
    }

    @Test
    void testLoginSuccess() throws Exception {
        facade.register(testUser);
        facade.logout();

        var loginResponse = facade.login("player1", "password");
        assertNotNull(loginResponse.getAuthToken());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        facade.register(testUser);

        var GenericResponse = facade.logout();
        assertNull(GenericResponse.getMessage());
    }

    @Test
    void testCreateGameSuccess() throws Exception {
        facade.register(testUser);

        var response = facade.createGame("test");
        assertNotNull(response.getGameID());
    }

    @Test
    void testListGamesSuccess() throws Exception {
        facade.register(testUser);
        facade.createGame("test");

        var response = facade.listGames();
        assertNotNull(response.getGames());
    }

    @Test
    void testJoinSuccess() throws Exception {
        facade.register(testUser);
        facade.createGame("test");

        var response = facade.joinGame("white", 1);
        assertNotNull(response.getMessage()); // TODO: change this after implementation
    }

    @Test
    void testClearSuccess() throws Exception {
        facade.register(testUser);
        facade.createGame("test");

        var response = facade.clear();
        assertNull(response.getMessage());
    }
}
