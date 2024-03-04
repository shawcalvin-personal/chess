package chess.ChessRuleBook;

import chess.*;
import java.util.Collection;
import java.util.HashSet;

public class QueenMovementRule extends MovementRule {
    public QueenMovementRule() {
        this.pieceType = ChessPiece.PieceType.QUEEN;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        validMoves.addAll(new RookMovementRule().getValidMoves(board, position));
        validMoves.addAll(new BishopMovementRule().getValidMoves(board, position));

        return validMoves;
    }
}
