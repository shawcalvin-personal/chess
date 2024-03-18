package serviceTests;

import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.responseModels.*;
import service.*;

public class ClearServiceTests {
    private static String newUsername;
    private static String newPassword;
    private static String newEmail;
    private static String existingUsername;
    private static String existingPassword;
    private static String existingEmail;
    private static String existingAuthToken;
    private static RegisterService registerService;
    private static LoginService loginService;
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
        loginService = new LoginService();
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
    void clear() {
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

        clearService.clearDatabase();
        RegisterResponse registerResponse2 = (RegisterResponse) registerService.register(newUsername, newPassword, newEmail);
        String newAuthToken2 = registerResponse2.authToken();

        ServiceResponse response2 = listGamesService.listGames(newAuthToken2);
        Assertions.assertEquals(ListGamesResponse.class, response2.getClass());
        ListGamesResponse listGamesResponse2 = (ListGamesResponse) response2;
        Assertions.assertEquals(0, listGamesResponse2.games().size());

        ServiceResponse testLoginResponse = loginService.login(existingUsername, existingPassword);
        Assertions.assertEquals(FailureResponse.class, testLoginResponse.getClass());
        FailureResponse testFailureResponse = (FailureResponse) testLoginResponse;
        Assertions.assertEquals(FailureType.UNAUTHORIZED_ACCESS, testFailureResponse.failureType());
    }

}
