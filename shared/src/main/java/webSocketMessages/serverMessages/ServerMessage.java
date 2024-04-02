package webSocketMessages.serverMessages;

import chess.ChessGame;
import com.google.gson.Gson;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
    public String message;
    public ChessGame game;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }
    public String getMessage() { return message; }
    public ChessGame getGame() { return game; }

    public static class Builder {
        private ServerMessage serverMessage;

        public Builder(ServerMessageType type) {
            serverMessage = new ServerMessage(type);
        }
        public Builder message(String message) {
            serverMessage.message = message;
            return this;
        }
        public Builder game(ChessGame game) {
            serverMessage.game = game;
            return this;
        }
        public ServerMessage build() {
            // Here you could validate the ServerMessage object before returning it
            return serverMessage;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServerMessage))
            return false;
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
