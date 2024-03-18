package server.responseModels;

import java.util.Collection;

public record ListGamesResponse(Collection<GameInstance> games) implements ServiceResponse {
}