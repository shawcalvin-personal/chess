package webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session, int gameID) {
        var connection = new Connection(authToken, session, gameID);
        connections.put(authToken, connection);
    }

    public void remove(String authToken) {
        connections.remove(authToken);
    }

    public void broadcast(String excludeAuthToken, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken) && c.gameID == notification.getGameID()) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        cleanConnections(removeList);
    }

    public void send(String authToken, ServerMessage notification) throws IOException {
        connections.get(authToken).send(notification.toString());
    }

    private void cleanConnections(Collection<Connection> openConnections) {
        for (var c : openConnections) {
            connections.remove(c.authToken);
        }
    }
}
