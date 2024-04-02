package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.*;
import model.responseModels.*;
import service.ChessService;
import service.ClearService;
import service.RegisterService;

class RegisterServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static ChessService service;

    @BeforeAll
    public static void init() {
        newUsername = "new-username";
        newPassword = "new-password";
        newEmail = "new-email";

        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        service = new ChessService();
    }


    @BeforeEach
    public void setup() {
        service.clearDatabase();
        service.register(existingUsername, existingPassword, existingEmail);
    }

    @Test
    void registerNewUser() {
        ServiceResponse response = service.register(newUsername, newPassword, newEmail);
        Assertions.assertEquals(RegisterResponse.class, response.getClass());

        RegisterResponse registerResponse = (RegisterResponse) response;
        Assertions.assertEquals(newUsername, registerResponse.username());
        Assertions.assertNotNull(registerResponse.authToken());
    }

    @Test
    void registerExistingUser() {
        ServiceResponse response = service.register(existingUsername, existingPassword, existingEmail);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.FORBIDDEN_RESOURCE, failureResponse.failureType());
    }

    @Test
    void registerNullPassword() {
        ServiceResponse response = service.register(existingUsername, null, existingEmail);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.BAD_REQUEST, failureResponse.failureType());
    }
}