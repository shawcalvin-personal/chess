package chess.ChessRuleBook;

import java.util.Collection;
import chess.*;

public abstract class MovementRule {
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
}
