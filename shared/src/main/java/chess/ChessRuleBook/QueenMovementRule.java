package chess.ChessRuleBook;

import chess.*;
import java.util.Collection;

public class QueenMovementRule extends MovementRule {
    public QueenMovementRule() {
        this.pieceType = ChessPiece.PieceType.QUEEN;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        int[][] moveDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        return super.getValidSlidingMoves(board, position, moveDirections);
    }
}
