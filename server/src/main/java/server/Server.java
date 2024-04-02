package server;

import model.responseModels.ServiceResponse;
import model.responseModels.StatusCodeResponse;
import model.responseModels.FailureResponse;
import service.*;
import spark.*;
import webSocket.WebSocketHandler;

public class Server {
    ChessService service;
    private WebSocketHandler webSocketHandler;

    public Server() {
        this.service = new ChessService();
        this.webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/connect", webSocketHandler);

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
            ServiceResponse serviceResponse = service.register(getRequestParameter(req, "username"), getRequestParameter(req, "password"), getRequestParameter(req, "email"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String login(Request req, Response res) {
        try {
            var body = Serializer.getBody(req);
            ServiceResponse serviceResponse = service.login(body.get("username").toString(), body.get("password").toString());
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String logout(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = service.logout(req.headers("Authorization"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String listGames(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = service.listGames(req.headers("Authorization"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String createGame(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = service.createGame(req.headers("Authorization"), getRequestParameter(req, "gameName"));
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
            ServiceResponse serviceResponse = service.joinGame(req.headers("Authorization"), getRequestParameter(req, "playerColor"), gameID);
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            System.out.println("ERROR MESSAGE: " + e.getMessage());
            res.status(500);
            return Serializer.getJson(new StatusCodeResponse(e.getMessage()));
        }
    }

    private String clearApplication(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = service.clearDatabase();
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
