package service;

import chess.ChessGame;
import chess.ChessMove;
import model.responseModels.ServiceResponse;
import org.eclipse.jetty.server.Authentication;
import webSocket.InvalidUserCommandException;
import webSocketMessages.userCommands.UserGameCommand;

public class ChessService {
    ClearService clearService;
    RegisterService registerService;
    LoginService loginService;
    LogoutService logoutService;
    ListGamesService listGamesService;
    CreateGameService createGameService;
    JoinGameService joinGameService;
    WebSocketService websocketService;

    public ChessService() {
        this.clearService = new ClearService();
        this.registerService = new RegisterService();
        this.loginService = new LoginService();
        this.logoutService = new LogoutService();
        this.listGamesService = new ListGamesService();
        this.createGameService = new CreateGameService();
        this.joinGameService = new JoinGameService();
        this.websocketService = new WebSocketService();
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
    public void validateUserGameCommand(UserGameCommand command) throws InvalidUserCommandException { websocketService.validateUserGameCommand(command); }
    public ChessGame getGame(int gameID) throws InvalidUserCommandException { return websocketService.getGame(gameID); }
    public ChessGame makeMove(int gameID, ChessMove move) throws InvalidUserCommandException { return websocketService.makeMove(gameID, move); }
    public void resign(int gameID, String authToken) throws InvalidUserCommandException { websocketService.resign(gameID, authToken); }
    public void leave(int gameID, String authToken) throws InvalidUserCommandException { websocketService.leave(gameID, authToken); }
}
