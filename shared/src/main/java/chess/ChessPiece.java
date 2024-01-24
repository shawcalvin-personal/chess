package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType pieceType;
    HashSet<ChessMove> validMoves;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
        this.validMoves = new HashSet<>();
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
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    private void addMove(ChessMove move) {
        this.validMoves.add(move);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        this.validMoves = new HashSet<>();
        return switch (this.getPieceType()) {
            case KING -> getKingPieceMoves(board, myPosition);
            case QUEEN -> getQueenPieceMoves(board, myPosition);
            case ROOK -> getRookPieceMoves(board, myPosition);
            case BISHOP -> getBishopPieceMoves(board, myPosition);
            case KNIGHT -> getKnightPieceMoves(board, myPosition);
            case PAWN -> getPawnPieceMoves(board, myPosition);
        };
    }

    private Collection<ChessMove> getKingPieceMoves(ChessBoard board, ChessPosition myPosition) {
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));

        return this.validMoves;
    }

    private Collection<ChessMove> getQueenPieceMoves(ChessBoard board, ChessPosition myPosition) {
        getRookPieceMoves(board, myPosition);
        getBishopPieceMoves(board, myPosition);
        return this.validMoves;
    }

    private Collection<ChessMove> getRookPieceMoves(ChessBoard board, ChessPosition myPosition) {
        boolean shouldContinue = true;
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn()));
            if (!shouldContinue) break;
        }
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn()));
            if (!shouldContinue) break;
        }
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i));
            if (!shouldContinue) break;
        }
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i));
            if (!shouldContinue) break;
        }
        return this.validMoves;
    }

    private Collection<ChessMove> getBishopPieceMoves(ChessBoard board, ChessPosition myPosition) {
        boolean shouldContinue = true;
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i));
            if (!shouldContinue) break;
        }
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i));
            if (!shouldContinue) break;
        }
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i));
            if (!shouldContinue) break;
        }
        for (int i = 1; i < 8; i++) {
            shouldContinue = validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i));
            if (!shouldContinue) break;
        }
        return this.validMoves;
    }

    private Collection<ChessMove> getKnightPieceMoves(ChessBoard board, ChessPosition myPosition) {
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() + 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 2));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn() - 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 2));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() + 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 2));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn() - 1));
        validateAndAddMove(board, myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 2));

        return this.validMoves;
    }

    private Collection<ChessMove> getPawnPieceMoves(ChessBoard board, ChessPosition myPosition) {
        int travelDirection = this.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 1 : -1;
        validateAndAddPawnMove(board, myPosition, new ChessPosition(myPosition.getRow() + travelDirection, myPosition.getColumn()));
        validateAndAddPawnMove(board, myPosition, new ChessPosition(myPosition.getRow() + travelDirection * 2, myPosition.getColumn()));
        validateAndAddPawnMove(board, myPosition, new ChessPosition(myPosition.getRow() + travelDirection, myPosition.getColumn() + 1));
        validateAndAddPawnMove(board, myPosition, new ChessPosition(myPosition.getRow() + travelDirection, myPosition.getColumn() - 1));

        return this.validMoves;
    }

    private boolean validateAndAddMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        if (!positionIsOnBoard(endPosition)) {
            return false;
        }
        if (positionIsEmpty(board, endPosition)) {
            addMove(new ChessMove(startPosition, endPosition, null));
            return true;
        }
        if (positionIsCapturable(board, endPosition)) {
            addMove(new ChessMove(startPosition, endPosition, null));
            return false;
        }
        return false;
    }

    private void validateAndAddPawnMove(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        int travelDirection = this.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 1 : -1;
        int promotionRow = this.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 8 : 1;
        int rowDistance = Math.abs(endPosition.getRow() - startPosition.getRow());
        int colDistance = Math.abs(endPosition.getColumn() - startPosition.getColumn());
        if (!positionIsOnBoard(endPosition)) {
            return;
        }
        if (rowDistance == 2 && colDistance == 0 && positionIsEmpty(board, endPosition) && pawnHasNotMoved(startPosition) && positionIsEmpty(board, new ChessPosition(startPosition.getRow() + travelDirection, startPosition.getColumn()))) {
            addMove(new ChessMove(startPosition, endPosition, null));
            return;
        }
        if (rowDistance == 1 && colDistance == 0 && positionIsEmpty(board, endPosition) && endPosition.getRow() != promotionRow) {
            addMove(new ChessMove(startPosition, endPosition, null));
            return;
        }
        if (rowDistance == 1 && colDistance == 0 && positionIsEmpty(board, endPosition) && endPosition.getRow() == promotionRow) {
            addMove(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
            addMove(new ChessMove(startPosition, endPosition, PieceType.ROOK));
            addMove(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
            addMove(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
            return;
        }
        if (rowDistance == 1 && colDistance == 1 && positionIsCapturable(board, endPosition) && endPosition.getRow() != promotionRow) {
            addMove(new ChessMove(startPosition, endPosition, null));
            return;
        }
        if (rowDistance == 1 && colDistance == 1 && positionIsCapturable(board, endPosition) && endPosition.getRow() == promotionRow) {
            addMove(new ChessMove(startPosition, endPosition, PieceType.QUEEN));
            addMove(new ChessMove(startPosition, endPosition, PieceType.ROOK));
            addMove(new ChessMove(startPosition, endPosition, PieceType.BISHOP));
            addMove(new ChessMove(startPosition, endPosition, PieceType.KNIGHT));
            return;
        }
    }

    private boolean pawnHasNotMoved(ChessPosition position) {
        return (this.getTeamColor().equals(ChessGame.TeamColor.WHITE) && position.getRow() == 2) || (this.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 7);
    }

    private boolean positionIsOnBoard(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() <= 8 && position.getColumn() > 0 && position.getColumn() <= 8;
    }

    private boolean positionIsEmpty(ChessBoard board, ChessPosition position) {
        return positionIsOnBoard(position) && board.getPiece(position) == null;
    }

    private boolean positionIsCapturable(ChessBoard board, ChessPosition position) {
        return positionIsOnBoard(position) && !positionIsEmpty(board, position) && board.getPiece(position).getTeamColor() != this.getTeamColor();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }
}
