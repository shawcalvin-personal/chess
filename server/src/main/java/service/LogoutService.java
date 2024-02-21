package service;

import server.responseModels.FailureResponse;
import server.responseModels.FailureType;
import server.responseModels.LogoutResponse;
import server.responseModels.ServiceResponse;

public class LogoutService extends Service {
    public ServiceResponse logout(String authToken) {
        if (authToken == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        if (!isValidAuthToken(authToken)) {
            return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
        }
        try {
            authDAO.deleteAuth(authToken);
            return new LogoutResponse();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }

    }
}
