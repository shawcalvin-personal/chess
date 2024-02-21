package model;

import chess.ChessGame;
import java.util.Collection;

public record GameData(int gameID, String whiteUsername, String blackUsername, Collection<String> observerUsernames, String gameName, ChessGame game) {}
