package service;

import dataAccess.*;
import dataAccess.MemoryDAO.*;

public class ClearService extends Service {
    public void clearDatabase() {
        userDAO.clearUsers();
        authDAO.clearAuth();
        gameDAO.clearGames();
    }
}
