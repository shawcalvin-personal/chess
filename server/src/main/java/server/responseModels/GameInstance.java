package server.responseModels;

public record GameInstance(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
}
