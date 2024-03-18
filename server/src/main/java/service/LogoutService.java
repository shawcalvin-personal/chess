package service;

import model.responseModels.LogoutResponse;
import model.responseModels.FailureResponse;
import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;

public class LogoutService extends Service {
    public ServiceResponse logout(String authToken) {
        if (authToken == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        try {
            if (!isValidAuthToken(authToken)) {
                return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
            }
            authDAO.delete(authToken);
            return new LogoutResponse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }

    }
}
