package client;

import exception.ResponseException;
import model.UserData;
import facade.ServerFacade;
import response.LoginResponse;
import response.RegisterResponse;
import ui.BoardDrawer;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import websocket.messages.Notification;


import java.util.Arrays;

public class Client {
    private final ServerFacade server;
    private State state = State.LOGGEDOUT;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private final ClientInfo clientInfo = new ClientInfo(null, null, null, null);

    public Client(int port, NotificationHandler notificationHandler) {
        server = new ServerFacade(port);
        this.notificationHandler = notificationHandler;
        server.clear(); // This should be removed later when testing is complete.
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
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
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
        } else if (state == State.LOGGEDIN) {
            return """
               create <NAME> - create a chess game
               list - shows all games
               join <ID> [WHITE|BLACK] - join a game as specified color
               observe <ID> - watch a game being played
               logout - log out of account
               quit - exit the program
               help - list available commands
               """;
        } else if (state == State.PLAYING) {
            return """
                   redraw - redraw the chessboard on your screen
                   move <startSquare> <endSquare> - move a chess piece (ex. move e2 e4)
                   leave - leave the chess game
                   resign - forfeit the match and end the game
                   highlight <square> - shows the legal moves for the piece at the specified square
                   help - list available commands
                   """;
        } else {
            throw new RuntimeException("Invalid state");
        }
    }

    public String register(String... params) throws ResponseException {
        assertLoggedOut();
        if (params.length == 3) {
            UserData user = new UserData(params[0], params[1], params[2]);
            RegisterResponse response = server.register(user);
            server.login(params[0], params[1]);
            state = State.LOGGEDIN;
            clientInfo.setAuthToken(response.getAuthToken());
            clientInfo.setUsername(response.getUsername());
            ws = new WebSocketFacade("http://localhost:8080", notificationHandler);
            return "You registered successfully! You are now logged in.";
        }
        throw new ResponseException("Expected: <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String... params) throws ResponseException {
        assertLoggedOut();
        if (params.length == 2) {
            LoginResponse response = server.login(params[0], params[1]);
            state = State.LOGGEDIN;
            clientInfo.setAuthToken(response.getAuthToken());
            clientInfo.setUsername(response.getUsername());
            ws = new WebSocketFacade("http://localhost:8080", notificationHandler);
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
            server.joinGame(params[1], Integer.parseInt(params[0]));
            if (params[1].equalsIgnoreCase("WHITE")) {
                BoardDrawer.drawWhiteBoard();
            } else {
                BoardDrawer.drawBlackBoard();
            }
            clientInfo.setPlayerColor(params[1].toLowerCase());
            clientInfo.setGameID(Integer.valueOf(params[0]));
            state = State.PLAYING;
            ws.connect(clientInfo.getAuthToken(), clientInfo.getGameID(), clientInfo.getUsername(), clientInfo.getPlayerColor());
            return "";
        }
        throw new ResponseException("Expected: <ID> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            clientInfo.setPlayerColor("observer");
            clientInfo.setGameID(Integer.valueOf(params[0]));
            state = State.PLAYING;
            ws.connect(clientInfo.getAuthToken(), clientInfo.getGameID(), clientInfo.getUsername(), clientInfo.getPlayerColor());
            BoardDrawer.drawWhiteBoard();
            return "";
        }
        throw new ResponseException("Expected: <ID>");
    }

    private String redraw() {
        return "";
    }

    public String leave() throws ResponseException {
        assertPlaying();
        server.leaveGame(clientInfo.getPlayerColor(), clientInfo.getGameID());
        ws.leave(clientInfo.getAuthToken(), clientInfo.getGameID(), clientInfo.getUsername());
        clientInfo.setPlayerColor(null);
        clientInfo.setGameID(null);
        state = State.LOGGEDIN;

        return "";
    }

    public String move(String... params) {
        // parse the move string to be a move
        return "";
    }

    public String resign() {
        return "";
    }

    private void assertLoggedIn() throws ResponseException {
        if (state != State.LOGGEDIN) {
            throw new ResponseException("You can't do that now. Type \"help\" to see the available commands");
        }
    }

    private void assertLoggedOut() throws ResponseException {
        if (state != State.LOGGEDOUT) {
            throw new ResponseException("You can't do that now. Type \"help\" to see the available commands");
        }
    }

    private void assertPlaying() throws ResponseException {
        if (state != State.PLAYING) {
            throw new ResponseException("You can't do that now. Type \"help\" to see the available commands");
        }
    }
}