package service;

import dataAccess.*;
import dataAccess.MemoryDAO.*;
import server.responseModels.ClearApplicationResponse;

public class ClearService extends Service {
    public ClearApplicationResponse clearDatabase() {
        userDAO.clearUsers();
        authDAO.clearAuth();
        gameDAO.clearGames();
        return new ClearApplicationResponse();
    }
}
