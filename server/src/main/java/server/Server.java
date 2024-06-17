package server;

import dataaccess.*;
import handler.HandlerRegistry;
import memory.*;
import service.*;
import spark.*;
import sql.*;
import websocket.WebSocketHandler;

public class Server {

    public int run(int desiredPort) {
        AuthDAO authDAO = new SQLAuthDAO();
        GameDAO gameDAO = new SQLGameDAO();
        UserDAO userDAO = new SQLUserDAO();

        UserService userService = new UserService(authDAO, userDAO);
        ClearService clearService = new ClearService(authDAO, gameDAO, userDAO);
        GameService gameService = new GameService(authDAO, gameDAO);

        WebSocketHandler webSocketHandler = new WebSocketHandler();

        Spark.port(desiredPort);

        Spark.staticFiles.location("/web");

        Spark.webSocket("/ws", webSocketHandler);

        HandlerRegistry handlerRegistry = new HandlerRegistry(clearService, gameService, userService);
        registerEndpoints(handlerRegistry);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void registerEndpoints(HandlerRegistry handlerRegistry) {
        Spark.get("/hello", (req, res) -> "Hello BYU!");
        Spark.delete("/db", (req, res) -> handlerRegistry.getHandler("clear").handle(req, res));
        Spark.post("/user", (req, res) -> handlerRegistry.getHandler("register").handle(req, res));
        Spark.post("/session", (req, res) -> handlerRegistry.getHandler("login").handle(req, res));
        Spark.delete("/session", (req, res) -> handlerRegistry.getHandler("logout").handle(req, res));
        Spark.get("/game", (req, res) -> handlerRegistry.getHandler("listGames").handle(req, res));
        Spark.post("/game", (req, res) -> handlerRegistry.getHandler("createGame").handle(req, res));
        Spark.put("/game", (req, res) -> handlerRegistry.getHandler("joinGame").handle(req, res));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
