package service;

import model.responseModels.ClearApplicationResponse;
import model.responseModels.FailureResponse;
import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;

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
