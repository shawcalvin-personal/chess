package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.SQLDataAccess.SQLGameDAO;
import dataAccess.SQLDataAccess.SQLUserDAO;
import dataAccess.UserDAO;
import model.chessModels.GameData;
import model.chessModels.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static passoffTests.TestFactory.loadBoard;

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
            GameData createdGame = gameDAO.create("existing-game-name");
            gameDAO.update(createdGame.gameID(), new GameData(createdGame.gameID(), existingWhiteUser.username(), existingBlackUser.username(), createdGame.observerUsernames(), createdGame.gameName(), createdGame.game()));
            existingGame = gameDAO.get( createdGame.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    @Test
    void createGameValid() {
        Assertions.assertDoesNotThrow(() -> {
            GameData createdGame = gameDAO.create(newGame.gameName());
            Assertions.assertNotNull(createdGame);
            GameData retrievedGame = gameDAO.get(createdGame.gameID());
            Assertions.assertEquals(createdGame, retrievedGame);
            Assertions.assertEquals(ChessGame.class, createdGame.game().getClass());
        });
    }
    @Test
    void createGameNullName() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            GameData createdGame = gameDAO.create(null);
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
            existingGame.game().setBoard(loadBoard("""
                | | | | | | | | |
                | | | | | | | |q|
                | | |n| | | |p| |
                | | | | | | | | |
                | | | | | | | | |
                | | | | | | | | |
                | | |B| | | | | |
                | |K| | | | | |R|
                """));
            existingGame.game().setTeamTurn(ChessGame.TeamColor.WHITE);
            gameDAO.update(existingGame.gameID(), new GameData(existingGame.gameID(), existingWhiteUser.username(), existingBlackUser.username(), existingGame.observerUsernames(), existingGame.gameName(), existingGame.game()));
            GameData retrievedGame = gameDAO.get(existingGame.gameID());
            Assertions.assertNotNull(retrievedGame);
            Assertions.assertEquals(existingWhiteUser.username(), retrievedGame.whiteUsername());
            Assertions.assertEquals(existingBlackUser.username(), retrievedGame.blackUsername());
            Assertions.assertEquals(ChessGame.class, retrievedGame.game().getClass());
            Assertions.assertEquals(existingGame.game(), retrievedGame.game());
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
    @Test
    void listGamesValid() {
        Assertions.assertDoesNotThrow(() -> {
            GameData createdGame = gameDAO.create(newGame.gameName());
            Collection<GameData> listedGames = gameDAO.list();
            Assertions.assertNotNull(listedGames);
            Assertions.assertEquals(2, listedGames.size());
            for (var game : listedGames) {
                Assertions.assertEquals(GameData.class, game.getClass());
            }
        });
    }
    @Test
    void listGamesNoGamesInDatabase() {
        Assertions.assertDoesNotThrow(() -> {
            gameDAO.clear();
            Collection<GameData> listedGames = gameDAO.list();
            Assertions.assertNotNull(listedGames);
            Assertions.assertTrue(listedGames.isEmpty());
        });
    }
}


