package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class ClientHandler {
    private final ChessClient client;

    public ClientHandler(String serverUrl) {
        client = new ChessClient(serverUrl);
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

    private void printPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_GREEN + ">>>  ");

    }
}