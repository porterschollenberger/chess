package websocket;

import exception.ResponseException;
import websocket.messages.*;
import websocket.messages.Error;

public interface NotificationHandler {
    void notify(Notification notification);

    void load(LoadGame loadGame) throws ResponseException;

    void report(Error error);
}