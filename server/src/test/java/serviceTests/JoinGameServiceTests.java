package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.*;
import model.responseModels.*;
import service.*;

class JoinGameServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static String existingAuthToken;
    private static ChessService service;
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

        service = new ChessService();
    }


    @BeforeEach
    public void setup() {
        service.clearDatabase();
        RegisterResponse registerResponse = (RegisterResponse) service.register(existingUsername, existingPassword, existingEmail);
        existingAuthToken = registerResponse.authToken();

        CreateGameResponse createGameResponse = (CreateGameResponse) service.createGame(existingAuthToken, existingGameName);
        existingGameID = createGameResponse.gameID();
    }

    @Test
    void joinGameValid() {
        RegisterResponse registerResponse = (RegisterResponse) service.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        ServiceResponse response1 = service.joinGame(existingAuthToken, whiteColor, existingGameID);
        Assertions.assertEquals(JoinGameResponse.class, response1.getClass());

        ServiceResponse response2 = service.joinGame(newAuthToken, blackColor, existingGameID);
        Assertions.assertEquals(JoinGameResponse.class, response2.getClass());
    }

    @Test
    void joinGameBadAuth() {
        service.register(newUsername, newPassword, newEmail);

        ServiceResponse response1 = service.joinGame("invalid-auth-token", whiteColor, existingGameID);
        Assertions.assertEquals(FailureResponse.class, response1.getClass());
        FailureResponse failureResponse1 = (FailureResponse) response1;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse1.failureType());

        ServiceResponse response2 = service.joinGame("invalid-auth-token", blackColor, existingGameID);
        Assertions.assertEquals(FailureResponse.class, response2.getClass());
        FailureResponse failureResponse2 = (FailureResponse) response2;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse2.failureType());
    }

    @Test
    void joinGameInvalidColor() {
        RegisterResponse registerResponse = (RegisterResponse) service.register(newUsername, newPassword, newEmail);
        String newAuthToken = registerResponse.authToken();

        ServiceResponse response1 = service.joinGame(existingAuthToken, whiteColor, existingGameID);
        Assertions.assertEquals(JoinGameResponse.class, response1.getClass());

        ServiceResponse response2 = service.joinGame(newAuthToken, whiteColor, existingGameID);
        Assertions.assertEquals(FailureResponse.class, response2.getClass());
        FailureResponse failureResponse = (FailureResponse) response2;
        Assertions.assertEquals(FailureType.FORBIDDEN_RESOURCE, failureResponse.failureType());
    }
}