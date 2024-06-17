package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import exception.ResponseException;
import model.UserData;
import facade.ServerFacade;
import response.LoginResponse;
import response.RegisterResponse;
import ui.BoardDrawer;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;


import java.util.Arrays;

public class Client {
    private final ServerFacade server;
    private State state = State.LOGGEDOUT;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private final ClientInfo clientInfo = new ClientInfo(null, null, null, null);
    private ChessGame chessGame;

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
                case "redraw" -> redraw(chessGame);
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
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
                highlight <square> - show the legal moves for the piece at the specified square
                help - list available commands
                """;
        } else if (state == State.OBSERVING) {
            return """
                redraw - redraw the chessboard on your screen
                leave - leave the chess game
                highlight <square> - show the legal moves for the piece at the specified square
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
            clientInfo.setPlayerColor(params[1].toLowerCase());
            clientInfo.setGameID(Integer.valueOf(params[0]));
            state = State.PLAYING;
            ws.connect(clientInfo.getAuthToken(), clientInfo.getGameID());
            return "";
        }
        throw new ResponseException("Expected: <ID> [WHITE|BLACK]");
    }

    public String observe(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            clientInfo.setPlayerColor("observer");
            clientInfo.setGameID(Integer.valueOf(params[0]));
            state = State.OBSERVING;
            ws.connect(clientInfo.getAuthToken(), clientInfo.getGameID());
            return "";
        }
        throw new ResponseException("Expected: <ID>");
    }

    public String redraw(ChessGame chessGame) throws ResponseException {
        assertPlayingOrObserving();
        if (clientInfo.getPlayerColor().equals("white") || clientInfo.getPlayerColor().equals("observer")) {
            BoardDrawer.drawWhiteBoard(chessGame);
        } else if (clientInfo.getPlayerColor().equals("black")) {
            BoardDrawer.drawBlackBoard(chessGame);
        }
        return "";
    }

    public String redraw(ChessGame chessGame, ChessPosition checkMove) throws ResponseException {
        assertPlayingOrObserving();
        if (clientInfo.getPlayerColor().equals("white") || clientInfo.getPlayerColor().equals("observer")) {
            BoardDrawer.drawWhiteBoard(chessGame, checkMove);
        } else if (clientInfo.getPlayerColor().equals("black")) {
            BoardDrawer.drawBlackBoard(chessGame, checkMove);
        }
        return "";
    }

    public String leave() throws ResponseException {
        assertPlayingOrObserving();
        server.leaveGame(clientInfo.getPlayerColor(), clientInfo.getGameID());
        ws.leave(clientInfo.getAuthToken(), clientInfo.getGameID());
        clientInfo.setPlayerColor(null);
        clientInfo.setGameID(null);
        state = State.LOGGEDIN;

        return "";
    }

    public String move(String... params) {
        return "";
    }

    public String resign() throws ResponseException {
        assertPlaying();
        if (!chessGame.getCompletionStatus()) {
            ws.resign(clientInfo.getAuthToken(), clientInfo.getGameID());
            return "";
        } else {
            throw new RuntimeException("Game has already ended");
        }
    }

    public String highlight(String... params) throws ResponseException {
        assertPlayingOrObserving();
        ChessPosition checkPosition = parseChessPosition(params[0]);
        redraw(chessGame, checkPosition);
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

    private void assertPlayingOrObserving() throws ResponseException {
        if (state != State.PLAYING && state != State.OBSERVING) {
            throw new ResponseException("You can't do that now. Type \"help\" to see the available commands");
        }
    }

    public void setChessGame(ChessGame game) {
        this.chessGame = game;
    }

    private ChessPosition parseChessPosition(String userInput) {
        String rowLetter = userInput.substring(0,1);
        String[] rowLetters = { "a", "b", "c", "d", "e", "f", "g", "h" };
        int col = 0;
        for (int i = 0; i < 8; i++) {
            if (rowLetters[i].equals(rowLetter)) {
                col = i + 1;
            }
        }
        if (col == 0) {
            throw new RuntimeException("Invalid move syntax");
        }
        int row = Integer.parseInt(userInput.substring(1,2));
        return new ChessPosition(row, col);
    }

    private ChessMove parseChessMove(String userInput) {
        return new ChessMove(new ChessPosition(1, 1), new ChessPosition(2,2), null);
    }
}