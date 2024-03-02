package dataAccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    /*
    Creates a new game with the associated gameName
    Generates a valid gameID and populates the rest with null values
    Returns the created game object
     */
    GameData create(String gameName);

    /*
    Creates a new game using a passed game object
    Throws a DataAccessException if the game already exists
    Returns the created game object
     */
    GameData create(GameData game) throws DataAccessException;

    /*
    Retrieves a game with a given gameID
    Returns null if no game exists with the given gameID
     */
    GameData get(int gameID);

    /*
    Retrieves all games from the database
    Returns an empty list if the database is empty
     */
    Collection<GameData> list();

    /*
    Updates a game using the values of a passed GameData object
    Throws an DataAccessException if the target game does not exist
     */
    void update(int gameID, GameData game) throws DataAccessException;

    /*
    Deletes a game with an associated gameID
    Throws an DataAccessException if the target game does not exist
     */
    void delete(int gameID) throws DataAccessException;

    /*
    Clears all games from the database
     */
    void clear();
}
