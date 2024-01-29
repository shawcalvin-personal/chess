package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovementRule extends MovementRule {
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn() + 1), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() + 2), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + 2, position.getColumn() - 1), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + 1, position.getColumn() - 2), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn() + 1), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() + 2), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() - 2, position.getColumn() - 1), null), validMoves);
        validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() - 1, position.getColumn() - 2), null), validMoves);

        return validMoves;
    }

    private void validateAndAddMove(ChessBoard board, ChessMove move, Collection<ChessMove> validMoves) {
        if (positionIsOnBoard(move.getEndPosition()) && (positionIsEmpty(board, move.getEndPosition()) || positionIsCapturable(board, move.getStartPosition(), move.getEndPosition()))) {
            validMoves.add(move);
        }
    }
}
