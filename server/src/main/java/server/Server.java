package server;

import server.responseModels.*;
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
        Spark.delete("/db", this::clear);

        Spark.awaitInitialization();
        return Spark.port();
    }
    private String register(Request req, Response res) {
        try {
            ServiceResponse serviceResponse = registerService.register(getRequestParameter(req, "username"), getRequestParameter(req, "password"), getRequestParameter(req, "email"));
            return parseRequest(serviceResponse, res);
        } catch (Exception e) {
            System.out.println(e.getMessage());
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
//        try {
//            return Serializer.getJson(gameService.listGames(req.headers("Authorization")));
//        } catch (BadRequestException e) {
//            res.status(400);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (UnauthorizedAccessException e) {
//            res.status(401);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (Exception e) {
//            res.status(500);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        }
        return "{}";
    }

    private String createGame(Request req, Response res) {
//        try {
//            var body = Serializer.getBody(req);
//            return Serializer.getJson(gameService.createGame(req.headers("Authorization"), body.get("gameName").toString()));
//        } catch (NullPointerException e) {
//            res.status(400);
//            return Serializer.getJson(new FailureResponse("Error: bad request"));
//        } catch (BadRequestException e) {
//            res.status(400);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (UnauthorizedAccessException e) {
//            res.status(401);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (Exception e) {
//            res.status(500);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        }
        return "{}";
    }

    private String joinGame(Request req, Response res) {
//        try {
//            var body = Serializer.getBody(req);
//            if (body.get("playerColor").toString().isBlank()) {
//                gameService.joinGame(req.headers("Authorization"), (int) body.get("gameID"));
//            } else {
//                gameService.joinGame(req.headers("Authorization"), body.get("playerColor").toString(), ((Double) body.get("gameID")).intValue());
//            }
//            return "{}";
//        } catch (NullPointerException e) {
//            res.status(400);
//            return Serializer.getJson(new FailureResponse("Error: bad request"));
//        } catch (BadRequestException e) {
//            res.status(400);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (UnauthorizedAccessException e) {
//            res.status(401);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (ForbiddenResourceException e) {
//            res.status(403);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        } catch (Exception e) {
//            res.status(500);
//            return Serializer.getJson(new FailureResponse(e.getMessage()));
//        }
        return "{}";
    }

    private String clear(Request req, Response res) {
        clearService.clearDatabase();
        return "{}";
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
