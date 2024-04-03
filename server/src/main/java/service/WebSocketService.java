package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataAccess.DataAccessException;
import model.chessModels.GameData;
import webSocket.InvalidUserCommandException;
import webSocketMessages.userCommands.UserGameCommand;

public class WebSocketService extends Service{
    public ChessGame makeMove(int gameID, ChessMove move) throws InvalidUserCommandException {
        try {
            GameData gameData = gameDAO.get(gameID);
            ChessGame game = gameData.game();
            game.makeMove(move);
            gameDAO.update(gameID, gameData);
            return game;
        } catch (DataAccessException e) {
            throw new InvalidUserCommandException("Error trying to make move: unable to retrieve game from database!");
        } catch (InvalidMoveException e) {
            throw new InvalidUserCommandException("Error trying to make move: invalid chess move!");
        }
    }

    public void resign(int gameID, String authToken) throws InvalidUserCommandException {
        try {
            String username = getUsername(authToken);
            if (username == null) {
                throw new InvalidUserCommandException("Error trying to resign game: username not found!");
            }
            GameData gameData = gameDAO.get(gameID);
            ChessGame game = gameData.game();
            if (game.getIsComplete()) {
                throw new InvalidUserCommandException("Error trying to resign game: game is already over!");
            }
            game.setIsComplete(true);
            GameData updatedGameData = resignRemoveUsername(gameID, gameData, username);
            gameDAO.update(gameID, updatedGameData);
        } catch (DataAccessException e) {
            throw new InvalidUserCommandException("Error trying to resign game: unable to retrieve game from database!");
        }
    }

    public void leave(int gameID, String authToken) throws InvalidUserCommandException {
        try {
            String username = getUsername(authToken);
            if (username == null) {
                throw new InvalidUserCommandException("Error trying to leave game: username not found!");
            }
            GameData gameData = gameDAO.get(gameID);
            GameData updatedGameData = leaveRemoveUsername(gameID, gameData, username);
            gameDAO.update(gameID, updatedGameData);
        } catch (DataAccessException e) {
            throw new InvalidUserCommandException("Error trying to leave game: unable to retrieve game from database!");
        }
    }

    private static GameData resignRemoveUsername(int gameID, GameData gameData, String username) throws InvalidUserCommandException {
        if (username.equals(gameData.whiteUsername())) {
            return new GameData(gameID, null, gameData.blackUsername(), gameData.observerUsernames(), gameData.gameName(), gameData.game());
        } else if (username.equals(gameData.blackUsername())) {
            return new GameData(gameID, gameData.whiteUsername(), null, gameData.observerUsernames(), gameData.gameName(), gameData.game());
        }
        throw new InvalidUserCommandException("Error trying to resign game: user is not a valid player!");
    }

    private static GameData leaveRemoveUsername(int gameID, GameData gameData, String username) throws InvalidUserCommandException {
        if (username.equals(gameData.whiteUsername())) {
            return new GameData(gameID, null, gameData.blackUsername(), gameData.observerUsernames(), gameData.gameName(), gameData.game());
        } else if (username.equals(gameData.blackUsername())) {
            return new GameData(gameID, gameData.whiteUsername(), null, gameData.observerUsernames(), gameData.gameName(), gameData.game());
        } else if (gameData.observerUsernames().contains(username)) {
            gameData.observerUsernames().remove(username);
            return gameData;
        }
        throw new InvalidUserCommandException("Error trying to leave game: user is not a player or an observer!");
    }

    public void validateUserGameCommand(UserGameCommand command) throws InvalidUserCommandException {
        String authToken = command.getAuthString();
        if (authToken != null && !isValidAuth(authToken)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid auth token!");
        }
        Integer gameID = command.getGameID();
        if (gameID != null && !isValidGameID(gameID)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid game ID!");
        }
        ChessGame.TeamColor playerColor = getPlayerColor(command);
        if (playerColor != null && gameID != null && !isValidPlayerColor(gameID, playerColor, authToken)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid player color!");
        }
        ChessMove move = command.getMove();
        if (move != null && gameID != null && !isValidChessMove(gameID, playerColor, move)) {
            throw new InvalidUserCommandException("Error parsing user command: invalid chess move!");
        }
    }

    private boolean isValidAuth(String authToken) {
        try {
            return isValidAuthToken(authToken);
        } catch (DataAccessException e) {
            System.out.println("Unable to retrieve auth data from database!");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    private boolean isValidGameID(int gameID) {
        try {
            return gameDAO.get(gameID) != null;
        } catch (DataAccessException e) {
            System.out.println("Unable to retrieve game data from database!");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    private boolean isValidPlayerColor(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        try {
            GameData game = gameDAO.get(gameID);
            String gameUsername = playerColor.equals(ChessGame.TeamColor.WHITE) ? game.whiteUsername() : game.blackUsername();
            String playerUsername = getUsername(authToken);
            return (playerColor.equals(ChessGame.TeamColor.WHITE) || playerColor.equals(ChessGame.TeamColor.BLACK)) && (gameUsername.equals(playerUsername));
        } catch (DataAccessException e) {
            System.out.println("Unable to retrieve game data from database!");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
    private boolean isValidChessMove(int gameID, ChessGame.TeamColor playerColor, ChessMove move) {
        try {
            GameData game = gameDAO.get(gameID);
            return playerColor != null && !game.game().getIsComplete() && game.game().validMoves(move.getStartPosition()).contains(move) && game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor().equals(playerColor);
        } catch (DataAccessException e) {
            System.out.println("Unable to retrieve game data from database!");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private String getUsername(String authToken) {
        try {
            return authDAO.get(authToken).username();
        } catch (DataAccessException e) {
            System.out.println("Unable to retrieve auth data from database!");
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private ChessGame.TeamColor getPlayerColor(UserGameCommand command) {
        try {
            String username = authDAO.get(command.getAuthString()).username();
            GameData game = gameDAO.get(command.getGameID());
            if (command.getPlayerColor() != null) {
                return command.getPlayerColor();
            } else if (username.equals(game.whiteUsername())) {
                return ChessGame.TeamColor.WHITE;
            } else if (username.equals(game.blackUsername())) {
                return ChessGame.TeamColor.BLACK;
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.out.println("Unable to retrieve auth data from database!");
            return null;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public ChessGame getGame(int gameID) throws InvalidUserCommandException {
        try {
            return gameDAO.get(gameID).game();
        } catch (DataAccessException e) {
            throw new InvalidUserCommandException("Error trying to make move: unable to retrieve game from database!");
        }
    }
}
