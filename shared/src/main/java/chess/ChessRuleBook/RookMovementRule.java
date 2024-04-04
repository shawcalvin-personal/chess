package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;

import java.util.Collection;

public class RookMovementRule extends MovementRule {
    public RookMovementRule() {
        this.pieceType = ChessPiece.PieceType.ROOK;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        int[][] moveDirections = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        return super.getValidSlidingMoves(board, position, moveDirections);
    }
}
