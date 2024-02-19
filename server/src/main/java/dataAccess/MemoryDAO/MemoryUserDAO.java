package dataAccess.MemoryDAO;

import dataAccess.UserDAO;
import dataAccess.DataAccessException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private static Map<String, UserData> userData = new HashMap<>();
    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        if (getUser(username) != null) {
            throw new DataAccessException("Error: already taken");
        }
        UserData userRecord = new UserData(username, password, email);
        userData.put(username, userRecord);
        return userRecord;
    }

    @Override
    public UserData getUser(String username) {
        return userData.get(username);
    }

    @Override
    public void deleteUser(String username) {
        userData.remove(username);
    }

    @Override
    public void clearUsers() {
        userData.clear();
    }
}
