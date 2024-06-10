package client;

import exception.ResponseException;
import model.UserData;
import facade.ServerFacade;
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
                case "join" -> join(params);
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
        assertNotLoggedIn();
        if (params.length == 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            server.register(user);
            server.login(params[0], params[1]);
            state = State.LOGGEDIN;
            return "You registered successfully! You are now logged in.";
        }
        throw new ResponseException("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        assertNotLoggedIn();
        if (params.length == 2) {
            server.login(params[0], params[1]);
            state = State.LOGGEDIN;
            return String.format("You signed in as %s", params[0]);
        }
        throw new ResponseException("Expected: <USERNAME> <PASSWORD>");
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
        throw new ResponseException("Expected: <NAME>");
    }

    public String list() throws ResponseException {
        assertLoggedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        for (var game : games.getGames()) {
            result.append(game.toString()).append('\n');
        }
        return result.toString();
    }

    public String join(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 2) {
            BoardDrawer.run();
            return "";
        }
        throw new ResponseException("Expected: <ID> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            BoardDrawer.run();
            return "";
        }
        throw new ResponseException("Expected: <ID>");
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == State.LOGGEDOUT) {
            throw new ResponseException("You must log in");
        }
    }

    private void assertNotLoggedIn() throws ResponseException {
        if (state == State.LOGGEDIN) {
            throw new ResponseException("You are already logged in");
        }
    }
}