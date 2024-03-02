package service;

import server.responseModels.ClearApplicationResponse;

public class ClearService extends Service {
    public ClearApplicationResponse clearDatabase() {
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
        return new ClearApplicationResponse();
    }
}
