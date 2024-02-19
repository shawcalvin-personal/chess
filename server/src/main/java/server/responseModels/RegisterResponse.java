package server.responseModels;

public record RegisterResponse(String username, String authToken) implements HTTPResponse {}
