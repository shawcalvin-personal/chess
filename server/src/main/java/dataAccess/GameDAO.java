package dataAccess;

import model.GameData;

public interface GameDAO {
    GameData createGame(String gameName);
    GameData getGame(int gameID);
    GameData listGames();
    void updateGame(int gameID, String clientUsername, String clientColor);
    void deleteGame(int gameID);
    void clearGames();
}
