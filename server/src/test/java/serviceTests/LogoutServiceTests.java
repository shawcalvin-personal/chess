package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.*;
import model.responseModels.*;
import service.*;

class LogoutServiceTests {
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
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
        service.register(existingUsername, existingPassword, existingEmail);
    }

    @Test
    void logoutValidUser() {
        ServiceResponse response = service.login(existingUsername, existingPassword);
        Assertions.assertEquals(LoginResponse.class, response.getClass());

        LoginResponse loginResponse = (LoginResponse) response;
        Assertions.assertEquals(existingUsername, loginResponse.username());

        String authToken = loginResponse.authToken();
        ServiceResponse logoutResponse = service.logout(authToken);
        Assertions.assertEquals(LogoutResponse.class, logoutResponse.getClass());
    }

    @Test
    void logoutBadAuth() {
        ServiceResponse response = service.logout("invalid-auth-token");
        Assertions.assertEquals(FailureResponse.class, response.getClass());

        FailureResponse failureResponse = (FailureResponse) response;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, failureResponse.failureType());
    }
}