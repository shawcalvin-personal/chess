package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess.SQLAuthDAO;
import dataAccess.SQLDataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class AuthDataAccessTests {
    static UserDAO userDAO;
    static AuthDAO authDAO;
    static UserData newUser;
    static UserData existingUser;
    @BeforeAll
    public static void init() {
        userDAO = new SQLUserDAO();
        authDAO = new SQLAuthDAO();
        newUser = new UserData("new-username", "new-password", "new-email");
        existingUser = new UserData("existing-username", "existing-password", "existing-email");
    }
    @BeforeEach
    public void setup() {
        try {
            authDAO.clear();
            userDAO.clear();
            userDAO.create(existingUser.username(), existingUser.password(), existingUser.email());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void addAuthValid() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = authDAO.create(existingUser.username(), existingUser.password());
            Assertions.assertNotNull(auth);
            Assertions.assertEquals(existingUser.username(), auth.username());
        });
    }
    @Test
    void addAuthInvalidUsername() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            AuthData auth = authDAO.create(newUser.username(), newUser.password());
            Assertions.assertNull(auth);
        });
    }
    @Test
    void getAuthValid() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = authDAO.create(existingUser.username(), existingUser.password());
            Assertions.assertNotNull(auth);
            Assertions.assertEquals(existingUser.username(), auth.username());

            AuthData retrievedAuth = authDAO.get(auth.authToken());
            Assertions.assertNotNull(retrievedAuth);
            Assertions.assertEquals(auth, retrievedAuth);
        });
    }
    @Test
    void getAuthDoesNotExist() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData retrievedAuth = authDAO.get("invalid-auth-token");
            Assertions.assertNull(retrievedAuth);
        });
    }
    @Test
    void deleteAuthValid() {
        Assertions.assertDoesNotThrow(() -> {
            AuthData auth = authDAO.create(existingUser.username(), existingUser.password());
            Assertions.assertNotNull(auth);
            Assertions.assertEquals(existingUser.username(), auth.username());

            AuthData retrievedAuth = authDAO.get(auth.authToken());
            Assertions.assertNotNull(retrievedAuth);
            Assertions.assertEquals(auth, retrievedAuth);

            authDAO.delete(auth.authToken());
            AuthData deletedAuth = authDAO.get(auth.authToken());
            Assertions.assertNull(deletedAuth);
        });
    }
    @Test
    void deleteAuthDoesNotExist() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authDAO.delete("invalid-auth-token");
        });
    }
    @Test
    void deleteAllAuth() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.create(newUser.username(), newUser.password(), newUser.email());
            AuthData newAuth = authDAO.create(newUser.username(), newUser.password());
            AuthData existingAuth = authDAO.create(existingUser.username(), existingUser.password());
            Assertions.assertNotNull(newAuth);
            Assertions.assertNotNull(existingAuth);

            authDAO.clear();
            Assertions.assertNull(authDAO.get(newAuth.authToken()));
            Assertions.assertNull(userDAO.get(existingAuth.authToken()));
        });
    }
}
