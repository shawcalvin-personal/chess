package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.chessModels.AuthData;
import model.chessModels.GameData;
import model.chessModels.UserData;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseUtilities {
    public static List<Object> executeQuery(String statement, Object objectClass, Object... params) throws DataAccessException {
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
                        case ChessGame p -> ps.setString(i + 1, ChessSerializer.serializeChessGame(p));
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

    private static List<Object> parseUserResultSet(ResultSet rs) throws Exception {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString(1);
            String password = rs.getString(2);
            String email = rs.getString(3);
            result.add(new UserData(username, password, email));
        }
        return result;
    }

    private static List<Object> parseGameResultSet(ResultSet rs) throws Exception {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            Integer gameID = rs.getInt(1);
            String whiteUsername = rs.getString(2);
            String blackUsername = rs.getString(3);
            ArrayList<String> observerUsernames = new Gson().fromJson(rs.getString(4), ArrayList.class);
            String gameName = rs.getString(5);
            ChessGame game = ChessSerializer.deserializeChessGame(rs.getString(6));
            result.add(new GameData(gameID, whiteUsername, blackUsername, observerUsernames, gameName, game));
        }
        return result;
    }

    private static List<Object> parseAuthResultSet(ResultSet rs) throws Exception {
        List<Object> result = new ArrayList<>();
        while (rs.next()) {
            String username = rs.getString(1);
            String authToken = rs.getString(2);
            result.add(new AuthData(username, authToken));
        }
        return result;
    }
}
