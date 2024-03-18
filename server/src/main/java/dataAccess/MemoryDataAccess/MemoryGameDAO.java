package dataAccess.MemoryDataAccess;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.chessModels.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private static Map<Integer, GameData> gameData = new HashMap<>();
    private static int lastGameID = 1;

    @Override
    public GameData create(String gameName) {
        int gameID = generateNewGameID();
        GameData game = new GameData(gameID, null, null, new ArrayList<>(), gameName, new ChessGame());
        gameData.put(gameID, game);
        return game;
    }
    @Override
    public GameData get(int gameID) {
        return gameData.get(gameID);
    }

    @Override
    public Collection<GameData> list() {
        return gameData.values();
    }

    @Override
    public void update(int gameID, GameData game) throws DataAccessException {
        if (get(gameID) == null) {
            throw new DataAccessException("Invalid Game ID: " + gameID + " - Attempted to update a game that does not exist.");
        }
        delete(gameID);
        gameData.put(gameID, game);
    }

    @Override
    public void delete(int gameID) throws DataAccessException {
        if (get(gameID) == null) {
            throw new DataAccessException("Invalid Game ID: " + gameID + " - Attempted to delete a game that does not exist.");
        }
        gameData.remove(gameID);
    }

    @Override
    public void clear() {
        gameData.clear();
    }

    int generateNewGameID() {
        return lastGameID++;
    }
}