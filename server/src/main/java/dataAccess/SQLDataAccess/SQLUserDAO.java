package dataAccess.SQLDataAccess;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.DatabaseUtilities;
import dataAccess.UserDAO;
import model.chessModels.UserData;
import java.util.List;

public class SQLUserDAO implements UserDAO {
    public SQLUserDAO() {
        try {
            DatabaseManager.configureDatabase();
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
        List<Object> result = DatabaseUtilities.executeQuery(sql, UserData.class, username);
        if (result.size() == 1) {
            return (UserData) result.getFirst();
        } else if (result.size() > 1) {
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
}
