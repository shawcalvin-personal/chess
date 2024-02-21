package service;

import dataAccess.*;
import dataAccess.MemoryDAO.*;
import model.AuthData;
import model.UserData;

public class Service {
    public static UserDAO userDAO;
    public static GameDAO gameDAO;
    public static AuthDAO authDAO;
    public static final String badRequestMessage = "Error: bad request";;
    public static final String unauthorizedAccessMessage = "Error: unauthorized";;
    public static final String forbiddenResourceMessage = "Error: already taken";;
    public static final String whiteColor = "WHITE";
    public static final String blackColor = "BLACK";

    Service() {
        init();
    }
    public void init() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
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
