package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.*;
import model.responseModels.*;
import service.ClearService;
import service.LoginService;
import service.RegisterService;

class LoginServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static RegisterService registerService;
    private static LoginService loginService;
    private static ClearService clearService;

    @BeforeAll
    public static void init() {
        newUsername = "new-username";
        newPassword = "new-password";

        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        registerService = new RegisterService();
        loginService = new LoginService();
        clearService = new ClearService();
    }


    @BeforeEach
    public void setup() {
        clearService.clearDatabase();
        registerService.register(existingUsername, existingPassword, existingEmail);
    }

    @Test
    void loginValidUser() {
        ServiceResponse response = loginService.login(existingUsername, existingPassword);
        Assertions.assertEquals(LoginResponse.class, response.getClass());

        LoginResponse loginResponse = (LoginResponse) response;
        Assertions.assertEquals(existingUsername, loginResponse.username());
    }

    @Test
    void loginInvalidUser() {
        ServiceResponse response = loginService.login(newUsername, newPassword);
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }
}