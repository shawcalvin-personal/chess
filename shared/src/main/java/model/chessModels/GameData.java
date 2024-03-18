package model.chessModels;

import chess.ChessGame;
import java.util.Collection;

public record GameData(Integer gameID, String whiteUsername, String blackUsername, Collection<String> observerUsernames, String gameName, ChessGame game) {}
