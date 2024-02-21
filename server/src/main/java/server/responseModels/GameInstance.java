package server.responseModels;

public record GameInstance(int gameID, String whiteUsername, String blackUsername, String gameName) {
}
