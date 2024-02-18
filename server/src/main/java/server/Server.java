package server;

import server.responseModels.ServiceResponse;
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
        return "{}";
    }

    private String login(Request req, Response res) {
        var body = Serializer.getBody(req);
        ServiceResponse serviceResponse = userService.login(body.get("username").toString(), body.get("password").toString());
        return parseHTTPResponse(serviceResponse, res);
    }

    private String logout(Request req, Response res) {
        return "{}";
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

    private String parseHTTPResponse(ServiceResponse serviceResponse, spark.Response res) {
        res.status(serviceResponse.statusCode());
        return Serializer.getJson(serviceResponse.response());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
