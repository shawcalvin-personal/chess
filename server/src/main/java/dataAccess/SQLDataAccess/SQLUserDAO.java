package dataAccess.SQLDataAccess;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.GameData;
import model.UserData;
import java.util.List;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() {
        try {
            configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("Unable to initialize database: " + e.getMessage());
        }

    }

    @Override
    public UserData create(String username, String password, String email) throws DataAccessException {
        String sql = "INSERT INTO user (username, password, email) VALUES (?, ?, ?)";
        DatabaseUtilities.executeUpdate(sql, username, password, email);
        return new UserData(username, password, email);
    }

    @Override
    public UserData get(String username) throws DataAccessException {
        String sql = "SELECT username, password, email FROM user WHERE username=?";
        List<UserData> users = DatabaseUtilities.executeQuery(sql, GameData.class, username);
        if (users.size() == 1) {
            return users.getFirst();
        } else if (users.size() > 1) {
            throw new DataAccessException("Expected one return value but received multiple.");
        } else {
            return null;
        }
    }

    @Override
    public void delete(String username) throws DataAccessException {
        UserData user = get(username);
        if (user == null) {
            throw new DataAccessException("attempted to delete a user that does not exist.");
        }
        String sql = "DELETE FROM user WHERE username=?";
        DatabaseUtilities.executeUpdate(sql, username);
    }

    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM user";
        DatabaseUtilities.executeUpdate(sql);
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        final String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(email)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.createDatabase();
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to configure database: %s", e.getMessage()));
        }
    }
}
