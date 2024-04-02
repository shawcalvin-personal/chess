package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.chessModels.UserData;
import org.junit.jupiter.api.*;

public class UserDataAccessTests {
    static UserDAO userDAO;
    static UserData newUser;
    static UserData existingUser;
    @BeforeAll
    public static void init() {
        userDAO = SQLUserDAO.getInstance();
        newUser = new UserData("new-username", "new-password", "new-email");
        existingUser = new UserData("existing-username", "existing-password", "existing-email");
    }
    @BeforeEach
    public void setup() {
        try {
            userDAO.clear();
            userDAO.create(existingUser.username(), existingUser.password(), existingUser.email());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void addUserValid() {
        Assertions.assertDoesNotThrow(() -> {
            UserData user = userDAO.create(newUser.username(), newUser.password(), newUser.email());
            Assertions.assertNotNull(user);
            UserData retrievedUser = userDAO.get(user.username());
            Assertions.assertNotNull(retrievedUser);
            Assertions.assertEquals(newUser, retrievedUser);
        });
    }
    @Test
    void addUserAlreadyCreated() {
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.create(existingUser.username(), existingUser.password(), existingUser.email()));
    }
    @Test
    void getUserValid() {
        Assertions.assertDoesNotThrow(() -> {
            UserData user = userDAO.create(newUser.username(), newUser.password(), newUser.email());
            Assertions.assertNotNull(user);
            UserData retrievedNewUser = userDAO.get(user.username());
            Assertions.assertNotNull(retrievedNewUser);
            Assertions.assertEquals(newUser, retrievedNewUser);

            UserData retrievedExistingUser = userDAO.get(existingUser.username());
            Assertions.assertNotNull(retrievedExistingUser);
            Assertions.assertEquals(existingUser, retrievedExistingUser);
        });
    }
    @Test
    void getUserDoesNotExist() {
        Assertions.assertDoesNotThrow(() -> {
            UserData user = userDAO.get(newUser.username());
            Assertions.assertNull(user);
        });
    }
    @Test
    void deleteUserValid() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.delete(existingUser.username());
            Assertions.assertNull(userDAO.get(existingUser.username()));
        });
    }
    @Test
    void deleteUserDoesNotExist() {
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.delete(newUser.username()));
    }
    @Test
    void deleteAllUsers() {
        Assertions.assertDoesNotThrow(() -> {
            userDAO.create(newUser.username(), newUser.password(), newUser.email());
            userDAO.clear();
            Assertions.assertNull(userDAO.get(newUser.username()));
            Assertions.assertNull(userDAO.get(existingUser.username()));
        });
    }
}
