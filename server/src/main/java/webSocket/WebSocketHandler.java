package webSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ChessService;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connectionManager = new ConnectionManager();
    private final ChessService service = new ChessService();

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
        try {
            validateUserCommand(request);

            connectionManager.add(request.getAuthString(), session);
            ServerMessage rootNotification = new ServerMessage.Builder(ServerMessage.ServerMessageType.LOAD_GAME)
                    .game(new ChessGame())
                    .build();
            ServerMessage broadcastNotification = new ServerMessage.Builder(ServerMessage.ServerMessageType.NOTIFICATION)
                    .message(String.format("%s joined the game.", request.getUsername()))
                    .build();
            connectionManager.send(session, rootNotification);
            connectionManager.broadcast(request.getAuthString(), broadcastNotification);
        } catch (InvalidUserCommandException e) {
            System.out.println(e.getMessage());
            ServerMessage errorNotification = new ServerMessage.Builder(ServerMessage.ServerMessageType.ERROR)
                    .errorMessage(e.getMessage())
                    .build();
            connectionManager.send(session, errorNotification);
        }

    }

    private void joinObserver() {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }

    private boolean validateUserCommand(UserGameCommand command) throws InvalidUserCommandException {
        String authToken = command.getAuthString();
        if (authToken != null && !service.isValidAuth(authToken)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid auth token!");
        }
        Integer gameID = command.getGameID();
        if (gameID != null && !service.isValidGameID(gameID)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid game ID!");
        }
        ChessGame.TeamColor playerColor = command.getPlayerColor();
        if (playerColor != null && gameID != null && !service.isValidPlayerColor(gameID, playerColor, authToken)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid player color!");
        }
        ChessMove move = command.getMove();
        if (move != null && gameID != null && !service.isValidChessMove(gameID, move)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid chess move!");
        }
        return true;
    }
}