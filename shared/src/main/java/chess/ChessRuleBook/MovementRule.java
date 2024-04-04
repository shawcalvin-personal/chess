package chess.ChessRuleBook;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

import chess.*;

public abstract class MovementRule {
    protected ChessPiece.PieceType pieceType;

    abstract Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position);

    public boolean positionIsOnBoard(ChessPosition position) {
        return position.getRow() > 0 && position.getRow() <=8 && position.getColumn() > 0 && position.getColumn() <= 8;
    }

    public boolean positionIsEmpty(ChessBoard board, ChessPosition position) {
        return positionIsOnBoard(position) && board.getPiece(position) == null;
    }

    public boolean positionIsCapturable(ChessBoard board, ChessPosition startPosition, ChessPosition endPosition) {
        return positionIsOnBoard(endPosition) && !positionIsEmpty(board, endPosition) && board.getPiece(startPosition).getTeamColor() != board.getPiece(endPosition).getTeamColor();
    }

    public void validateAndAddMove(ChessBoard board, ChessMove move, Collection<ChessMove> validMoves) {
        if (positionIsOnBoard(move.getEndPosition()) && (positionIsEmpty(board, move.getEndPosition()) || positionIsCapturable(board, move.getStartPosition(), move.getEndPosition()))) {
            validMoves.add(move);
        }
    }

    public Collection<ChessMove> getValidSlidingMoves(ChessBoard board, ChessPosition position, int[][] moveDirections) {
        Collection<ChessMove> validMoves = new HashSet<>();
        for (int[] moveDirection : moveDirections) {
            for (int j = 1; j < 8; j++) {
                ChessPosition endPosition = new ChessPosition(position.getRow() + moveDirection[0] * j, position.getColumn() + moveDirection[1] * j);
                if (!positionIsOnBoard(endPosition)) {
                    break;
                } else if (positionIsEmpty(board, endPosition)) {
                    validMoves.add(new ChessMove(position, endPosition, null));
                } else if (positionIsCapturable(board, position, endPosition)) {
                    validMoves.add(new ChessMove(position, endPosition, null));
                    break;
                } else {
                    break;
                }
            }
        }

        return validMoves;
    }

    @Override
    public String toString() {
        return "MovementRule{" +
                "pieceType=" + pieceType +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementRule that = (MovementRule) o;
        return pieceType == that.pieceType;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceType);
    }
}
