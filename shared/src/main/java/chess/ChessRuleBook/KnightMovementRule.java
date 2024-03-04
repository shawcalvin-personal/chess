package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovementRule extends MovementRule {
    public KnightMovementRule() {
        this.pieceType = ChessPiece.PieceType.KNIGHT;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        int[][] validMoveSet = {{2, 1}, {1, 2}, {2, -1}, {1, -2}, {-2, 1}, {-1, 2}, {-2, -1}, {-1, -2}};
        for (var move : validMoveSet) {
            validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + move[0], position.getColumn() + move[1]), null), validMoves);
        }
        return validMoves;
    }
}
