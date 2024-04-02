package client;

import client.webSocket.NotificationHandler;
import ui.*;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

public class ClientHandler implements NotificationHandler {
    private final ChessClient client;

    public ClientHandler(String serverUrl) {
        client = new ChessClient(serverUrl, this);
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
        System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message.getMessage());
        if (message.getServerMessageType().equals(ServerMessage.ServerMessageType.LOAD_GAME)) {
            ChessGamePrinter.printGame(message.getGame());
        }
        printPrompt();
    }

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + ">>>  ");
    }
}