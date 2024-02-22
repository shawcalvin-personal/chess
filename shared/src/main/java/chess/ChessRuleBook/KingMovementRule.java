package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;
import java.util.Collection;
import java.util.HashSet;

public class KingMovementRule extends MovementRule {
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        int[][] validMoveSet = {{1, 1}, {1, 0}, {1, -1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, 1}, {0, -1}};
        for (var move : validMoveSet) {
            validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + move[0], position.getColumn() + move[1]), null), validMoves);
        }
        return validMoves;
    }
}
