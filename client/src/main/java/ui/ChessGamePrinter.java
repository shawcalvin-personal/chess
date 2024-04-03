package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class ChessGamePrinter {
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = EscapeSequences.EMPTY;
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void printBlackBoard(ChessBoard board) {
        out.println();
        printBlackRowBorder();
        for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
            for (int squareIteration = 1; squareIteration <= SQUARE_SIZE_IN_CHARS; squareIteration++) {
                for (int col = BOARD_SIZE_IN_SQUARES + 1; col >= 1; col--) {
                    printSquare(row, col, board, squareIteration);
                }
                out.print(EscapeSequences.RESET_BG_COLOR);
                out.println();
            }
        }
    }

    public static void printWhiteBoard(ChessBoard board) {
        out.println();
        printWhiteRowBorder();
        for (int row = BOARD_SIZE_IN_SQUARES; row >= 1; row--) {
            for (int squareIteration = 1; squareIteration <= SQUARE_SIZE_IN_CHARS; squareIteration++) {
                for (int col = 0; col <= BOARD_SIZE_IN_SQUARES; col++) {
                    printSquare(row, col, board, squareIteration);
                }
                out.print(EscapeSequences.RESET_BG_COLOR);
                out.println();
            }
        }
    }

    private static void printBlackRowBorder() {
        out.print(" ".repeat(3));
        for (int i = 0x41 + BOARD_SIZE_IN_SQUARES - 1; i >= 0x41; i--) {
            printBorderFile((char) i);
        }
        out.println();
    }

    private static void printWhiteRowBorder() {
        out.print(" ".repeat(3));
        for (int i = 0x41; i < 0x41 + BOARD_SIZE_IN_SQUARES; i++) {
            printBorderFile((char) i);
        }
        out.println();
    }

    private static void printBorderFile(char character) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
        String whiteSpace = "\u200A";
        int n = 5;

        out.print(EscapeSequences.SET_TEXT_BOLD);
        out.print(EscapeSequences.SET_TEXT_FAINT);
        out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
        out.print(EMPTY.repeat(prefixLength));
        out.print(whiteSpace.repeat(n));
        out.print(character);
        out.print(whiteSpace.repeat(n + 1));
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printSquare(int row, int col, ChessBoard board, int squareIteration) {
        if (col == 0 || col == 9) {
            String rank = Math.floorDiv(SQUARE_SIZE_IN_CHARS, 2) + 1 == squareIteration ? String.valueOf(row) : " ";
            out.print(EscapeSequences.SET_TEXT_BOLD);
            out.print(EscapeSequences.SET_TEXT_FAINT);
            out.print(EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY);
            out.print(rank + "  ");
            return;
        }
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        ChessPiece piece = Math.floorDiv(SQUARE_SIZE_IN_CHARS, 2) + 1 == squareIteration ? board.getPiece(new ChessPosition(row, col)) : null;
        String pieceChar = getPieceChar(piece);
        ChessGame.TeamColor teamColor = piece == null ? null : piece.getTeamColor();

        if (row % 2 - col % 2 == 0) {
            setDarkColor();
        } else {
            setLightColor();
        }
        if (piece != null && teamColor.equals(ChessGame.TeamColor.WHITE)) {
            out.print(EscapeSequences.SET_LIGHT_PIECE_COLOR);
        } else {
            out.print(EscapeSequences.SET_DARK_PIECE_COLOR);
        }

        out.print(EMPTY.repeat(prefixLength));
        out.print(pieceChar);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static String getPieceChar(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        return switch (piece.getPieceType()) {
            case KING -> EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case PAWN -> EscapeSequences.BLACK_PAWN;
        };
    }

    private static void setLightColor() {
        out.print(EscapeSequences.SET_LIGHT_SQUARE_COLOR);
    }

    private static void setDarkColor() {
        out.print(EscapeSequences.SET_DARK_SQUARE_COLOR);
    }
}