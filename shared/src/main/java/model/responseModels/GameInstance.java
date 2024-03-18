package model.responseModels;

public record GameInstance(Integer gameID, String whiteUsername, String blackUsername, String gameName) {
    @Override
    public String toString() {
        return "GAME " + gameID + "\n" +
                "   White Username: " + whiteUsername + '\n' +
                "   Black Name: " + blackUsername + '\n' +
                "   Game Name: " + gameName + '\n';
    }
}
