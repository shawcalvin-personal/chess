package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.*;
import model.responseModels.*;
import service.*;

class CreateGameServiceTests {
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static String existingAuthToken;
    private static ChessService service;

    @BeforeAll
    public static void init() {
        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        service = new ChessService();
    }


    @BeforeEach
    public void setup() {
        service.clearDatabase();
        RegisterResponse response = (RegisterResponse) service.register(existingUsername, existingPassword, existingEmail);
        existingAuthToken = response.authToken();
    }

    @Test
    void createGameValid() {
        String gameName = "cool-new-game";
        ServiceResponse response = service.createGame(existingAuthToken, gameName);
        Assertions.assertEquals(CreateGameResponse.class, response.getClass());

        CreateGameResponse createGameResponse = (CreateGameResponse) response;
        Assertions.assertTrue(createGameResponse.gameID() > 0);
    }

    @Test
    void createGameBadAuth() {
        String gameName = "cool-new-game";
        ServiceResponse response = service.createGame("invalid-auth-token", gameName);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }

    @Test
    void createGameNullName() {
        ServiceResponse response = service.createGame(existingAuthToken, null);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.BAD_REQUEST, failureResponse.failureType());
    }
}