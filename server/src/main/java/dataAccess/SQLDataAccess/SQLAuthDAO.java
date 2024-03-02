package dataAccess.SQLDataAccess;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public AuthData create(String username, String password) {
        return null;
    }

    @Override
    public AuthData get(String authToken) {
        return null;
    }

    @Override
    public void delete(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
