package client;

import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    void testRegister() throws Exception {
        var registerResponse = facade.register(new UserData("player1", "password", "p1@email.com"));
        assertNotNull(registerResponse.getAuthToken());
    }

    @Test
    void testLogin() throws Exception {
        facade.register(new UserData("player1", "password", "p1@email.com"));

        var loginResponse = facade.login("player1", "password");
        assertNotNull(loginResponse.getAuthToken());
    }

    @Test
    void testLogout() throws Exception {
        facade.register(new UserData("player1", "password", "p1@email.com"));
        facade.login("player1", "password");

        var GenericResponse = facade.logout();
        assertNotNull(GenericResponse.getMessage());
    }

}
