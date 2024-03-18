package service;

import model.chessModels.GameData;
import model.responseModels.CreateGameResponse;
import model.responseModels.FailureResponse;
import model.responseModels.FailureType;
import model.responseModels.ServiceResponse;

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
