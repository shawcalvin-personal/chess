package service;

import server.responseModels.ClearApplicationResponse;
import server.responseModels.FailureResponse;
import server.responseModels.FailureType;
import server.responseModels.ServiceResponse;

public class ClearService extends Service {
    public ServiceResponse clearDatabase() {
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
            return new ClearApplicationResponse();
        } catch (Exception e) {
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}
