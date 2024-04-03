package dataAccess.SQLDataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.DatabaseUtilities;
import dataAccess.GameDAO;
import model.chessModels.GameData;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SQLGameDAO implements GameDAO {
    private static SQLGameDAO instance;
    private SQLGameDAO() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("Unable to initialize database: " + e.getMessage());
        }
    }

    public static SQLGameDAO getInstance() {
        if (instance == null) {
            instance = new SQLGameDAO();
        }
        return instance;
    }

    @Override
    public GameData create(String gameName) throws DataAccessException {
        ChessGame game = new ChessGame();
        game.init();

        String sql = "INSERT INTO game (game_name, game) VALUES (?, ?)";
        int gameID = DatabaseUtilities.executeUpdate(sql, gameName, game);
        return get(gameID);
    }
    @Override
    public GameData get(int gameID) throws DataAccessException {
        String sql = "SELECT * FROM game WHERE game_id=?";
        List<Object> result = DatabaseUtilities.executeQuery(sql, GameData.class, gameID);
        if (result.size() == 1) {
            return (GameData) result.getFirst();
        } else if (result.size() > 1) {
            throw new DataAccessException("Expected one return value but received multiple.");
        } else {
            return null;
        }
    }

    @Override
    public Collection<GameData> list() throws DataAccessException {
        String sql = "SELECT * FROM game";
        List<Object> result = DatabaseUtilities.executeQuery(sql, GameData.class);
        return result
                .stream()
                .map(e -> (GameData) e)
                .collect(Collectors.toList());
    }

    @Override
    public void update(int gameID, GameData game) throws DataAccessException {
        String sql = "UPDATE game SET game_id=?, white_username=?, black_username=?, observer_usernames=?, game_name=?, game=? WHERE game_id=?";
        if (get(gameID) == null) {
            throw new DataAccessException("attempted to update a game that does not exist.");
        }
        String observerUsernamesJson = new Gson().toJson(game.observerUsernames());
        DatabaseUtilities.executeUpdate(sql, gameID,
                game.whiteUsername(),
                game.blackUsername(),
                observerUsernamesJson,
                game.gameName(),
                game.game(),
                gameID);
    }

    @Override
    public void delete(int gameID) throws DataAccessException {
        GameData game = get(gameID);
        if (game == null) {
            throw new DataAccessException("attempted to delete a game that does not exist.");
        }
        String sql = "DELETE FROM game WHERE game_id=?";
        DatabaseUtilities.executeUpdate(sql, gameID);
    }

    @Override
    public void clear() throws DataAccessException{
        String sql = "DELETE FROM game";
        DatabaseUtilities.executeUpdate(sql);
    }
}
