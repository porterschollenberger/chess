package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.*;
import sql.SQLAuthDAO;
import sql.SQLGameDAO;
import websocket.commands.*;
import websocket.messages.Error;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private AuthDAO authDAO = new SQLAuthDAO();
    private GameDAO gameDAO = new SQLGameDAO();
    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.println("New connection: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("Connection closed: " + session.getRemoteAddress().getAddress() + ", reason: " + reason);
        connections.remove(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        var action = new Gson().fromJson(message, Connect.class);
        if (authDAO.getAuth(action.getAuthToken()) == null) {
            String jsonString = new Gson().toJson(new Error("AuthToken is not valid"));
            session.getRemote().sendString(jsonString);
            return;
        }
        if (gameDAO.getGame(action.getGameID()) == null) {
            String jsonString = new Gson().toJson(new Error("GameID is not valid"));
            session.getRemote().sendString(jsonString);
            return;
        }
        switch (action.getCommandType()) {
            case CONNECT:
                Connect connectCommand = new Gson().fromJson(message, Connect.class);
                connect(session, connectCommand.getAuthToken(), connectCommand.getGameID());
                break;
            case LEAVE:
                Leave leaveCommand = new Gson().fromJson(message, Leave.class);
                leave(session, leaveCommand.getGameID(), getUsername(leaveCommand.getAuthToken()));
                break;
            case MAKE_MOVE:
                MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
                makeMove(session, makeMove.getAuthToken(), makeMove.getGameID(), makeMove.getMove());
                break;
            case RESIGN:
                Resign resignCommand = new Gson().fromJson(message, Resign.class);
                resign(session, resignCommand.getGameID(), getUsername(resignCommand.getAuthToken()));
                break;
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {

    }

    private void connect(Session session, String authToken, Integer gameID) throws IOException {
        connections.add(gameID, session);
        String message;
        String username = getUsername(authToken);
        String color = getColor(gameID, username);
        if (color.equals("observer")) {
            message = String.format("%s is watching the game as an %s", username, color);
        } else {
            message = String.format("%s joined the game as %s", username, color);
        }
        var notification = new Notification(message);
        var loadGame = new LoadGame(getGame(gameID));
        connections.broadcastRoot(gameID, loadGame, session);
        connections.broadcast(gameID, notification, session);
    }

    private void leave(Session session, Integer gameID, String username) throws IOException {
        connections.remove(session);
//        gameDAO.updateGame(gameID, getColor(gameID, username) + "Username", null);
        String message = String.format("%s left the game", username);
        var notification = new Notification(message);
        connections.broadcast(gameID, notification, session);
    }

    private void resign(Session session, Integer gameID, String username) throws IOException {
        GameData game = gameDAO.getGame(gameID);
        if (!game.whiteUsername().equals(username) && !game.blackUsername().equals(username)) { // player is observer
            String jsonString = new Gson().toJson(new Error("Can't resign as an observer"));
            session.getRemote().sendString(jsonString);
            return;
        }
        if (game.game().getCompletionStatus()) {
            String jsonString = new Gson().toJson(new Error("Game is over"));
            session.getRemote().sendString(jsonString);
            return;
        }
        String message = String.format("%s resigned", username);
        Notification notification = new Notification(message);
        setGame(gameID, new ChessGame(getGame(gameID).getBoard(), true));
        connections.broadcast(gameID, notification, null);
    }

    private void makeMove(Session session, String authToken, Integer gameID, ChessMove chessMove) throws IOException {
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData.game().getCompletionStatus()) {
            String jsonString = new Gson().toJson(new Error("Can't make move because game is over"));
            session.getRemote().sendString(jsonString);
            return;
        }
        if (getColor(gameID, getUsername(authToken)).equals("observer")) {
            String jsonString = new Gson().toJson(new Error("Can't make move as observer"));
            session.getRemote().sendString(jsonString);
            return;
        }
        ChessGame.TeamColor moveColor = gameData.game().getBoard().getPiece(chessMove.getStartPosition()).getTeamColor();
        ChessGame.TeamColor playerColor;
        if (getColor(gameID, getUsername(authToken)).equals("white")) {
            playerColor = ChessGame.TeamColor.WHITE;
        } else if (getColor(gameID, getUsername(authToken)).equals("black")) {
            playerColor = ChessGame.TeamColor.BLACK;
        } else {
            throw new IllegalArgumentException();
        }
        if (!playerColor.equals(moveColor)) {
            String jsonString = new Gson().toJson(new Error("You can't move your opponents pieces"));
            session.getRemote().sendString(jsonString);
            return;
        }
        try {
            gameData.game().makeMove(chessMove);
        } catch (InvalidMoveException e) {
            String jsonString = new Gson().toJson(new Error("Invalid move"));
            session.getRemote().sendString(jsonString);
            return;
        }
        gameDAO.updateGame(gameID, gameData.game());
        LoadGame loadGame = new LoadGame(getGame(gameID));
        Notification notification = new Notification(String.format("%s made the move %s", getUsername(authToken), chessMove.toString()));
        connections.broadcast(gameID, loadGame, null);
        connections.broadcast(gameID, notification, session);
    }

    private String getUsername(String authToken) {
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null) {
            throw new RuntimeException("AuthToken was not valid");
        }
        return authData.username();
    }

    private String getColor(Integer gameID, String username) {
        GameData gameData = gameDAO.getGame(gameID);
        if (username.equals(gameData.whiteUsername())) {
            return "white";
        } else if (username.equals(gameData.blackUsername())) {
            return "black";
        } else {
            return "observer";
        }
    }

    private ChessGame getGame(Integer gameID) {
        return gameDAO.getGame(gameID).game();
    }

    private void setGame(Integer gameID, ChessGame game) {
        gameDAO.updateGame(gameID, game);
    }
}
