package server;

import spark.*;

import java.util.Map;

public class Server {

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
    private Object register(Request req, Response res) {
        return "{}";
    }

    private Object login(Request req, Response res) {
        var body = Serializer.getBody(req, Map.class);
        return Serializer.getJson(body);
    }

    private Object logout(Request req, Response res) {
        return "{}";
    }

    private Object listGames(Request req, Response res) {
        return "{}";
    }

    private Object createGame(Request req, Response res) {
        return "{}";
    }

    private Object joinGame(Request req, Response res) {
        return "{}";
    }

    private Object clearDatabase(Request req, Response res) {
        return "{}";
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
