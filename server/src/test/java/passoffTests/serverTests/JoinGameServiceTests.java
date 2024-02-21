package passoffTests.serverTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.responseModels.*;
import service.*;

class JoinGameServiceTests {
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
    void joinGameValid() {
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        ServiceResponse response1 = joinGameService.joinGame(existingAuthToken, whiteColor, existingGameID);
        Assertions.assertEquals(JoinGameResponse.class, response1.getClass());

        ServiceResponse response2 = joinGameService.joinGame(newAuthToken, blackColor, existingGameID);
        Assertions.assertEquals(JoinGameResponse.class, response2.getClass());
    }

    @Test
    void joinGameBadAuth() {
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);

        ServiceResponse response1 = joinGameService.joinGame("invalid-auth-token", whiteColor, existingGameID);
        Assertions.assertEquals(FailureResponse.class, response1.getClass());
        FailureResponse failureResponse1 = (FailureResponse) response1;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse1.failureType());

        ServiceResponse response2 = joinGameService.joinGame("invalid-auth-token", blackColor, existingGameID);
        Assertions.assertEquals(FailureResponse.class, response2.getClass());
        FailureResponse failureResponse2 = (FailureResponse) response2;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse2.failureType());
    }

    @Test
    void joinGameInvalidColor() {
        RegisterResponse registerResponse = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        ServiceResponse response1 = joinGameService.joinGame(existingAuthToken, whiteColor, existingGameID);
        Assertions.assertEquals(JoinGameResponse.class, response1.getClass());

        ServiceResponse response2 = joinGameService.joinGame(newAuthToken, whiteColor, existingGameID);
        Assertions.assertEquals(FailureResponse.class, response2.getClass());
        FailureResponse failureResponse = (FailureResponse) response2;
        Assertions.assertEquals(FailureType.FORBIDDEN_RESOURCE, failureResponse.failureType());
    }
}