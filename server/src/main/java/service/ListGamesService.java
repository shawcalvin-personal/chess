package service;

import server.responseModels.*;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesService extends Service {
    public ServiceResponse listGames(String authToken) {
        if (!isValidAuthToken(authToken)) {
            return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
        }
        Collection<GameInstance> listedGames = new ArrayList<>();
        for (var game : gameDAO.listGames()) {
            listedGames.add(new GameInstance(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
        }
        return new ListGamesResponse(listedGames);
    }
}
