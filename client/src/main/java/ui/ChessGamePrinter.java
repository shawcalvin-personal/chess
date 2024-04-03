package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class ChessGamePrinter {
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final String EMPTY = EscapeSequences.EMPTY;
    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public static void printBlackBoard(ChessGame game, ChessPosition highlightSquare) {
        Collection<ChessPosition> validMovePositions = getValidMovePositions(game, highlightSquare);
        out.println();
        printBlackRowBorder();
        for (int row = 1; row <= BOARD_SIZE_IN_SQUARES; row++) {
            for (int squareIteration = 1; squareIteration <= SQUARE_SIZE_IN_CHARS; squareIteration++) {
                for (int col = BOARD_SIZE_IN_SQUARES + 1; col >= 1; col--) {
                    printSquare(row, col, game, squareIteration, highlightSquare, validMovePositions);
                }
                out.print(EscapeSequences.RESET_BG_COLOR);
                out.println();
            }
        }
    }

    public static void printWhiteBoard(ChessGame game, ChessPosition highlightSquare) {
        Collection<ChessPosition> validMovePositions = getValidMovePositions(game, highlightSquare);
        out.println();
        printWhiteRowBorder();
        for (int row = BOARD_SIZE_IN_SQUARES; row >= 1; row--) {
            for (int squareIteration = 1; squareIteration <= SQUARE_SIZE_IN_CHARS; squareIteration++) {
                for (int col = 0; col <= BOARD_SIZE_IN_SQUARES; col++) {
                    printSquare(row, col, game, squareIteration, highlightSquare, validMovePositions);
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

    private static void printSquare(int row, int col, ChessGame game, int squareIteration, ChessPosition highlightSquare, Collection<ChessPosition> validMovePositions) {
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
        ChessPosition currentDrawPosition = new ChessPosition(row, col);

        ChessPiece piece = Math.floorDiv(SQUARE_SIZE_IN_CHARS, 2) + 1 == squareIteration ? game.getBoard().getPiece(currentDrawPosition) : null;
        String pieceChar = getPieceChar(piece);
        ChessGame.TeamColor teamColor = piece == null ? null : piece.getTeamColor();

        if (currentDrawPosition.equals(highlightSquare)) {
            out.print(EscapeSequences.SET_DARK_HIGHLIGHT_SQUARE_COLOR);
        } else if (validMovePositions.contains(currentDrawPosition)) {
            out.print(EscapeSequences.SET_LIGHT_HIGHLIGHT_SQUARE_COLOR);
        } else if (row % 2 - col % 2 == 0) {
            out.print(EscapeSequences.SET_DARK_SQUARE_COLOR);
        } else {
            out.print(EscapeSequences.SET_LIGHT_SQUARE_COLOR);
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
            case KING -> EscapeSequences.KING;
            case QUEEN -> EscapeSequences.QUEEN;
            case ROOK -> EscapeSequences.ROOK;
            case BISHOP -> EscapeSequences.BISHOP;
            case KNIGHT -> EscapeSequences.KNIGHT;
            case PAWN -> EscapeSequences.PAWN;
        };
    }

    private static Collection<ChessPosition> getValidMovePositions(ChessGame game, ChessPosition startPosition) {
        Collection<ChessPosition> validPositions = new ArrayList<>();
        if (startPosition == null) {
            return validPositions;
        }
        for (var move : game.validMoves(startPosition)) {
            validPositions.add(move.getEndPosition());
        }
        return validPositions;
    }
}