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
            case ROOK -> getRookMoves(board, myPosition);
            case QUEEN -> getQueenMoves(board, myPosition);
            case KING -> getKingMoves(board, myPosition);
            case KNIGHT -> getKnightMoves(board, myPosition);
            case PAWN -> getPawnMoves(board, myPosition);
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

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        final int startRow = myPosition.getRow();
        final int startCol = myPosition.getColumn();

        boolean shouldContinueSearchUp = true;
        boolean shouldContinueSearchDown = true;
        boolean shouldContinueSearchLeft = true;
        boolean shouldContinueSearchRight = true;

        for (int i = 1; i <= 8; i++) {
            shouldContinueSearchUp = validateMove(board, myPosition, new ChessPosition(startRow + i, startCol), shouldContinueSearchUp, validMoves);
            shouldContinueSearchDown = validateMove(board, myPosition, new ChessPosition(startRow - i, startCol), shouldContinueSearchDown, validMoves);
            shouldContinueSearchRight = validateMove(board, myPosition, new ChessPosition(startRow, startCol + i), shouldContinueSearchRight, validMoves);
            shouldContinueSearchLeft = validateMove(board, myPosition, new ChessPosition(startRow, startCol - i), shouldContinueSearchLeft, validMoves);

        }
        return validMoves;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        validMoves.addAll(getBishopMoves(board, myPosition));
        validMoves.addAll(getRookMoves(board, myPosition));
        return validMoves;
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        final int startRow = myPosition.getRow();
        final int startCol = myPosition.getColumn();

        validateMove(board, myPosition, new ChessPosition(startRow + 1, startCol + 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow + 1, startCol - 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 1, startCol + 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 1, startCol - 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow + 1, startCol), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 1, startCol), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow, startCol + 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow, startCol - 1), true, validMoves);

        return validMoves;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        final int startRow = myPosition.getRow();
        final int startCol = myPosition.getColumn();

        validateMove(board, myPosition, new ChessPosition(startRow + 2, startCol + 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow + 1, startCol + 2), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow + 2, startCol - 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow + 1, startCol - 2), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 2, startCol + 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 1, startCol + 2), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 2, startCol - 1), true, validMoves);
        validateMove(board, myPosition, new ChessPosition(startRow - 1, startCol - 2), true, validMoves);

        return validMoves;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> validMoves = new HashSet<>();
        final ChessGame.TeamColor teamColor = board.getPiece(myPosition).getTeamColor();
        final int moveDirection = teamColor.equals(ChessGame.TeamColor.WHITE) ? 1 : -1;
        final int startRow = myPosition.getRow();
        final int startCol = myPosition.getColumn();

        if (pawnHasNotMoved(myPosition, teamColor)) {
            validatePawnMove(board, myPosition, new ChessPosition(startRow + moveDirection * 2, startCol), teamColor, moveDirection, validMoves);
        }
        validatePawnMove(board, myPosition, new ChessPosition(startRow + moveDirection, startCol), teamColor, moveDirection, validMoves);
        validatePawnMove(board, myPosition, new ChessPosition(startRow + moveDirection, startCol + 1), teamColor, moveDirection, validMoves);
        validatePawnMove(board, myPosition, new ChessPosition(startRow + moveDirection, startCol - 1), teamColor, moveDirection, validMoves);

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

    private void validatePawnMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition, ChessGame.TeamColor teamColor, int moveDirection, HashSet<ChessMove> validMoves) {
        int yMov = Math.abs(endPosition.getRow() - startPosition.getRow());
        int xMov = Math.abs(endPosition.getColumn() - startPosition.getColumn());
        int promotionRank = teamColor.equals(ChessGame.TeamColor.WHITE) ? 8 : 1;
        boolean isPromotion = endPosition.getRow() == promotionRank;
        ChessPiece endPiece = board.getPiece(endPosition);
        // validate forward move
        if (yMov == 1 && xMov == 0 && endPiece == null) {
            validatePawnMoveWithPromotion(isPromotion, startPosition, endPosition, validMoves);
        }
        // validate double forward move
        if (yMov == 2 && xMov == 0 && endPiece == null) {
            ChessPosition skippedPosition = new ChessPosition(startPosition.getRow() + moveDirection, startPosition.getColumn());
            if (board.getPiece(skippedPosition) == null) {
                validatePawnMoveWithPromotion(isPromotion, startPosition, endPosition, validMoves);
            }
        } // validate captures
        if (yMov == 1 && xMov != 0 && endPiece != null && !endPiece.getTeamColor().equals(teamColor)) {
            validatePawnMoveWithPromotion(isPromotion, startPosition, endPosition, validMoves);
        }
    }

    private boolean isValidPosition(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() <= 8 && position.getColumn() > 0 && position.getColumn() <= 8;
    }

    private boolean pawnHasNotMoved(ChessPosition pawnSquare, ChessGame.TeamColor teamColor) {
        return (teamColor.equals(ChessGame.TeamColor.WHITE) && pawnSquare.getRow() == 2) || (teamColor.equals(ChessGame.TeamColor.BLACK) && pawnSquare.getRow() == 7);
    }

    private void validatePawnMoveWithPromotion(boolean isPromotion, ChessPosition startPosition, ChessPosition endPosition, HashSet<ChessMove> validMoves) {
        if (isPromotion) {
            validMoves.add(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
            validMoves.add(new ChessMove(startPosition, endPosition, PieceType.ROOK));
            validMoves.add(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
            validMoves.add(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
        } else {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
        }
    }

    @Override
    public String toString() {
        return getTeamColor() + " " + getPieceType();
    }
}
