package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import client.webSocket.NotificationHandler;
import client.webSocket.WebSocketFacade;
import model.requestModels.*;
import model.responseModels.*;
import model.requestModels.LoginRequest;
import model.requestModels.RegisterRequest;
import model.requestModels.RequestHeader;
import ui.ChessGamePrinter;


import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class ChessClient {

    private final ServerFacade server;
    private WebSocketFacade ws;
    private State userState = State.SIGNEDOUT;
    private RequestHeader auth;
    private ChessGame game;
    private String serverUrl;
    private NotificationHandler notificationHandler;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        server = new ServerFacade(serverUrl);
        game = new ChessGame();
        game.init();
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "creategame" -> createGame(params);
                case "listgames" -> listGames();
                case "joingame" -> joinPlayer(params);
                case "joinobserver" -> joinObserver(params);
                case "clear" -> clear();
                case "print" -> print();
                case "quit" -> quit();
                default -> "Unrecognized command. Type 'help' for a list of available commands.";
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String help() {
        if (userState == State.SIGNEDOUT) {
            return """
                    Available commands:
                    - login <username> <password>
                    - register <username> <password> <email>
                    - quit
                    """;
        }
        return """
                Available commands:
                - logout
                - create-game <game-name>
                - list-games
                - join-game <player-color> <game-id>
                - join-observer <game-id>
                """;
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResponse res = server.login(new LoginRequest(params[0], params[1]));
            userState = State.SIGNEDIN;
            auth = new RequestHeader(res.authToken());
            return String.format("You are signed in as %s.", res.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }
    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            RegisterResponse res = server.register(new RegisterRequest(params[0], params[1], params[2]));
            userState = State.SIGNEDIN;
            auth = new RequestHeader(res.authToken());
            return String.format("Successfully registered user %s. You are now logged in.", res.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }
    public String logout() throws ResponseException {
        server.logout(auth);
        this.userState = State.SIGNEDOUT;
        auth = null;
        return "Successfully signed out.";
    }
    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            CreateGameResponse res = server.createGame(new CreateGameRequest(params[0]), auth);
            return String.format("Successfully created game %d. Use the 'join' command to join the game.", res.gameID());
        }
        throw new ResponseException(400, "Expected: <game-name>");
    }
    public String listGames() throws ResponseException {
        return server.listGames(auth).toString();
    }
    public String joinPlayer(String... params) throws ResponseException {
        if (params.length == 2) {
            server.joinGame(new JoinGameRequest(params[0].toUpperCase(), parseInt(params[1])), auth);
            ws = new WebSocketFacade(serverUrl, notificationHandler);
            ws.joinPlayer(auth);
            return String.format("Successfully joined game %s as the %s color.", params[1], params[0]);
        }
        throw new ResponseException(400, "Expected: <team-color ('WHITE'/'BLACK')> <game-id>");
    }
    public String joinObserver(String... params) throws ResponseException {
        if (params.length == 1) {
            server.joinGame(new JoinGameRequest(null, parseInt(params[0])), auth);
            return String.format("Successfully joined game %s as an observer.", params[0]);
        }
        throw new ResponseException(400, "Expected: <game-id>");
    }
    public String quit() throws ResponseException {
        return "quit";
    }

    public String clear() throws ResponseException {
        server.clearApplication();
        return "cleared\n";
    }

    public String print() {
        ChessGamePrinter.printGame(game);
        return "printed\n";
    }
}
