package serviceTests;

import org.junit.jupiter.api.*;
import server.responseModels.*;
import service.ClearService;
import service.RegisterService;

class RegisterServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static RegisterService registerService;
    private static ClearService clearService;

    @BeforeAll
    public static void init() {
        newUsername = "new-username";
        newPassword = "new-password";
        newEmail = "new-email";

        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        registerService = new RegisterService();
        clearService = new ClearService();
    }


    @BeforeEach
    public void setup() {
        clearService.clearDatabase();
        registerService.register(existingUsername, existingPassword, existingEmail);
    }

    @Test
    void registerNewUser() {
        ServiceResponse response = registerService.register(newUsername, newPassword, newEmail);
        Assertions.assertEquals(RegisterResponse.class, response.getClass());

        RegisterResponse registerResponse = (RegisterResponse) response;
        Assertions.assertEquals(newUsername, registerResponse.username());
        Assertions.assertNotNull(registerResponse.authToken());
    }

    @Test
    void registerExistingUser() {
        ServiceResponse response = registerService.register(existingUsername, existingPassword, existingEmail);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.FORBIDDEN_RESOURCE, failureResponse.failureType());
    }

    @Test
    void registerNullPassword() {
        ServiceResponse response = registerService.register(existingUsername, null, existingEmail);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.BAD_REQUEST, failureResponse.failureType());
    }
}