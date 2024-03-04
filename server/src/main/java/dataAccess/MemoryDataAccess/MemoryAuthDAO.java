package dataAccess.MemoryDataAccess;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static Map<String, AuthData> authData = new HashMap<>();

    @Override
    public AuthData create(String username, String password) {
        String authToken = UUID.randomUUID().toString();
        AuthData authRecord = new AuthData(username, authToken);
        authData.put(authToken, authRecord);
        return authRecord;
    }

    @Override
    public AuthData get(String authToken) {
        return authData.get(authToken);
    }

    @Override
    public void delete(String authToken) throws DataAccessException {
        if (get(authToken) == null) {
            throw new DataAccessException("Invalid auth-token: " + authToken + " - Attempted to delete an auth token that does not exist.");
        }
        authData.remove(authToken);
    }

    @Override
    public void clear() {
        authData.clear();
    }
}