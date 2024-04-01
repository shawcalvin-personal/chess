package webSocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand request = new Gson().fromJson(message, UserGameCommand.class);
        switch (request.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(request, session);
            case JOIN_OBSERVER -> joinObserver();
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void joinPlayer(UserGameCommand request, Session session) throws IOException {
        connectionManager.add(request.getAuthString(), session);
        ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.notificationMessage = String.format("%s is in the shop", request.getAuthString());
        connectionManager.broadcast(null, notification);
    }

    private void joinObserver() {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}