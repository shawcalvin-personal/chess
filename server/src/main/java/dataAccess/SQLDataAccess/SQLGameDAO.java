package dataAccess.SQLDataAccess;

import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public GameData create(String gameName) {
        return null;
    }

    @Override
    public GameData create(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public GameData get(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> list() {
        return null;
    }

    @Override
    public void update(int gameID, GameData game) throws DataAccessException {

    }

    @Override
    public void delete(int gameID) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
