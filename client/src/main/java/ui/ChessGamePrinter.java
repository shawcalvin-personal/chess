package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.lang.Math;

public class ChessGamePrinter {
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void printGame(ChessGame game) {
        ChessBoard board = game.getBoard();
        ChessPiece[] squares = board.getSquares();
        double boardWidth = Math.sqrt(squares.length);

        for (int i = 0; i < boardWidth; i++) {
            out.print(i);
        }

    }
}
