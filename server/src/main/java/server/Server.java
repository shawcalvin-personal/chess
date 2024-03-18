package server;

import model.responseModels.ServiceResponse;
import model.responseModels.StatusCodeResponse;
import model.responseModels.FailureResponse;
import service.*;
import spark.*;

public class Server {
    ClearService clearService = new ClearService();
    RegisterService registerService = new RegisterService();
    LoginService loginService = new LoginService();
    LogoutService logoutService = new LogoutService();
    ListGamesService listGamesService = new ListGamesService();
    CreateGameService createGameService = new CreateGameService();
    JoinGameService joinGameService = new JoinGameService();
    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.delete("/db", this::clearApplication);

        Spark.awaitInitialization();
        return Spark.port();
    }
    private String register(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = registerService.register(getRequestParameter(req, "username"), getRequestParameter(req, "password"), getRequestParameter(req, "email"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String login(Request req, Response res) {
        try {
            var body = Serializer.getBody(req);
            ServiceResponse serviceResponse = loginService.login(body.get("username").toString(), body.get("password").toString());
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String logout(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = logoutService.logout(req.headers("Authorization"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String listGames(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = listGamesService.listGames(req.headers("Authorization"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String createGame(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = createGameService.createGame(req.headers("Authorization"), getRequestParameter(req, "gameName"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String joinGame(Request req, Response res) {
        try {
            var body = Serializer.getBody(req);
            Double serializedGameID = body.get("gameID") == null ? null : (Double) body.get("gameID");
            Integer gameID = serializedGameID == null ? null : serializedGameID.intValue();
            ServiceResponse serviceResponse = joinGameService.joinGame(req.headers("Authorization"), getRequestParameter(req, "playerColor"), gameID);
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            System.out.println("ERROR MESSAGE: " + e.getMessage());
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String clearApplication(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = clearService.clearDatabase();
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String parseRequest(ServiceResponse serviceResponse, Response res) {
        if (!serviceResponse.getClass().equals(FailureResponse.class)) {
            return Serializer.getJson(serviceResponse);
        }
        FailureResponse failureResponse = (FailureResponse) serviceResponse;

        switch (failureResponse.failureType()) {
            case BAD_REQUEST -> res.status(400);
            case UNAUTHORIZED_ACCESS -> res.status(401);
            case FORBIDDEN_RESOURCE -> res.status(403);
            case SERVER_ERROR -> res.status(500);
        }
        return Serializer.getJson(new StatusCodeResponse(failureResponse.message()));
    }

    private String getRequestParameter(Request req, String parameter) {
        var body = Serializer.getBody(req);
        return body.get(parameter) == null ? null : body.get(parameter).toString();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
