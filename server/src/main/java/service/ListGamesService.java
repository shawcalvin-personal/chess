package service;

import server.responseModels.ListGamesResponse;
import server.responseModels.ServiceResponse;

import java.util.ArrayList;

public class ListGamesService extends Service {
    public ServiceResponse listGames(String authToken) {
        return new ListGamesResponse(new ArrayList<>());
    }
}
