package service;

import chess.ChessGame;
import server.responseModels.JoinGameResponse;
import server.responseModels.LogoutResponse;
import server.responseModels.ServiceResponse;

public class JoinGameService extends Service {
    public ServiceResponse joinGame(String authToken, ChessGame.TeamColor playerColor, int gameID ) {
        return new JoinGameResponse();
    }
}
