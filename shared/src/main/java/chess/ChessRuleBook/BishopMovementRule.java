package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;

import java.util.Collection;

public class BishopMovementRule extends MovementRule {

    public BishopMovementRule() {
        this.pieceType = ChessPiece.PieceType.BISHOP;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        int[][] moveDirections = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        return super.getValidSlidingMoves(board, position, moveDirections);
    }
}
