package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor teamColor;
    private final ChessPiece.PieceType pieceType;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.pieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return teamColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece myPiece = board.getPiece(myPosition);
        return switch(myPiece.getPieceType()) {
            case BISHOP -> getBishopMoves(board, myPosition);
            default -> null;
        };
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        final int startRow = myPosition.getRow();
        final int startCol = myPosition.getColumn();

        boolean shouldContinueSearchPP = true;
        boolean shouldContinueSearchPN = true;
        boolean shouldContinueSearchNP = true;
        boolean shouldContinueSearchNN = true;

        for (int i = 1; i <= 8; i++) {
            shouldContinueSearchPP = validateMove(board, myPosition, new ChessPosition(startRow + i, startCol + i), shouldContinueSearchPP, validMoves);
            shouldContinueSearchPN = validateMove(board, myPosition, new ChessPosition(startRow + i, startCol - i), shouldContinueSearchPN, validMoves);
            shouldContinueSearchNP = validateMove(board, myPosition, new ChessPosition(startRow - i, startCol + i), shouldContinueSearchNP, validMoves);
            shouldContinueSearchNN = validateMove(board, myPosition, new ChessPosition(startRow - i, startCol - i), shouldContinueSearchNN, validMoves);

        }
        return validMoves;
    }

    /**
     * Assumes that the piece can move to the square, ignoring blocks, checks, and captures
     * Validates that the square is a valid square and the piece's path to the square is not blocked
     *
     * @return true if the caller should continue searching for valid moves in this direction
     */
    private boolean validateMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, boolean shouldContinueSearch, HashSet<ChessMove> validMoves) {
        if (!shouldContinueSearch || !isValidPosition(endPosition)) {
            return false;
        }
        if (board.getPiece(endPosition) == null) {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
            return true;
        }
        if (!board.getPiece(endPosition).getTeamColor().equals(getTeamColor())) {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
        }
        return false;
    }

    private boolean isValidPosition(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() <= 8 && position.getColumn() > 0 && position.getColumn() <= 8;
    }

    @Override
    public String toString() {
        return getTeamColor() + " " + getPieceType();
    }
}
