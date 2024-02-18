package server.responseModels;

public record FailureResponse(String message) implements HTTPResponse {
}
