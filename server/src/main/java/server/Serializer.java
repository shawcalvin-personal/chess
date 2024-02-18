package server;
import com.google.gson.Gson;
import spark.*;

import java.util.Map;

public class Serializer {
    public static Map<?, ?> getBody(Request request) {
        var body = new Gson().fromJson(request.body(), Map.class);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        return body;
    }

    public static String getJson(Object o) {
        return new Gson().toJson(o);
    }
}
