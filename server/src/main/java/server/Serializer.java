package server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.*;

import java.util.HashMap;
import java.util.Map;

public class Serializer {
    public static <T> T getBody(Request request, Class<T> clazz) {
        System.out.println("HERE!");
        var body = new Gson().fromJson(request.body(), clazz);
        if (body == null) {
            throw new RuntimeException("missing required body");
        }
        System.out.println(body);
        return body;
    }

    public static String getJson(Object o) {
        return new Gson().toJson(o);
    }
}
