package service;

import dataAccess.*;
import dataAccess.MemoryDAO.*;
import model.AuthData;
import model.UserData;

public class Service {
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    public static AuthDAO authDAO;
    public static String badRequestMessage;
    public static String unauthorizedAccessMessage;
    public static String forbiddenResourceMessage;

    Service() {
        init();
    }
    public void init() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();

        badRequestMessage = "Error: bad request";
        unauthorizedAccessMessage = "Error: unauthorized";
        forbiddenResourceMessage = "Error: already taken";
    }

    public boolean isValidAuthToken(String authToken) {
        AuthData auth = authDAO.getAuth(authToken);
        return !(auth == null);
    }

    public boolean isValidUsernamePassword(String username, String password) {
        UserData user = userDAO.getUser(username);
        return user != null && password.equals(user.password());
    }

    public String getServerErrorMessage(Exception e) {
        return "Error: " + e.getMessage();
    }
}
