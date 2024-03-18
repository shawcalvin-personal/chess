package model.responseModels;

public record LoginResponse(String username, String authToken) implements ServiceResponse {
}
