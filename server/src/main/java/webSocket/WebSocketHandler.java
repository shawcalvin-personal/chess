package webSocket;

import chess.ChessGame;
import com.google.gson.Gson;
import model.chessModels.GameData;
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
        connectionManager.add(request.getAuthString(), session, request.getGameID());
        try {
            service.validateUserGameCommand(request);
            switch (request.getCommandType()) {
                case JOIN_PLAYER -> joinGame(request, session, String.format("%s joined the game as the %s player.", request.getUsername(), request.getPlayerColor().toString().toLowerCase()));
                case JOIN_OBSERVER -> joinGame(request, session, String.format("%s joined the game as an observer.", request.getUsername()));
                case MAKE_MOVE -> makeMove(request, session);
                case LEAVE -> leave(request, session);
                case RESIGN -> resign(request, session);
            }
        } catch (InvalidUserCommandException e) {
            System.out.println(e.getMessage());
            sendErrorNotification(request.getAuthString(), request.getGameID(), e.getMessage());
        }
    }

    private void joinGame(UserGameCommand request, Session session, String message) throws IOException {
        try {
            ServerMessage loadGameMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.LOAD_GAME)
                    .game(service.getGame(request.getGameID()))
                    .gameID(request.getGameID())
                    .build();
            ServerMessage notificationMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.NOTIFICATION)
                    .message(message)
                    .gameID(request.getGameID())
                    .build();
            connectionManager.send(request.getAuthString(), loadGameMessage);
            connectionManager.broadcast(request.getAuthString(), notificationMessage);
        } catch (InvalidUserCommandException e) {
            System.out.println(e.getMessage());
            sendErrorNotification(request.getAuthString(), request.getGameID(), e.getMessage());
        }
    }

    private void makeMove(UserGameCommand request, Session session) throws IOException {
        try {
            GameData gameData = service.makeMove(request.getGameID(), request.getMove());
            ServerMessage loadGameMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.LOAD_GAME)
                    .game(gameData.game())
                    .gameID(request.getGameID())
                    .build();
            ServerMessage notificationMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.NOTIFICATION)
                    .message(getMakeMoveMessage(request, gameData))
                    .gameID(request.getGameID())
                    .build();
            connectionManager.broadcast(null, loadGameMessage);
            connectionManager.broadcast(request.getAuthString(), notificationMessage);
        } catch (InvalidUserCommandException e) {
            System.out.println(e.getMessage());
            sendErrorNotification(request.getAuthString(), request.getGameID(), e.getMessage());
        }
    }

    private void leave(UserGameCommand request, Session session) throws IOException {
        try {
            service.leave(request.getGameID(), request.getAuthString());
            ServerMessage notificationMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.NOTIFICATION)
                    .message(String.format("%s left the game.", request.getUsername()))
                    .gameID(request.getGameID())
                    .build();
            connectionManager.broadcast(request.getAuthString(), notificationMessage);
            connectionManager.remove(request.getAuthString());
        } catch (InvalidUserCommandException e) {
            System.out.println(e.getMessage());
            sendErrorNotification(request.getAuthString(), request.getGameID(), e.getMessage());
        }
    }

    private void resign(UserGameCommand request, Session session) throws IOException {
        try {
            service.resign(request.getGameID(), request.getAuthString());
            ServerMessage notificationMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.NOTIFICATION)
                    .message(String.format("%s resigned from the game.", request.getUsername()))
                    .gameID(request.getGameID())
                    .build();
            connectionManager.broadcast(null, notificationMessage);
            connectionManager.remove(request.getAuthString());
        } catch (InvalidUserCommandException e) {
            System.out.println(e.getMessage());
            sendErrorNotification(request.getAuthString(), request.getGameID(), e.getMessage());
        }
    }

    private String getMakeMoveMessage(UserGameCommand request, GameData gameData) {
        ChessGame game = gameData.game();
        String message = String.format("%s made a move: %s", request.getUsername(), request.getMove().toString());
        if (game.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            message += String.format("\n%s is now in check! %s has won the game!", gameData.whiteUsername(), gameData.blackUsername());
        } else if (game.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            message += String.format("\n%s is in checkmate! %s has won the game!", gameData.blackUsername(), gameData.whiteUsername());
        } else if (game.isInCheck(ChessGame.TeamColor.WHITE)) {
            message += String.format("\n%s is now in check!", gameData.whiteUsername());
        } else if (game.isInCheck(ChessGame.TeamColor.BLACK)) {
            message += String.format("\n%s is now in check!", gameData.blackUsername());
        }
        return message;
    }

    private void sendErrorNotification(String authToken, int gameID, String message) throws IOException {
        ServerMessage errorMessage = new ServerMessage.Builder(ServerMessage.ServerMessageType.ERROR)
                .errorMessage(message)
                .gameID(gameID)
                .build();
        connectionManager.send(authToken, errorMessage);
    }
}