package handler;

import com.google.gson.Gson;
import dataaccess.*;
import request.*;
import response.*;
import service.*;
import model.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class HandlerRegistry {
    private final Map<String, JsonHandler> handlers = new HashMap<>();
    private final Gson gson = new Gson();

    private void clearHandler(ClearService clearService) {
        handlers.put("clear", (req, res) -> {
            try {
                clearService.clear();
                res.status(200);
                return gson.toJson(new GenericResponse());
            } catch (Exception e) {
                res.status(500);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void registerHandler(UserService userService) {
        handlers.put("register", (req, res) -> {
            try {
                RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
                UserData user = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
                AuthData authData = userService.register(user);
                res.status(200);
                return gson.toJson(new RegisterResponse(authData.username(), authData.authToken()));
            } catch (BadRequestException e) {
                res.status(400);
                return gson.toJson(new RegisterResponse("Error: " + e.getMessage()));
            } catch (AlreadyTakenException e) {
                res.status(403);
                return gson.toJson(new RegisterResponse("Error: " + e.getMessage()));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new RegisterResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void loginHandler(UserService userService) {
        handlers.put("login", (req, res) -> {
            try {
                LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
                AuthData authData = userService.login(loginRequest.username(), loginRequest.password());
                res.status(200);
                return gson.toJson(new LoginResponse(authData.username(), authData.authToken()));
            } catch (UnauthorizedException e) {
                res.status(401);
                return gson.toJson(new LoginResponse("Error: " + e.getMessage()));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new LoginResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void logoutHandler(UserService userService) {
        handlers.put("logout", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                userService.logout(authToken);
                res.status(200);
                return gson.toJson(new GenericResponse());
            } catch (UnauthorizedException e) {
                res.status(401);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void listGamesHandler(GameService gameService) {
        handlers.put("listGames", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                Collection<GameData> games = gameService.listGames(authToken);
                res.status(200);
                return gson.toJson(new ListGamesResponse(games));
            } catch (UnauthorizedException e) {
                res.status(401);
                return gson.toJson(new ListGamesResponse("Error: " + e.getMessage()));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new ListGamesResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void createGameHandler(GameService gameService) {
        handlers.put("createGame", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);
                String gameName = createGameRequest.gameName();
                GameData game = gameService.createGame(authToken, gameName);
                res.status(200);
                return gson.toJson(new CreateGameResponse(game.gameID()));
            } catch (BadRequestException e) {
                res.status(400);
                return gson.toJson(new CreateGameResponse("Error: " + e.getMessage()));
            } catch (UnauthorizedException e) {
                res.status(401);
                return gson.toJson(new CreateGameResponse("Error: " + e.getMessage()));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new CreateGameResponse("Error: " + e.getMessage()));
            }
        });
    }

    private void joinGameService(GameService gameService) {
        handlers.put("joinGame", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                JoinGameRequest requestBody = gson.fromJson(req.body(), JoinGameRequest.class);
                String playerColor = requestBody.playerColor();
                int gameId = requestBody.gameID();
                gameService.joinGame(authToken, playerColor, gameId);
                res.status(200);
                return gson.toJson(new GenericResponse());
            } catch (BadRequestException e) {
                res.status(400);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            } catch (UnauthorizedException e) {
                res.status(401);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            } catch (AlreadyTakenException e) {
                res.status(403);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            } catch (DataAccessException e) {
                res.status(500);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            }
        });
    }

    public HandlerRegistry(ClearService clearService, GameService gameService, UserService userService) {
        clearHandler(clearService);
        registerHandler(userService);
        loginHandler(userService);
        logoutHandler(userService);
        listGamesHandler(gameService);
        createGameHandler(gameService);
        joinGameService(gameService);
    }

    public JsonHandler getHandler(String action) {
        return handlers.get(action);
    }
}