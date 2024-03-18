package service;

import model.chessModels.GameData;
import server.responseModels.*;

public class CreateGameService extends Service {
    public ServiceResponse createGame(String authToken, String gameName) {
        if (authToken == null || gameName == null) {
            return new FailureResponse(FailureType.BAD_REQUEST, badRequestMessage);
        }
        try {
            if (!isValidAuthToken(authToken)) {
                return new FailureResponse(FailureType.UNAUTHORIZED_ACCESS, unauthorizedAccessMessage);
            }
            GameData game = gameDAO.create(gameName);
            return new CreateGameResponse(game.gameID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new FailureResponse(FailureType.SERVER_ERROR, getServerErrorMessage(e));
        }
    }
}
