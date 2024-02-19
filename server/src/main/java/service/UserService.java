package service;

import dataAccess.MemoryDAO.MemoryAuthDAO;
import dataAccess.MemoryDAO.MemoryUserDAO;
import dataAccess.*;
import model.*;
import server.responseModels.*;

public class UserService extends Service {
    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();

    public HTTPResponse register(String username, String password, String email) throws Exception {
        UserData user = userDAO.createUser(username, password, email);
        AuthData auth = authDAO.createAuth(user.username(), user.password());
        return new RegisterResponse(auth.username(), auth.authToken());
    }
    public HTTPResponse login(String username, String password) throws Exception {
        UserData user = userDAO.getUser(username);
        if (user == null || !password.equals(user.password())) {
            throw new UnauthorizedAccessException("Error: unauthorized");
        }
        AuthData auth = authDAO.createAuth(username, password);
        return new LoginResponse(auth.username(), auth.authToken());
    }
    public void logout(String authToken) throws Exception {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedAccessException("Error: unauthorized");
        }
        authDAO.deleteAuth(authToken);
    }
}
