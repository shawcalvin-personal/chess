package clientTests;

import client.ResponseException;
import client.ServerFacade;
import model.requestModels.*;
import model.responseModels.*;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String registeredUsername;
    private static String registeredPassword;
    private static String registeredEmail;
    private static String loggedInUsername;
    private static String loggedInPassword;
    private static String loggedInEmail;
    private static RequestHeader loggedInAuth;
    private static RequestHeader invalidAuth;
    private static String newGameName;
    private static String createdGameName;
    private static Integer createdGameID;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        String url = "http://localhost:" + port;
        serverFacade = new ServerFacade(url);

        newUsername = "new-username";
        newPassword = "new-password";
        newEmail = "new-email";
        registeredUsername = "registered-username";
        registeredPassword = "registered-password";
        registeredEmail = "registered-email";
        loggedInUsername = "logged-username";
        loggedInPassword = "logged-password";
        loggedInEmail = "logged-email";
        newGameName = "new-game";
        createdGameName = "created-game";

        invalidAuth = new RequestHeader("invalid-auth-token");

        try {
            serverFacade.clearApplication();
            serverFacade.register(new RegisterRequest(registeredUsername, registeredPassword, registeredEmail));
            serverFacade.register(new RegisterRequest(loggedInUsername, loggedInPassword, loggedInEmail));
            LoginResponse res = serverFacade.login(new LoginRequest(loggedInUsername, loggedInPassword));
            loggedInAuth = new RequestHeader(res.authToken());
            createdGameID = serverFacade.createGame(new CreateGameRequest(createdGameName), loggedInAuth).gameID();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @BeforeEach
    public void setup() {
        try {
            serverFacade.clearApplication();
            serverFacade.register(new RegisterRequest(registeredUsername, registeredPassword, registeredEmail));
            serverFacade.register(new RegisterRequest(loggedInUsername, loggedInPassword, loggedInEmail));
            LoginResponse res = serverFacade.login(new LoginRequest(loggedInUsername, loggedInPassword));
            loggedInAuth = new RequestHeader(res.authToken());
            createdGameID = serverFacade.createGame(new CreateGameRequest(createdGameName), loggedInAuth).gameID();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testRegisterValid() {
        Assertions.assertDoesNotThrow(() -> {
            RegisterResponse res = serverFacade.register(new RegisterRequest(newUsername, newPassword, newEmail));
            Assertions.assertEquals(newUsername, res.username());
            Assertions.assertNotNull(res.authToken());
        } );
    }
    @Test
    public void registerNullEmail() {
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.register(new RegisterRequest(newUsername, newPassword, null));
        } );
    }
    @Test
    public void loginValid() {
        Assertions.assertDoesNotThrow(() -> {
            LoginResponse res = serverFacade.login(new LoginRequest(registeredUsername, registeredPassword));
            Assertions.assertEquals(registeredUsername, res.username());
        } );
    }
    @Test
    public void loginInvalidPassword() {
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.login(new LoginRequest(registeredUsername, newPassword));
        } );
    }
    @Test
    public void logoutValid() {
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.logout(loggedInAuth);
            LoginResponse res = serverFacade.login(new LoginRequest(loggedInUsername, loggedInPassword));
            Assertions.assertEquals(loggedInUsername, res.username());
        } );
    }
    @Test
    public void logoutNotLoggedIn() {
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.logout(invalidAuth);
        } );
    }
    @Test
    public void createGameValid() {
        Assertions.assertDoesNotThrow(() -> {
            CreateGameResponse res = serverFacade.createGame(new CreateGameRequest(newGameName), loggedInAuth);
            Assertions.assertNotNull(res.gameID());
        } );
    }
    @Test
    public void createGameNotLoggedIn() {
        Assertions.assertThrows(ResponseException.class, () -> {
            serverFacade.createGame(new CreateGameRequest(newGameName), invalidAuth);
        } );
    }
    @Test
    public void joinGameValid() {
        Assertions.assertDoesNotThrow(() -> {
            LoginResponse loginResponse = serverFacade.login(new LoginRequest(registeredUsername, registeredPassword));
            RequestHeader registeredAuth = new RequestHeader(loginResponse.authToken());
            serverFacade.joinGame(new JoinGameRequest("WHITE", createdGameID), loggedInAuth);
            serverFacade.joinGame(new JoinGameRequest("BLACK", createdGameID), registeredAuth);

            ListGamesResponse listResult = serverFacade.listGames(loggedInAuth);

            Assertions.assertEquals(1, listResult.games().size());
            GameInstance game = listResult.games().iterator().next();
            Assertions.assertEquals(loggedInUsername, game.whiteUsername());
            Assertions.assertEquals(registeredUsername, game.blackUsername());
        } );
    }
    @Test
    public void joinGameInvalidID() {
        Assertions.assertThrows(ResponseException.class, () -> {
            LoginResponse loginResponse = serverFacade.login(new LoginRequest(registeredUsername, registeredPassword));
            RequestHeader registeredAuth = new RequestHeader(loginResponse.authToken());
            serverFacade.joinGame(new JoinGameRequest("WHITE", 99), loggedInAuth);
            serverFacade.joinGame(new JoinGameRequest("BLACK", 99), registeredAuth);

            ListGamesResponse listResult = serverFacade.listGames(loggedInAuth);

            Assertions.assertEquals(1, listResult.games().size());
            GameInstance game = listResult.games().iterator().next();
            Assertions.assertNull(game.whiteUsername());
            Assertions.assertNull(game.blackUsername());
        } );
    }
    @Test
    public void joinObserverValid() {
        Assertions.assertDoesNotThrow(() -> {
            LoginResponse loginResponse = serverFacade.login(new LoginRequest(registeredUsername, registeredPassword));
            RequestHeader registeredAuth = new RequestHeader(loginResponse.authToken());
            serverFacade.joinGame(new JoinGameRequest(null, createdGameID), loggedInAuth);
            serverFacade.joinGame(new JoinGameRequest(null, createdGameID), registeredAuth);
        } );
    }
    @Test
    public void joinObserverInvalidID() {
        Assertions.assertThrows(ResponseException.class, () -> {
            LoginResponse loginResponse = serverFacade.login(new LoginRequest(registeredUsername, registeredPassword));
            RequestHeader registeredAuth = new RequestHeader(loginResponse.authToken());
            serverFacade.joinGame(new JoinGameRequest(null, 99), loggedInAuth);
            serverFacade.joinGame(new JoinGameRequest(null, 99), registeredAuth);
        } );
    }
    @Test
    public void listGamesValid() {
        Assertions.assertDoesNotThrow(() -> {
            ListGamesResponse listResult = serverFacade.listGames(loggedInAuth);
            Assertions.assertEquals(1, listResult.games().size());

            serverFacade.createGame(new CreateGameRequest(newGameName), loggedInAuth);
            listResult = serverFacade.listGames(loggedInAuth);
            Assertions.assertEquals(2, listResult.games().size());
        } );
    }
    @Test
    public void listGamesNoCurrentGames() {
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.clearApplication();
            RegisterResponse res = serverFacade.register(new RegisterRequest(loggedInUsername, loggedInPassword, loggedInEmail));
            RequestHeader auth = new RequestHeader(res.authToken());
            ListGamesResponse listResult = serverFacade.listGames(auth);
            Assertions.assertEquals(0, listResult.games().size());
        } );
    }
}
