package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;
    private final String authToken;
    private String username;
    private Integer gameID;
    private ChessGame.TeamColor playerColor;
    private ChessMove move;

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public CommandType getCommandType() { return commandType; }
    public String getAuthString() { return authToken; }
    public String getUsername() { return username; }
    public Integer getGameID() { return gameID; }
    public ChessGame.TeamColor getPlayerColor() { return playerColor; }
    public ChessMove getMove() { return move; }

    public static class Builder {
        private UserGameCommand command;

        public Builder(String authToken) {
            command = new UserGameCommand(authToken);
        }
        public Builder commandType(CommandType commandType) {
            command.commandType = commandType;
            return this;
        }
        public Builder username(String username) {
            command.username = username;
            return this;
        }
        public Builder gameID(Integer gameID) {
            command.gameID = gameID;
            return this;
        }
        public Builder playerColor(ChessGame.TeamColor playerColor) {
            command.playerColor = playerColor;
            return this;
        }
        public Builder move(ChessMove move) {
            command.move = move;
            return this;
        }
        public UserGameCommand build() {
            return command;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}


