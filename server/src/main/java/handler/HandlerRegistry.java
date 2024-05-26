package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
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

    public HandlerRegistry(ClearService clearService, GameService gameService, UserService userService) {
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

        handlers.put("register", (req, res) -> {
            try {
                RegisterRequest registerRequest = gson.fromJson(req.body(), RegisterRequest.class);
                UserData user = new UserData(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail());
                AuthData authData = userService.register(user);
                res.status(200);
                return gson.toJson(new RegisterResponse(authData.username(), authData.authToken()));
            } catch (DataAccessException e) {
                res.status(400);
                return gson.toJson(new RegisterResponse("Error: " + e.getMessage()));
            }
        });

        handlers.put("login", (req, res) -> {
            try {
                LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
                AuthData authData = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
                res.status(200);
                return gson.toJson(new LoginResponse(authData.username(), authData.authToken()));
            } catch (DataAccessException e) {
                res.status(401);
                return gson.toJson(new LoginResponse("Error: " + e.getMessage()));
            }
        });

        handlers.put("logout", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                userService.logout(authToken);
                res.status(200);
                return gson.toJson(new GenericResponse());
            } catch (DataAccessException e) {
                res.status(401);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            }
        });

        handlers.put("listGames", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                Collection<GameData> games = gameService.listGames(authToken);
                res.status(200);
                return gson.toJson(new ListGamesResponse(games));
            } catch (DataAccessException e) {
                res.status(401);
                return gson.toJson(new ListGamesResponse("Error: " + e.getMessage()));
            }
        });

        handlers.put("createGame", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);
                String gameName = createGameRequest.getGameName();
                GameData game = gameService.createGame(authToken, gameName);
                res.status(200);
                return gson.toJson(new CreateGameResponse(game.gameID()));
            } catch (DataAccessException e) {
                res.status(401);
                return gson.toJson(new CreateGameResponse("Error: " + e.getMessage()));
            }
        });

        handlers.put("joinGame", (req, res) -> {
            try {
                String authToken = req.headers("authorization");
                JoinGameRequest requestBody = gson.fromJson(req.body(), JoinGameRequest.class);
                String playerColor = requestBody.getPlayerColor();
                int gameId = requestBody.getGameID();
                gameService.joinGame(authToken, playerColor, gameId);
                res.status(200);
                return gson.toJson(new GenericResponse());
            } catch (DataAccessException e) {
                res.status(401);
                return gson.toJson(new GenericResponse("Error: " + e.getMessage()));
            }
        });
    }

    public JsonHandler getHandler(String action) {
        return handlers.get(action);
    }
}