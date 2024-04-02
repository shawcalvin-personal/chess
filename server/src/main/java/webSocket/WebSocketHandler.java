package webSocket;

import chess.ChessGame;
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
        // test if player spot is open

        // if not open, send ERROR

        // if open, join game as specified color and broadcast changes
        connectionManager.add(request.getAuthString(), session);
        ServerMessage rootNotification = new ServerMessage.Builder(ServerMessage.ServerMessageType.LOAD_GAME)
            .game(new ChessGame())
            .build();
        ServerMessage broadcastNotification = new ServerMessage.Builder(ServerMessage.ServerMessageType.NOTIFICATION)
            .message(String.format("%s joined the game.", request.getUsername()))
            .build();
        connectionManager.send(session, rootNotification);
        connectionManager.broadcast(request.getAuthString(), broadcastNotification);
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