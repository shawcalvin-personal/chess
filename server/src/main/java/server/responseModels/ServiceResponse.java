package server.responseModels;

public record ServiceResponse(int statusCode, HTTPResponse response) {
}