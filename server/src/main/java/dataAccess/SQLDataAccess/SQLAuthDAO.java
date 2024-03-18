package dataAccess.SQLDataAccess;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.DatabaseUtilities;
import model.chessModels.AuthData;

import java.util.List;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {
    public SQLAuthDAO() {
        try {
            DatabaseManager.configureDatabase();
        } catch (DataAccessException e) {
            System.out.println("Unable to initialize database: " + e.getMessage());
        }

    }
    @Override
    public AuthData create(String username, String password) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        String sql = "INSERT INTO auth (username, auth_token) VALUES (?, ?)";
        DatabaseUtilities.executeUpdate(sql, username, authToken);
        return new AuthData(username, authToken);
    }
    @Override
    public AuthData get(String authToken) throws DataAccessException {
        String sql = "SELECT username, auth_token FROM auth WHERE auth_token=?";
        List<Object> result = DatabaseUtilities.executeQuery(sql, AuthData.class, authToken);
        if (result.size() == 1) {
            return (AuthData) result.getFirst();
        } else if (result.size() > 1) {
            throw new DataAccessException("Expected one return value but received multiple.");
        } else {
            return null;
        }
    }
    @Override
    public void delete(String authToken) throws DataAccessException {
        AuthData auth = get(authToken);
        if (auth == null) {
            throw new DataAccessException("attempted to delete a user that does not exist.");
        }
        String sql = "DELETE FROM auth WHERE auth_token=?";
        DatabaseUtilities.executeUpdate(sql, authToken);
    }
    @Override
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM auth";
        DatabaseUtilities.executeUpdate(sql);
    }
}
