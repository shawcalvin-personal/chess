package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    /*
    Creates a new game with the associated gameName
    Generates a valid gameID and populates the rest with null values
    Returns the created game object
     */
    GameData createGame(String gameName);

    /*
    Creates a new game using a passed game object
    Throws a DuplicateDatabaseObjectException if the game already exists
    Returns the created game object
     */
    GameData createGame(GameData game) throws DataAccessException;

    /*
    Retrieves a game with a given gameID
    Returns null if no game exists with the given gameID
     */
    GameData getGame(int gameID);

    /*
    Retrieves all games from the database
    Returns an empty list if the database is empty
     */
    Collection<GameData> listGames();

    /*
    Updates a game using the values of a passed GameData object
    Throws an InvalidDatabaseTargetException if the target game does not exist
     */
    void updateGame(int gameID, GameData game) throws DataAccessException;

    /*
    Deletes a game with an associated gameID
    Throws an InvalidDatabaseTargetException if the target game does not exist
     */
    void deleteGame(int gameID) throws DataAccessException;

    /*
    Clears all games from the database
     */
    void clearGames();
}
