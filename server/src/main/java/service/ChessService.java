package service;

import model.responseModels.ServiceResponse;

public class ChessService {
    ClearService clearService;
    RegisterService registerService;
    LoginService loginService;
    LogoutService logoutService;
    ListGamesService listGamesService;
    CreateGameService createGameService;
    JoinGameService joinGameService;

    public ChessService() {
        this.clearService = new ClearService();
        this.registerService = new RegisterService();
        this.loginService = new LoginService();
        this.logoutService = new LogoutService();
        this.listGamesService = new ListGamesService();
        this.createGameService = new CreateGameService();
        this.joinGameService = new JoinGameService();
    }
    public ServiceResponse register(String username, String password, String email) {
        return registerService.register(username, password, email);
    }
    public ServiceResponse login(String username, String password) {
        return loginService.login(username, password);
    }
    public ServiceResponse logout(String authToken) {
        return logoutService.logout(authToken);
    }
    public ServiceResponse listGames(String authToken) {
        return listGamesService.listGames(authToken);
    }
    public ServiceResponse createGame(String authToken, String gameName) {
        return createGameService.createGame(authToken, gameName);
    }
    public ServiceResponse joinGame(String authToken, String playerColor, Integer gameID ) {
        return joinGameService.joinGame(authToken, playerColor, gameID);
    }
    public ServiceResponse clearDatabase() {
        return clearService.clearDatabase();
    }
}
