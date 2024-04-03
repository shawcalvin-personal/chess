package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.*;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

public class ClientNotificationHandler {
    private final ChessClient client;
    private ChessGame.TeamColor playerColor;
    ChessGame game;

    public ClientNotificationHandler(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {
        this.playerColor = playerColor;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(EscapeSequences.SET_TEXT_COLOR_SYSTEM_PROMPT + EscapeSequences.QUEEN + " Welcome to 240 chess. Take a look at the valid commands below to get started.");
        String result = "";
        String line = "help";
        while (!result.equals("quit")) {
            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_SYSTEM_MESSAGE + result);
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
            printPrompt();
            line = scanner.nextLine();
        }
    }

    public void notify(ServerMessage message) {
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            game = message.getGame();
            printBoard(null);
        } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WEBSOCKET_MESSAGE + message.getMessage());
        } else {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_WEBSOCKET_ERROR + message.getErrorMessage());
        }
        printPrompt();
    }

    public void printBoard(ChessPosition highlightSquare) {
        if (playerColor != null && playerColor.equals(ChessGame.TeamColor.BLACK)) {
            ChessGamePrinter.printBlackBoard(game, highlightSquare);
        } else {
            ChessGamePrinter.printWhiteBoard(game, highlightSquare);
        }
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_SYSTEM_PROMPT + ">>>  ");
    }
}