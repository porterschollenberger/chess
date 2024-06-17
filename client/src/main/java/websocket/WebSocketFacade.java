package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.ResponseException;

import websocket.commands.*;
import websocket.messages.*;
import websocket.messages.Error;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        var serverMessage = new Gson().fromJson(message, ServerMessage.class);
                        switch (serverMessage.getServerMessageType()) {
                            case LOAD_GAME:
                                LoadGame loadGame = new Gson().fromJson(message, LoadGame.class);
                                notificationHandler.load(loadGame);
                                break;
                            case NOTIFICATION:
                                Notification notification = new Gson().fromJson(message, Notification.class);
                                notificationHandler.notify(notification);
                                break;
                            case ERROR:
                                Error error = new Gson().fromJson(message, Error.class);
                                notificationHandler.report(error);
                        }
                    } catch (JsonSyntaxException ex) {
                        System.out.println("Failed to deserialize message: " + ex.getMessage());
                        ex.printStackTrace();
                    } catch (ResponseException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
    }

    public void connect(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new Connect(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new Leave(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new Resign(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(ex.getMessage());
        }
    }
}