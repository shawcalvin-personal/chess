package client;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

import model.chessModels.AuthData;
import model.chessModels.GameData;
import model.chessModels.UserData;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData login(UserData user) throws ResponseException {
        String path = "/session";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public AuthData register(UserData user) throws ResponseException {
        String path = "/user";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public void logout(AuthData auth) throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE", path, null, auth, null);
    }

    public GameData createGame(GameData game, AuthData auth) throws ResponseException {
        String path = "/game";
        return this.makeRequest("POST", path, game, auth, GameData.class);
    }

    public void listGames(AuthData auth) throws ResponseException {
        String path = "/game";
    }

    public void joinGame(GameData game, AuthData auth) throws ResponseException {
        String path = "/game";
    }

    public void joinObserver(GameData game, AuthData auth) throws ResponseException {
        String path = "/game";
    }

    public void clearApplication() throws ResponseException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object requestBody, AuthData requestHeader, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (requestHeader != null) {
                http.addRequestProperty("Authorization", requestHeader.authToken());
            }

            writeBody(requestBody, http);
            http.connect();

            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object requestBody, HttpURLConnection http) throws IOException {
        if (requestBody != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(requestBody);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
