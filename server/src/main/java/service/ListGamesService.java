package service;

import model.responseModels.GameInstance;
import model.responseModels.ListGamesResponse;
import model.responseModels.FailureResponse;
import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;

import java.util.ArrayList;
import java.util.Collection;

public class ListGamesService extends Service {
    public ServiceResponse listGames(String authToken) {
        try {
            if (!isValidAuthToken(authToken)) {
                return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
            }
            Collection<GameInstance> listedGames = new ArrayList<>();
            for (var game : gameDAO.list()) {
                listedGames.add(new GameInstance(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
            }
            return new ListGamesResponse(listedGames);
        } catch (Exception e) {
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}
