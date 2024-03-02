package dataAccess.SQLDataAccess;

import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.UserData;

public class SQLUserDAO implements UserDAO {
    @Override
    public UserData create(String username, String password, String email) throws DataAccessException {
        return null;
    }

    @Override
    public UserData get(String username) {
        return null;
    }

    @Override
    public void delete(String username) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
