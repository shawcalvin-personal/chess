package dataAccess;

import model.UserData;

public interface UserDAO {
    UserData createUser(String username, String password, String email);
    UserData getUser(String username);
    void deleteUser(String username);
    void clearUsers();
}
