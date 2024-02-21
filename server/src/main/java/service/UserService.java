//package service;
//
//import dataAccess.MemoryDAO.MemoryAuthDAO;
//import dataAccess.MemoryDAO.MemoryUserDAO;
//import dataAccess.*;
//import model.*;
//import server.responseModels.*;
//import service.exceptions.BadRequestException;
//import service.exceptions.ForbiddenResourceException;
//import service.exceptions.UnauthorizedAccessException;
//
//public class UserService {
//    UserDAO userDAO = new MemoryUserDAO();
//    AuthDAO authDAO = new MemoryAuthDAO();
//
//    public RegisterResponse register(String username, String password, String email) throws Exception {
//        if (username == null || password == null || email == null) {
//            throw new BadRequestException("Error: bad request");
//        }
//        UserData user = userDAO.createUser(username, password, email);
//        AuthData auth = authDAO.createAuth(user.username(), user.password());
//        return new RegisterResponse(auth.username(), auth.authToken());
//    }
//
//    public LoginResponse login(String username, String password) throws Exception {
//        AuthValidator.validateUsernamePassword(username, password);
//        AuthData auth = authDAO.createAuth(username, password);
//        return new LoginResponse(auth.username(), auth.authToken());
//    }
//
//    public void logout(String authToken) throws Exception {
//        AuthValidator.validateAuthToken(authToken);
//        authDAO.deleteAuth(authToken);
//    }
//}
