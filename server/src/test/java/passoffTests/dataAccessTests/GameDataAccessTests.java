package passoffTests.dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLDataAccess.SQLGameDAO;
import dataAccess.SQLDataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class GameDataAccessTests {
    static UserDAO userDAO;
    static GameDAO gameDAO;
    static UserData newWhiteUser;
    static UserData newBlackUser;
    static UserData existingWhiteUser;
    static UserData existingBlackUser;
    static GameData newGame;
    static GameData existingGame;
    @BeforeAll
    public static void init() {
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        newWhiteUser = new UserData("new-white-username", "new-white-password", "new-white-email");
        newBlackUser = new UserData("new-black-username", "new-black-password", "new-black-email");
        existingWhiteUser = new UserData("existing-white-username", "existing-white-password", "existing-white-email");
        existingBlackUser = new UserData("existing-black-username", "existing-black-password", "existing-black-email");
        newGame = new GameData(99, null, null, null, "new-game-name", null);
    }

    @BeforeEach
    public void setup() {
        try {
            userDAO.clear();
            gameDAO.clear();
            userDAO.create(existingWhiteUser.username(), existingWhiteUser.password(), existingWhiteUser.email());
            userDAO.create(existingBlackUser.username(), existingBlackUser.password(), existingBlackUser.email());
            existingGame = gameDAO.create("existing-game-name");
            gameDAO.update(existingGame.gameID(), new GameData(existingGame.gameID(), existingWhiteUser.username(), existingBlackUser.username(), existingGame.observerUsernames(), existingGame.gameName(), existingGame.game()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void createGameByNameValid() {
        Assertions.assertDoesNotThrow(() -> {
            GameData createdGame = gameDAO.create(newGame.gameName());
            Assertions.assertNotNull(createdGame);
            GameData retrievedGame = gameDAO.get(createdGame.gameID());
            Assertions.assertEquals(createdGame, retrievedGame);
            Assertions.assertEquals(ChessGame.class, createdGame.game().getClass());
        });
    }
    @Test
    void createGameAlreadyExists() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            GameData createdGame = gameDAO.create(existingGame);
            Assertions.assertNull(createdGame);
        });
    }
    @Test
    void getGameValid() {
        Assertions.assertDoesNotThrow(() -> {
            GameData retrievedGame = gameDAO.get(existingGame.gameID());
            Assertions.assertNotNull(retrievedGame);
            Assertions.assertEquals(existingGame, retrievedGame);
        });
    }
    @Test
    void getGameDoesNotExist() {
        Assertions.assertDoesNotThrow(() -> {
            GameData retrievedGame = gameDAO.get(newGame.gameID());
            Assertions.assertNull(retrievedGame);
        });
    }
    @Test
    void deleteGameValid() {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.delete(existingGame.gameID());
            GameData retrievedGame = gameDAO.get(existingGame.gameID());
            Assertions.assertNull(retrievedGame);
        });
    }
    @Test
    void deleteGameDoesNotExist() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.delete(newGame.gameID());
        });
    }
    @Test
    void updateGameValid() {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.update(existingGame.gameID(), new GameData(existingGame.gameID(), existingWhiteUser.username(), existingBlackUser.username(), existingGame.observerUsernames(), existingGame.gameName(), existingGame.game()));
            GameData retrievedGame = gameDAO.get(existingGame.gameID());
            Assertions.assertNotNull(retrievedGame);
            Assertions.assertEquals(existingWhiteUser.username(), retrievedGame.whiteUsername());
            Assertions.assertEquals(existingBlackUser.username(), retrievedGame.blackUsername());
            Assertions.assertEquals(ChessGame.class, retrievedGame.game().getClass());
        });
    }
    @Test
    void updateGameDoesNotExist() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.update(newGame.gameID(), new GameData(newGame.gameID(), existingWhiteUser.username(), existingBlackUser.username(), existingGame.observerUsernames(), existingGame.gameName(), existingGame.game()));
            GameData retrievedGame = gameDAO.get(newGame.gameID());
            Assertions.assertNull(retrievedGame);
        });
    }
    @Test
    void updateGameUsersDoNotExist() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameDAO.update(existingGame.gameID(), new GameData(existingGame.gameID(), newWhiteUser.username(), newBlackUser.username(), existingGame.observerUsernames(), existingGame.gameName(), existingGame.game()));
            GameData retrievedGame = gameDAO.get(existingGame.gameID());
            Assertions.assertNotNull(retrievedGame);
            Assertions.assertNull(retrievedGame.whiteUsername());
            Assertions.assertNull(retrievedGame.blackUsername());
            Assertions.assertEquals(ChessGame.class, retrievedGame.game().getClass());
        });
    }
    @Test
    void deleteAllGames() {
        Assertions.assertDoesNotThrow(() -> {
            GameData createdGame = gameDAO.create(newGame.gameName());
            gameDAO.clear();
            Assertions.assertNull(gameDAO.get(createdGame.gameID()));
            Assertions.assertNull(gameDAO.get(existingGame.gameID()));
        });
    }
}


