package service;

import server.responseModels.*;

public class UserService extends Service {
    public ServiceResponse login(String username, String password) {
        return new ServiceResponse(401, new FailureResponse("Error: unauthorized"));
//        return new ServiceResponse(200, new LoginResponse(username, "test-auth-token"));
    }
}
