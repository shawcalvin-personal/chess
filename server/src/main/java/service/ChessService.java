package service;

import chess.ChessGame;
import chess.ChessMove;
import model.responseModels.ServiceResponse;

public class ChessService {
    ClearService clearService;
    RegisterService registerService;
    LoginService loginService;
    LogoutService logoutService;
    ListGamesService listGamesService;
    CreateGameService createGameService;
    JoinGameService joinGameService;
    ValidateService validateService;

    public ChessService() {
        this.clearService = new ClearService();
        this.registerService = new RegisterService();
        this.loginService = new LoginService();
        this.logoutService = new LogoutService();
        this.listGamesService = new ListGamesService();
        this.createGameService = new CreateGameService();
        this.joinGameService = new JoinGameService();
        this.validateService = new ValidateService();
    }
    public ServiceResponse register(String username, String password, String email) { return registerService.register(username, password, email); }
    public ServiceResponse login(String username, String password) {
        return loginService.login(username, password);
    }
    public ServiceResponse logout(String authToken) {
        return logoutService.logout(authToken);
    }
    public ServiceResponse listGames(String authToken) {
        return listGamesService.listGames(authToken);
    }
    public ServiceResponse createGame(String authToken, String gameName) { return createGameService.createGame(authToken, gameName); }
    public ServiceResponse joinGame(String authToken, String playerColor, Integer gameID ) { return joinGameService.joinGame(authToken, playerColor, gameID); }
    public ServiceResponse clearDatabase() {
        return clearService.clearDatabase();
    }
    public boolean isValidAuth(String authToken) { return validateService.isValidAuth(authToken); }
    public boolean isValidGameID(int gameID) { return validateService.isValidGameID(gameID); }
    public boolean isValidPlayerColor(int gameID, ChessGame.TeamColor playerColor, String authToken) { return validateService.isValidPlayerColor(gameID, playerColor, authToken); }
    public boolean isValidChessMove(int gameID, ChessMove move) { return validateService.isValidChessMove(gameID, move); }
}
