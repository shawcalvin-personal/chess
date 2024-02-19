package dataAccess;

import model.*;

public interface AuthDAO {
    AuthData createAuth(String username, String password);

    AuthData getAuth(String authToken);
    void deleteAuth(String authToken);
    void clearAuth();
}
