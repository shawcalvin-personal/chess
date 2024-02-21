package service;

import server.responseModels.CreateGameResponse;
import server.responseModels.ServiceResponse;

public class CreateGameService extends Service {
    public ServiceResponse createGame(String authToken, String gameName) {
        return new CreateGameResponse(99);
    }
}
