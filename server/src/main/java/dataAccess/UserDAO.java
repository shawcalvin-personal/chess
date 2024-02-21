package dataAccess;

import model.UserData;

public interface UserDAO {
    /*
    Adds a new user to the user database
    If user already exists, throws DataAccessException
    Returns created user record
     */
    UserData createUser(String username, String password, String email) throws DataAccessException;

    /*
    Retrieves a user from the user database by username
    Returns retrieved user record, or null if user does not exist
     */
    UserData getUser(String username);

    /*
    Deletes a user from the user database by username
    If user does not exist, throws DataAccessException
     */
    void deleteUser(String username) throws DataAccessException;

    /*
    Clears all user data from the user database
     */
    void clearUsers();
}
