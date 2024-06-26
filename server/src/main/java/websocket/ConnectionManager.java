package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private final ConcurrentHashMap<Integer, List<Session>> gameSessions = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        gameSessions.computeIfAbsent(gameID, k -> new ArrayList<>()).add(session);
    }

    public void remove(Session session) {
        gameSessions.forEach((gameID, sessions) -> sessions.removeIf(s -> s.equals(session)));
    }

    public void broadcast(Integer gameID, ServerMessage serverMessage, Session excludeSession) throws IOException {
        List<Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            var removeList = new ArrayList<Session>();
            for (Session session : sessions) {
                if (session.isOpen()) {
                    if (!session.equals(excludeSession)) {
                        String jsonString=new Gson().toJson(serverMessage);
                        session.getRemote().sendString(jsonString);
                    }
                } else {
                    removeList.add(session);
                }
            }
            sessions.removeAll(removeList);
        }
    }

    public void broadcastRoot(Integer gameID, ServerMessage serverMessage, Session uniqueSession) throws IOException {
        List<Session> sessions = gameSessions.get(gameID);
        if (sessions != null) {
            var removeList = new ArrayList<Session>();
            for (Session session : sessions) {
                if (session.isOpen()) {
                    if (session.equals(uniqueSession)) {
                        String jsonString=new Gson().toJson(serverMessage);
                        session.getRemote().sendString(jsonString);
                    }
                } else {
                    removeList.add(session);
                }
            }
            sessions.removeAll(removeList);
        }
    }

}
