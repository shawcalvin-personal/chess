package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import client.webSocket.WebSocketFacade;
import model.chessModels.AuthData;
import model.requestModels.*;
import model.responseModels.*;
import model.requestModels.LoginRequest;
import model.requestModels.RegisterRequest;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Integer.parseInt;

public class ChessClient {

    private final ServerFacade server;
    private WebSocketFacade ws;
    private State userState = State.SIGNED_OUT;
    private AuthData auth;
    int gameID;
    private final String serverUrl;
    private final ClientNotificationHandler notificationHandler;
    private final String EMPTY = "";

    public ChessClient(String serverUrl, ClientNotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        server = new ServerFacade(serverUrl);
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();

                case "create-game" -> createGame(params);
                case "list-games" -> listGames();
                case "join-player" -> joinPlayer(params);
                case "join-observer" -> joinObserver(params);
                case "quit" -> quit();

                case "make-move" -> makeMove(params);
                case "leave" -> leave();
                case "resign" -> resign();
                case "redraw" -> redraw();
                case "show-moves" -> highlightValidMoves(params);

                case "clear" -> clear();
                default -> "Unrecognized command. Type 'help' for a list of available commands.";
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String help() {
        return switch (userState) {
            case SIGNED_OUT ->
                """
                Available commands:
                - login <username> <password>
                - register <username> <password> <email>
                - quit
                """;
            case SIGNED_IN ->
                """
                Available commands:
                - logout
                - create-game <game-name>
                - list-games
                - join-player <player-color> <game-id>
                - join-observer <game-id>
                """;
            case IN_GAME_PLAYER ->
                """
                Available commands:
                - make-move <start-position> <end-position>
                    [ex: make-move a3 a5]
                - leave
                - resign
                - redraw
                - show-moves
                """;
            case IN_GAME_OBSERVER ->
                """
                Available commands:
                - leave
                - redraw
                - show-moves
                """;
        };
    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            LoginResponse res = server.login(new LoginRequest(params[0], params[1]));
            userState = State.SIGNED_IN;
            auth = new AuthData(res.username(), res.authToken());
            return String.format("You are signed in as %s.", res.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }
    public String register(String... params) throws ResponseException {
        if (params.length != 3) {
            throw new ResponseException(400, "Expected: <username> <password> <email>");
        }

        RegisterResponse res = server.register(new RegisterRequest(params[0], params[1], params[2]));
        userState = State.SIGNED_IN;
        auth = new AuthData(res.username(), res.authToken());
        return String.format("Successfully registered user %s. You are now logged in.", res.username());
    }

    public String logout() throws ResponseException {
        server.logout(auth);
        this.userState = State.SIGNED_OUT;
        auth = null;
        return "Successfully signed out.";
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(400, "Expected: <team-color ('WHITE'/'BLACK')> <game-id>");
        }

        CreateGameResponse res = server.createGame(new CreateGameRequest(params[0]), auth);
        return String.format("Successfully created game %d. Use the 'join' command to join the game.", res.gameID());
    }

    public String listGames() throws ResponseException {
        return server.listGames(auth).toString();
    }

    public String joinPlayer(String... params) throws ResponseException {
        if (params.length != 2) {
            throw new ResponseException(400, "Expected: <team-color ('WHITE'/'BLACK')> <game-id>");
        }
        this.userState = State.IN_GAME_PLAYER;
        ChessGame.TeamColor playerColor = getTeamColor(params[0]);
        int gameID = parseInt(params[1]);
        this.gameID = gameID;
        server.joinGame(new JoinGameRequest(gameID, playerColor), auth);
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.joinPlayer(auth, gameID, playerColor);
        notificationHandler.setPlayerColor(playerColor);
        return EMPTY;

    }
    public String joinObserver(String... params) throws ResponseException {
        if (params.length != 1) {
            throw new ResponseException(400, "Expected: <game-id>");
        }
        this.userState = State.IN_GAME_OBSERVER;
        int gameID = parseInt(params[0]);
        this.gameID = gameID;
        server.joinGame(new JoinGameRequest(gameID, null), auth);
        ws = new WebSocketFacade(serverUrl, notificationHandler);
        ws.joinObserver(auth, gameID);
        return EMPTY;
    }

    public String quit() throws ResponseException {
        return "quit";
    }

    public String makeMove(String... params) throws ResponseException {
        ChessMove move = getChessMove(params);
        ws.makeMove(auth, gameID, move);
        return EMPTY;
    }

    public String leave() throws ResponseException {
        this.userState = State.SIGNED_IN;
        ws.leave(auth, gameID);
        return EMPTY;
    }

    public String resign() throws ResponseException {
        this.userState = State.SIGNED_IN;
        ws.resign(auth, gameID);
        return EMPTY;
    }

    public String redraw() throws ResponseException {
        notificationHandler.printBoard(null);
        return EMPTY;
    }

    public String highlightValidMoves(String... params) throws ResponseException {
        if (params.length != 1 || !isValidRankFileNotation(params[0])) {
            System.out.println(params.length);
            throw new ResponseException(400, "Expected: <chess-position>\n  ex: [a4]");
        }
        notificationHandler.printBoard(getChessPosition(params[0]));
        return EMPTY;
    }

    public String clear() throws ResponseException {
        server.clearApplication();
        return "cleared\n";
    }

    private ChessGame.TeamColor getTeamColor(String userInputColor) {
        return userInputColor.equalsIgnoreCase("white") ? ChessGame.TeamColor.WHITE : ChessGame.TeamColor.BLACK;
    }

    private boolean isValidMoveNotation(String... params) {
        if (params.length < 2 || params.length > 3) {
            return false;
        }

        for (int i = 0; i < 2; i++) {
            String param = params[i];
            if (param.length() != 2 || !isValidRankFileNotation(param)) {
                return false;
            }
        }

        ArrayList<String> validPromotionPieces = new ArrayList<>(List.of("queen", "rook", "bishop", "knight"));
        return params.length != 3 || validPromotionPieces.contains(params[2].toLowerCase());
    }

    private boolean isValidRankFileNotation(String rankFileNotation) {
        if (rankFileNotation == null || rankFileNotation.length() != 2) {
            return false;
        }

        char char0 = rankFileNotation.charAt(0);
        char char1 = rankFileNotation.charAt(1);

        return char0 >= 'a' && char0 <= 'h' && char1 >= '1' && char1 <= '8';
    }

    private ChessPosition getChessPosition(String rankFileNotation) {
        int rank = parseInt(rankFileNotation.substring(1));
        int file = Character.toLowerCase(rankFileNotation.charAt(0)) - 'a' + 1;
        return new ChessPosition(rank, file);
    }

    private ChessMove getChessMove(String... params) throws ResponseException {
        if (!isValidMoveNotation(params)) {
            throw new ResponseException(400, "Expected: <start-position> <end-position>\n[ex: make-move a3 a5]");
        }

        ChessPosition startPosition = getChessPosition(params[0]);
        ChessPosition endPosition = getChessPosition(params[1]);

        if (params.length == 2) {
            return new ChessMove(startPosition, endPosition, null);
        }

        return new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN);
    }
}
