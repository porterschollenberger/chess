package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.Connect;
import websocket.commands.Leave;
import websocket.commands.Resign;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {
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
        var action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case CONNECT:
                Connect connectCommand = new Gson().fromJson(message, Connect.class);
                connect(session, connectCommand.getGameID(), connectCommand.getUsername(), connectCommand.getColor());
                break;
            case LEAVE:
                Leave leaveCommand = new Gson().fromJson(message, Leave.class);
                leave(session, leaveCommand.getGameID(), leaveCommand.getUsername());
                break;
            case MAKE_MOVE:
                break;
            case RESIGN:
                Resign resignCommand = new Gson().fromJson(message, Resign.class);
                resign(resignCommand.getGameID(), resignCommand.getUsername());
                break;
        }
    }

    private void connect(Session session, Integer gameID, String username, String color) throws IOException {
        connections.add(gameID, session);
        String message;
        if (color.equals("observer")) {
            message = String.format("%s is watching the game as an %s", username, color);
        } else {
            message = String.format("%s joined the game as %s", username, color);
        }
        var notification = new Notification(message);
        connections.broadcast(gameID, notification, session);
    }

    private void leave(Session session, Integer gameID, String username) throws IOException {
        connections.remove(session);
        String message = String.format("%s left the game", username);
        var notification = new Notification(message);
        connections.broadcast(gameID, notification, session);
    }

    private void resign(Integer gameID, String username) throws IOException {
        String message = String.format("%s resigned", username);
        var notification = new Notification(message);
        connections.broadcast(gameID, notification, null);
    }
}
