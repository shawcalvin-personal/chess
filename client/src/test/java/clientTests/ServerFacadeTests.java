package clientTests;

import client.ResponseException;
import client.ServerFacade;
import model.chessModels.AuthData;
import model.chessModels.GameData;
import model.chessModels.UserData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static UserData newUser;
    private static UserData registeredUser;
    private static UserData loggedInUser;
    private static AuthData loggedInAuth;
    private static AuthData invalidAuth;
    private static GameData newGame;
    private static GameData createdGame;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        String url = "http://localhost:" + port;
        serverFacade = new ServerFacade(url);
        newUser = new UserData("new-username", "new-password", "new-email");
        registeredUser = new UserData("registered-username", "registered-password", "registered-email");
        loggedInUser = new UserData("logged-username", "logged-password", "logged-email");
        invalidAuth = new AuthData(newUser.username(), "invalid-auth-token");
        newGame = new GameData(null, null, null, null, "new-game", null);
        createdGame = new GameData(null, null, null, null, "created-game", null);

        try {
            serverFacade.clearApplication();
            serverFacade.register(registeredUser);
            serverFacade.register(loggedInUser);
            loggedInAuth = serverFacade.login(loggedInUser);
            createdGame = serverFacade.createGame(createdGame, loggedInAuth);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @BeforeEach
    public void setup() {
        try {
            serverFacade.clearApplication();
            serverFacade.register(registeredUser);
            serverFacade.register(loggedInUser);
            loggedInAuth = serverFacade.login(loggedInUser);
            createdGame = serverFacade.createGame(createdGame, loggedInAuth);
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
            AuthData auth = serverFacade.register(newUser);
            Assertions.assertEquals(newUser.username(), auth.username());
        } );
    }
    @Test
    public void registerNullEmail() {
        Assertions.assertThrows(ResponseException.class, () -> {
            AuthData auth = serverFacade.register(new UserData(newUser.username(), newUser.password(), null));
            Assertions.assertNull(auth);
        } );
    }
    @Test
    public void loginValid() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = serverFacade.login(registeredUser);
            Assertions.assertEquals(registeredUser.username(), auth.username());
        } );
    }
    @Test
    public void loginInvalidPassword() {
        Assertions.assertThrows(ResponseException.class, () -> {
            UserData user = new UserData(registeredUser.username(), "invalid-password", registeredUser.email());
            AuthData auth = serverFacade.login(user);
            Assertions.assertNull(auth);
        } );
    }
    @Test
    public void logoutValid() {
        Assertions.assertDoesNotThrow(() -> {
            serverFacade.logout(loggedInAuth);
            AuthData auth = serverFacade.login(loggedInUser);
            Assertions.assertEquals(loggedInUser.username(), auth.username());
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
            GameData game = serverFacade.createGame(newGame, loggedInAuth);
            Assertions.assertEquals(newGame.gameName(), game.gameName());
        } );
    }
    @Test
    public void createGameNotLoggedIn() {
        Assertions.assertThrows(ResponseException.class, () -> {
            GameData game = serverFacade.createGame(newGame, invalidAuth);
            Assertions.assertNull(game);
        } );
    }
    @Test
    public void joinGameValid() {
        Assertions.assertDoesNotThrow(() -> {
        } );
    }
    @Test
    public void joinGameInvalidID() {

    }
    @Test
    public void joinGameNoColorSpecified() {

    }
    @Test
    public void joinObserverValid() {

    }
    @Test
    public void joinObserverInvalidID() {

    }
}
