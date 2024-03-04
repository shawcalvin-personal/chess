package chess.ChessRuleBook;

import chess.*;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovementRule extends MovementRule {
    public PawnMovementRule() {
        this.pieceType = ChessPiece.PieceType.PAWN;
    }
    public Collection<ChessMove> getValidMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> validMoves = new HashSet<>();
        int travelDirection = board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 1 : -1;
        int[][] validMoveSet = {{travelDirection, 0}, {travelDirection * 2, 0}, {travelDirection, 1}, {travelDirection, -1}};
        for (var move : validMoveSet) {
            validateAndAddMove(board, new ChessMove(position, new ChessPosition(position.getRow() + move[0], position.getColumn() + move[1]), null), validMoves);
        }
        return validMoves;
    }

    @Override
    public void validateAndAddMove(ChessBoard board, ChessMove move, Collection<ChessMove> validMoves) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        int travelDirection = board.getPiece(startPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 1 : -1;
        int promotionRow = board.getPiece(startPosition).getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 8 : 1;
        int rowDistance = Math.abs(endPosition.getRow() - startPosition.getRow());
        int colDistance = Math.abs(endPosition.getColumn() - startPosition.getColumn());
        if (!positionIsOnBoard(endPosition)) {
            return;
        }
        if (rowDistance == 2 && colDistance == 0 && positionIsEmpty(board, endPosition) && pawnHasNotMoved(board, startPosition) && positionIsEmpty(board, new ChessPosition(startPosition.getRow() + travelDirection, startPosition.getColumn()))) {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
        }
        else if (rowDistance == 1 && colDistance == 0 && positionIsEmpty(board, endPosition) && endPosition.getRow() != promotionRow) {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
        }
        else if (rowDistance == 1 && colDistance == 0 && positionIsEmpty(board, endPosition) && endPosition.getRow() == promotionRow) {
            addPromotionMoves(startPosition, endPosition, validMoves);
        }
        else if (rowDistance == 1 && colDistance == 1 && positionIsCapturable(board, startPosition, endPosition) && endPosition.getRow() != promotionRow) {
            validMoves.add(new ChessMove(startPosition, endPosition, null));
        }
        else if (rowDistance == 1 && colDistance == 1 && positionIsCapturable(board, startPosition, endPosition) && endPosition.getRow() == promotionRow) {
            addPromotionMoves(startPosition, endPosition, validMoves);
        }
    }

    private boolean pawnHasNotMoved(ChessBoard board, ChessPosition position) {
        return (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.WHITE) && position.getRow() == 2) || (board.getPiece(position).getTeamColor().equals(ChessGame.TeamColor.BLACK) && position.getRow() == 7);
    }

    private void addPromotionMoves(ChessPosition startPosition, ChessPosition endPosition, Collection<ChessMove> validMoves) {
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
    }
}
