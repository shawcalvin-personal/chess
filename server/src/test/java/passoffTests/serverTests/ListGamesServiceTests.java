package passoffTests.serverTests;

import org.junit.jupiter.api.*;
import server.responseModels.*;
import service.*;

class ListGamesServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static String existingAuthToken;
    private static RegisterService registerService;
    private static CreateGameService createGameService;
    private static JoinGameService joinGameService;
    private static ListGamesService listGamesService;
    private static ClearService clearService;
    private static String existingGameName;
    private static int existingGameID;

    private static String whiteColor;
    private  static String blackColor;

    @BeforeAll
    public static void init() {
        newUsername = "new-username";
        newPassword = "new-password";
        newEmail = "new-email";

        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        existingGameName = "cool-new-game";

        whiteColor = "WHITE";
        blackColor = "BLACK";

        registerService = new RegisterService();
        createGameService = new CreateGameService();
        joinGameService = new JoinGameService();
        listGamesService = new ListGamesService();
        clearService = new ClearService();
    }

    @BeforeEach
    public void setup() {
        clearService.clearDatabase();
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(existingUsername, existingPassword, existingEmail);
        existingAuthToken = registerResponse.authToken();

        CreateGameResponse createGameResponse = (CreateGameResponse) createGameService.createGame(existingAuthToken, existingGameName);
        existingGameID = createGameResponse.gameID();
    }

    @Test
    void listGamesValidUser() {
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        joinGameService.joinGame(existingAuthToken, whiteColor, existingGameID);
        joinGameService.joinGame(newAuthToken, blackColor, existingGameID);

        ServiceResponse response = listGamesService.listGames(existingAuthToken);
        Assertions.assertEquals(ListGamesResponse.class, response.getClass());
        ListGamesResponse listGamesResponse = (ListGamesResponse) response;
        Assertions.assertFalse(listGamesResponse.games().isEmpty());
    }

    @Test
    void listGamesBadAuth() {
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        joinGameService.joinGame(existingAuthToken, whiteColor, existingGameID);
        joinGameService.joinGame(newAuthToken, blackColor, existingGameID);

        ServiceResponse response = listGamesService.listGames("invalid-auth-token");
        Assertions.assertEquals(FailureResponse.class, response.getClass());
        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }

    @Test
    void listManyGames() {
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        CreateGameResponse createGameResponse1 = (CreateGameResponse) createGameService.createGame(existingAuthToken, "new-game-1");
        CreateGameResponse createGameResponse2 = (CreateGameResponse) createGameService.createGame(existingAuthToken, "new-game-1");
        CreateGameResponse createGameResponse3 = (CreateGameResponse) createGameService.createGame(existingAuthToken, "new-game-1");

        int gameID1 = createGameResponse1.gameID();
        int gameID2 = createGameResponse2.gameID();
        int gameID3 = createGameResponse3.gameID();

        joinGameService.joinGame(existingAuthToken, whiteColor, existingGameID);
        joinGameService.joinGame(newAuthToken, blackColor, existingGameID);

        joinGameService.joinGame(existingAuthToken, whiteColor, gameID1);
        joinGameService.joinGame(newAuthToken, blackColor, gameID1);

        joinGameService.joinGame(existingAuthToken, whiteColor, gameID2);
        joinGameService.joinGame(newAuthToken, blackColor, gameID2);

        joinGameService.joinGame(existingAuthToken, whiteColor, gameID3);
        joinGameService.joinGame(newAuthToken, blackColor, gameID3);


        ServiceResponse response = listGamesService.listGames(existingAuthToken);
        Assertions.assertEquals(ListGamesResponse.class, response.getClass());
        ListGamesResponse listGamesResponse = (ListGamesResponse) response;
        Assertions.assertEquals(4, listGamesResponse.games().size());
    }
}