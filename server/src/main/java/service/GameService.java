//package service;
//
//import chess.ChessGame;
//import dataAccess.*;
//import dataAccess.MemoryDAO.*;
//import model.GameData;
//import server.responseModels.CreateGameResponse;
//import server.responseModels.GameInstance;
//import server.responseModels.ListGamesResponse;
//import service.exceptions.BadRequestException;
//import service.exceptions.ForbiddenResourceException;
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class GameService {
//    public static String whiteColor = "WHITE";
//    public static String blackColor = "BLACK";
//    private static GameDAO gameDAO = new MemoryGameDAO();
//    private static AuthDAO authDAO = new MemoryAuthDAO();
//    public ListGamesResponse listGames(String authToken) throws Exception{
//        AuthValidator.validateAuthToken(authToken);
//        Collection<GameInstance> listedGames = new ArrayList<>();
//        for (var game : gameDAO.listGames()) {
//            listedGames.add(new GameInstance(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName()));
//        }
//        return new ListGamesResponse(listedGames);
//    }
//
//    public CreateGameResponse createGame(String authToken, String gameName) throws Exception {
//        AuthValidator.validateAuthToken(authToken);
//        return new CreateGameResponse(gameDAO.createGame(gameName).gameID());
//    }
//
//    public void joinGame(String authToken, String clientColor, int gameID) throws Exception {
//        AuthValidator.validateAuthToken(authToken);
//        GameData targetGame = gameDAO.getGame(gameID);
//        if (targetGame == null) {
//            throw new BadRequestException("Error: bad request");
//        }
//
//        String whiteUsername = targetGame.whiteUsername();
//        String blackUsername = targetGame.blackUsername();
//        String clientUsername = authDAO.getAuth(authToken).username();
//
//        if (clientColor.equals(whiteColor) && whiteUsername == null) {
//            whiteUsername = clientUsername;
//        } else if (clientColor.equals(blackColor) && blackUsername == null) {
//            blackUsername = clientUsername;
//        } else {
//            throw new ForbiddenResourceException("Error: already taken");
//        }
//
//        gameDAO.updateGame(targetGame.gameID(), whiteUsername, blackUsername, targetGame.observerUsernames(), targetGame.gameName(), targetGame.game());
//    }
//
//    public void joinGame(String authToken, int gameID) throws Exception {
//        AuthValidator.validateAuthToken(authToken);
//        GameData targetGame = gameDAO.getGame(gameID);
//        if (targetGame == null) {
//            throw new ForbiddenResourceException("Error: bad request");
//        }
//        String clientUsername = authDAO.getAuth(authToken).username();
//        Collection<String> observerUsernames = targetGame.observerUsernames();
//        observerUsernames.add(clientUsername);
//        gameDAO.updateGame(targetGame.gameID(), targetGame.whiteUsername(), targetGame.blackUsername(), observerUsernames, targetGame.gameName(), targetGame.game());
//    }
//}
