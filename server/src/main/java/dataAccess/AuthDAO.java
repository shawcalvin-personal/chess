package dataAccess;

import model.*;

public interface AuthDAO {

    /*
    Creates an auth token for a user given a username and password
    Returns the associated AuthData object
     */
    AuthData create(String username, String password);

    /*
    Retrieves an AuthData object given an auth token
    Returns retrieved AuthData object, or null if it does not exist
     */
    AuthData get(String authToken);

    /*
    Deletes a given auth token
    Throws an InvalidDatabaseTargetException if the passed token does not exist
     */
    void delete(String authToken) throws DataAccessException;

    /*
    Clears all auth data from the database
     */
    void clear();

}
