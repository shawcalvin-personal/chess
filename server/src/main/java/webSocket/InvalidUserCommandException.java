package webSocket;

public class InvalidUserCommandException extends Exception {
    public InvalidUserCommandException(String message) {
        super(message);
    }
}