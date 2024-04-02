package client.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import client.ResponseException;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
import model.chessModels.AuthData;
import webSocketMessages.userCommands.UserGameCommand;

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
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void joinPlayer(AuthData auth, int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand.Builder(auth.authToken())
                .commandType(UserGameCommand.CommandType.JOIN_PLAYER)
                .username(auth.username())
                .gameID(gameID)
                .playerColor(playerColor)
                .build();
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void joinObserver(AuthData auth, int gameID) throws ResponseException {
    }

    public void makeMove(AuthData auth, int gameID, ChessMove move) throws ResponseException {
    }

    public void resign(AuthData auth, int gameID) throws ResponseException {
    }

    public void leave(AuthData auth, int gameID) throws ResponseException {
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
