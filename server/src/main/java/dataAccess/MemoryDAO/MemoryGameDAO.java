package dataAccess.MemoryDAO;

import dataAccess.GameDAO;
import model.GameData;

public class MemoryGameDAO implements GameDAO {
    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public GameData listGames() {
        return null;
    }

    @Override
    public void updateGame(int gameID, String clientUsername, String clientColor) {

    }

    @Override
    public void deleteGame(int gameID) {

    }

    @Override
    public void clearGames() {

    }
}
