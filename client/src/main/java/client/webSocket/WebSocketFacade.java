package client.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import client.ResponseException;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ServerMessage;
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
                    System.out.println("Received a message!!");
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
    public void joinPlayer(String username, String authToken, int gameID, ChessGame.TeamColor playerColor) throws ResponseException {
    }

    public void joinObserver(String username, String authToken, int gameID) throws ResponseException {
    }

    public void makeMove(String username, String authToken, int gameID, ChessMove move) throws ResponseException {
    }

    public void resign(String username, String authToken, int gameID) throws ResponseException {
    }

    public void leave(String username, String authToken, int gameID) throws ResponseException {
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
