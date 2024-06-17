package websocket;

import exception.ResponseException;
import websocket.messages.*;

public interface NotificationHandler {
    void notify(Notification notification);

    void load(LoadGame loadGame) throws ResponseException;
}