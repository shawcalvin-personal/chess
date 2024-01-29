package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import chess.ChessRuleBook.*;

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
            case KING -> new KingMovementRule().getValidMoves(board, myPosition);
            case QUEEN -> new QueenMovementRule().getValidMoves(board, myPosition);
            case ROOK -> new RookMovementRule().getValidMoves(board, myPosition);
            case BISHOP -> new BishopMovementRule().getValidMoves(board, myPosition);
            case KNIGHT -> new KnightMovementRule().getValidMoves(board, myPosition);
            case PAWN -> new PawnMovementRule().getValidMoves(board, myPosition);
        };
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", pieceType=" + pieceType +
                '}';
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
