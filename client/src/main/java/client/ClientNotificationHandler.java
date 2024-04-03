package client;

import chess.ChessGame;
import client.webSocket.NotificationHandler;
import ui.*;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

public class ClientNotificationHandler implements NotificationHandler {
    private final ChessClient client;
    private ChessGame.TeamColor playerColor;

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

        System.out.println(EscapeSequences.WHITE_QUEEN + " Welcome to 240 chess. Type 'help' to get started.");
        String result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }
    }

    public void notify(ServerMessage message) {
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            if (playerColor.equals(ChessGame.TeamColor.BLACK)) {
                ChessGamePrinter.printBlackBoard(message.getGame().getBoard());
            } else {
                ChessGamePrinter.printWhiteBoard(message.getGame().getBoard());
            }
        } else if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.NOTIFICATION)) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN + message.getMessage());
        } else {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message.getErrorMessage());
        }
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + ">>>  ");
    }
}