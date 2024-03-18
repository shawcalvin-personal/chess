package model.responseModels;

import java.util.Collection;

public record ListGamesResponse(Collection<GameInstance> games) implements ServiceResponse {
    @Override
    public String toString() {
        return "CURRENT GAMES:" + "\n" + games;
    }
}