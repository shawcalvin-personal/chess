package server.responseModels;

public record FailureResponse(FailureType failureType, String message) implements ServiceResponse{
}
