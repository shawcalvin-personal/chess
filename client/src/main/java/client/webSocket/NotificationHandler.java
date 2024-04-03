package client.webSocket;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface NotificationHandler {
    public ChessGame.TeamColor getPlayerColor();
    public void setPlayerColor(ChessGame.TeamColor playerColor);
    void notify(ServerMessage notification);
}
