package client;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import server.ServerFacade;
import ui.BoardDrawer;

import java.util.Arrays;

public class Client {
    private final ServerFacade server;
    private State state = State.LOGGEDOUT;

    public Client(int port) {
        server = new ServerFacade(port);
        server.clear();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "quit" -> "quit";
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> create(params);
                case "list" -> list();
                case "play" -> play(params);
                case "observe" -> observe(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        if (state == State.LOGGEDOUT) {
            return """
               register <USERNAME> <PASSWORD> <EMAIL> - create an account
               login <USERNAME> <PASSWORD> - login and play chess
               quit - exit the program
               help - list available commands
               """;
        } else {
            return """
               create <NAME> - create a chess game
               list - shows all games
               join <ID> [WHITE|BLACK] - join a game as specified color
               observe <ID> - watch a game being played
               logout - log out of account
               quit - exit the program
               help - list available commands
               """;
        }
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            server.register(user);
            return "You registered successfully!";
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            server.login(params[0], params[1]);
            state = State.LOGGEDIN;
            return String.format("You signed in as %s", params[0]);
        }
        throw new ResponseException(400, "Expected: <USERNAME> <PASSWORD>");
    }

    public String logout() throws ResponseException {
        assertLoggedIn();
        server.logout();
        state = State.LOGGEDOUT;
        return "You logged out";
    }

    public String create(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            server.createGame(params[0]);
            return "You created a game successfully!";
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String list() throws ResponseException {
        assertLoggedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games.getGames()) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String play(String... params) throws ResponseException {
        assertLoggedIn();
        BoardDrawer.run();
        return "";
    }

    public String observe(String... params) throws ResponseException {
        assertLoggedIn();
        BoardDrawer.run();
        return "";
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException(400, "You must log in");
        }
    }
}