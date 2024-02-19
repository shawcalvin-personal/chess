package server;

import dataAccess.DataAccessException;
import server.responseModels.FailureResponse;
import service.*;
import spark.*;

public class Server {

    UserService userService = new UserService();
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
        Spark.delete("/db", this::clearDatabase);

        Spark.awaitInitialization();
        return Spark.port();
    }
    private String register(Request req, Response res) {
        try {
            var body = Serializer.getBody(req);
            return Serializer.getJson(userService.register(body.get("username").toString(), body.get("password").toString(), body.get("email").toString()));
        } catch (NullPointerException e) {
            res.status(400);
            return Serializer.getJson(new FailureResponse("bad request"));
        } catch (DataAccessException e) {
            res.status(403);
            return Serializer.getJson(new FailureResponse(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new FailureResponse(e.getMessage()));
        }
    }

    private String login(Request req, Response res) {
        try {
            var body = Serializer.getBody(req);
            return Serializer.getJson(userService.login(body.get("username").toString(), body.get("password").toString()));
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return Serializer.getJson(new FailureResponse(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new FailureResponse(e.getMessage()));
        }
    }

    private String logout(Request req, Response res) {
        try {
            userService.logout(req.headers("Authorization"));
            return "{}";
        } catch (UnauthorizedAccessException e) {
            res.status(401);
            return Serializer.getJson(new FailureResponse(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return Serializer.getJson(new FailureResponse(e.getMessage()));
        }
    }

    private String listGames(Request req, Response res) {
        return "{}";
    }

    private String createGame(Request req, Response res) {
        return "{}";
    }

    private String joinGame(Request req, Response res) {
        return "{}";
    }

    private String clearDatabase(Request req, Response res) {
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
