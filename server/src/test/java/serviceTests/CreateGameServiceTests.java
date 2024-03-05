package serviceTests;

import org.junit.jupiter.api.*;
import server.responseModels.*;
import service.*;

class CreateGameServiceTests {
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static String existingAuthToken;
    private static RegisterService registerService;
    private static CreateGameService createGameService;
    private static ClearService clearService;

    @BeforeAll
    public static void init() {
        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        registerService = new RegisterService();
        createGameService = new CreateGameService();
        clearService = new ClearService();
    }


    @BeforeEach
    public void setup() {
        clearService.clearDatabase();
        RegisterResponse response = (RegisterResponse) registerService.register(existingUsername, existingPassword, existingEmail);
        existingAuthToken = response.authToken();
    }

    @Test
    void createGameValid() {
        String gameName = "cool-new-game";
        ServiceResponse response = createGameService.createGame(existingAuthToken, gameName);
        Assertions.assertEquals(CreateGameResponse.class, response.getClass());

        CreateGameResponse createGameResponse = (CreateGameResponse) response;
        Assertions.assertTrue(createGameResponse.gameID() > 0);
    }

    @Test
    void createGameBadAuth() {
        String gameName = "cool-new-game";
        ServiceResponse response = createGameService.createGame("invalid-auth-token", gameName);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }

    @Test
    void createGameNullName() {
        ServiceResponse response = createGameService.createGame(existingAuthToken, null);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.BAD_REQUEST, failureResponse.failureType());
    }
}