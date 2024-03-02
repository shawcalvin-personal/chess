package service;

import dataAccess.DataAccessException;
import server.responseModels.*;
import model.*;

public class RegisterService extends Service {
    public ServiceResponse register(String username, String password, String email) {
        if (username == null || password == null || email == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        try {
            UserData user = userDAO.create(username, password, email);
            AuthData auth = authDAO.create(user.username(), user.password());
            return new RegisterResponse(auth.username(), auth.authToken());
        } catch (DataAccessException e) {
            System.out.println("DATA ACCESS: " + e.getMessage());
            return new FailureResponse(FailureType.FORBIDDEN_RESOURCE, forbiddenResourceMessage);
        } catch (Exception e) {
            System.out.println("SERVER ERROR: " + e.getMessage());
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}
