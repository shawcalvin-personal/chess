package client;

import com.google.gson.Gson;
import java.io.*;
import java.net.*;

import model.chessModels.AuthData;
import model.requestModels.*;
import model.responseModels.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public LoginResponse login(LoginRequest req) throws ResponseException {
        String path = "/session";
        return this.makeRequest("POST", path, req, null, LoginResponse.class);
    }

    public RegisterResponse register(RegisterRequest req) throws ResponseException {
        String path = "/user";
        return this.makeRequest("POST", path, req, null, RegisterResponse.class);
    }

    public void logout(AuthData auth) throws ResponseException {
        String path = "/session";
        this.makeRequest("DELETE", path, null, auth, LogoutResponse.class);
    }

    public CreateGameResponse createGame(CreateGameRequest req, AuthData auth) throws ResponseException {
        String path = "/game";
        return this.makeRequest("POST", path, req, auth, CreateGameResponse.class);
    }

    public ListGamesResponse listGames(AuthData auth) throws ResponseException {
        String path = "/game";
        return this.makeRequest("GET", path, null, auth, ListGamesResponse.class);
    }

    public void joinGame(JoinGameRequest req, AuthData auth) throws ResponseException {
        String path = "/game";
        this.makeRequest("PUT", path, req, auth, JoinGameResponse.class);
    }

    public void clearApplication() throws ResponseException {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    private <T> T makeRequest(String method, String path, Object requestBody, AuthData auth, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (auth != null) {
                http.addRequestProperty("Authorization", auth.authToken());
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
