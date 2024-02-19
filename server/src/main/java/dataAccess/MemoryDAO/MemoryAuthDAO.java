package dataAccess.MemoryDAO;

import dataAccess.AuthDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private static Map<String, AuthData> authData = new HashMap<>();;

    @Override
    public AuthData createAuth(String username, String password) {
        String authToken = UUID.randomUUID().toString();
        AuthData authRecord = new AuthData(username, authToken);
        authData.put(authToken, authRecord);
        return authRecord;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authData.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authData.remove(authToken);
    }

    @Override
    public void clearAuth() {
        authData.clear();
    }
}
