package webSocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String authToken, Session session) {
        var connection = new Connection(authToken, session);
        connections.put(authToken, connection);
    }

    public void remove(String userName) {
        connections.remove(userName);
    }

    public void broadcast(String excludeAuthToken, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.authToken.equals(excludeAuthToken)) {
                    c.send(notification.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        cleanConnections(removeList);
    }

    public void send(Session session, ServerMessage notification) throws IOException {
        session.getRemote().sendString(notification.toString());
    }

    private void cleanConnections(Collection<Connection> openConnections) {
        for (var c : openConnections) {
            connections.remove(c.authToken);
        }
    }
}
