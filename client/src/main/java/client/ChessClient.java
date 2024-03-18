package client;

import model.chessModels.*;
import model.requestModels.*;

import java.util.Arrays;

public class ChessClient {

    private final ServerFacade server;
    private State userState = State.SIGNEDOUT;
    private AuthData auth;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
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
                case "joingame" -> joinGame(params);
                case "joinobserver" -> joinObserver(params);
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
        if (params.length >= 2) {
            userState = State.SIGNEDIN;
            auth = server.login(new LoginRequest(params[0], params[1]));
            return String.format("You are signed in as %s.", auth.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }
    public String register(String... params) throws ResponseException {
        if (params.length >= 2) {
            this.auth = server.register(new RegisterRequest(params[0], params[1], params[2]));
            userState = State.SIGNEDIN;
            return String.format("Successfully registered user %s. You are now logged in.", auth.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }
    public String logout(String... params) throws ResponseException {
        server.logout(auth);
        String returnString = "Successfully logged out user " + auth.username() + ".";
        this.userState = State.SIGNEDOUT;
        this.auth = null;
        return returnString;
    }
    public String createGame(String... params) throws ResponseException {
        return "create-game";
    }
    public String listGames(String... params) throws ResponseException {
        return "list-games";
    }
    public String joinGame(String... params) throws ResponseException {
        return "join-game";
    }
    public String joinObserver(String... params) throws ResponseException {
        return "join-observer";
    }
    public String quit(String... params) throws ResponseException {
        return "quit";
    }
}
