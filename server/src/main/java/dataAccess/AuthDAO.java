package dataAccess;

import model.*;

public interface AuthDAO {
    AuthData createAuth(String username);
    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    void clearAuth();
}
