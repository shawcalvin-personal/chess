package dataAccess.MemoryDAO;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private static Map<Integer, GameData> gameData = new HashMap<>();
    private static int lastGameID = 1;

    @Override
    public GameData createGame(String gameName) {
        int gameID = generateNewGameID();
        GameData game = new GameData(gameID, null, null, new ArrayList<>(), gameName, new ChessGame());
        gameData.put(gameID, game);
        return game;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        if (getGame(game.gameID()) != null) {
            throw new DataAccessException("Invalid Game ID: " + game.gameID() + " - Attempted to create a game that already exists.");
        }
        gameData.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameData.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameData.values();
    }

    @Override
    public void updateGame(int gameID, GameData game) throws DataAccessException {
        if (getGame(gameID) == null) {
            throw new DataAccessException("Invalid Game ID: " + gameID + " - Attempted to update a game that does not exist.");
        }
        deleteGame(gameID);
        gameData.put(gameID, game);
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        if (getGame(gameID) == null) {
            throw new DataAccessException("Invalid Game ID: " + gameID + " - Attempted to delete a game that does not exist.");
        }
        gameData.remove(gameID);
    }

    @Override
    public void clearGames() {
        gameData.clear();
    }

    int generateNewGameID() {
        return lastGameID++;
    }
}
