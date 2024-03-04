package dataAccess.SQLDataAccess;

import dataAccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseUtilities {
    public static List<UserData> executeQuery(String statement, Object objectClass, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                try (ResultSet rs = ps.executeQuery()) {
                    if (objectClass == UserData.class) {
                        return parseUserResultSet(rs);
                    } else if (objectClass == GameData.class) {
                        return parseGameResultSet(rs);
                    } else if (objectClass == AuthData.class) {
                        return parseAuthResultSet(rs);
                    } else {
                        throw new DataAccessException("invalid model provided - unable to parse query result.");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to query database: %s, %s", statement, e.getMessage()));
        }
    }
    public static int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private static List<UserData> parseUserResultSet(ResultSet rs) throws Exception {
        List<UserData> result = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString(1);
            String password = rs.getString(2);
            String email = rs.getString(3);
            result.add(new UserData(username, password, email));
        }
        return result;
    }

    private static List<UserData> parseGameResultSet(ResultSet rs) throws Exception {
        List<UserData> result = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString(1);
            String password = rs.getString(2);
            String email = rs.getString(3);
            result.add(new UserData(username, password, email));
        }
        return result;
    }

    private static List<UserData> parseAuthResultSet(ResultSet rs) throws Exception {
        List<UserData> result = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString(1);
            String password = rs.getString(2);
            String email = rs.getString(3);
            result.add(new UserData(username, password, email));
        }
        return result;
    }

//    public static List<UserData> queryUsers(String statement, Object... params) throws DataAccessException {
//        List<UserData> users = new ArrayList<>();
//        try(ResultSet rs = executeQuery(statement, params)) {
//            while (rs.next()) {
//                String username = rs.getString(1);
//                String password = rs.getString(2);
//                String email = rs.getString(3);
//                users.add(new UserData(username, password, email));
//            }
//            return users;
//        } catch (Exception e) {
//            throw new DataAccessException(e.getMessage());
//        }
//    }

//    public static GameData queryGames(String statement, Object... params) throws SQLException {
//    }
//
//    public static AuthData queryAuth(String statement, Object... params) throws SQLException {
//    }
}
