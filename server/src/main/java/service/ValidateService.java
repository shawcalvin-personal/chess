package service;

import chess.ChessGame;
import chess.ChessMove;
import model.chessModels.GameData;

public class ValidateService extends Service{
    public boolean isValidAuth(String authToken) {
        try {
            return isValidAuthToken(authToken);
        } catch (Exception e) {
            System.out.println("Unable to retrieve auth data from database!");
            return false;
        }
    }
    public boolean isValidGameID(int gameID) {
        try {
            return gameDAO.get(gameID) != null;
        } catch (Exception e) {
            System.out.println("Unable to retrieve game data from database!");
            return false;
        }
    }
    public boolean isValidPlayerColor(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        try {
            GameData game = gameDAO.get(gameID);
            String gameUsername = playerColor.equals(ChessGame.TeamColor.WHITE) ? game.whiteUsername() : game.blackUsername();
            String playerUsername = getUsername(authToken);
            return (playerColor.equals(ChessGame.TeamColor.WHITE) || playerColor.equals(ChessGame.TeamColor.BLACK)) && (gameUsername.equals(playerUsername));
        } catch (Exception e) {
            System.out.println("Unable to retrieve game data from database!");
            return false;
        }
    }
    public boolean isValidChessMove(int gameID, ChessMove move) {
        try {
            GameData game = gameDAO.get(gameID);
            return game.game().validMoves(move.getStartPosition()).contains(move);
        } catch (Exception e) {
            System.out.println("Unable to retrieve game data from database!");
            return false;
        }
    }

    private String getUsername(String authToken) {
        try {
            return authDAO.get(authToken).username();
        } catch (Exception e) {
            System.out.println("Unable to retrieve auth data from database!");
            return null;
        }
    }
}
