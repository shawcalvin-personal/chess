package client.webSocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessSerializer;
import client.ClientNotificationHandler;
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
    ClientNotificationHandler notificationHandler;
    public WebSocketFacade(String url, ClientNotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    try {
                        ServerMessage notification = ChessSerializer.createSerializer().fromJson(message, ServerMessage.class);
                        notificationHandler.notify(notification);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
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
        try {
            UserGameCommand userGameCommand = new UserGameCommand.Builder(auth.authToken())
                    .commandType(UserGameCommand.CommandType.JOIN_OBSERVER)
                    .username(auth.username())
                    .gameID(gameID)
                    .build();
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(AuthData auth, int gameID, ChessMove move) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand.Builder(auth.authToken())
                    .commandType(UserGameCommand.CommandType.MAKE_MOVE)
                    .username(auth.username())
                    .gameID(gameID)
                    .move(move)
                    .build();
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(AuthData auth, int gameID) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand.Builder(auth.authToken())
                    .commandType(UserGameCommand.CommandType.RESIGN)
                    .username(auth.username())
                    .gameID(gameID)
                    .build();
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(AuthData auth, int gameID) throws ResponseException {
        try {
            UserGameCommand userGameCommand = new UserGameCommand.Builder(auth.authToken())
                    .commandType(UserGameCommand.CommandType.LEAVE)
                    .username(auth.username())
                    .gameID(gameID)
                    .build();
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
