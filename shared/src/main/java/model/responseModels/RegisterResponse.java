package model.responseModels;

public record RegisterResponse(String username, String authToken) implements ServiceResponse {}
