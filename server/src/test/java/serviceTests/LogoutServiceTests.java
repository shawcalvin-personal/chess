package serviceTests;

import org.junit.jupiter.api.*;
import server.responseModels.*;
import service.ClearService;
import service.LoginService;
import service.LogoutService;
import service.RegisterService;

class LogoutServiceTests {
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static RegisterService registerService;
    private static LoginService loginService;
    private static LogoutService logoutService;
    private static ClearService clearService;

    @BeforeAll
    public static void init() {
        existingUsername = "existing-username";
        existingPassword = "existing-password";
        existingEmail = "existing-email";

        registerService = new RegisterService();
        loginService = new LoginService();
        logoutService = new LogoutService();
        clearService = new ClearService();
    }


    @BeforeEach
    public void setup() {
        clearService.clearDatabase();
        registerService.register(existingUsername, existingPassword, existingEmail);
    }

    @Test
    void logoutValidUser() {
        ServiceResponse response = loginService.login(existingUsername, existingPassword);
        Assertions.assertEquals(LoginResponse.class, response.getClass());

        LoginResponse loginResponse = (LoginResponse) response;
        Assertions.assertEquals(existingUsername, loginResponse.username());

        String authToken = loginResponse.authToken();
        ServiceResponse logoutResponse = logoutService.logout(authToken);
        Assertions.assertEquals(LogoutResponse.class, logoutResponse.getClass());
    }

    @Test
    void logoutBadAuth() {
        ServiceResponse response = logoutService.logout("invalid-auth-token");
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }
}