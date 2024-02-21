package server.responseModels;

import model.GameData;
import java.util.Collection;

public record ListGamesResponse(Collection<GameInstance> games) implements ServiceResponse {
}