import client.ClientHandler;

public class Main {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }
        new ClientHandler(serverUrl).run();
    }
}